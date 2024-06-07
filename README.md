[![Maven Central](https://img.shields.io/maven-central/v/com.splendo.kaluga/base)](https://central.sonatype.com/search?q=g:com.splendo.kaluga) [![Build Status](https://app.bitrise.io/app/14b7d4be75507f70/status.svg?token=KawVB7619B-KRBdIADdplg&branch=master)](https://app.bitrise.io/app/14b7d4be75507f70)<sup>🤖</sup> [![Build Status](https://app.bitrise.io/app/67ee1b576288ad31/status.svg?token=hUq90QsaCPe2nyWxyX1KGg&branch=master)](https://app.bitrise.io/app/67ee1b576288ad31)<sup>🍏</sup>

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
BluetoothBuilder().create().devices().collect {
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

However Kaluga's [architecture module](architecture/) offers a cross-platform [`LifecycleViewModel`](architecture/src/commonMain/kotlin/viewmodel/LifecycleViewModel.kt) class (which extends `androidx.lifecycle.ViewModel` on Android) that will automatically bind the builder to its lifecycle:

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
    implementation("com.splendo.kaluga:alerts:$kalugaVersion")
}
```

You can also use the `SNAPSHOT` version based on the latest in the `develop` branch:

```kotlin
repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}
dependencies {
    implementation("com.splendo.kaluga:alerts:$kalugaDevelopVersion-SNAPSHOT")
}
```

To use kaluga with SwiftUI and/or Combine we have a [repo with Sourcery templates](https://github.com/splendo/kaluga-swiftui) to generate some Swift code to help get you started.

### Available Modules

| Module                                                      | Usage                                                                              | Artifact Name                                  |
|-------------------------------------------------------------|------------------------------------------------------------------------------------|------------------------------------------------|
| [alerts](alerts/)                                           | Used for Showing Alert Dialogs                                                     | com.splendo.kaluga:alerts                      |
| [architecture](architecture/)                               | MVVM architecture                                                                  | com.splendo.kaluga:architecture                |
| [architecture-compose](architecture-compose/)               | Compose extensions for architecture                                                | com.splendo.kaluga:architecture-compose        |
| [base](base/)                                               | Core components of Kaluga. Contains threading, flowables and localization features | com.splendo.kaluga:base                        |
| [beacons](beacons/)                                         | Tracking the availability of Beacons using the Eddystone protocol                  | com.splendo.kaluga:beacons                     |
| [bluetooth](bluetooth/)                                     | Scanning for and communicating with BLE devices                                    | com.splendo.kaluga:bluetooth                   |
| [date-time](date-time/)                                     | Contains multiplatform classes to work with date and time                          | com.splendo.kaluga:date-time                   |
| [date-timepicker](date-time-picker/)                        | Used for showing a Date or Time Picker                                             | com.splendo.kaluga:date-time-picker            |
| [hud](hud/)                                                 | Used for showing a Loading indicator HUD                                           | com.splendo.kaluga:hud                         |
| [keyboard](keyboard/)                                       | Used for showing and hiding the keyboard                                           | com.splendo.kaluga:keyboard                    |
| [keyboard-compose](keyboard-compose/)                       | Compose extensions for keyboard                                                    | com.splendo.kaluga:keyboard-compose            |
| [links](links/)                                             | Used for decoding url into an object                                               | com.splendo.kaluga:links                       |
| [location](location/)                                       | Provides the User' geolocation                                                     | com.splendo.kaluga:location                    |
| [logging](logging/)                                         | Shared console logging                                                             | com.splendo.kaluga:logging                     |
| [media](media/)                                             | Playing audio/video                                                                | com.splendo.kaluga:media                       |
| [base-permissions](base-permissions/)                       | Managing permissions, used in conjunction with modules below                       | com.splendo.kaluga:base-permissions            |
| [bluetooth-permissions](bluetooth-permissions/)             | Managing bluetooth permissions                                                     | com.splendo.kaluga:bluetooth-permissions       |
| [calendar-permissions](calendar-permissions/)               | Managing calendar permissions                                                      | com.splendo.kaluga:calendar-permissions        |
| [camera-permissions](camera-permissions/)                   | Managing camera permissions                                                        | com.splendo.kaluga:camera-permissions          |
| [contacts-permissions](contacts-permissions/)               | Managing contacts permissions                                                      | com.splendo.kaluga:contacts-permissions        |
| [location-permissions](location-permissions/)               | Managing location permissions                                                      | com.splendo.kaluga:location-permissions        |
| [microphone-permissions](microphone-permissions/)           | Managing microphone permissions                                                    | com.splendo.kaluga:microphone-permissions      |
| [notifications-permissions](notifications-permissions/)     | Managing notifications permissions                                                 | com.splendo.kaluga:notifications-permissions   |
| [storage-permissions](storage-permissions/)                 | Managing storage permissions                                                       | com.splendo.kaluga:storage-permissions         |
| [resources](resources/)                                     | Provides shared Strings, Images, Colors and Fonts                                  | com.splendo.kaluga:resources                   |
| [resources-compose](resources-compose/)                     | Compose extensions for resources                                                   | com.splendo.kaluga:resources-compose           |
| [resources-databinding](resources-databinding/)             | Data Binding extensions for resources                                              | com.splendo.kaluga:resources-databinding       |
| [review](review/)                                           | Used for requesting the user to review the app                                     | com.splendo.kaluga:review                      |
| [scientific](scientific/)                                   | Scientific units and conversions                                                   | com.splendo.kaluga:scientific                  |
| [service](service/)                                         | Used for adding services to Kaluga                                                 | com.splendo.kaluga:service                     |
| [system](system/)                                           | System APIs such as network, audio, battery                                        | com.splendo.kaluga:system                      |
| [test-utils](test-utils/)                                   | Enables easier testing of Kaluga components                                        | com.splendo.kaluga:test-utils                  |
| [test-utils-base](test-utils-base/)                         | Enables easier testing of Kaluga components                                        | com.splendo.kaluga:test-utils-base             |
| [test-utils-alerts](test-utils-alerts/)                     | Enables easier testing of Alerts module                                            | com.splendo.kaluga:test-utils-alerts           |
| [test-utils-architecture](test-utils-architecture/)         | Enables easier testing of Architecture module                                      | com.splendo.kaluga:test-utils-architecture     |
| [test-utils-bluetooth](test-utils-bluetooth/)               | Enables easier testing of Bluetooth module                                         | com.splendo.kaluga:test-utils-bluetooth        |
| [test-utils-date-time-picker](test-utils-date-time-picker/) | Enables easier testing of Date Time Picker module                                  | com.splendo.kaluga:test-utils-date-time-picker |
| [test-utils-hud](test-utils-hud/)                           | Enables easier testing of HUD module                                               | com.splendo.kaluga:test-utils-hud              |
| [test-utils-keyboard](test-utils-keyboard/)                 | Enables easier testing of Keyboard module                                          | com.splendo.kaluga:test-utils-keyboard         |
| [test-utils-koin](test-utils-koin/)                         | Enables easier testing with Koin                                                   | com.splendo:kaluga.test-utils-koin             |
| [test-utils-location](test-utils-location/)                 | Enables easier testing of Location module                                          | com.splendo.kaluga:test-utils-location         |
| [test-utils-permissions](test-utils-permissions/)           | Enables easier testing of Permissions modules                                      | com.splendo.kaluga:test-utils-permissions      |
| [test-utils-resources](test-utils-resources/)               | Enables easier testing of Resources module                                         | com.splendo.kaluga:test-utils-resources        |
| [test-utils-service](test-utils-service/)                   | Enables easier testing of Service module                                           | com.splendo.kaluga:test-utils-service          |
| [test-utils-system](test-utils-system/)                     | Enables easier testing of System module                                            | com.splendo.kaluga:test-utils-system           |

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
