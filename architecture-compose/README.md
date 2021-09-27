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

#### Display contact details using a local view model 

```kotlin
@Composable
fun ContactDetailsLayout(
    contactDetails: ContactDetails,
    navigator: Navigator<ContactDetailsNavigation<*>>
) {
    // Create a view model and store it in a local ViewModelStore to ensure cleanup
    val viewModel = store {
        remember {
            ContactDetailsViewModel(contactDetails)
        }
    }

    ViewModelComposable(viewModel) { vm ->
        // Add a hardware back button handler  
        HardwareBackButtonNavigation(vm::onBackPressed)
        
        // Add the rest of content
        Column {
            //...
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
        is ShowContactsList -> action.route()
        is ShowContactDetails -> {
            val contactId = Json.encodeToString(action.bundle!!.get(action.type))
            action.route(detailsAsJson)
        }
        //...
    }

// Construct a route navigator
val routeNavigator = RouteNavigator(
    rememberNavController(),
    ::routeMapper
)

// set up nav host with routes
contactsNavigator.SetupNavHost(
    startDestination = route<ShowContactsList>(),
    builder = {
        composable(route<ShowContactsList>()) { 
            // Display a contacts list
            ContactsListLayout() 
        }
        composable(route<ShowContactDetails>("{json}")) {
            // Extract contact details from route arguments
            val details: ContactDetails = Json.decodeFromString(it.arguments!!.getString("json")!!)
            // Display contact details 
            ContactDetailsLayout(person, combinedNavigator)
        }
        // other routes
    }
)
```

Set up NavHost navigation in combination with activity navigation

```kotlin
// action mappers
fun routeMapper(action: ContactsNavigation<*>): String = 
    when (action) {
        is ContactsNavigation.ContactListNavigation.Close -> BACK_ROUTE
        is ContactsNavigation.ContactListNavigation.ShowContactDetails -> 
            action.route(Json.encodeToString(action.bundle!!.get(action.type)))
        is ContactsNavigation.ContactDetailsNavigation.Close, is ContactsNavigation.ShowContacts -> 
            route<ContactsNavigation.ShowContacts>()
        else -> throw IllegalStateException()
    }
fun activityMapper(action: ContactsNavigation<*>): NavigationSpec = 
    when (action) {
        is ContactsNavigation.ContactDetailsNavigation.CallNumber -> 
            NavigationSpec.Phone(
                type = Dial,
                phoneNumber = action.bundle!!.get(action.type)
            )
        else -> throw IllegalStateException()
    }

// constructing and combining navigators
val routeNavigator = KalugaNavigatorComposeAdapter(
    rememberNavController(),
    ::routeMapper
)
val combinedNavigator = rememberCombinedNavigator {action: ContactsNavigation<*> ->
    when(action) {
        is ContactsNavigation.ContactDetailsNavigation.CallNumber ->
            ::activityMapper.toActivityNavigator()
        is ContactsNavigation.ContactListNavigation.Close,
        is ContactsNavigation.ContactListNavigation.ShowContactDetails,
        is ContactsNavigation.ContactDetailsNavigation.Close, 
        is ContactsNavigation.ShowContacts ->
            routeNavigator
    }
}

// set up nav host with routes
contactsNavigator.SetupNavHost(
    startDestination = ShowContacts.route(),
    builder = {
        composable(ShowContacts.route()) { ContactListLayout(combinedNavigator) }
        composable(route<ShowContactDetails>("{json}")) {
            val details: ContactDetails = Json.decodeFromString(it.arguments!!.getString("json")!!)
            PersonContactDetailsLayout(person, combinedNavigator)
        }
    }
)
```