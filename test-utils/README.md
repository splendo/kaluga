# Test Utils

## Testing from a background thread to let `Dispatchers.Main` function properly

By default Kotlin/Native tests run on the UI thread, similar to regular Swift tests.

As with these regular test, this presents problems when trying to dispatch work to the main thread.
In particular for Kotlin bases test, usinf the `Dispatchers.Main`dispatcher -either directly or indirectly- will not work.

One solution is to test from a background thread, `test-utils` provides an alternate test entry point which launches a background thread and then consumes the iOS main runloop from the test thread. This allows work dispatched to `Main` to function.

This alternate test entry point can be configured in your gradle configuration, inside the `ios {}` (or similar) target :

for `.gradle`
```groovy

binaries {
    // Use this entry point to turn the thread tests are run to a background thread instead of the main thread
    // This better allows testing the main dispatcher, and testing cross thread access
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

### Testing a ViewModel when testing from the background

ViewModels often have associated classes (HUD, Alerts etc) that under Kotlin/Native should be instantiated and used from the main thread.

When testing from a background thread (see above) it's possible to extend `UIThreadViewModelTest` or `SimpleUIThreadViewModelTest`
This will instantiate the ViewModel in the main thread when using the `testWithViewModel` method to run your tests:

```kotlin

class SomeTest:SimpleUIThreadViewModelTest<MyViewModel> {

    override fun createViewModel() = MyViewModel()

    @Test
    fun someTest() = testWithViewModel {
       // suspended from `Dispatchers.Main`
       viewModel.doSomething() // available in scope
    } 
}
```

Often times you want to have other objects instantiated alongside alongside your ViewModel. This can be done by extending the `ViewModelTestContext` object for the test:

```kotlin
class SomeTest:UIThreadViewModelTest<MyViewModelTestContext, MyViewModel> {

    class MyViewModelTestContext:
        ViewModelTestContext<ViewModel> {

        val alertBuilder = MockAlertPresenter.Builder()
        
        override fun createViewModel() = MyViewModel(alertBuilder) // can be passed to viewmodel
    }

    @Test
    fun someTest() = testWithViewModel {
       alertBuilder // also available in scope now
    } 
}
```
