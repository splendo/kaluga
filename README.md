## Build instructions

This project uses Android Studio. You might need a canary version at times. 

Some components use Google Play services. For this you will need to supply your own `google-services.json` file.

## Tests

The main tests are in `commonTest`. These test should be able to run in iOS, Android and JVM scopes. JS is not yet properly supported.

The test classes are `open` so that specific platform tests can extend them to test their own implementations.

There is also limited support for code coverage for the common sources by running the `commonTestCoverageReport` task. Some limitations in the current jacoco tools cause it not to find source files (even if coverage data is reported).

### Android tests

Android has two test targets, unit and integration. Both should be able to run the tests defined in the common set.

The integration tests also extends some base test (like for location) to test the specific android implementation. 

To run the location tests, the `Splendo MPP Tests` app needs to be set as the mock location app in developer settings (on lower platform levels just allowing mock locations might suffice)

If running the tests times out (according to Android Studio), try running the tests in debug mode.

### JVM tests

Consider using a Gradle task configuration to run JVM tests (you can use the `--tests` to filter) if the regular JUnit runner does not work. The test runner UI and debugging will still work.

### iOS tests

iOs tests can be run using the `iosTest` task in gradle which uses XCTest. 
Make sure you have the Simulator setup with a working target device. For now you can change the target device inside the gradle build file.

The `ioTest` task supports the `--tests` flag like other Gradle tasks to filter which tests to run.
 
## Architecture

Most of the components within this project use Kotlin coroutines and `Flow` to deliver values from the supported platforms. Backing the `Flow` (by default) is a conflated channel. This pattern (though with a different, framework lever implementation) is currently under consideration for inclusion in the `coroutinesx` library from Jetbrains. If this happens, the API will likely change slightly.
 
## Publishing

The libraries are not published on any hosted repository yet, but can be published to your local maven repo  
 
## Code conventions

The project uses regular Kotlin code conventions. This includes not creating `com/splendo/mpp` directories, since they are common to all other folders.