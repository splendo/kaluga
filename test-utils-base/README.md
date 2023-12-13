# Test Utils Base
This library adds support for testing the [`base` module](../base) and provides functionality for testing on threads.
The library contains support for mocking methods.

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Base as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-base:$kalugaVersion")
}
```

## Testing from a background thread to let `Dispatchers.Main` function properly

By default Kotlin/Native tests run on the UI thread, similar to regular Swift tests.
As with these regular tests, this presents problems when trying to dispatch work to the UI thread.
In particular for Kotlin based tests, using `Dispatchers.Main` -either directly or indirectly- will not work.

One solution is to test from a background thread, `test-utils-base` provides an alternate test entry point which launches a background thread and then consumes the iOS main run loop from the test thread. This allows work dispatched to `Main` to function.

This alternate test entry point can be configured in your gradle configuration, inside the `ios {}` (or similar) target :

for `.gradle`
```groovy

binaries {
    // Use this entry point to turn the thread tests are run to a background thread instead of the main thread
    // This better allows testing the Main dispatcher, and testing cross thread access
    binaries.getTest("DEBUG").freeCompilerArgs += ["-e", "com.splendo.kaluga.test.base.mainBackground"]
}
```
for `.gradle.kts`

```kotlin
binaries {
    getTest("DEBUG").apply {
        freeCompilerArgs = freeCompilerArgs + "-e"
        freeCompilerArgs = freeCompilerArgs + "com.splendo.kaluga.test.base.mainBackground"
    }
}
```

### Testing on the UI Thread when testing from the background

ViewModels often have associated classes (HUD, Alerts etc) that under Kotlin/Native should be instantiated and used from the UI thread.

When testing from a background thread (see above) it's possible to extend `BaseUIThreadTest`, `UIThreadTest` or `SimpleUIThreadTest`.
This allows you to use the `testOnUIThread` method to run your test block on the UI.

`BaseUIThreadTest` lets you define a configuration and a context class, which is the scope for your test method. 
Since the context is created on the UI thread and your test runs on it, this allow mutation of object, or using objects that need to be created on the UI thread only.

```kotlin

class MyUIThreadTest : BaseUIThreadTest<MyUIThreadTest.Configuration, MyUIThreadTest.MyTestContext>() {
    
    private data class Configuration(val configuration: Int)
    
    class MyTestContext(configuration: Configuration, scope: CoroutineScope) : TestContext {
        var myContextVar = "myContext ${configuration.configuration}" // created in UI thread
        
        override fun dispose() { myContextVar = "" } // runs after the test block is run
    }

    override val createTestContextWithConfiguration = { configuration, scope -> MyTestContext(configuration, scope) }
    @Test
    fun testUIThreadTest() = testOnUIThread(Configuration(0)) {
        myContextVar = "somethingElse" // does not crash because access is also from the UI thread
        
        coroutineScope {
            launch(Dispatchers.Main) {
                println("this will actually run during the test")
            }
        }
    }
}
```

The configuration class allows each test to configure the Context, allowing different setups. When no configuration is needed use `UIThreadTest` instead. 

`testOnUIThread` also has a parameter `cancelScopeAfterTest` that is disable by default. 
With this enabled, the scope will be canceled with a custom cancellation exception after your test block is run, and the resulting cancellation exception is ignored. 
Other exceptions (including cancellation) will still surface as normal.

## Testing flows

`BaseFlowTest` and derivative classes like `FlowTest` and `SimpleFlowTest` can be used to test Flows in a cross thread manner, with tests running in a test thread, and the flow being collected in a main thread.

In this example we extend `SimpleFlowTest` and run our test function with `testWithFlow { }`

```kotlin

class SuperSimpleFlowTestTest: SimpleFlowTest<Int>() {
    override val flow: suspend () -> Flow<Int> = { flowOf(1, 2, 3) }

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

## Mocking
Test utils allow for declaring mock implementations of methods. To declare a mock of a function, simply call the `mock` function on a function reference. This returns a `MethodMock` class that can be used to call, stub and verify the method.
Usually, declaring a mock will look something like:

```kotlin

val methodMock = ::method.mock()
fun method(parameterOne: String, parameterTwo: Boolean, parameterThree: Double): Int = methodMock.call(parameterOne, parameterTwo, parameterThree)
```

Methods with up to 5 parameters can be mocked directly. In case of more parameters the `ParametersSpec` interface should be overwritten, though it is recommended to simplify methods to take less parameters instead.

### Stubbing
Mocks can be stubbed to return certain values. To create a stub, call the `on` method on a `MethodMock` and set the result using `doAnswer` or one of its derived methods: `doExecute`, `doReturn`, `doThrow`, or for `SuspendMethodMock` `doExecuteSuspended`, or `doAwait`.
Mocks for methods returning primitive and common data structures such as Strings, Collections or nullables have a default stub returning an empty data type (e.g. `false` for booleans or `emptyList` for a List).

Multiple stubs can be set for a single mock by using `ParameterMatchers`:
* `any` matches and parameter that is received. This is the default.
* `notNull` matches only non-null value of a nullable parameter.
* `isInstance` matches only instances of a subclass of the parameter.
* `matching` allows for a custom matcher for the parameter.
* `oneOf` checks if the parameter matches one of a list of values.
* `notEq` matches if the parameter is not equal to a given value.
* `eq` matches if the parameter matches a given value.

When called, the mock will try and determine the best stub for a given set of parameters. It does so by prioritizing matches with stricter `ParameterMatchers` first. E.g.:

```kotlin
methodMock.on(any(), eq(true), oneOf(listOf(0.0, 1.0))).doReturn(1)
methodMock.on(eq("Test"), any(), eq(1.0)).doReturn(2)
methodMock.on(any(), any(), any()).doReturn(3)

println(method("Test", true, 1.0)) // prints 2
println(method("Other", true, 0.0)) // Prints 1
println(method("Test", false, 2.0)) // Prints 3
```

Call `resetStubs` to reset all stubbing (note this will also remove any default stubs that may have been set)

### Verification
Calls to a `MethodMock` can be verified using the `verify` method. Like with stubbing, verification can use `ParameterMatchers` to filter for a given parameter. In addition, a `Captor` can be passed to capture an argument when called.
By default, verify will check that the method has been called with the given `ParameterMatchers` exactly once. Pass a `VerificationRule` or simply the number of times the method should be called to verify different calls.

```kotlin
methodMock.verify(rule = never()) // Verifies the method has never been called
method("", false, 0.0)
methodMock.verify(second = eq(false))
```

A special `suspend` version of `verify()` is also available, `verifyWithin()`. 
Instead of failing if the rules are not immediately met, it will wait for a certain `Duration` to see if the mock will be called again, each time re-evaluating the `VerificationRule`.     

```kotlin
method("", false, 0.0)
launch {
    delay(1500)
    method("", false, 0.0)
}
methodMock.verifyWithin(duration = 2.seconds, times = 2) // at the time this is called the method will only have been called once, but it will wait for 2 more seconds to see if additional calls are made
```

Note that the increase of calls for fast successive invocations may be conflated (e.g. from 1 to 3). If you still want your `verifyWithin` call to pass, use a rule like `atLeast`:

```kotlin
mock.verifyWithin(rule = atLeast(2))
```

Call `resetCalls` to reset all calls to a `MethodMock`.
