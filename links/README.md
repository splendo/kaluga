# Links

This module is used in order to decode an object from either an App Link, Universal Link or Deep Link's query. It also uses [kaluga-architecture](https://github.com/splendo/kaluga/tree/master/architecture) to open links in the Browser.

## Usage

`Links` is made by 3 events

- `Incoming`: fired when `handleIncomingLink` method is called. When the app intercept an [App Link](https://developer.android.com/training/app-links)/[Universal Link](https://developer.apple.com/ios/universal-links/)/[Deep Link](https://firebase.google.com/products/dynamic-links#:~:text=Dynamic%20Links%20are%20smart%20URLs,free%20forever%2C%20for%20any%20scale.) call `handleIncomingLink` passing the query arrived with the link and the `Class.serializer()`. If the query is invalid, the `Failure` event will be fired with an error message. If the query is valid the flow will collect a `Links.Incoming.Result` that contains the data deserialized into an object.
- `Outgoing`: fired when `validateLink` is called. The method `validateLink` takes a parameter `url` and validate using native libraries. If the passed `url` is invalid then `Links.Failure` will be fired.
- `Failure`: fired when `query` and `url` passed in `handleIncomingLink` or `validateLink`  are invalid.

```
// MainActivity.kt

override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    val appLinkData: Uri? = intent?.data
		appLinkData?.let {
		    sharedViewModel.handleIncomingLink(it.query, Person.serializer())
		}	
}
```

```
// AppDelegate.swift
func application(_ application: NSApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([NSUserActivityRestoring]) -> Void) -> Bool {
    // Get URL components from the incoming user activity.
    guard userActivity.activityType == NSUserActivityTypeBrowsingWeb,
        let incomingURL = userActivity.webpageURL,
        let components = NSURLComponents(url: incomingURL, resolvingAgainstBaseURL: true) else {
        return false
    }

    // Check for specific URL components that you need.
    guard let path = components.path,
    let query = components.query else {
        return false
    }    
    
    viewController.viewModel.handleIncomingLinks(query, Person.Companion.serializer())
}
```



```
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
            linksStateRepo.flow().collect { it: LinksState ->
                when(it) {
                    is Links.Failure -> {
                        println("Links Error 🔗❌: ${it.error}")
                    }
                    is Links.Incoming.Result<*> -> {
                        println("Links Ready 🔗✅: ${it.data}")
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
    }
    
    fun <T> handleIncomingData(query: String, serializer: KSerializer<T>) {
        linksStateRepo.handleIncomingLink(data, serializer)
    }
    
    fun handleOutgoingLink(url: String) {
        linksStateRepo.handleOutgoingLink(url)
    }
}
```



Follow [navigation](https://github.com/splendo/kaluga/tree/master/architecture#navigation) in order to create a `Navigator`.