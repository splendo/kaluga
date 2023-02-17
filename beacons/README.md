## Beacons

This library provides support for out-of-the-box access to Beacons.
Currently only the [Eddystone protocol](https://github.com/google/eddystone/blob/master/protocol-specification.md) is supported

## Installing
This library is available on Maven Central. You can import Kaluga Beacons as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:beacons:$kalugaVersion")
}
```

### Usage
Create a `DefaultBeacons` object. This gives you access to a `Flow` of `Set<BeaconInfo>`. To monitor for beacons simply call:

```kotlin
val beacons = DefaultBeacons(
    bluetooth = // Bluetooth
    beaconLifetime = 10.seconds,
    coroutineContext = // CoroutineContext
)
beacons.startMonitoring()
launch {
    beacons.beacons.collect {}
}
///
beacons.stopMonitoring()
```

