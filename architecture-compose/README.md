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
### Examples

#### Display content using a local data model 

```kotlin
// View model that expose contact details fields and handle user actions
class ContactDetailsViewModel(
    //...
) {
    val contactEmail: String // ...
    
    fun sendEmail() {
        // send an email    
    }
    
    fun onBackPressed() {
        // handle the back button 
    }
}

// Contact details UI
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
fun listRouteMapper(action: ContactsListNavigation<*>): String = 
    when (action) {
        is ContactsListNavigation.ShowContactDetails -> action.next
        //...
    }

fun detailRouteMapper(action: ContactDetailsNavigation<*>): Route {
    return when (action) {
        is ContactDetailsNavigation.Close -> Route.Back
    }
}

// Root view of the Activity. Contains a navigation graph with destinations
@Composable
fun ContactsLayout() { 
    val routeController = NavHostRouteController(rememberNavHostController())
    // set up nav host with routes
    routeController.SetupNavHost(
            rootView = { navHostController ->
                // Display a contact list
                ContactsListLayout(navHostController)
            }
        ) { navHostController ->
            composable<ContactDetails, ContactsListNavigation.ShowContactDetails>(
                type = NavigationBundleSpecType.SerializedType(
                    ContactDetails.serializer()
                )
            ) { details ->
                ContactDetailsLayout(details, navHostController)
            }
        }
}

@Composable
fun ContactsListLayout(navHostController: NavHostController) {
    // Construct a route navigator
    val routeNavigator = RouteNavigator(
            rememberNavController(),
        ::listRouteMapper
    )
  
    // Setup Contact List view
}

@Composable
fun ContactDetailsLayout(contactDetails: ContactDetails, navHostController: NavHostController) {
    val routeNavigator = RouteNavigator(
        navHostController,
        ::detailRouteMapper
    )
    
    // Setup Details View
    // No need to set up NavHost since it is managed by the parent
}
```

You can also call `SetupNavHost` directly from a `RouteNavigator`, bearing in mind that the navHostController linked to the Navigator can only be setup once.

#### Mix NavHost and Kaluga navigation

Sometimes it's necessary to mix a route navigation within the same activity and navigate to 
other activities

```kotlin
internal fun activityMapper(action: ContactsNavigation<*>): NavigationSpec =
    when (action) {
        is SendEmail -> NavigationSpec.Email(/* ... */)
        //...
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

#### Modal Bottom Sheet

A special case is provided for using `ModalBottomSheet` navigation

````kotlin
internal fun bottomSheetParentNavigationRouteMapper(action: BottomSheetParentNavigation): BottomSheetRoute {
    return when (action) {
        is BottomSheetParentNavigation.SubPage -> action.next.bottomSheetContent
        is BottomSheetParentNavigation.ShowSheet -> action.next.bottomSheetSheetContent
    }
}

internal fun bottomSheetParentSubPageNavigationRouteMapper(action: BottomSheetParentSubPageNavigation): Route {
    return when (action) {
        is BottomSheetParentSubPageNavigation.Back -> Route.Back
    }
}

internal fun bottomSheetNavigationRouteMapper(action: BottomSheetNavigation): BottomSheetRoute {
    return when (action) {
        is BottomSheetNavigation.Close -> Route.Close.bottomSheetSheetContent
        is BottomSheetNavigation.SubPage -> action.next.bottomSheetSheetContent
    }
}

internal fun bottomSheetSubPageNavigationRouteMapper(action: BottomSheetSubPageNavigation): BottomSheetRoute {
    return when (action) {
        is BottomSheetSubPageNavigation.Close -> Route.Close.bottomSheetSheetContent
        is BottomSheetSubPageNavigation.Back -> Route.Back.bottomSheetSheetContent
    }
}

@Composable
fun BottomSheetParentLayout() {
  val bottomSheetRouteController = BottomSheetRouteController(
    NavHostRouteController(rememberNavController()),
    BottomSheetSheetContentRouteController(
      rememberNavController(),
      rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
      rememberCoroutineScope()
    )
  )

  bottomSheetRouteController.NavigatingModalBottomSheetLayout(
    sheetContent = { contentNavHostController, sheetContentNavHostController, sheetState ->
                    composable(BottomSheetParentNavigation.ShowSheet.route()) {
                      BottomSheetLayout(contentNavHostController, sheetContentNavHostController, sheetState)
                    }
                    composable(BottomSheetNavigation.SubPage.route()) {
                      BottomSheetSubPageLayout(
                        contentNavHostController, sheetContentNavHostController, sheetState
                      )
                    }
                   },
    contentRoot = { contentNavHostController, sheetContentNavHostController, sheetState ->
                   BottomSheetParentLayoutContent(contentNavHostController, sheetContentNavHostController, sheetState)
                  },
    content = { contentNavHostController, _, _ ->
               composable(BottomSheetParentNavigation.SubPage.route()) {
                 BottomSheetParentSubPageLayout(contentNavHostController)
               }
              }
  )
}

@Composable
fun BottomSheetParentLayoutContent(contentNavHostController: NavHostController, sheetNavHostController: NavHostController, sheetState: ModalBottomSheetState) {

  val navigator = ModalBottomSheetNavigator(
    NavHostRouteController(contentNavHostController),
    BottomSheetSheetContentRouteController(
      sheetNavHostController,
      sheetState,
      rememberCoroutineScope()
    ),
    ::bottomSheetParentNavigationRouteMapper
  )
  // Show Content View
}

@Composable
fun BottomSheetParentSubPageLayout(navHostController: NavHostController) {
    val navigator = RouteNavigator(
        navHostController,
        ::bottomSheetParentSubPageNavigationRouteMapper
    )
  
  // Show Content Sub View
}

@Composable
fun BottomSheetLayout(contentNavHostController: NavHostController, sheetContentNavHostController: NavHostController, sheetState: ModalBottomSheetState) {
    val navigator = ModalBottomSheetNavigator(
        contentNavHostController,
        sheetContentNavHostController,
        sheetState,
        rememberCoroutineScope(),
        ::bottomSheetNavigationRouteMapper,
    )
  
  // Show Bottom Sheet Content
}

@Composable
fun BottomSheetSubPageLayout(contentNavHostController: NavHostController, sheetContentNavHostController: NavHostController, sheetState: ModalBottomSheetState) {
    val navigator = ModalBottomSheetNavigator(
        contentNavHostController,
        sheetContentNavHostController,
        sheetState,
        rememberCoroutineScope(),
        ::bottomSheetSubPageNavigationRouteMapper
    )
  
  // Show Bottom Sheet Sub Content
}
````

