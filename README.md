[![Maven Central](https://img.shields.io/maven-central/v/com.splendo.kaluga/base)](https://central.sonatype.com/search?q=g:com.splendo.kaluga)
![kaluga logo](https://raw.githubusercontent.com/splendo/kaluga/b1198b0427046f7aa3de5f74fd2fcebd461eb6c1/logo/Logo.svg)

This project is named after the Kaluga, the world's biggest freshwater fish, which is found in the icy Amur river.

Kaluga's main goal is to provide access to common features used in cross-platform mobile app development, separated into modules such as architecture (MVVM), location, permissions, bluetooth etc.

To reach this goal it uses Kotlin, specifically [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) which allows running Kotlin code not just on JVM+Android, but also iOS/iPadOS, amongst others (inndeed some kaluga modules also work for Kotlin.js and/or JVM standalone).

Where appropriate coroutines and `Flow` are used in the API. This enables developers to use [cold streams](https://medium.com/@elizarov/cold-flows-hot-channels-d74769805f9) for a modern and efficient design.

While Kaluga modules can be used individually, together they form a comprehensive approach to cross-platform development with [shared native code](https://kotlinlang.org/docs/mpp-share-on-platforms.html) and native UIs, including SwiftUI and Compose. 

### Short examples

With Kaluga it is possible to create cross-platform functionality in a few lines of code, that would normally take many lines of code even on just one platform. 

Below are some examples, using a [`commonMain` source-set](https://kotlinlang.org/docs/mpp-dsl-reference.html#predefined-source-sets):

Scanning for nearby devices with Bluetooth LE:

```kotlin
// will auto request permissions and try to enable bluetooth
BluetoothClientBuilder().createClient().devices().collect {
    i("discovered device: $it") // log found device
}
```

Showing a spinner while doing some work:

```kotlin

suspend fun doWork(hudBuilder: HUD.Builder) {
    hudBuilder.presentDuring { // shows spinner while code in this block is running
        // simulate doing work
        delay(1000)
    }
}
    
```

in this case, since HUD is a UI component the builder needs to be configured on the platform side:
```kotlin
val builder = HUD.Builder() // same for iOS and Android
// ...
builder.subscribe(activity) // this needs be done in the Android source-set to bind the HUD to the lifecycle of the Activity
// ...
builder.unsubscribe(activity) // when the Activity is stopped
```

However Kaluga's [architecture module](architecture/) offers a cross-platform [`LifecycleViewModel`](architecture/architecture/src/commonMain/kotlin/viewmodel/LifecycleViewModel.kt) class (which extends `androidx.lifecycle.ViewModel` on Android) that will automatically bind the builder to its lifecycle:

```kotlin
// this can just be in the commonMain source
class HudViewModel(private val hudBuilder: HUD.Builder): BaseLifecycleViewModel(hudBuilder) {
    
    suspend fun doWork() = 
        hudBuilder.presentDuring {
            delay(1000)
        }
}
```
### More examples

Kaluga contains [an example project](example/) that is used to test the developed modules.

## Using Kaluga

For starting a new project based on Kaluga see the [kaluga-starter repo](https://github.com/splendo/kaluga-starter), which shows how to do this step by step.

Kaluga is available on Maven Central. For example the Kaluga Alerts can be imported like this:

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("com.splendo.kaluga.alerts:alerts:$kalugaVersion")
}
```

You can also use the `SNAPSHOT` version based on the latest in the `develop` branch:

```kotlin
repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}
dependencies {
    implementation("com.splendo.kaluga.alerts:alerts:$kalugaDevelopVersion-SNAPSHOT")
}
```

To use kaluga with SwiftUI and/or Combine we have a [repo with Sourcery templates](https://github.com/splendo/kaluga-swiftui) to generate some Swift code to help get you started.

### Available Modules

Modules are organized into feature groups; each group directory has its own `README` with more detail. The table below lists every published module, its artifact coordinate, and the platforms it supports.

| Module | Usage | Artifact | Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|---|---|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| [alerts/alerts](alerts/alerts/) | Showing alert dialogs | `com.splendo.kaluga.alerts:alerts` | ✅ | ✅ |  |  |  |  |  |  |
| [alerts/test-utils](alerts/test-utils/) | Test helpers for the alerts module | `com.splendo.kaluga.alerts:test` | ✅ | ✅ |  |  |  |  |  |  |
| [architecture/architecture](architecture/architecture/) | MVVM architecture | `com.splendo.kaluga.architecture:architecture` | ✅ | ✅ |  |  |  |  |  |  |
| [architecture/compose](architecture/compose/) | Compose extensions for architecture | `com.splendo.kaluga.architecture:compose` | ✅ |  |  |  |  |  |  |  |
| [architecture/test-utils](architecture/test-utils/) | Test helpers for the architecture module | `com.splendo.kaluga.architecture:test` | ✅ | ✅ |  |  |  |  |  |  |
| [architecture/test-koin](architecture/test-koin/) | Koin-based test helpers | `com.splendo.kaluga.architecture:test-koin` | ✅ | ✅ |  |  |  |  |  |  |
| [base/core](base/core/) | Threading, flowables and concurrent collections | `com.splendo.kaluga.base:core` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/state](base/state/) | State machines (`KalugaState`, `StateRepo`) | `com.splendo.kaluga.base:state` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/decimal](base/decimal/) | High-precision decimal arithmetic | `com.splendo.kaluga.base:decimal` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/bytes](base/bytes/) | ByteArray utilities, hex conversion and numeric byte encoding | `com.splendo.kaluga.base:bytes` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/crc](base/crc/) | Cyclic Redundancy Check (CRC) calculation | `com.splendo.kaluga.base:crc` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/i18n](base/i18n/) | Locales, unit systems and locale-aware String casing | `com.splendo.kaluga.base:i18n` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/formatting](base/formatting/) | Formatting and parsing of numbers and strings | `com.splendo.kaluga.base:formatting` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [base/test-utils](base/test-utils/) | Test helpers built on top of base | `com.splendo.kaluga.base:test` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bluetooth/core](bluetooth/core/) | Shared Bluetooth attributes and the BluetoothFormat (de)serialization framework | `com.splendo.kaluga.bluetooth:core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bluetooth/client](bluetooth/client/) | Scanning for and connecting to BLE devices as a Client | `com.splendo.kaluga.bluetooth:client` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bluetooth/server](bluetooth/server/) | Advertising and exposing GATT attributes as a Server | `com.splendo.kaluga.bluetooth:server` | ✅ | ✅ |  |  |  | ✅ |  |  |
| [bluetooth/beacons](bluetooth/beacons/) | Tracking the availability of Beacons using the Eddystone protocol | `com.splendo.kaluga.bluetooth:beacons` | ✅ | ✅ |  |  |  | ✅ | ✅ | ✅ |
| [bluetooth/test-utils/core](bluetooth/test-utils/core/) | Test helpers for the Bluetooth core module | `com.splendo.kaluga.bluetooth:test-core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bluetooth/test-utils/client](bluetooth/test-utils/client/) | Test helpers for the Bluetooth client module | `com.splendo.kaluga.bluetooth:test-client` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bluetooth/test-utils/server](bluetooth/test-utils/server/) | Test helpers for the Bluetooth server module | `com.splendo.kaluga.bluetooth:test-server` | ✅ | ✅ |  |  |  | ✅ |  |  |
| [date-time/date-time](date-time/date-time/) | Dates, time zones and date formatting | `com.splendo.kaluga.date-time:date-time` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [date-time/timer](date-time/timer/) | A RecurringTimer providing ticks at regular intervals | `com.splendo.kaluga.date-time:timer` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [date-time-picker/date-time-picker](date-time-picker/date-time-picker/) | Showing a Date or Time Picker | `com.splendo.kaluga.date-time-picker:date-time-picker` | ✅ | ✅ |  |  |  |  |  |  |
| [date-time-picker/test-utils](date-time-picker/test-utils/) | Test helpers for the Date Time Picker module | `com.splendo.kaluga.date-time-picker:test` | ✅ | ✅ |  |  |  |  |  |  |
| [hud/hud](hud/hud/) | Showing a loading-indicator HUD | `com.splendo.kaluga.hud:hud` | ✅ | ✅ |  |  |  |  |  |  |
| [hud/test-utils](hud/test-utils/) | Test helpers for the HUD module | `com.splendo.kaluga.hud:test` | ✅ | ✅ |  |  |  |  |  |  |
| [keyboard/keyboard](keyboard/keyboard/) | Showing and hiding the keyboard | `com.splendo.kaluga.keyboard:keyboard` | ✅ | ✅ |  |  |  |  |  |  |
| [keyboard/compose](keyboard/compose/) | Compose extensions for keyboard | `com.splendo.kaluga.keyboard:compose` | ✅ |  |  |  |  |  |  |  |
| [keyboard/test-utils](keyboard/test-utils/) | Test helpers for the keyboard module | `com.splendo.kaluga.keyboard:test` | ✅ | ✅ |  |  |  |  |  |  |
| [lifecycle/lifecycle](lifecycle/lifecycle/) | Platform-host lifecycle bindings for service builders | `com.splendo.kaluga.lifecycle:lifecycle` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [lifecycle/compose](lifecycle/compose/) | Compose extensions for lifecycle | `com.splendo.kaluga.lifecycle:compose` | ✅ | ✅ |  |  | ✅ | ✅ |  |  |
| [lifecycle/test-utils](lifecycle/test-utils/) | Test helpers for the lifecycle module | `com.splendo.kaluga.lifecycle:test` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [links](links/) | Decoding a url into an object | `com.splendo.kaluga:links` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [location/location](location/location/) | Provides the user's geolocation | `com.splendo.kaluga.location:location` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [location/test-utils](location/test-utils/) | Test helpers for the location module | `com.splendo.kaluga.location:test` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [logging](logging/) | Shared console logging | `com.splendo.kaluga:logging` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [media/media](media/media/) | Playing audio/video | `com.splendo.kaluga.media:media` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ |  |
| [media/compose](media/compose/) | Compose extensions for media | `com.splendo.kaluga.media:compose` | ✅ | ✅ |  |  | ✅ | ✅ |  |  |
| [media/test-utils](media/test-utils/) | Test helpers for the media module | `com.splendo.kaluga.media:test` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ |  |
| [permissions/core](permissions/core/) | The permissions framework, used in conjunction with the per-type modules | `com.splendo.kaluga.permissions:core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [permissions/bluetooth](permissions/bluetooth/) | Managing Bluetooth permissions | `com.splendo.kaluga.permissions:bluetooth` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [permissions/calendar](permissions/calendar/) | Managing calendar permissions | `com.splendo.kaluga.permissions:calendar` | ✅ | ✅ |  |  |  | ✅ |  | ✅ |
| [permissions/camera](permissions/camera/) | Managing camera permissions | `com.splendo.kaluga.permissions:camera` | ✅ | ✅ |  | ✅ | ✅ | ✅ |  |  |
| [permissions/contacts](permissions/contacts/) | Managing contacts permissions | `com.splendo.kaluga.permissions:contacts` | ✅ | ✅ |  |  |  | ✅ |  | ✅ |
| [permissions/location](permissions/location/) | Managing location permissions | `com.splendo.kaluga.permissions:location` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [permissions/microphone](permissions/microphone/) | Managing microphone permissions | `com.splendo.kaluga.permissions:microphone` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [permissions/notifications](permissions/notifications/) | Managing notifications permissions | `com.splendo.kaluga.permissions:notifications` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [permissions/storage](permissions/storage/) | Managing storage permissions | `com.splendo.kaluga.permissions:storage` | ✅ | ✅ |  |  |  | ✅ | ✅ |  |
| [permissions/test-utils](permissions/test-utils/) | Test helpers for the Permissions modules | `com.splendo.kaluga.permissions:test` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [resources/resources](resources/resources/) | Shared Strings, Images, Colors and Fonts | `com.splendo.kaluga.resources:resources` | ✅ | ✅ |  |  |  |  |  |  |
| [resources/compose](resources/compose/) | Compose extensions for resources | `com.splendo.kaluga.resources:compose` | ✅ |  |  |  |  |  |  |  |
| [resources/databinding](resources/databinding/) | Data Binding extensions for resources | `com.splendo.kaluga.resources:databinding` | ✅ |  |  |  |  |  |  |  |
| [resources/test-utils](resources/test-utils/) | Test helpers for the resources module | `com.splendo.kaluga.resources:test` | ✅ | ✅ |  |  |  |  |  |  |
| [review](review/) | Requesting the user to review the app | `com.splendo.kaluga:review` | ✅ | ✅ |  |  |  | ✅ |  |  |
| [scientific/scientific](scientific/scientific/) | Scientific units and conversions | `com.splendo.kaluga.scientific:scientific` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [scientific/converters](scientific/converters/) | Converters between scientific units | `com.splendo.kaluga.scientific:converters` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [service/service](service/service/) | Adding services to Kaluga | `com.splendo.kaluga.service:service` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [service/test-utils](service/test-utils/) | Test helpers for the service module | `com.splendo.kaluga.service:test` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| [system/system](system/system/) | System APIs such as network, audio, battery | `com.splendo.kaluga.system:system` | ✅ | ✅ |  | ✅ | ✅ | ✅ |  |  |
| [system/test-utils](system/test-utils/) | Test helpers for the system module | `com.splendo.kaluga.system:test` | ✅ | ✅ |  | ✅ | ✅ | ✅ |  |  |

### Friends of kaluga

Of course not every possible functionality is provided by kaluga. However, this is often because other good multiplatform libraries that work nicely with kaluga already exist. These use similar patterns such as coroutines and `Flow`, and include the following:

| Project                                                                       | Usage                                  |
|-------------------------------------------------------------------------------|----------------------------------------|
| [kotlin-firebase-sdk](https://github.com/GitLiveApp/firebase-kotlin-sdk)      | wraps most of the Firebase SDK APIs    |
| [multiplatform-settings](https://github.com/russhwolf/multiplatform-settings) | store key/value data                   |
| [SQLDelight](https://github.com/cashapp/sqldelight)                           | access SQLite (and other SQL database) |

Kaluga also uses some multiplatform libraries itself, so our thanks to:

| Project                                        | Usage                     |
|------------------------------------------------|---------------------------|
| [Napier](https://github.com/AAkira/Napier)     | powers the logging module |
| [Koin](https://insert-koin.io/)                | dependency injection      |

## Developing Kaluga

see [DEVELOP](DEVELOP.md).
