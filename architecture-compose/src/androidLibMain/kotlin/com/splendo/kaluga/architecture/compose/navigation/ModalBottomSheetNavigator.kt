package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Route for navigating a [ModalBottomSheetLayout] using a [BottomSheetSheetContentRouteController]
 */
sealed class BottomSheetRoute {
    /**
     * Navigates within the sheet content of the [ModalBottomSheetLayout]
     * @param route The [Route] to navigate
     */
    data class SheetContent(val route: Route) : BottomSheetRoute()

    /**
     * Navigates within the content of the [ModalBottomSheetLayout]
     * @param route The [Route] to navigate
     */
    data class Content(val route: Route) : BottomSheetRoute()
}

/**
 * Converts a [Route] to a [BottomSheetRoute.SheetContent] route
 */
val Route.bottomSheetSheetContent get() = BottomSheetRoute.SheetContent(this)

/**
 * Converts a [Route] to a [BottomSheetRoute.Content] route
 */
val Route.bottomSheetContent get() = BottomSheetRoute.Content(this)

/**
 * A controller that handles the [Route] for a [ModalBottomSheetLayout]
 */
class BottomSheetRouteController(
    internal val contentRouteController: RouteController,
    internal val sheetContentRouteController: BottomSheetSheetContentRouteController
) {
    fun navigate(route: BottomSheetRoute) = when (route) {
        is BottomSheetRoute.SheetContent -> sheetContentRouteController.navigate(route.route)
        is BottomSheetRoute.Content -> contentRouteController.navigate(route.route)
    }
}

class BottomSheetContentNavHostRouteController(
    provider: StateFlow<BottomSheetNavigatorState?>,
    parent: RouteController? = null,
) : NavHostProvidingRouteController<BottomSheetNavigatorState>(provider, parent) {
    override fun BottomSheetNavigatorState.provide(): NavHostController = contentNavHostController
}

class BottomSheetSheetContentNavHostRouteController(
    provider: StateFlow<BottomSheetNavigatorState?>,
    parent: RouteController? = null,
) : NavHostProvidingRouteController<BottomSheetNavigatorState>(provider, parent) {
    override fun BottomSheetNavigatorState.provide(): NavHostController = sheetContentNavHostController
}

fun <A : NavigationAction<*>> BottomSheetContentRouteNavigator(
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    parentRouteController: RouteController? = null,
    navigationMapper: (A) -> Route
): NavHostProvidingContentRouteNavigator<A, BottomSheetNavigatorState> = NavHostProvidingContentRouteNavigator(
    BottomSheetContentNavHostRouteController(bottomSheetNavigator, parentRouteController),
    navigationMapper
)

class BottomSheetSheetContentRouteController(
    private val bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    private val coroutineScope: StateFlow<CoroutineScope?>
) : RouteController {

    private val sheetContentRouteController = BottomSheetSheetContentNavHostRouteController(bottomSheetNavigatorState, this)

    override fun navigate(newRoute: Route) {
        bottomSheetNavigatorState.value?.let { (_, _, sheetState) ->
            if (!sheetState.isVisible) {
                coroutineScope.value?.launch {
                    sheetState.animateTo(ModalBottomSheetValue.Expanded)
                }
            }
        }
        sheetContentRouteController.navigate(newRoute)
    }

    override fun back(result: Route.Result): Boolean {
        val navHostController = bottomSheetNavigatorState.value?.contentNavHostController
        return if (navHostController != null && navHostController.backQueue.isNotEmpty()) {
            navHostController.previousBackStackEntry?.setResult(result)
            navHostController.popBackStack()
        } else {
            close()
            true
        }
    }

    override fun close() {
        bottomSheetNavigatorState.value?.let { (_, _, sheetState) ->
            coroutineScope.value?.launch {
                sheetState.hide()
            }
        }
    }
}

sealed class ModalBottomSheetNavigator<A : NavigationAction<*>>(
    private val navigationMapper: (A) -> BottomSheetRoute
) : Navigator<A>, ComposableLifecycleSubscribable {

    abstract val routeController: BottomSheetRouteController

    override fun navigate(action: A) = routeController.navigate(navigationMapper(action))
}

class NavHostModalBottomSheetNavigator<A : NavigationAction<*>>(
    navigationMapper: (A) -> BottomSheetRoute,
    contentBuilder: BottomSheetContentBuilder,
    contentRootResultHandlers: List<NavHostRootResultHandler<*, *>> = emptyList(),
    sheetContentBuilder: BottomSheetContentBuilder,
    private val initialSheetValue: ModalBottomSheetValue = ModalBottomSheetValue.Hidden
) : ModalBottomSheetNavigator<A>(navigationMapper) {

    private val bottomSheetNavigationState = MutableStateFlow<BottomSheetNavigatorState?>(null)
    private val sheetStateCoroutineScope = MutableStateFlow<CoroutineScope?>(null)
    override val routeController = BottomSheetRouteController(
        BottomSheetContentNavHostRouteController(bottomSheetNavigationState),
        BottomSheetSheetContentRouteController(
            bottomSheetNavigationState,
            sheetStateCoroutineScope
        )
    )

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        val contentNavController = rememberNavController()
        bottomSheetNavigationState.tryEmit(
            BottomSheetNavigatorState(
                contentNavController,
                rememberNavController(),
                rememberModalBottomSheetState(initialValue = initialSheetValue)
            )
        )
        sheetStateCoroutineScope.tryEmit(rememberCoroutineScope())
        SetupNavigatingModalBottomSheetLayout(
            bottomSheetNavigationState,
            routeController.sheetContentRouteController,
            sheetContentBuilder,
            {
                contentRootResultHandlers.forEach { 
                    it.HandleResult(viewModel = this, navHostController = contentNavController)
                }
                content()
            },
            contentBuilder
        )
    }
}

class NavHostModalBottomSheetContentNavigator<A : NavigationAction<*>>(
    bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    navigationMapper: (A) -> BottomSheetRoute,
) : ModalBottomSheetNavigator<A>(navigationMapper) {

    private val sheetStateCoroutineScope = MutableStateFlow<CoroutineScope?>(null)


    override val routeController = BottomSheetRouteController(
        BottomSheetContentNavHostRouteController(bottomSheetNavigatorState),
        BottomSheetSheetContentRouteController(
            bottomSheetNavigatorState,
            sheetStateCoroutineScope
        )
    )

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        sheetStateCoroutineScope.tryEmit(rememberCoroutineScope())
        content()
    }
}

data class BottomSheetNavigatorState(
    val contentNavHostController: NavHostController,
    val sheetContentNavHostController: NavHostController,
    val sheetState: ModalBottomSheetState
    )

fun StateFlow<BottomSheetNavigatorState?>.contentNavHostControllerState(coroutineScope: CoroutineScope) = map { it?.contentNavHostController }.stateIn(coroutineScope, SharingStarted.Lazily, null)
fun StateFlow<BottomSheetNavigatorState?>.sheetContentNavHostControllerState(coroutineScope: CoroutineScope) = map { it?.sheetContentNavHostController }.stateIn(coroutineScope, SharingStarted.Lazily, null)

typealias BottomSheetRootContentBuilder = (bottomSheetNavigationState: StateFlow<BottomSheetNavigatorState?>) -> Unit
typealias BottomSheetContentBuilder = NavGraphBuilder.(bottomSheetNavigationState: StateFlow<BottomSheetNavigatorState?>) -> Unit

/**
 * Creates a [ModalBottomSheetLayout] that supports navigation using a [ModalBottomSheetNavigator]
 * @param sheetContent The [BottomSheetContentBuilder] for building the navigation sheet content of the [ModalBottomSheetLayout]
 * @param contentRoot The [BottomSheetRootContentBuilder] for building the root view of the content of the [ModalBottomSheetLayout]
 * @param content The [BottomSheetContentBuilder] for building the navigation content of the [ModalBottomSheetLayout]
 */
@Composable
private fun SetupNavigatingModalBottomSheetLayout(
    bottomSheetNavigationState: StateFlow<BottomSheetNavigatorState?>,
    sheetContentRouteController: RouteController,
    sheetContent: BottomSheetContentBuilder,
    contentRoot: @Composable () -> Unit,
    content: BottomSheetContentBuilder
) {
    val currentBottomSheetNavigationState by bottomSheetNavigationState.collectAsState()
    currentBottomSheetNavigationState?.let { navigationState ->
        ModalBottomSheetLayout(
            sheetContent = {
                if (navigationState.sheetState.isVisible) {
                    HardwareBackButtonNavigation(onBackButtonClickHandler = sheetContentRouteController::close)
                }
                Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                    SetupNavHost(
                        bottomSheetNavigatorState = bottomSheetNavigationState,
                        navHostController = { sheetContentNavHostController },
                    ) {state ->
                        sheetContent(
                            state
                        )
                    }
                }
            },
            sheetState = navigationState.sheetState,
        ) {
            SetupNavHost(
                bottomSheetNavigatorState = bottomSheetNavigationState,
                navHostController = { contentNavHostController },
                rootView = contentRoot
            ) { state ->
                content(
                    state
                )
            }
        }
    }
}

@Composable
fun SetupNavHost(
    bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    navHostController: BottomSheetNavigatorState.() -> NavHostController,
    builder: BottomSheetContentBuilder
) = SetupNavHost(
    bottomSheetNavigatorState = bottomSheetNavigatorState,
    navHostController = navHostController,
    rootView = { Spacer(modifier = Modifier.fillMaxWidth()) },
    builder
)

@Composable
fun SetupNavHost(
    bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    navHostController: BottomSheetNavigatorState.() -> NavHostController,
    rootView: @Composable () -> Unit,
    builder: BottomSheetContentBuilder
) {
    val currentBottomSheetNavigatorState by bottomSheetNavigatorState.collectAsState()
    currentBottomSheetNavigatorState?.let {
        SetupNavHost(
            navHostController = it.navHostController(),
            startDestination = ROOT_VIEW
        ) {
            composable(ROOT_VIEW, content = { rootView() })
            builder(bottomSheetNavigatorState)
        }
    }
}