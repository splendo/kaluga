# Bluetooth

Bluetooth Low Energy support for both the Client (scanning / connecting) and Server (advertising / GATT) roles, with a shared attribute (de)serialization framework and Eddystone beacon tracking.

This is a feature group of [Kaluga](https://github.com/splendo/kaluga), containing the following modules:

| Module | Usage | Artifact | Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|---|---|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| [core](core/) | Shared Bluetooth attributes and the BluetoothFormat (de)serialization framework | `com.splendo.kaluga.bluetooth:core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [client](client/) | Scanning for and connecting to BLE devices as a Client | `com.splendo.kaluga.bluetooth:client` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [server](server/) | Advertising and exposing GATT attributes as a Server | `com.splendo.kaluga.bluetooth:server` | ✅ | ✅ |  |  |  | ✅ |  |  |
| [beacons](beacons/) | Tracking the availability of Beacons using the Eddystone protocol | `com.splendo.kaluga.bluetooth:beacons` | ✅ | ✅ |  |  |  | ✅ | ✅ | ✅ |
| [test-utils/core](test-utils/core/) | Test helpers for the Bluetooth core module | `com.splendo.kaluga.bluetooth:test-core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [test-utils/client](test-utils/client/) | Test helpers for the Bluetooth client module | `com.splendo.kaluga.bluetooth:test-client` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [test-utils/server](test-utils/server/) | Test helpers for the Bluetooth server module | `com.splendo.kaluga.bluetooth:test-server` | ✅ | ✅ |  |  |  | ✅ |  |  |
