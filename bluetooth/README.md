# Bluetooth

Bluetooth Low Energy support for both the Client (scanning / connecting) and Server (advertising / GATT) roles, with a shared attribute (de)serialization framework and Eddystone beacon tracking.

This is a feature group of [Kaluga](https://github.com/splendo/kaluga), containing the following modules:

| Module | Usage | Artifact | Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|---|---|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| [core](core/) | Shared Bluetooth attributes and the BytesFormat (de)serialization framework | `com.splendo.kaluga.bluetooth:core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [client](client/) | Scanning for and connecting to BLE devices as a Client | `com.splendo.kaluga.bluetooth:client` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [server](server/) | Advertising and exposing GATT attributes as a Server | `com.splendo.kaluga.bluetooth:server` | ✅ | ✅ |  |  |  | ✅ |  |  |
| [beacons](beacons/) | Tracking the availability of Beacons using the Eddystone protocol | `com.splendo.kaluga.bluetooth:beacons` | ✅ | ✅ |  |  |  | ✅ | ✅ | ✅ |
| [test-utils/core](test-utils/core/) | Test helpers for the Bluetooth core module | `com.splendo.kaluga.bluetooth:test-core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [test-utils/client](test-utils/client/) | Test helpers for the Bluetooth client module | `com.splendo.kaluga.bluetooth:test-client` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [test-utils/server](test-utils/server/) | Test helpers for the Bluetooth server module | `com.splendo.kaluga.bluetooth:test-server` | ✅ | ✅ |  |  |  | ✅ |  |  |
| [annotations](annotations/) | The `@Bluetooth` annotations that describe a device for code generation | `com.splendo.kaluga.bluetooth:annotations` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [ksp](ksp/) | KSP processor that generates clients and servers from `@Bluetooth` definitions (build-time) | `com.splendo.kaluga.bluetooth:ksp` |  |  | ✅ |  |  |  |  |  |
| [plugin](plugin/) | Gradle plugin wiring up the Bluetooth code generation (build-time) | `com.splendo.kaluga.bluetooth.plugin` |  |  | ✅ |  |  |  |  |  |

## Code generation

The [`annotations`](annotations/), [`ksp`](ksp/) and [`plugin`](plugin/) modules together generate typed Bluetooth
clients and servers from an annotated description of a device, so you don't hand-write GATT plumbing or attribute
(de)serialization. See the [code generation plugin README](plugin/) for the full guide.

## Device scenarios

The client and server modules carry scenarios that run against a real peer: `src/deviceScenarios` of each module, compiled
into its Android device tests and its Apple tests. The client scenarios talk to a test peripheral, the server scenarios
to a test central, steered through control characteristics whose layouts are in
[`core/src/deviceScenarios/python/bytes-layouts.json`](core/src/deviceScenarios/python/bytes-layouts.json).

Each side has a switch naming who plays its peer, and does nothing without it:

| Scenarios | Android instrumentation argument | Apple environment variable | Value |
|---|---|---|---|
| client, against the test peripheral | `netsimPeripheral` | `KALUGA_BLUETOOTH_TEST_PERIPHERAL` | `true` for `peripheral.py`, `kaluga` for a kaluga server |
| server, against the test central | `netsimCentral` | `KALUGA_BLUETOOTH_TEST_CENTRAL` | `true` for `central.py`, `kaluga` for a kaluga client |

The bumble scripts (`client/src/deviceScenarios/python/peripheral.py`, `server/src/deviceScenarios/python/central.py`)
run on the emulator's virtual radio or, with `--transport`, on a USB controller in range. Kaluga plays a peer on another
device: `KalugaTestPeripheral` of the server module with `playTestPeripheral` / `KALUGA_BLUETOOTH_PLAY_TEST_PERIPHERAL`,
`KalugaTestCentral` of the client module with `playTestCentral` / `KALUGA_BLUETOOTH_PLAY_TEST_CENTRAL`. Each player lists
what it can do (`TestPeripheral.Capability`, `TestCentral.Capability`); a scenario that needs more, such as ending a link,
holding a request, refusing to pair, capping the MTU or sending prepared writes of its own, is skipped against a kaluga
player with the reason.

[`script/run_bluetooth_kaluga_peer_scenarios.sh`](../script/run_bluetooth_kaluga_peer_scenarios.sh) runs one module's
scenarios between an Android device and a Mac in radio range of each other, kaluga playing the peer on the other one:
`client android`, `client macos`, `server android` or `server macos`.
