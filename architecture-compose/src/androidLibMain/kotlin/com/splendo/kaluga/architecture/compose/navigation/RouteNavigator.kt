package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.compose.navigation.result.NavHostResultHandler
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

typealias RouteContentBuilder = NavGraphBuilder.(StateFlow<NavHostController?>) -> Unit

/**
 * A Navigator managed by a [RouteController]
 * @param navigationMapper A mapper that converts an [NavigationAction] handled by this navigator into a [Route]
 */
sealed class RouteNavigator<A : NavigationAction<*>>(
    private val navigationMapper: (A) -> Route
) : Navigator<A>, ComposableLifecycleSubscribable {

    /**
     * [RouteController] managing the [Route] navigation
     */
    abstract val routeController: RouteController

    override fun navigate(action: A) {
        routeController.navigate(navigationMapper(action))
    }
}

/**
 * A [RouteNavigator] that creates and manages a [NavHost]. This should be on top of any [NavHostRouteNavigator]
 * @param navigationMapper Maps [A] to a [Route] to be navigated to.
 * @param parentRouteController A [RouteController] managing the parent layout of the [NavHost].
 * @param rootResultHandlers A list of [NavHostResultHandler] to be added to the root view.
 * @param contentBuilder The [RouteContentBuilder] describing the navigation graph
 */
class RootNavHostRouteNavigator<A : NavigationAction<*>>(
    navigationMapper: (A) -> Route,
    parentRouteController: RouteController? = null,
    rootResultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    contentBuilder: RouteContentBuilder
) : RouteNavigator<A>(navigationMapper) {

    private val navHostController = MutableStateFlow<NavHostController?>(null)
    override val routeController: RouteController = NavHostRouteController(navHostController, parentRouteController)

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        val navController = rememberNavController()
        navHostController.tryEmit(navController)
        SetupNavHost(
            navHostController = navHostController,
            rootView = {
                rootResultHandlers.forEach { resultHandler ->
                    resultHandler.HandleResult(viewModel = this, navHostController = navController)
                }
                content()
            },
            builder = contentBuilder
        )
    }
}

/**
 * A [RouteNavigator] that is managed by a [NavHost]
 * @param routeController The [ProvidingNavHostRouteController] controlling the route.
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [Route] to be navigated to.
 */
sealed class ProvidingNavHostRouteNavigator<A : NavigationAction<*>, Provider>(
    override val routeController: ProvidingNavHostRouteController<Provider>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: (A) -> Route
) : RouteNavigator<A>(navigationMapper) {

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        routeController.AddResultHandlers(viewModel = this, resultHandlers = resultHandlers)
        content()
    }
}

/**
 * A [RouteNavigator] that navigates for a flow of [NavHostController]
 * @param navHostController The flow of [NavHostController] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [Route] to be navigated to.
 */
class NavHostRouteNavigator<A : NavigationAction<*>> (
    navHostController: StateFlow<NavHostController?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    parentRouteController: RouteController? = null,
    navigationMapper: (A) -> Route
) : ProvidingNavHostRouteNavigator<A, NavHostController>(
    NavHostRouteController(navHostController, parentRouteController),
    resultHandlers,
    navigationMapper
)

/**
 * A [RouteNavigator] that navigates for the content of a flow of [BottomSheetNavigatorState]
 * @param bottomSheetNavigator The flow of [BottomSheetNavigatorState] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [Route] to be navigated to.
 */
class BottomSheetContentNavHostRouteNavigator<A : NavigationAction<*>> (
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    parentRouteController: RouteController? = null,
    navigationMapper: (A) -> Route
) : ProvidingNavHostRouteNavigator<A, BottomSheetNavigatorState>(
    BottomSheetContentRouteController(bottomSheetNavigator, parentRouteController),
    resultHandlers,
    navigationMapper
)

/**
 * A [RouteNavigator] that navigates for the sheet content of a flow of [BottomSheetNavigatorState]
 * @param bottomSheetNavigator The flow of [BottomSheetNavigatorState] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [Route] to be navigated to.
 */
class BottomSheetSheetContentNavHostRouteNavigator<A : NavigationAction<*>> (
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: (A) -> Route
) : RouteNavigator<A>(navigationMapper) {

    private val coroutineScopeState = MutableStateFlow<CoroutineScope?>(null)
    override val routeController = BottomSheetSheetContentRouteController(bottomSheetNavigator, coroutineScopeState)

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        coroutineScopeState.tryEmit(rememberCoroutineScope())
        routeController.sheetContentRouteController.AddResultHandlers(viewModel = this, resultHandlers = resultHandlers)
        content()
    }
}
