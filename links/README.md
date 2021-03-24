# Links

This module is used in order to decode an object from either an App Link, Universal Link or Deep Link's query. It also uses [kaluga-architecture](https://github.com/splendo/kaluga/tree/master/architecture) to open links in the Browser.

## Deserializer
`LinksDecoder` is used to convert query values into an object and it takes a list of values and a serializer. **Is important that the list of values passed to `LinksDecoder` are ordered in the order they are declared in the data class**.
When `decodeFromList` is called, a `LinksDecoder` is created and it goes through the passed `List<Any>` one per one referring to the passed `serializer` in order to know the parameter's type.


## Usage

`Links` is made by 3 events

- `Incoming`: fired when `handleIncomingLink` method is called. When the app intercept an [App Link](https://developer.android.com/training/app-links)/[Universal Link](https://developer.apple.com/ios/universal-links/)/[Deep Link](https://firebase.google.com/products/dynamic-links#:~:text=Dynamic%20Links%20are%20smart%20URLs,free%20forever%2C%20for%20any%20scale.) call `handleIncomingLink` passing the arrived url and the `Class.serializer()`. If the url's query is invalid, the `Failure` event will be fired with an error message, otherwise the flow will collect a `Links.Incoming.Result` that contains the data deserialized into an object. Collect this flow from `linksEventFlow`.
- `Outgoing`: fired when `validateLink` is called. The method `validateLink` takes a parameter `url` and validate using native libraries. If the passed `url` is invalid then `Links.Failure` will be fired. Collect the result from `validateEventFlow`.
- `Failure`: fired when `url` passed in `validateLink` or `handleIncomingLink` is invalid.

``` kotlin
// MainActivity.kt

override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    val appLinkData: Uri? = intent?.data
		appLinkData?.let {
		    val url = URL(it.path)
		    sharedViewModel.handleIncomingLink(url, Person.serializer())
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
    
    viewController.viewModel.handleIncomingLinks(incomingUrl, Person.Companion.serializer())
}
```



``` kotlin
// common data class
@Serializable
data class Person(val name: String, val surname: String)

// ExampleSharedViewModel
class SharedViewModel(
    linksStateRepoBuilder: LinksStateRepoBuilder,
    navigator: Navigator<BrowserNavigationActions<BrowserSpecRow>>
) : NavigatingViewModel<BrowserNavigationActions<BrowserSpecRow>>(navigator) {
    private val linksStateRepo = linksStateRepoBuilder.create()

    fun bar() {
        scope.launch {
            linksStateRepo.linksEventFlow.collect { it: LinksState ->
                when(it) {
                    is Links.Failure -> {
                        println("Links Error 🔗❌: ${it.error}")
                    }
                    is Links.Incoming.Result<*> -> {
                        println("Links Ready 🔗✅: ${it.data}")
                    }
                } 
            }
            
            linksStateRepo.validateEventFlow.collect {
                is Links.Failure -> {
                    println("Links Error 🔗❌: ${it.error}")
                }
                is Links.Outgoing.Link -> {
                    println("Links Open 🔗📖️: $it")
                    navigator.navigate(
                        BrowserNavigationActions.OpenWebView(
                            BrowserSpec().toBundle { row ->
                                when (row) {
                                    is BrowserSpecRow.UrlSpecRow -> row.convertValue(it.url)
                                }
                            }
                        )
                   )
                }
            }
            
        }
    }
    
    fun <T> handleIncomingData(url: String, serializer: KSerializer<T>) {
        linksStateRepo.handleIncomingLink(url, serializer)
    }
    
    fun handleOutgoingLink(url: String) {
        linksStateRepo.validateLink(url)
    }
}
```



Follow [navigation](https://github.com/splendo/kaluga/tree/master/architecture#navigation) in order to create a `Navigator`.