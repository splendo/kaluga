# Test Utils Architecture
This library adds support for testing the [`architecture` module](../architecture) to [`test-utils`](../test-utils-base)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Architecture as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-architecture:$kalugaVersion")
}
```

## Testing a ViewModel when testing from the background

It's also possible use the UI Thread context to create a Kaluga ViewModel. 
This can be done by extending `BaseUIThreadViewModelTest` or `UIThreadViewModelTest`.

```kotlin
class CustomUIThreadViewModelTest : UIThreadViewModelTest<CustomViewModelTestContext, MyViewModel>() {

    class MyViewModel(private val alertBuilder: BaseAlertPresenter.Builder) : BaseLifecycleViewModel(alertBuilder)

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
