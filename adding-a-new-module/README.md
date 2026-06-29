## Template
This **project** includes all common dependencies and settings for kaluga module.

# Steps for adding a new module
1. Run gradle task: `./gradlew createNewModule -P module_name=<your-module> -P package_name=<your-package> -P create-test-utils -P create-compose -P create-databinding -P include-jvm -P include-js -P include-wasm-js -P include-macos -P include-tvos -P include-watchos` from the root directory.
   * Modules are scaffolded as an AndroidX-style nested feature group `<your-module>/`. The main module is created at `<your-module>/<your-module>` (published as `com.splendo.kaluga.<your-module>:<your-module>`) with package `com.splendo.kaluga.<your-package>`.
   * `package_name` can be omitted if the module and package names are the same. Package name will be prefixed with `com.splendo.kaluga`.
   * Only pass `create-test-utils` if you want to also create `<your-module>/test-utils` (published as `com.splendo.kaluga.<your-module>:test`) with package `com.splendo.kaluga.<your-package>.test`.
      * Use `./gradlew createNewTestModule -P module_name=<your-module> -P package_name=<your-package>` to create the test-utils module for an existing module.
   * Only pass `create-compose` if you want to also create `<your-module>/compose` (published as `com.splendo.kaluga.<your-module>:compose`) with package `com.splendo.kaluga.<your-package>.compose`.
      * Use `./gradlew createNewComposeModule -P module_name=<your-module> -P package_name=<your-package>` to create the compose module for an existing module.
   * Only pass `create-databinding` if you want to also create `<your-module>/databinding` (published as `com.splendo.kaluga.<your-module>:databinding`) with package `com.splendo.kaluga.<your-package>.databinding`.
      * Use `./gradlew createNewDataBindingModule -P module_name=<your-module> -P package_name=<your-package>` to create the databinding module for an existing module.
   * Android and iOS are always targeted. Pass any of the following to opt into extra targets (a matching `support…` flag is added to the module's `build.gradle.kts`, and placeholder source sets are created):
      * `include-jvm` — (non-Android) JVM (`jvmMain`)
      * `include-js` — Kotlin/JS (`jsMain`)
      * `include-wasm-js` — Kotlin/Wasm-JS (`wasmJsMain`)
      * `include-macos` — macOS (`macosMain`)
      * `include-tvos` — tvOS (`tvosMain`)
      * `include-watchos` — watchOS (`watchosMain`)
      * Enabling any Apple target beyond iOS (macOS / tvOS / watchOS) also creates an `appleMain` source set for code shared across all Apple targets; iOS-only code stays in `iosMain`.
1. Include your module(s) in the kaluga project by editing `kaluga/settings.gradle.kts`. The task prints the exact lines to add when it finishes, for example:
   ```kotlin
   include(":<your-module>:<your-module>")
   include(":<your-module>:compose")      // only if created
   include(":<your-module>:databinding")  // only if created
   include(":<your-module>:test")         // only if created
   project(":<your-module>:test").projectDir = file("<your-module>/test-utils")
   ```
1. Add Unit tests
    * [Common Unit Tests](#commonTests)
    * [iOS Unit Tests](#iosTests)
    * [Android Unit Tests](#androidTests)
1. [Add Android Instrumented Unit Tests](#androidInstrumentedTests)
1. Add your source code
    * <a name="commonMain-sources"></a>**commonMain** - non-platform-specific implementation and `expect` declaration.
    * <a name="iosMain-sources"></a>**iosMain** - iOS specific implementation.
    * <a name="androidMain-sources"></a>**androidMain** - Android specific implementation.
    * <a name="jvmMain-sources"></a>**jvmMain** - jvm specific implementation.
    * <a name="jsMain-sources"></a>**jvmMain** - js specific implementation.
1. Don't forget to update copyright if files were imported from another project. To do that use **Code > Update Copyright...**.
1. Add documentation to your code if needed.
1. Add an example of usage to the example project.

---

### <a name="commonTests"></a> Common Unit Tests

To run all common tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
1. Press "+" button and select "Gradle".
1. In the right part of dialog enter name.
1. Fill in the Gradle project field (kaluga:<your-module>:<your-module>).
1. In a field "Tasks" enter cleanAllTests allTests.
1. Press "Ok" or "Apply". Now you can choose and run configuration.

Please note that this scheme will run all gradle test tasks including iOS. Use [test filtering](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering) if you want to run some specific tests.
Use cleanJvmTest jvmTest if you want to run only JVM tests.

----

### <a name="iosTests"></a> iOS Unit Tests

To run all common tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
1. Press "+" button and select "Gradle".
1. In the right part of dialog enter name.
1. Fill in the Gradle project field (kaluga:<your-module>:<your-module>).
1. In a field "Tasks" enter cleanIosx64Test iosx64Test.
1. Press "Ok" or "Apply". Now you can choose and run configuration.

Use [test filtering](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering) if you want to run some specific tests.

----

### <a name="androidTests"></a> Add Android Unit Tests

Unit tests which can run without a device.

Example:

```kotlin
class ExampleTest {

    val instance = ExampleClass()

    @Test
    fun exampleTest() {
        assertNotNull(instance.someProperty)
    }
}
```

To run all android unit tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
1. Press "+" button and select "Android JUnit".
1. In the right part of dialog enter name.
1. For "Test kind" select "All in package".
1. Enter your package name (com.splendo.kaluga.<your-module>)
1. For "Use class path of module" select your module.
1. Press "OK" or "Apply". Now you can choose and run configuration.

----

### <a name="androidInstrumentedTests"></a> Add Android Instrumented Unit Tests

Add unit tests which should run on a device or emulator

Example:

```kotlin
val DEFAULT_TIMEOUT = 20.seconds.inWholeMilliseconds

fun UiDevice.assertTextAppears(text: String) {
    assertNotNull(this.wait(Until.findObject(By.text(text)), DEFAULT_TIMEOUT))
}

class ExampleInstrumentedTest {

    @Test
    fun sampleTextOnScreenTest() {
        device.assertTextAppears("Some text on screen")
    }
}

```

To run all instrumented unit tests
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
1. Press "+" button and select "Android Instrumented Tests".
1. In the right part of dialog enter name and select your module.
1. Press "OK" or "Apply". Now you can choose and run configuration.
