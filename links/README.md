# Links

Module used to decode an object from either an App Link, Universal Link or Deep Link's query. It also uses [kaluga-architecture](https://github.com/splendo/kaluga/tree/master/architecture) to open links in the Browser.

## Deserializer
`LinksDecoder` is used to convert query values into an object and it takes a list of values and a serializer. **It is important that the values passed to `LinksDecoder` are ordered in the same way they are declared in the data class**.
When `decodeFromList` is called, a `LinksDecoder` is created and it goes through the passed `List<Any>` one per one referring to the passed `serializer` in order to know the parameter's type and finally convert it.

At the moment kaluga-links does not support missing values in url query. So eventually you will have to pass "something=1&somethingElse=NULL".
### Special usages
- Query contains an array of values: In this case the query will have to contain a parameter (just before the list of values) that identifies the size of that array. **It's important** that also the array size parameter is the named as the actual array property.

``` kotlin
@Serializable 
data class Aliment(val name: String)

@Serializable 
data class Recipe(val name: String, val ingredients: List<Aliment>)

// Somewhere in the code
val query = "name=Carbonara&ingredients=3&ingredients=Spaghetti&ingredients=Bacon&ingredients=Egg"
```
The list size's parameter is not included in the data class `Recipe`, but then is used by [LinksDecoder]. It's name must be the same as the array property it represents.
```kotlin
@Serializable
data class Foo(val bar: String?, baz: String)

// Somewhere in the code
val query = "bar=NULL&baz=Bazinga"
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