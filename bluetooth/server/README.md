# Bluetooth Server

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  | ✅ |  |  |

This library provides support for acting as a Bluetooth Server: advertising and exposing GATT attributes for clients to read, write and subscribe to.

## Installing
This library is available on Maven Central. You can import Kaluga Bluetooth Server as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bluetooth:server:$kalugaVersion")
}
```

## Usage
Create a `BluetoothServer` through the `BluetoothServerBuilder`. In it, specify the advertising and the attributes to support:

```kotlin

val server = builder.createServer(context) {
    advertise {
        localName = "Kaluga Server"
        serviceUUIDs(kalugaUUID)
    }
    service(kalugaUUID) {
        characteristic(characteristicUUID) {
            readable {
                GattResponse.ReadSuccess(byteArrayOf())
            }
            writable { _, data, _ ->
                println("Did write ${data.toHexString()}")
                GattResponse.WriteSuccess
            }
            notifiable {
                onSubscribe {
                    coroutineScope.launch {
                        notify(byteArrayOf())
                    }
                }
            }
            
            // Alternative notification
            flowOf(byteArrayOf()).collectTo(coroutineScope) {
                triggerNotification()
            }
            
            // Ignored on iOS
            descriptor(descriptorUUID) {
                
            }
        }
    }
}

val notifiableCharacteristic = server.services[kalugaUUID].characteristics[characteristicUUID] as LocalCharacteristic.Notifiable
notifiableCharacteristic.notifyAll(byteArrayOf())

server.close() // server must be closed when done
```

For (de)serializing the data exchanged with clients, see the `BluetoothFormat` documentation in [`core`](../core/).
