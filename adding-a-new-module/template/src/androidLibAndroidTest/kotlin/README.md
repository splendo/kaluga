## Android instrumented unit tests

Add unit tests which should run on a device or emulator

Example:
```kotlin
const val DEFAULT_TIMEOUT = 2_500L

fun UiDevice.assertTextAppears(text: String) {
    assertNotNull(this.wait(Until.findObject(By.text(text)), DEFAULT_TIMEOUT))
}

fun UiDevice.assertTextDisappears(text: String) {
    assertTrue(wait(Until.gone(By.text(text)), DEFAULT_TIMEOUT))
}

class ExampleInstrumentedTest {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val viewModel get() = activityRule.activity.viewModel

    @Test
    fun viewModelTitleNotNullTest() {
        assertNotNull(viewModel.title)
    }

    @Test
    fun sampleTextOnScreenTest() {
        device.assertTextAppears("Some text on screen")
    }
}

```

To run all instrumented unit tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
2. Press "+" button and select "Android Instrumented Tests".
3. In the right part of dialog enter name and select your module.
4. Press "Ok" or "Apply". Now you can choose and run configuration.