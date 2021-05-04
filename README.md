## Kaluga

This project is named after the Kaluga, the world's biggest freshwater fish, which is found in the icy Amur river.

Its main goal is to provide access to common multiplatform features used in mobile app development, such as MVVM-Architecture, location, permissions, bluetooth etc.

Where appropriate it uses Coroutines, Channels and Flow. This enables developers to use [cold streams](https://medium.com/@elizarov/cold-flows-hot-channels-d74769805f9) from Kotlin code that is shared amongst multiple platforms such as Android and iOS.

## Installing
Kaluga is currently only available through bintray. Add `https://dl.bintray.com/kaluga/com.splendo.kaluga/` as a maven repository to your project to import different kaluga modules. For example the Kaluga Alerts can be imported like this:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:alerts:$kalugaVersion")
}
```

### Available Modules
Module | Usage | Library Name | Latest Version
--- | --- | --- | ---
[Alerts](https://github.com/splendo/kaluga/tree/master/alerts) | Used for Showing Alert Dialogs | com.splendo.kaluga.alerts | 0.1.9
[Architecture](https://github.com/splendo/kaluga/tree/master/architecture) | MVVM architecture | com.splendo.kaluga.architecture | 0.1.9
[Base](https://github.com/splendo/kaluga/tree/master/base) | Core components of Kaluga. Contains threading, flowables and localization features | com.splendo.kaluga.base | 0.1.9
[DateTimePicker](https://github.com/splendo/kaluga/tree/master/date-time-picker) | Used for showing a Date or Time Picker | com.splendo.kaluga.date-time-picker | 0.1.9
[HUD](https://github.com/splendo/kaluga/tree/master/hud) | Used for showing a Loading indicator HUD | com.splendo.kaluga.hud | 0.1.9
[Keyboard](https://github.com/splendo/kaluga/tree/master/keyboard) | Used for showing and hiding the keyboard | com.splendo.kaluga.keyboard | 0.1.9
[Location](https://github.com/splendo/kaluga/tree/master/location) | Provides the User' geolocation | com.splendo.kaluga.location | 0.1.9
[Logging](https://github.com/splendo/kaluga/tree/master/logging) | Shared console logging | com.splendo.kaluga.logging | 0.1.9
[Permissions](https://github.com/splendo/kaluga/tree/master/permissions) | Managing user permissions | com.splendo.kaluga.permissions | 0.1.9
[Resources](https://github.com/splendo/kaluga/tree/master/resources) | Provides shared Strings, Images, Colors and Fonts | com.splendo.kaluga.resources | 0.1.9
[Review](https://github.com/splendo/kaluga/tree/master/review) | Used for requesting the user to review the app | com.splendo.kaluga.review | 0.1.9
[System](https://github.com/splendo/kaluga/tree/master/system) | System APIs such as network, audio, battery  | com.splendo.kaluga.system | 0.1.9
[TestUtils](https://github.com/splendo/kaluga/tree/master/test-utils) | Enables easier testing of Kaluga components | com.splendo.kaluga.test-utils | 0.1.9

## Build instructions

This project uses Android Studio. You might need a canary version at times. 
______
Both IDEA and Android Studio (at time of writing 10.09.2019) will report warning about not having the right Kotlin plugin installed. 

to resolve these issues, go to `Idea`/`Android Studio` -> `Preferences` -> `Languages & Frameworks` -> `Kotlin` and install the latest available plugin.
______
Some components use Google Play services. For this you will need to supply your own `google-services.json` file.
______
Konan issue:
```xcrun: error: SDK "iphonesimulator" cannot be located
xcrun: error: unable to lookup item 'Path' in SDK 'iphonesimulator'
e: org.jetbrains.kotlin.konan.KonanExternalToolFailure: The /usr/bin/xcrun command returned non-zero exit code: 1.
```
Go to `XCode` -> `Preferences` -> `Locations tab` -> `Command Line Tools` dropdown. 

It should show none selected, so select any item.
______
## Tests

The main tests are in `commonTest`. These test should be able to run in iOS, Android and JVM scopes. JS is not yet properly supported.

The test classes are `open` so that specific platform tests can extend them to test their own implementations.

There is also limited support for code coverage for the common sources by running the `commonTestCoverageReport` task. Some limitations in the current jacoco tools cause it not to find source files (even if coverage data is reported).

### Android tests

Android has two test targets, unit and integration. Both should be able to run the tests defined in the common set.

The integration tests also extends some base test (like for location) to test the specific android implementation. 

To run the location tests, the `Kaluga Tests` app needs to be set as the mock location app in developer settings (on lower platform levels just allowing mock locations might suffice)

If running the tests times out (according to Android Studio), try running the tests in debug mode.

### JVM tests

Consider using a Gradle task configuration to run JVM tests (you can use the `--tests` to filter) if the regular JUnit runner does not work. The test runner UI and debugging will still work.

### iOS tests

iOS tests can be run using the `iosTest` task in gradle which uses XCTest. 
Make sure you have the Simulator setup with a working target device. For now you can change the target device inside the gradle build file.

The `ioTest` task supports the `--tests` flag like other Gradle tasks to filter which tests to run.
 
## Architecture

Most of the components within this project use Kotlin coroutines and `Flow` to deliver values from the supported platforms. Backing the `Flow` (by default) is a conflated channel. This pattern (though with a different, framework lever implementation) is currently under consideration for inclusion in the `coroutinesx` library from Jetbrains. If this happens, the API will likely change slightly.
 
## Publishing

### Publishing process

1. Bump version at `gradle/ext.gradle`:

```sh
library_version = 'X.X.X'
```

2. Publish to local maven:

```sh
./gradlew publishToMavenLocal
```

3. Upload and publish on Maven Central:

```sh
./gradlew publishAllPublicationsToSonatypeRepository -PsigningKeyId=SIGNING_KEY_ID -PsigningPassword=SIGNING_KEY_PASSWORD -PsigningSecretKeyRingFile=SIGNING_KEY_FILE -PossrhUsername=OSSRH_USERNAME -PossrhPassword=OSSRH_PASSWORD
```

Where `SIGNING_KEY_ID` is the key id associated with the signing key,
`SIGNING_KEY_PASSWORD` is the password for the signing key,
`SIGNING_KEY_PASSWORD` is the gpg file used for signing,
`OSSRH_USERNAME` is the Sonatype user name to upload the repository to,
and `OSSRH_PASSWORD` is the password for the Sonatype account to upload the repository to.

## Code conventions

The project uses regular Kotlin code conventions. This includes not creating `com/splendo/kaluga` directories, since they are common to all other folders.

### Code style verification

This project uses [ktlint](https://github.com/pinterest/ktlint) Kotlin linter with standard rules.
Each component should setup ktlint gradle [plugin](https://github.com/jlleitschuh/ktlint-gradle) in `build.gradle.kts` file:

```kotlin
apply(plugin = "org.jlleitschuh.gradle.ktlint")
```

You can run `Ktlint Check` configuration from IDE before commit changes to git.

#### Formatting

You can run `Ktlint Format` configuration to reformat source code if needed.

See ktlint and gradle plugin documentation for more details.
