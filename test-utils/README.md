# Test Utils

## Installing
To import this library add the Kaluga Bintray as a maven dependency `https://dl.bintray.com/kaluga/com.splendo.kaluga/`. You can then import Kaluga Test Utils as follows:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils:$kalugaVersion")
}
```

## Testing from a background thread to let `Dispatchers.Main` function properly

By default Kotlin/Native tests run on the UI thread, similar to regular Swift tests.
As with these regular tests, this presents problems when trying to dispatch work to the UI thread.
In particular for Kotlin based tests, using `Dispatchers.Main` -either directly or indirectly- will not work.

One solution is to test from a background thread, `test-utils` provides an alternate test entry point which launches a background thread and then consumes the iOS main run loop from the test thread. This allows work dispatched to `Main` to function.

This alternate test entry point can be configured in your gradle configuration, inside the `ios {}` (or similar) target :

for `.gradle`
```groovy

binaries {
    // Use this entry point to turn the thread tests are run to a background thread instead of the main thread
    // This better allows testing the Main dispatcher, and testing cross thread access
    binaries.getTest("DEBUG").freeCompilerArgs += ["-e", "com.splendo.kaluga.test.mainBackground"]
}
```
for `.gradle.kts`

```kotlin
binaries {
    getTest("DEBUG").apply {
        freeCompilerArgs = freeCompilerArgs + "-e"
        freeCompilerArgs = freeCompilerArgs + "com.splendo.kaluga.test.mainBackground"
    }
}
```

### Testing a ViewModel in the UI Thread when testing from the background

ViewModels often have associated classes (HUD, Alerts etc) that under Kotlin/Native should be instantiated and used from the UI thread.

When testing from a background thread (see above) it's possible to extend `UIThreadTest` or `SimpleUIThreadTest`.
This allows you to use the `testOnUIThread` method to run your test block on the UI.

`UIThreadTest` lets you define a context class which is the scope for your test method. 
Since the context is created on the UI thread and your test runs on it, this allow mutation of object, or using objects that need to be created on the UI thread only.

```kotlin

class MyUIThreadTest : UIThreadTest<UIThreadTestTest.MyTestContext>() {
    inner class MyTestContext : TestContext {
        var myContextVar = "myContext" // created in UI thread
        
        override fun dispose() { myContextVar = "" } // runs after the test block is run
    }

    override fun CoroutineScope.createTestContext(): MyTestContext = MyTestContext()

    @Test
    fun testUIThreadTest() = testOnUIThread {
        myContextVar = "somethingElse" // does not crash because access is also from the UI thread
        
        coroutineScope {
            launch(Dispatchers.Main) {
                println("this will actually run during the test")
            }
        }
    }
}
```

`testOnUIThread` also has a parameter `cancelScopeAfterTest` that is disable by default. 
With this enabled, the scope will be canceled with a custom cancellation exception after your test block is run, and the resulting cancellation exception is ignored. 
Other exceptions (including cancellation) will still surface as normal.

### Testing with Koin on the UI thread

The `KoinUIThreadTest` class can be extended to configure Koin from the UI thread before running a test:

```kotlin
class MyKoinUIThreadTest : KoinUIThreadTest<MyKoinUIThreadTest.MyKoinTestContext>() {

    inner class MyKoinTestContext : KoinUIThreadTest.KoinTestContext(
        // constructor of takes app declarations and modules as parameters
        module {
            single { "K" }
        }
    ) {
        // test injection into context
        val k: String by inject()
    }

    override fun CoroutineScope.createTestContext(): MyKoinTestContext = MyKoinTestContext()

    @Test
    fun testKoinUIThreadViewModelTest() = testOnUIThread {
        assertEquals("K", k)
    }
}
```

### Testing a ViewModel when testing from the background

It's also possible use the UI Thread context to create a kaluga ViewModel. 
This can be done by extending  `UIThreadViewModelTest`.

```kotlin
class CustomUIThreadViewModelTest : UIThreadViewModelTest<CustomViewModelTestContext, MyViewModel>() {

    class MyViewModel(val alertBuilder: BaseAlertPresenter.Builder) : BaseViewModel()

    class CustomViewModelTestContext : ViewModelTestContext<MyViewModel> {
        val mockAlertBuilder = MockAlertPresenter.Builder() // creates on UI thread and can be passed to viewModel
        override val viewModel: MyViewModel = MyViewModel(mockAlertBuilder)
    }

    override fun CoroutineScope.createTestContext(): CustomViewModelTestContext = CustomViewModelTestContext()

    @Test
    fun testCustomUIThreadViewModelTest() = testOnUIThread {

        viewModel.alertBuilder //...
    }
}
```

In case no other objects are needed from the UI thread `SimpleUIThreadViewModelTest` can be used.

In case both Koin and a ViewModel are used, there is `KoinUIThreadViewModelTest`. 
If Koin provides the ViewModel you can inject it inside the TestContext:

```kotlin
override val viewModel: KoinViewModel by inject()
```

if not you can use lazy initialization:
```kotlin
override val viewModel by lazy { MyViewModel(myArgs) }
```
