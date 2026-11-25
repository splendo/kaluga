"""Bumble GATT peripheral on the Android emulator's netsim radio, steered by the device test itself.

The device test writes commands to the control characteristic. A command that holds back an answer has to be
armed before the request it holds: the client sends one ATT request at a time, so nothing can reach the
peripheral while an answer is held. Held answers are therefore released by a timer or end with the link.

Commands (one write each: a command byte, a little-endian u16 argument, then an optional payload):
  00                      reset: disarm everything and release held answers
  01 <release>            hold the answer to the next read of the data characteristic (0: until the link ends)
  02 <release>            hold the answer to the next write of the data characteristic's CCCD (0: until the link ends)
  03 <delay>              once a held request arrives, drop the link after <delay> ms
  04 <delay>              drop the link <delay> ms after answering this write
  05 <delay> <value..>    notify <value> on the data characteristic <delay> ms after answering this write
  06 <code>               answer every read and write of the error characteristic with ATT error <code> (0: succeed)
  07 <delay> <value..>    indicate <value> on the indicate characteristic <delay> ms after answering this write
  08 <count>              notify <count> values on the data characteristic back to back, each its index as a u16
  09 <length>             set the value of the long characteristic to <length> bytes counting up from 0
  0A <delay> <i> <value..> notify <value> on twin characteristic <i> (0 or 1) <delay> ms after answering this write
  0B <delay>              add the extra service and indicate Service Changed <delay> ms after answering this write
  0C                      start advertising the beacon, a second device with name, manufacturer and service data
  0D                      stop advertising the beacon
  0E <length>             notify <length> bytes counting up from 0 on the data characteristic
  0F 0000 <value..>       set the value of the long characteristic to <value>

Everything armed by a command ends with the link, and the GATT table loses the extra service again.
"""

import argparse
import asyncio
import logging
import struct
import sys
import time

from bumble import hci
from bumble.att import ATT_Error
from bumble.core import UUID, AdvertisingData
from bumble.device import AdvertisingType, Connection, Device
from bumble.gatt import Characteristic, CharacteristicValue, Service
from bumble.hci import Address
from bumble.pairing import PairingConfig, PairingDelegate
from bumble.transport import open_transport

SERVICE = UUID("6b7a0000-5d1c-4f0e-9a51-2f6f3c1d8e01")
DATA = UUID("6b7a0001-5d1c-4f0e-9a51-2f6f3c1d8e01")
CONTROL = UUID("6b7a0002-5d1c-4f0e-9a51-2f6f3c1d8e01")
ERROR = UUID("6b7a0003-5d1c-4f0e-9a51-2f6f3c1d8e01")
INDICATE = UUID("6b7a0004-5d1c-4f0e-9a51-2f6f3c1d8e01")
LONG = UUID("6b7a0005-5d1c-4f0e-9a51-2f6f3c1d8e01")
SINK = UUID("6b7a0006-5d1c-4f0e-9a51-2f6f3c1d8e01")
TWIN_SERVICE = UUID("6b7a0100-5d1c-4f0e-9a51-2f6f3c1d8e01")
TWIN = UUID("6b7a0101-5d1c-4f0e-9a51-2f6f3c1d8e01")
EXTRA_SERVICE = UUID("6b7a0200-5d1c-4f0e-9a51-2f6f3c1d8e01")
EXTRA = UUID("6b7a0201-5d1c-4f0e-9a51-2f6f3c1d8e01")
BEACON_SERVICE = UUID("6b7a0300-5d1c-4f0e-9a51-2f6f3c1d8e01")
BEACON_SERVICE_DATA_UUID = 0xFFF0
BEACON_SERVICE_DATA = bytes([0x5A, 0x01, 0x02, 0x03])
BEACON_COMPANY = 0xFFFF
BEACON_MANUFACTURER_DATA = bytes([0xCA, 0xFE, 0x00, 0x01])

INITIAL_VALUE = bytes([1, 2, 3])
TWIN_VALUES = (b"A", b"B")
EXTRA_VALUE = b"extra"

(
    RESET,
    HOLD_READ,
    HOLD_CCCD,
    DROP_WHEN_HELD,
    DROP,
    NOTIFY,
    ERROR_CODE,
    INDICATE_VALUE,
    NOTIFY_BURST,
    LONG_LENGTH,
    NOTIFY_TWIN,
    CHANGE_SERVICES,
    BEACON_START,
    BEACON_STOP,
    NOTIFY_LENGTH,
    LONG_VALUE,
) = range(16)

START = time.monotonic()


def log(message):
    print(f"{time.monotonic() - START:9.3f} {message}", flush=True)


def counting(length):
    return bytes(i & 0xFF for i in range(length))


class RejectPairing(PairingDelegate):
    # An ATT authentication error makes Android try to pair; rejecting keeps the emulator from bonding.
    async def accept(self):
        log("rejecting a pairing request")
        return False


class Peripheral:
    def __init__(self, device: Device, beacon: Device):
        self.device = device
        self.beacon = beacon
        self.value = INITIAL_VALUE
        self.connection: Connection | None = None
        self.hold_read: int | None = None
        self.hold_cccd: int | None = None
        self.drop_when_held: int | None = None
        self.error_code = 0
        self.long_value = b""
        self.sink = bytearray()
        self.twin_values = list(TWIN_VALUES)
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
        self.error = Characteristic(
            ERROR,
            Characteristic.Properties.READ | Characteristic.Properties.WRITE,
            Characteristic.READABLE | Characteristic.WRITEABLE,
            CharacteristicValue(read=self.read_error, write=self.write_error),
        )
        self.indicate = Characteristic(INDICATE, Characteristic.Properties.INDICATE, Characteristic.READABLE, b"")
        self.long = Characteristic(
            LONG,
            Characteristic.Properties.READ | Characteristic.Properties.WRITE,
            Characteristic.READABLE | Characteristic.WRITEABLE,
            CharacteristicValue(read=lambda _: self.long_value, write=self.write_long),
        )
        self.sink_characteristic = Characteristic(
            SINK,
            Characteristic.Properties.READ | Characteristic.Properties.WRITE_WITHOUT_RESPONSE,
            Characteristic.READABLE | Characteristic.WRITEABLE,
            CharacteristicValue(read=lambda _: bytes(self.sink), write=self.write_sink),
        )
        self.twins = [
            Characteristic(
                TWIN,
                Characteristic.Properties.READ | Characteristic.Properties.WRITE | Characteristic.Properties.NOTIFY,
                Characteristic.READABLE | Characteristic.WRITEABLE,
                CharacteristicValue(read=lambda _, i=i: self.read_twin(i), write=lambda _, value, i=i: self.write_twin(i, value)),
            )
            for i in range(2)
        ]
        self.extra_service = Service(EXTRA_SERVICE, [Characteristic(EXTRA, Characteristic.Properties.READ, Characteristic.READABLE, EXTRA_VALUE)])
        device.add_service(Service(SERVICE, [self.data, self.control, self.error, self.indicate, self.long, self.sink_characteristic]))
        device.add_service(Service(TWIN_SERVICE, self.twins))
        device.pairing_config_factory = lambda _: PairingConfig(delegate=RejectPairing())
        server = device.gatt_server
        write_cccd = server.write_cccd

        def held_write_cccd(bearer, characteristic, value):
            if characteristic is self.data and self.hold_cccd is not None:
                release, self.hold_cccd = self.hold_cccd, None
                return self.answer_later(f"CCCD write {value.hex()}", release, lambda: write_cccd(bearer, characteristic, value))
            log(f"CCCD write {value.hex()} for {self.describe(characteristic)}")
            return write_cccd(bearer, characteristic, value)

        server.write_cccd = held_write_cccd

    def reset(self, release=True):
        self.hold_read = self.hold_cccd = self.drop_when_held = None
        self.error_code = 0
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

    def read_error(self, _connection):
        if self.error_code:
            log(f"read of the error characteristic -> ATT error 0x{self.error_code:02X}")
            raise ATT_Error(self.error_code)
        return b"ok"

    def write_error(self, _connection, value):
        if self.error_code:
            log(f"write {value.hex()} to the error characteristic -> ATT error 0x{self.error_code:02X}")
            raise ATT_Error(self.error_code)

    def write_long(self, _connection, value):
        log(f"long write of {len(value)} bytes")
        self.long_value = bytes(value)

    def write_sink(self, _connection, value):
        self.sink += value

    def describe(self, characteristic):
        if characteristic in self.twins:
            return f"twin {self.twins.index(characteristic)}"
        return str(characteristic.uuid)

    def read_twin(self, index):
        log(f"read twin {index} -> {self.twin_values[index].hex()}")
        return self.twin_values[index]

    def write_twin(self, index, value):
        log(f"write {value.hex()} to twin {index}")
        self.twin_values[index] = bytes(value)

    def write_control(self, _connection, value):
        command = value[0]
        argument = struct.unpack_from("<H", value, 1)[0] if len(value) >= 3 else 0
        payload = bytes(value[3:])
        log(f"command {value[:32].hex()}" + ("..." if len(value) > 32 else ""))
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
            self.later(argument, lambda: self.notify(self.data, payload))
        elif command == ERROR_CODE:
            self.error_code = argument
        elif command == INDICATE_VALUE:
            self.later(argument, lambda: self.indicate_value(payload))
        elif command == NOTIFY_BURST:
            self.later(0, lambda: self.burst(argument))
        elif command == LONG_LENGTH:
            self.long_value = counting(argument)
        elif command == NOTIFY_TWIN:
            self.later(argument, lambda: self.notify(self.twins[payload[0]], payload[1:]))
        elif command == CHANGE_SERVICES:
            self.later(argument, self.change_services)
        elif command == BEACON_START:
            asyncio.get_running_loop().create_task(self.start_beacon())
        elif command == BEACON_STOP:
            asyncio.get_running_loop().create_task(self.stop_beacon())
        elif command == NOTIFY_LENGTH:
            self.later(0, lambda: self.notify(self.data, counting(argument)))
        elif command == LONG_VALUE:
            self.long_value = payload

    async def drop(self):
        if self.connection is not None:
            log("dropping the link")
            await self.connection.disconnect(hci.HCI_REMOTE_USER_TERMINATED_CONNECTION_ERROR)

    async def notify(self, characteristic, payload):
        if self.connection is not None:
            log(f"notify {payload[:32].hex()}" + ("..." if len(payload) > 32 else "") + f" ({len(payload)} bytes) on {self.describe(characteristic)}")
            if characteristic is self.data:
                self.value = payload
            await self.device.notify_subscriber(self.connection, characteristic, payload)

    async def indicate_value(self, payload):
        if self.connection is not None:
            log(f"indicate {payload.hex()}")
            await self.device.indicate_subscriber(self.connection, self.indicate, payload)
            log("indication confirmed")

    async def burst(self, count):
        log(f"notifying {count} values")
        for index in range(count):
            if self.connection is None:
                return
            await self.device.notify_subscriber(self.connection, self.data, struct.pack("<H", index))

    async def change_services(self):
        if self.connection is None:
            return
        server = self.device.gatt_server
        if self.extra_service not in server.services:
            server.add_service(self.extra_service)
        start, end = self.extra_service.handle, self.extra_service.end_group_handle
        changed = self.device.gatt_service.service_changed_characteristic
        subscribed = server.subscribers.get(self.connection, {}).get(changed.handle)
        log(f"added the extra service at 0x{start:04X}-0x{end:04X}; Service Changed CCCD {subscribed.hex() if subscribed else 'not written'}")
        await self.device.indicate_subscriber(self.connection, changed, struct.pack("<HH", start, end), force=not subscribed)
        log("Service Changed indication confirmed")

    def remove_extra_service(self):
        server = self.device.gatt_server
        if self.extra_service in server.services:
            del server.attributes[server.attributes.index(self.extra_service):]
            server.services.remove(self.extra_service)
            server.attributes_by_handle.clear()
            log("removed the extra service")

    async def start_beacon(self):
        await self.beacon.start_advertising(advertising_type=AdvertisingType.UNDIRECTED_SCANNABLE)
        log("beacon advertising")

    async def stop_beacon(self):
        await self.beacon.stop_advertising()
        log("beacon stopped")

    def on_connection(self, connection: Connection):
        log(f"connected {connection.peer_address}")
        self.reset()
        self.value = INITIAL_VALUE
        self.long_value = b""
        self.sink.clear()
        self.twin_values = list(TWIN_VALUES)
        self.connection = connection

        def on_disconnection(reason):
            log(f"disconnected reason 0x{reason:02X}")
            self.connection = None
            self.reset(release=False)
            for task in list(self.tasks):
                task.cancel()
            self.remove_extra_service()

        connection.on("disconnection", on_disconnection)
        connection.on("connection_att_mtu_update", lambda: log(f"ATT MTU {connection.att_mtu}"))


async def main(transport, address, name, beacon_transport, beacon_address, beacon_name):
    async with await open_transport(transport) as hci_transport, await open_transport(beacon_transport) as beacon_hci_transport:
        device = Device.with_hci(name, Address(address), hci_transport.source, hci_transport.sink)
        beacon = Device.with_hci(beacon_name, Address(beacon_address), beacon_hci_transport.source, beacon_hci_transport.sink)
        peripheral = Peripheral(device, beacon)
        # Legacy advertising data holds 31 bytes: the 128-bit service UUID fills most of it, so the name goes in the scan response.
        device.advertising_data = bytes(AdvertisingData([(AdvertisingData.COMPLETE_LIST_OF_128_BIT_SERVICE_CLASS_UUIDS, bytes(SERVICE))]))
        device.scan_response_data = bytes(AdvertisingData([(AdvertisingData.COMPLETE_LOCAL_NAME, name.encode())]))
        beacon.advertising_data = bytes(
            AdvertisingData(
                [
                    (AdvertisingData.COMPLETE_LIST_OF_128_BIT_SERVICE_CLASS_UUIDS, bytes(BEACON_SERVICE)),
                    (AdvertisingData.SERVICE_DATA_16_BIT_UUID, struct.pack("<H", BEACON_SERVICE_DATA_UUID) + BEACON_SERVICE_DATA),
                ]
            )
        )
        beacon.scan_response_data = bytes(
            AdvertisingData(
                [
                    (AdvertisingData.COMPLETE_LOCAL_NAME, beacon_name.encode()),
                    (AdvertisingData.MANUFACTURER_SPECIFIC_DATA, struct.pack("<H", BEACON_COMPANY) + BEACON_MANUFACTURER_DATA),
                ]
            )
        )
        device.on("connection", peripheral.on_connection)
        await beacon.power_on()
        await device.power_on()
        await device.start_advertising(auto_restart=True)
        log(f"advertising {name} {address}")
        await hci_transport.source.terminated


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--transport", default="android-netsim:name=kaluga-peripheral")
    parser.add_argument("--address", default="F0:F1:F2:F3:F4:F6")
    parser.add_argument("--name", default="kaluga-netsim")
    parser.add_argument("--beacon-transport", default="android-netsim:name=kaluga-beacon")
    parser.add_argument("--beacon-address", default="F0:F1:F2:F3:F4:F7")
    parser.add_argument("--beacon-name", default="kaluga-beacon")
    parser.add_argument("--verbose", action="store_true")
    arguments = parser.parse_args()
    logging.basicConfig(level=logging.DEBUG if arguments.verbose else logging.WARNING, stream=sys.stderr)
    asyncio.run(
        main(
            arguments.transport,
            arguments.address,
            arguments.name,
            arguments.beacon_transport,
            arguments.beacon_address,
            arguments.beacon_name,
        )
    )
