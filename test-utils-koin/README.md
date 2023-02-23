# Test Utils Koin

This library adds support for [Koin](https://insert-koin.io/) to the [`test-utils-base` module](../test-utils-base)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Koin as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-koin:$kalugaVersion")
}
```

### Testing with Koin on the UI thread

The `BaseKoinUIThreadTest` or `KoinUIThreadTest` class can be extended to configure Koin from the UI thread before running a test:

```kotlin
class MyKoinUIThreadTest : BaseKoinUIThreadTest<MyKoinUIThreadTest.Configuration, MyKoinUIThreadTest.MyKoinTestContext>() {

    data class Configuration(val initialValue: String)
    
    class MyKoinTestContext(configuration: Configuration, coroutineScope: CoroutineScope) : KoinUIThreadTest.KoinTestContext(
        // constructor of takes app declarations and modules as parameters
        module {
            single { configuration.initialValue }
        }
    ) {
        // test injection into context
        val k: String by inject()
    }

    override val createTestContextWithConfiguration = { (configuration, scope) -> MyKoinTestContext(configuration, scope) }

    @Test
    fun testKoinUIThreadViewModelTest() = testOnUIThread(Configuration("K")) {
        assertEquals("K", k)
    }
}
```

## Testing ViewModels
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

`BaseKoinFlowTest` or `KoinFlowTest` can be used to test Flows in a cross thread manner while maintaining access to Koin.

