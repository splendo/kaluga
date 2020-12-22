## Kaluga

This project is named after the Kaluga, the world's biggest freshwater fish, which is found in the icy Amur river.

Its main goal is to provide access to common multiplatform features used in mobile app development, such as location, permissions, bluetooth etc.

Where appropriate it uses Coroutines, Channels and Flow. This enables developers to use [cold streams](https://medium.com/@elizarov/cold-flows-hot-channels-d74769805f9) from Kotlin code that is shared amongst multiple platforms such as Android and iOS.

## Build instructions

This project uses Android Studio. You might need a canary version at times. 
______
Both IDEA and Android Studio (at time of writing 10.09.2019) will report warning about not having the right Kotlin plugin installed. 

Just go to `Idea`/`Android Studio` -> `Preferences` -> `Languages & Frameworks` -> `Kotlin` and install latest available plugin.
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

The libraries are not published on any hosted repository yet, but can be published to your local maven repo.

In same time it is available via Bintray. Here is example how to include alerts module your into project:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/splendo/kaluga")
}
// ...
dependencies {
    // ...
    api("com.splendo.kaluga:alerts:0.1.3")
}
```

### Publishing process

1. Bump version at `gradle/ext.gradle`:

```sh
library_version = 'X.X.X'
```

2. Publish to local maven:

```sh
./gradlew publishToMavenLocal
```

3. Upload and publish on Bintray:

```sh
./gradlew -Pbintray_user=splendo -Pbintray_key=API_KEY -Pbintray_dry_run=false -Pbintray_publish=true bintrayUpload
```

Where `API_KEY` is Bintray's API key.
`bintray_dry_run` flag set to `false` used to perform upload.
And `bintray_publish` flag set to `true` used to perform publish.

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
