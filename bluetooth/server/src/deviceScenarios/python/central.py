"""Bumble GATT central for the Bluetooth server device tests, steered by the test itself.

By default it runs on the Android emulator's netsim radio. With `--transport` it runs on any controller bumble can
drive, such as a USB HCI controller (`serial:/dev/cu.usbmodem2101`) in range of the test's own radio.

The central scans for the test server's service, connects, subscribes to its control characteristic and carries out
every instruction the server notifies there. It answers each instruction by writing a result to the result
characteristic. When the link ends it scans again, so each test gets a fresh connection by advertising.

Each instruction is a `Message` notification and each answer a `Report` write, layouts of `bytes-layouts.json` in the
core module's `src/deviceScenarios/python`, which this script reads at start-up. A target is the last byte of a
characteristic UUID of the server's service.
"""

import argparse
import asyncio
import logging
import os
import struct
import sys
import time

from bumble import att, hci
from bumble.core import UUID, AdvertisingData
from bumble.device import Connection, Device, Peer
from bumble.hci import Address
from bumble.transport import open_transport

PROTOCOLS = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "..", "..", "core", "src", "deviceScenarios", "python")
sys.path.insert(0, PROTOCOLS)
from bytes_layout import Schema  # noqa: E402

CONTROL_PROTOCOL = Schema.load(os.path.join(PROTOCOLS, "bytes-layouts.json"))

SERVICE = UUID("6b7b0000-5d1c-4f0e-9a51-2f6f3c1d8e01")
CONTROL = UUID("6b7b0001-5d1c-4f0e-9a51-2f6f3c1d8e01")
RESULT = UUID("6b7b0002-5d1c-4f0e-9a51-2f6f3c1d8e01")

ECHO_MISMATCH, NO_TARGET, FAILURE = 0xE0, 0xE1, 0xE2

START = time.monotonic()


def log(message):
    print(f"{time.monotonic() - START:9.3f} {message}", flush=True)


def target_uuid(target):
    return UUID(f"6b7b00{target:02x}-5d1c-4f0e-9a51-2f6f3c1d8e01")


class Central:
    def __init__(self, device: Device):
        self.device = device
        self.found: asyncio.Future | None = None

    def on_advertisement(self, advertisement):
        if self.found is None or self.found.done():
            return
        uuids = []
        for kind in (AdvertisingData.COMPLETE_LIST_OF_128_BIT_SERVICE_CLASS_UUIDS, AdvertisingData.INCOMPLETE_LIST_OF_128_BIT_SERVICE_CLASS_UUIDS):
            for value in advertisement.data.get_all(kind):
                uuids += value if isinstance(value, list) else [value]
        if SERVICE in uuids and advertisement.is_connectable:
            self.found.set_result(advertisement.address)

    async def run(self):
        self.device.on("advertisement", self.on_advertisement)
        while True:
            self.found = asyncio.get_running_loop().create_future()
            await self.device.start_scanning(filter_duplicates=False)
            log("scanning")
            address = await self.found
            await self.device.stop_scanning()
            log(f"found the server at {address}")
            try:
                connection = await self.device.connect(address, timeout=10)
            except Exception as error:
                log(f"connecting failed: {error!r}")
                continue
            await Session(connection).serve()


class Session:
    def __init__(self, connection: Connection):
        self.connection = connection
        self.peer = Peer(connection)
        self.client = connection.gatt_client
        self.staged = bytearray()
        self.instructions: asyncio.Queue = asyncio.Queue()
        self.ended = asyncio.get_running_loop().create_future()
        self.characteristics = {}
        self.result = None

    async def serve(self):
        log(f"connected {self.connection.peer_address}")
        self.connection.on("disconnection", lambda reason: self.on_disconnection(reason))
        try:
            await self.peer.discover_services([SERVICE])
            await self.peer.discover_characteristics()
            self.result = self.characteristic(RESULT)
            control = self.characteristic(CONTROL)
            await self.peer.subscribe(control, lambda value: self.instructions.put_nowait(bytes(value)))
            log("subscribed to the control characteristic")
        except Exception as error:
            log(f"setting up the session failed: {error!r}")
            await self.disconnect()
            await self.ended
            return
        worker = asyncio.get_running_loop().create_task(self.work())
        await self.ended
        worker.cancel()

    def on_disconnection(self, reason):
        log(f"disconnected reason 0x{reason:02X}")
        if not self.ended.done():
            self.ended.set_result(None)

    def characteristic(self, uuid):
        for characteristic in self.peer.get_characteristics_by_uuid(uuid, SERVICE):
            return characteristic
        return None

    async def work(self):
        while True:
            message = await self.instructions.get()
            log(f"instruction {message.hex()}")
            try:
                decoded = CONTROL_PROTOCOL.decode("Message", message)
            except Exception as error:
                log(f"undecodable instruction: {error!r}")
                continue
            identifier, instruction = decoded["id"], decoded["instruction"]
            try:
                status, data, after = await self.carry_out(instruction)
            except att.ATT_Error as error:
                status, data, after = error.error_code, b"", None
            except Exception as error:
                log(f"instruction failed: {error!r}")
                status, data, after = FAILURE, b"", None
            await self.answer({"type": "Result", "id": identifier, "status": status, "data": data})
            if after is not None:
                await after()

    async def answer(self, report):
        result = CONTROL_PROTOCOL.encode("Report", report)
        log(f"result {result.hex()}")
        try:
            await self.client.write_value(self.result, result, with_response=True)
        except Exception as error:
            log(f"writing the result failed: {error!r}")

    async def carry_out(self, instruction):
        kind = instruction["type"]
        if kind == "Stage":
            self.staged += instruction["bytes"]
            return 0, struct.pack("<H", len(self.staged)), None
        if kind == "Clear":
            self.staged.clear()
            return 0, b"", None
        if kind == "Disconnect":
            return 0, b"", self.disconnect
        if kind == "Execute":
            return await self.execute(instruction["flags"]), b"", None
        if kind == "RequestMtu":
            mtu = await self.client.request_mtu(instruction["mtu"])
            log(f"ATT MTU {mtu}")
            return 0, struct.pack("<H", mtu), None
        target = self.characteristic(target_uuid(instruction["target"]))
        if target is None:
            return NO_TARGET, b"", None
        if kind == "Write":
            return await self.write(target.handle, bytes(self.staged)), b"", None
        if kind == "Prepare":
            start, length = instruction["start"], instruction["length"]
            return await self.prepare(target.handle, instruction["offset"], bytes(self.staged[start : start + length])), b"", None
        if kind == "Subscribe":
            number = instruction["target"]
            await self.peer.subscribe(target, lambda value: asyncio.get_running_loop().create_task(self.forward(number, bytes(value))))
            return 0, b"", None
        if kind == "Unsubscribe":
            await self.peer.unsubscribe(target)
            return 0, b"", None
        raise ValueError(f"no handler for the instruction {kind}")

    async def forward(self, target, value):
        log(f"notification from 0x{target:02X}: {value.hex()}")
        await self.answer({"type": "Forwarded", "target": target, "value": value})

    async def write(self, handle, value):
        if len(value) <= self.client.mtu - 3:
            response = await self.client.send_request(att.ATT_Write_Request(attribute_handle=handle, attribute_value=value))
            return response.error_code if response.op_code == att.Opcode.ATT_ERROR_RESPONSE else 0
        part_size = self.client.mtu - 5
        for offset in range(0, len(value), part_size):
            status = await self.prepare(handle, offset, value[offset : offset + part_size])
            if status:
                await self.execute(0)
                return status
        return await self.execute(1)

    async def prepare(self, handle, offset, part):
        response = await self.client.send_request(
            att.ATT_Prepare_Write_Request(attribute_handle=handle, value_offset=offset, part_attribute_value=part)
        )
        if response.op_code == att.Opcode.ATT_ERROR_RESPONSE:
            log(f"prepared write at {offset} -> ATT error 0x{response.error_code:02X}")
            return response.error_code
        echoed = (response.attribute_handle, response.value_offset, bytes(response.part_attribute_value))
        if echoed != (handle, offset, part):
            # A central has to check the echo of each part: a mismatch means the server queued something else.
            log(f"prepared write at {offset}: sent {len(part)} bytes, echo {echoed[1]} {echoed[2].hex()}")
            return ECHO_MISMATCH
        return 0

    async def execute(self, flags):
        response = await self.client.send_request(att.ATT_Execute_Write_Request(flags=flags))
        if response.op_code == att.Opcode.ATT_ERROR_RESPONSE:
            log(f"execute write {flags} -> ATT error 0x{response.error_code:02X}")
            return response.error_code
        return 0

    async def disconnect(self):
        log("disconnecting")
        try:
            await self.connection.disconnect(hci.HCI_REMOTE_USER_TERMINATED_CONNECTION_ERROR)
        except Exception as error:
            log(f"disconnecting failed: {error!r}")


async def main(transport, address, name):
    async with await open_transport(transport) as hci_transport:
        device = Device.with_hci(name, Address(address), hci_transport.source, hci_transport.sink)
        await device.power_on()
        await Central(device).run()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--transport", default="android-netsim:name=kaluga-central")
    parser.add_argument("--address", default="F0:F1:F2:F3:F4:F8")
    parser.add_argument("--name", default="kaluga-central")
    parser.add_argument("--verbose", action="store_true")
    arguments = parser.parse_args()
    logging.basicConfig(level=logging.DEBUG if arguments.verbose else logging.WARNING, stream=sys.stderr)
    asyncio.run(main(arguments.transport, arguments.address, arguments.name))
