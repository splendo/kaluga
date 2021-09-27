## Architecture-compose
This Android library contains composable functions to work with Kaluga states and androidx navigation.

## Installing
This library is available on Maven Central. You can import Kaluga Base as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.architecture.compose:$kalugaVersion")
}
```

## Usage
###Examples

#### Display content using a local data model 

```kotlin
class ContactDetailsViewModel(
    //...
) {
    val contactEmail: String get() = { /*  ...  */ }
    
    fun sendEmail() {
        //...    
    }
    
    fun onBackPressed() {
        //...
    }
}

@Composable
fun ContactDetailsLayout(
    contactDetails: ContactDetails, 
    navigator: Navigator<ContactDetailsNavigation<*>>
) {
    // Create a view model and store it in a local ViewModelStore to ensure cleanup
    val viewModel = store {
        remember {
            ContactDetailsViewModel(contactDetails, navigator)
        }
    }

    ViewModelComposable(viewModel) {
        // Add a hardware back button handler  
        HardwareBackButtonNavigation(::onBackPressed)
        
        Column {
            // Use the view model fields to render content
            Text(text = contactEmail)
            
            // Use the view model methods to handle user actions
            Button(onClick = ::sendEmail) {
                // ...
            }
        }
    }
}
```

#### Set up NavHost navigation

```kotlin
// Define an action mapper for a route navigator
fun routeMapper(action: ContactsNavigation<*>): String = 
    when (action) {
        is Close -> BACK_ROUTE 
        is ShowContactsList -> action.route() // route with no parameters
        is ShowContactDetails -> { // route with contact id as a parameter 
            val contactId = Json.encodeToString(action.bundle!!.get(action.type))
            action.route(contactId)
        }
        //...
    }

// Root view of the Activity. Contains a navigation graph with destinations
@Composable
fun ContactsLayout() { 
    // Construct a route navigator
    val routeNavigator = RouteNavigator(
        rememberNavController(),
        ::routeMapper
    )

    // set up nav host with routes
    routeNavigator.SetupNavHost(
        startDestination = route<ShowContactsList>(),
        builder = {
            composable(route<ShowContactsList>()) {
                // Display a contacts list
                ContactsListLayout()
            }
            composable(route<ShowContactDetails>("{json}")) {
                // Extract contact details from route arguments
                val details: ContactDetails =
                    Json.decodeFromString(it.arguments!!.getString("json")!!)
                // Display contact details 
                ContactDetailsLayout(details, routeNavigator)
            }
            // other routes
        }
    )
}
```

#### Mix NavHost and Kaluga navigation
Sometimes it's necessary to mix a route navigation within the same activity and navigate to 
other activities

```
internal fun activityMapper(action: ContactsNavigation<*>): NavigationSpec =
    when (action) {
        is SendEmail -> NavigationSpec.Email(/* ... */)
        //...
    }
}

@Composable
fun ContactsLayout() { 
    //...
    
    // create a mixed route-activity navigator
    val combinedNavigator = rememberCombinedNavigator { action: ContactsNavigation<*> ->
        when (action) {
            // actions which are redirected to `routeNavigator`
            is ShowContactsList, is ShowContactDetails, is Close -> routeNavigator
            // actions which are redirected to an activity navigator
            is SendEmail -> ::activityMapper.toActivityNavigator()
        }
    }
    
    routeNavigator.SetupNavHost( 
        //...
        composable(route<ShowContactDetails>("{json}")) {
            //...
            ContactDetailsLayout(details, combinedNavigator)
        }
    )
}

class ContactDetailsViewModel(
    private val contactDetails: ContactDetails,
    private val navigator: Navigator<ContactDetailsNavigation<*>>
) {
    //...
    // opens another activity
    fun sendEmail() = navigator.navigate(SendEmail(contactDetails.email))
    // navigates NavHost graph
    fun onBackPressed() = navigator.navigate(Close)
}  
```
