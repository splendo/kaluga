"""Bumble GATT peripheral on the Android emulator's netsim radio, steered by the device test itself.

The device test writes commands to the control characteristic. A command that holds back an answer has to be
armed before the request it holds: the client sends one ATT request at a time, so nothing can reach the
peripheral while an answer is held. Held answers are therefore released by a timer or end with the link.

Commands (one write each, little-endian u16 arguments in milliseconds):
  00                    reset: disarm everything and release held answers
  01 <release>          hold the answer to the next read of the data characteristic (0: until the link ends)
  02 <release>          hold the answer to the next write of the data characteristic's CCCD (0: until the link ends)
  03 <delay>            once a held request arrives, drop the link after <delay>
  04 <delay>            drop the link <delay> after answering this write
  05 <delay> <value..>  notify <value> on the data characteristic <delay> after answering this write
"""

import argparse
import asyncio
import logging
import struct
import sys
import time

from bumble import hci
from bumble.core import UUID, AdvertisingData
from bumble.device import Connection, Device
from bumble.gatt import Characteristic, CharacteristicValue, Service
from bumble.hci import Address
from bumble.transport import open_transport

SERVICE = UUID("6b7a0000-5d1c-4f0e-9a51-2f6f3c1d8e01")
DATA = UUID("6b7a0001-5d1c-4f0e-9a51-2f6f3c1d8e01")
CONTROL = UUID("6b7a0002-5d1c-4f0e-9a51-2f6f3c1d8e01")

INITIAL_VALUE = bytes([1, 2, 3])

RESET, HOLD_READ, HOLD_CCCD, DROP_WHEN_HELD, DROP, NOTIFY = range(6)

START = time.monotonic()


def log(message):
    print(f"{time.monotonic() - START:9.3f} {message}", flush=True)


class Peripheral:
    def __init__(self, device: Device):
        self.device = device
        self.value = INITIAL_VALUE
        self.connection: Connection | None = None
        self.hold_read: int | None = None
        self.hold_cccd: int | None = None
        self.drop_when_held: int | None = None
        self.held: list[asyncio.Future] = []
        self.tasks: set[asyncio.Task] = set()
        self.data = Characteristic(
            DATA,
            Characteristic.Properties.READ | Characteristic.Properties.WRITE | Characteristic.Properties.NOTIFY,
            Characteristic.READABLE | Characteristic.WRITEABLE,
            CharacteristicValue(read=self.read_data, write=self.write_data),
        )
        self.control = Characteristic(
            CONTROL,
            Characteristic.Properties.WRITE,
            Characteristic.WRITEABLE,
            CharacteristicValue(write=self.write_control),
        )
        device.add_service(Service(SERVICE, [self.data, self.control]))
        server = device.gatt_server
        write_cccd = server.write_cccd

        def held_write_cccd(bearer, characteristic, value):
            if characteristic is self.data and self.hold_cccd is not None:
                release, self.hold_cccd = self.hold_cccd, None
                return self.answer_later(f"CCCD write {value.hex()}", release, lambda: write_cccd(bearer, characteristic, value))
            log(f"CCCD write {value.hex()}")
            return write_cccd(bearer, characteristic, value)

        server.write_cccd = held_write_cccd

    def reset(self, release=True):
        self.hold_read = self.hold_cccd = self.drop_when_held = None
        for future in self.held:
            if future.done():
                continue
            if release:
                future.set_result(None)
            else:
                future.cancel()
        self.held.clear()

    def later(self, delay_ms, action):
        async def run():
            await asyncio.sleep(delay_ms / 1000)
            # Cancelling a running drop would cancel the future bumble completes on the disconnection event.
            self.tasks.discard(task)
            await action()

        task = asyncio.get_running_loop().create_task(run())
        self.tasks.add(task)
        task.add_done_callback(self.tasks.discard)

    async def answer_later(self, what, release_ms, answer):
        future = asyncio.get_running_loop().create_future()
        self.held.append(future)
        log(f"holding the answer to {what}" + (f" for {release_ms} ms" if release_ms else ""))
        if self.drop_when_held is not None:
            delay, self.drop_when_held = self.drop_when_held, None
            self.later(delay, self.drop)
        if release_ms:
            asyncio.get_running_loop().call_later(release_ms / 1000, lambda: future.done() or future.set_result(None))
        try:
            await future
        except asyncio.CancelledError:
            log(f"the link ended, {what} stays unanswered")
            raise
        log(f"answering {what}")
        return answer()

    def read_data(self, _connection):
        if self.hold_read is not None:
            release, self.hold_read = self.hold_read, None
            return self.answer_later("read", release, lambda: self.value)
        log(f"read -> {self.value.hex()}")
        return self.value

    def write_data(self, _connection, value):
        log(f"write {value.hex()}")
        self.value = bytes(value)

    def write_control(self, _connection, value):
        command = value[0]
        argument = struct.unpack_from("<H", value, 1)[0] if len(value) >= 3 else 0
        log(f"command {value.hex()}")
        if command == RESET:
            self.reset()
        elif command == HOLD_READ:
            self.hold_read = argument
        elif command == HOLD_CCCD:
            self.hold_cccd = argument
        elif command == DROP_WHEN_HELD:
            self.drop_when_held = argument
        elif command == DROP:
            self.later(argument, self.drop)
        elif command == NOTIFY:
            payload = bytes(value[3:])
            self.later(argument, lambda: self.notify(payload))

    async def drop(self):
        if self.connection is not None:
            log("dropping the link")
            await self.connection.disconnect(hci.HCI_REMOTE_USER_TERMINATED_CONNECTION_ERROR)

    async def notify(self, payload):
        if self.connection is not None:
            log(f"notify {payload.hex()}")
            self.value = payload
            await self.device.notify_subscriber(self.connection, self.data, payload)

    def on_connection(self, connection: Connection):
        log(f"connected {connection.peer_address}")
        self.reset()
        self.value = INITIAL_VALUE
        self.connection = connection

        def on_disconnection(reason):
            log(f"disconnected reason 0x{reason:02X}")
            self.connection = None
            self.reset(release=False)
            for task in list(self.tasks):
                task.cancel()

        connection.on("disconnection", on_disconnection)
        connection.on("connection_att_mtu_update", lambda: log(f"ATT MTU {connection.att_mtu}"))


async def main(transport, address, name):
    async with await open_transport(transport) as hci_transport:
        device = Device.with_hci(name, Address(address), hci_transport.source, hci_transport.sink)
        peripheral = Peripheral(device)
        # Legacy advertising data holds 31 bytes: the 128-bit service UUID fills it, so the name goes in the scan response.
        device.advertising_data = bytes(AdvertisingData([(AdvertisingData.COMPLETE_LIST_OF_128_BIT_SERVICE_CLASS_UUIDS, bytes(SERVICE))]))
        device.scan_response_data = bytes(AdvertisingData([(AdvertisingData.COMPLETE_LOCAL_NAME, name.encode())]))
        device.on("connection", peripheral.on_connection)
        await device.power_on()
        await device.start_advertising(auto_restart=True)
        log(f"advertising {name} {address}")
        await hci_transport.source.terminated


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--transport", default="android-netsim")
    parser.add_argument("--address", default="F0:F1:F2:F3:F4:F6")
    parser.add_argument("--name", default="kaluga-netsim")
    parser.add_argument("--verbose", action="store_true")
    arguments = parser.parse_args()
    logging.basicConfig(level=logging.DEBUG if arguments.verbose else logging.WARNING, stream=sys.stderr)
    asyncio.run(main(arguments.transport, arguments.address, arguments.name))
