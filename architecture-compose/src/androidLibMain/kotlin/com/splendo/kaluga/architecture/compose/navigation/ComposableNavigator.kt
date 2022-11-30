package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
sealed class ComposableNavigator<A : NavigationAction<*>>(
    private val navigationMapper: @Composable (A) -> ComposableNavSpec
) : Navigator<A>, ComposableLifecycleSubscribable {

    /**
     * [RouteController] managing the [Route] navigation
     */
    abstract val routeController: RouteController?

    private val actionState = MutableStateFlow<A?>(null)

    override fun navigate(action: A) {
        actionState.value = action
    }

    final override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        // Make sure Navigation actions are launched
        SetupNavigationAction()
        // Actually modify the content
        contentModifier(content)
    }

    abstract val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit

    @Composable
    protected fun BaseLifecycleViewModel.SetupNavigationAction() {
        val currentAction by actionState.collectAsState()
        // If there is a current action, render it
        currentAction?.let { action ->
            // We should only set the actionState back to null when navigation has completed.
            // Otherwise some logic may break, such as with rememberLauncherForActivityResult.
            val onDispose: () -> Unit = { actionState.compareAndSet(currentAction, null) }
            when (val spec = navigationMapper(action)) {
                is Route -> {
                    // Navigate the route controller using a LaunchedEffect to ensure it only fires once.
                    LaunchedEffect(this, spec) {
                        routeController?.navigate(spec)
                        onDispose()
                    }
                }
                is ComposableNavSpec.LaunchedNavigation -> spec.Launch(this, onDispose)
            }
        }
    }
}

/**
 * A [ComposableNavigator] that creates and manages a [NavHost]. This should be on top of any [NavHostComposableNavigator]
 * @param navigationMapper Maps [A] to a [ComposableNavSpec] to be navigated to.
 * @param parentRouteController A [RouteController] managing the parent layout of the [NavHost].
 * @param rootResultHandlers A list of [NavHostResultHandler] to be added to the root view.
 * @param contentBuilder The [RouteContentBuilder] describing the navigation graph
 */
class RootNavHostComposableNavigator<A : NavigationAction<*>>(
    navigationMapper: @Composable (A) -> ComposableNavSpec,
    parentRouteController: RouteController? = null,
    rootResultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    contentBuilder: RouteContentBuilder
) : ComposableNavigator<A>(navigationMapper) {

    private val navHostController = MutableStateFlow<NavHostController?>(null)
    override val routeController: RouteController = NavHostRouteController(navHostController, parentRouteController)

    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        val navController = rememberNavController()
        // Update the NavHostController to the current state
        navHostController.tryEmit(navController)

        // Show a NavHost displaying the content
        SetupNavHost(
            navHostController = navHostController,
            rootView = {
                // Handle results of the Root View
                rootResultHandlers.forEach { resultHandler ->
                    resultHandler.HandleResult(viewModel = this, navHostController = navController)
                }

                // Render original content
                content()
            },
            builder = contentBuilder
        )
    }
}

/**
 * A [ComposableNavigator] that is managed by a [NavHost]
 * @param routeController The [ProvidingNavHostRouteController] controlling the route.
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [Route] to be navigated to.
 */
sealed class ProvidingNavHostComposableNavigator<A : NavigationAction<*>, Provider>(
    override val routeController: ProvidingNavHostRouteController<Provider>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: @Composable (A) -> ComposableNavSpec
) : ComposableNavigator<A>(navigationMapper) {

    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        // Add Result Handlers
        routeController.AddResultHandlers(viewModel = this, resultHandlers = resultHandlers)
        content()
    }
}

/**
 * A [ComposableNavigator] that navigates for a flow of [NavHostController]
 * @param navHostController The flow of [NavHostController] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [ComposableNavSpec] to be navigated to.
 */
class NavHostComposableNavigator<A : NavigationAction<*>>(
    navHostController: StateFlow<NavHostController?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    parentRouteController: RouteController? = null,
    navigationMapper: @Composable (A) -> ComposableNavSpec
) : ProvidingNavHostComposableNavigator<A, NavHostController>(
    NavHostRouteController(navHostController, parentRouteController),
    resultHandlers,
    navigationMapper
)

/**
 * A [ComposableNavigator] that navigates for the content of a flow of [BottomSheetNavigatorState]
 * @param bottomSheetNavigator The flow of [BottomSheetNavigatorState] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [ComposableNavSpec] to be navigated to.
 */
class BottomSheetContentNavHostComposableNavigator<A : NavigationAction<*>>(
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    parentRouteController: RouteController? = null,
    navigationMapper: @Composable (A) -> ComposableNavSpec
) : ProvidingNavHostComposableNavigator<A, BottomSheetNavigatorState>(
    BottomSheetContentRouteController(bottomSheetNavigator, parentRouteController),
    resultHandlers,
    navigationMapper
)

/**
 * A [ComposableNavigator] that navigates for the sheet content of a flow of [BottomSheetNavigatorState]
 * @param bottomSheetNavigator The flow of [BottomSheetNavigatorState] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [A] to a [ComposableNavSpec] to be navigated to.
 */
class BottomSheetSheetContentNavHostComposableNavigator<A : NavigationAction<*>>(
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: @Composable (A) -> ComposableNavSpec
) : ComposableNavigator<A>(navigationMapper) {

    private val coroutineScopeState = MutableStateFlow<CoroutineScope?>(null)
    override val routeController = BottomSheetSheetContentRouteController(bottomSheetNavigator, coroutineScopeState)

    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        coroutineScopeState.tryEmit(rememberCoroutineScope())
        // Add Result Handlers for SheetContent
        routeController.sheetContentRouteController.AddResultHandlers(viewModel = this, resultHandlers = resultHandlers)
        content()
    }
}

/**
 * A [ComposableNavigator] that only supports [ComposableNavSpec.LaunchedNavigation] navigation.
 * @param navigationMapper Maps [A] to a [ComposableNavSpec.LaunchedNavigation] to be navigated to.
 */
class LaunchedComposableNavigator<A : NavigationAction<*>>(
    navigationMapper: @Composable (A) -> ComposableNavSpec.LaunchedNavigation
) : ComposableNavigator<A>(navigationMapper) {
    override val routeController: RouteController? = null
    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content -> content() }
}
