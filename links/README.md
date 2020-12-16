# Links

This module is used in order to decode an object from an incoming link and in combination with [kaluga-architecture](https://github.com/splendo/kaluga/tree/master/architecture) open links in the Browser.

## Usage

`Links` has 4 states:
 - `Error`: takes a `String` named `message` and it is returned only when there have been an error. 
 - `Ready`: created with a type `T` representing the decoded object, takes a parameter called `data`. Data is `T`. It is returned when an incoming link has been successfully decoded.
 - `Open`: takes a `String` parameter called `url`. It is returned in case the `url` is valid and it needs to be handled by [navigation](https://github.com/splendo/kaluga/tree/master/architecture#navigation) module, otherwise `Error` will be returned.
 - `Pending`: state that hangs until a new incoming/outgoing link has to be handled.
 
 `LinksStateRepo` has 2 methods in order to handle the links. The first called `handleIncomingLink` which takes the query extracted from the received link and the `Serializer` of the object you want to decode.
 The second methods is called `handleOutgoingLink` takes the url as `String`.
 ```kotlin
// common data class
@Serializable
data class Person(
    val name: String,
    val surname: String 
)

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
                    is LinksState.Error -> {
                        println("Links Error 🔗❌: ${it.error}")
                    }
                    is LinksState.Ready<*> -> {
                        println("Links Ready 🔗✅: ${it.data}")
                    }
                    is LinksState.Pending -> {
                        println("Links Pending 🔗⌛️: $it")
                    }
                    is LinksState.Open -> {
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

