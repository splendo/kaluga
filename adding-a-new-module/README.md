## Template 
This **project** includes all common dependencies and settings for kaluga module.

# Steps for adding a new module
1. CopyPaste template module, select name for your module (we recommend to use only lowercase characters).
2. `<your module>src/androidLibMain/AndroidManifest.xml` change `com.splendo.kaluga.template` to your module package name.	
3. `<your module>src/androidLibAndroidTests/AndroidManifest.xml` change `com.splendo.kaluga.template` to your module package name.
4. `<your module>src/androidLibAndroidTests/kotlin/TestActivity.kt` change package name from `com.splendo.kaluga.template` to `com.splendo.kaluga.your module`.
5. Include your module to kaluga project
	go to `kaluga/settings.gradle/kts`
	And add in the end of the file:
	`include(":<your module>")`.
6. Add Unit tests
    * [Common Unit Tests](#commonTests)
    * [iOS Unit Tests](#iosTests)
    * [Android Unit Tests](#androidLibTests)
7. [Add Android Instrumented Unit Tests](#instrumentedUnitTests)
8. Add your source code
    * <a name="commonMain-sources"></a>**commonMain** - non-platform-specific implementation and `expect` declaration.
    * <a name="iosMain-sources"></a>**iosMain** - iOS specific implementation.
    * <a name="androidLibMain-sources"></a>**androidLibMain** - Android specific implementation.
    * <a name="jvmMain-sources"></a>**jvmMain** - jvm specific implementation.
    * <a name="jsMain-sources"></a>**jvmMain** - js specific implementation.
8. Remove template README files from your module when you don't need them anymore.
9. Don't forget to update copyright if files were imported from another project. To do that use **Code > Update Copyright...**.
10. Add documentation to your code if needed.
11. Add an example of usage to the example project.

---
### <a name="commonTests"></a> Common Unit Tests 

To run all common tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
2. Press "+" button and select "Gradle".
3. In the right part of dialog enter name.
4. Fill in the Gradle project field (kaluga:<your module>).
5. In a field "Tasks" enter cleanAllTests allTests.
6. Press "Ok" or "Apply". Now you can choose and run configuration.

Please note that this scheme will run all gradle test tasks including iOS. Use [test filtering](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering) if you want to run some specific tests.   
Use cleanJvmTest jvmTest if you want to run only JVM tests.

----
### <a name="iosTests"></a> iOS Unit Tests

To run all common tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
2. Press "+" button and select "Gradle".
3. In the right part of dialog enter name.
4. Fill in the Gradle project field (kaluga:<your module>).
5. In a field "Tasks" enter cleanIosx64Test iosx64Test.
6. Press "Ok" or "Apply". Now you can choose and run configuration.

Use [test filtering](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering) if you want to run some specific tests.   
 

----
### <a name="androidLibTests"></a> Add Android Unit Tests 

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
2. Press "+" button and select "Android JUnit".
3. In the right part of dialog enter name.
4. For "Test kind" select "All in package".
5. Enter your package name (com.splendo.kaluga.<your package name>)
6. For "Use class path of module" select your module.
7. Press "Ok" or "Apply". Now you can choose and run configuration.

----
### <a name="instrumentedUnitTests"></a> Add Android Instrumented Unit Tests 
Add unit tests which should run on a device or emulator

Example:
```kotlin
const val DEFAULT_TIMEOUT = 2_500L

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
2. Press "+" button and select "Android Instrumented Tests".
3. In the right part of dialog enter name and select your module.
4. Press "Ok" or "Apply". Now you can choose and run configuration.
