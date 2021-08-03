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

## Testing flows

`BaseFlowTest` and derivative classes like `BaseKoinFlowTest`, `FlowTest` and `SimpleFlowTest` can be used to test Flows in a cross thread manner, with tests running in a test thread, and the flow being collected in a main thread.

In this example we extend `SimpleFlowTest` and run our test function with `testWithFlow { }`

```kotlin

class SuperSimpleFlowTestTest: SimpleFlowTest<Int>() {
    override val flow: () -> Flow<Int> = { flowOf(1, 2, 3) }

    @Test
    // use testWithFlow to run the test
    fun test() = testWithFlow {
        val complete = EmptyCompletableDeferred()

        // tests values collected from the flow on the main thread asynchronously
        test {
            assertEquals(1, it)
        }

        test {
            assertEquals(2, it)
            delay(10)
            complete.complete()
        }
        // wait for all asynchronous tests to be completed
        action {
            assertTrue(complete.isCompleted)
        }
        test {
            assertEquals(3, it)
        }
        // no action needed, last test blocks will be run before test is complete
    }
}
```

This works by providing `action { }` and `test { }` blocks, where test blocks run asynchronously on the main thread, when there is a new value collected from the `Flow`, and action blocks are only run when all test blocks have been executed. `mainAction` can be used to run an action block on the main thread.

The flow to be tested is provided through an abstract field (`flow` or `flowFromTestContext`) with closure that returns the flow. 

Extending `BaseFlowTest` (or `BaseKoinFlowTest`) also allows for supplying a `TestContext`, since these classes extend `UIThreadTest`. `mainAction` blocks have this `TestContext` as its context, as does the `flowFromTestContext` closure.

In particular for `BaseKoinFlowTest` this allows for Koin injected objects only from the main thread. 

Kaluga itself uses these classes to test many of the flows (e.g. from `StateRepo`s), so more advanced example can be found throughout the codebase.
