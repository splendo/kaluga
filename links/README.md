# Links

Module used to decode an object from either an App Link, Universal Link or Deep Link's query. The example uses [kaluga-architecture](https://github.com/splendo/kaluga/tree/master/architecture) to open links in the Browser.

### Usages highlights
Links uses [kotlinx.serialization-properties](kotlinx-serialization-properties) format module which allow to decode an object from a map of strings or any. Usage specification can be found in the KDoc for [`Properties`](https://github.com/Kotlin/kotlinx.serialization/blob/master/formats/properties/commonMain/src/kotlinx/serialization/properties/Properties.kt#L16) class.

``` kotlin
@Serializable
data class Aliment(val name: String)

@Serializable
data class Recipe(val name: String, val ingredients: List<Aliment>)

// Somewhere in the code
val query = "name=Carbonara&ingredients=3&ingredients=Spaghetti&ingredients=Bacon&ingredients=Egg"
```
The list size's parameter is not included in the data class `Recipe`, but then is used by [LinksDecoder]. It's name must be the same as the array property it represents.

- Nullable values should not be omitted from url query. Kaluga links does not support yet default null value for missing values.
```kotlin
@Serializable
data class Foo(val bar: String?, baz: String)

// Somewhere in the code
val query = "bar=null&baz=Bazinga"
```

## Usage

The entry point for universal links or dynamic links in Android or iOS are `MainActivity.kt` and `AppDelegate.swift`.
Said so `MainActivity.kt` will have to override `onNewIntent` and call `handleIncomingLink`, while on iOS you should override the `application` method that receives a `NSUserActivity`.


``` kotlin
// MainActivity.kt

override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    val appLinkData: Uri? = intent?.data
		appLinkData?.let {
		    val url = URL(it.path)
		    sharedViewModel.handleIncomingData(url, Person.serializer())
		}
}
```

``` swift
// AppDelegate.swift
func application(_ application: NSApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([NSUserActivityRestoring]) -> Void) -> Bool {
    // Get URL components from the incoming user activity.
    guard userActivity.activityType == NSUserActivityTypeBrowsingWeb,
        let incomingURL = userActivity.webpageURL else {
        return false
    }

    viewController.viewModel.handleIncomingData(incomingUrl, Person.Companion.serializer())
}
```



``` kotlin
// common data class
@Serializable
data class Person(val name: String, val surname: String)

// ExampleSharedViewModel
class SharedViewModel(
    linksBuilder: LinksBuilder,
    navigator: Navigator<BrowserNavigationActions<BrowserSpecRow>>
) : NavigatingViewModel<BrowserNavigationActions<BrowserSpecRow>>(navigator) {
    private val links = linksBuilder.create()

    fun <T> handleIncomingData(url: String, serializer: KSerializer<T>) {
        links.handleIncomingLink(url, serializer)
    }

    fun handleOutgoingLink(url: String) {
        links.validateLink(url)
    }
}
```

### Android
Android `PlatformLinksHandler` implementation uses `UrlQuerySanitizer` in order to extract the full query from a url. Keep in mind that `UrlQuerySanitizer`
will convert space characters into underscore character. Instead use `+` character between 2 words if you want to represent a space.

Follow [navigation](https://github.com/splendo/kaluga/tree/master/architecture#navigation) in order to create a `Navigator`.