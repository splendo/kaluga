## Build instructions

This project uses Android Studio. You might need a canary version at times. Check [gradle/ext.gradle](gradle/ext.gradle) to see which Android gradle plugin is used, and then [this page](https://developer.android.com/studio/releases/gradle-plugin#updating-gradle) from Google to see which version of Android Studio is required.

You can also check the Kotlin version, and if needed align this with the Kotlin plugin version for Android Studio under `Android Studio` -> `Preferences` -> `Languages & Frameworks` -> `Kotlin`.

Install the Kotlin Mobile Multiplatform plugin (KMM) for additional functionality, such as running the iOS app straight from Android Studio.

For advanced iOS development XCode and/or AppCode can be used. AppCode can also use the KMM plugin. 

## Adding new modules

There is a template and step list for adding new modules, see [adding-a-new-module](adding-a-new-module/).

## Tests

The main tests are in `commonTest`. These test should be able to run in iOS, Android, JS and JVM scopes. However the latter two are not yet properly supported, and tests might be broken as a result.

If a purely common test does not work, you can make the test's class `open` or `abstract` so that specific platform tests can extend them to test their own implementations.

By extending `BaseTest` you can avoid a lot of boilerplate to make tests behave cross-platform (e.g. enableing the coroutines debugger).

There are specialized test classes in the [`test-utils`](tree/master/test-utils) module, e.g. for testings `Flows`, `ViewModel`s etc. 

### Android tests

Android has two test targets, unit and integration. Both should be able to run the tests defined in the common set.

To run the location tests, the `Kaluga Tests` app needs to be set as the mock location app in developer settings (on lower platform levels just allowing mock locations might suffice)

### JVM tests

Consider using a Gradle task configuration to run JVM tests (you can use the `--tests` to filter) if the regular JUnit runner does not work. The test runner UI and debugging will still work.

### iOS tests

iOS tests can be run using the `iosTest` task in gradle which uses XCTest.
Make sure you have the Simulator setup with a working target device. For now you can change the target device inside the gradle build file.

The `ioTest` task supports the `--tests` flag like other Gradle tasks to filter which tests to run.

Note that for kaluga iOS tests for kaluga run on a background thread, in order to have a properly working Main dispatchers (and align better with Android). How this works is descriped in [`test-utils`](tree/master/test-utils)


## Architecture

Most of the components within this project use Kotlin coroutines and `Flow`, so for new components try to use these APIs where it makes sense.

For modules that rely on state, try using `StateRepo` based classes. These automatically expose a `Flow` for state changes, and using `ColdStateRepo` cold flow behaviour can be used.

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

## Frequent issues

```xcrun: error: SDK "iphonesimulator" cannot be located
xcrun: error: unable to lookup item 'Path' in SDK 'iphonesimulator'
e: org.jetbrains.kotlin.konan.KonanExternalToolFailure: The /usr/bin/xcrun command returned non-zero exit code: 1.
```
Go to `XCode` -> `Preferences` -> `Locations tab` -> `Command Line Tools` dropdown.

It should show none selected, so select any item.