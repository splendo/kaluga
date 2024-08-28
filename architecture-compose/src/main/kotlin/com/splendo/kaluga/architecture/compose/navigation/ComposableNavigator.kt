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
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param navigationMapper A mapper that converts an [Action] handled by this navigator into a [ComposableNavSpec]
 */
sealed class ComposableNavigator<Action : NavigationAction<*>>(private val navigationMapper: @Composable (Action) -> ComposableNavSpec) :
    Navigator<Action>,
    ComposableLifecycleSubscribable {

    protected abstract val routeController: RouteController?

    private val actionState = MutableStateFlow<Action?>(null)

    override fun navigate(action: Action) {
        actionState.value = action
    }

    final override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        // Make sure Navigation actions are launched
        SetupNavigationAction()
        // Actually modify the content
        contentModifier(content)
    }

    protected abstract val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit

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
 * A [ComposableNavigator] that creates and manages a [NavHost]. This should be on top of any [NavHostComposableNavigator] in the View-hierarchy
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param navigationMapper Maps [Action] to a [ComposableNavSpec] to be navigated to.
 * @param parentRouteController A [RouteController] managing the parent layout of the [NavHost].
 * @param rootResultHandlers A list of [NavHostResultHandler] to be added to the root view.
 * @param contentBuilder The [RouteContentBuilder] describing the navigation graph
 */
class RootNavHostComposableNavigator<Action : NavigationAction<*>>(
    navigationMapper: @Composable (Action) -> ComposableNavSpec,
    parentRouteController: RouteController? = null,
    rootResultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    contentBuilder: RouteContentBuilder,
) : ComposableNavigator<Action>(navigationMapper) {

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
            builder = contentBuilder,
        )
    }
}

/**
 * A [ComposableNavigator] that is managed by a [NavHost].
 * Must have a NavHost set up higher up in the View-hierarchy.
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param Provider the type of provider associated with the [ProvidingNavHostRouteController] controlling the route.
 * @param routeController The [ProvidingNavHostRouteController] controlling the route.
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [Action] to a [Route] to be navigated to.
 */
sealed class ProvidingNavHostComposableNavigator<Action : NavigationAction<*>, Provider>(
    override val routeController: ProvidingNavHostRouteController<Provider>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: @Composable (Action) -> ComposableNavSpec,
) : ComposableNavigator<Action>(navigationMapper) {

    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        // Add Result Handlers
        routeController.AddResultHandlers(viewModel = this, resultHandlers = resultHandlers)
        content()
    }
}

/**
 * A [ProvidingNavHostComposableNavigator] that navigates for a flow of [NavHostController]
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param navHostController The [StateFlow] of [NavHostController] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param parentRouteController An optional parent [RouteController] managing the navigation of a view higher in the hierarchy.
 * @param navigationMapper Maps [Action] to a [ComposableNavSpec] to be navigated to.
 */
class NavHostComposableNavigator<Action : NavigationAction<*>>(
    navHostController: StateFlow<NavHostController?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    parentRouteController: RouteController? = null,
    navigationMapper: @Composable (Action) -> ComposableNavSpec,
) : ProvidingNavHostComposableNavigator<Action, NavHostController>(
    NavHostRouteController(navHostController, parentRouteController),
    resultHandlers,
    navigationMapper,
)

/**
 * A [ProvidingNavHostComposableNavigator] that navigates for the [BottomSheetNavigatorState.contentNavHostController] of a flow of [BottomSheetNavigatorState]
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param bottomSheetNavigator The [StateFlow] of [BottomSheetNavigatorState] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param parentRouteController An optional parent [RouteController] managing the navigation of a view higher in the hierarchy.
 * @param navigationMapper Maps [Action] to a [ComposableNavSpec] to be navigated to.
 */
class BottomSheetContentNavHostComposableNavigator<Action : NavigationAction<*>>(
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    parentRouteController: RouteController? = null,
    navigationMapper: @Composable (Action) -> ComposableNavSpec,
) : ProvidingNavHostComposableNavigator<Action, BottomSheetNavigatorState>(
    BottomSheetContentRouteController(bottomSheetNavigator, parentRouteController),
    resultHandlers,
    navigationMapper,
)

/**
 * A [ComposableNavigator] that navigates for the sheet content of a flow of [BottomSheetNavigatorState]
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param bottomSheetNavigator The flow of [BottomSheetNavigatorState] to manage this navigator
 * @param resultHandlers The [NavHostResultHandler] to add to this navigator.
 * @param navigationMapper Maps [Action] to a [ComposableNavSpec] to be navigated to.
 */
class BottomSheetSheetContentNavHostComposableNavigator<Action : NavigationAction<*>>(
    bottomSheetNavigator: StateFlow<BottomSheetNavigatorState?>,
    resultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: @Composable (Action) -> ComposableNavSpec,
) : ComposableNavigator<Action>(navigationMapper) {

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
 * @param Action the type of [NavigationAction] this navigator should respond to.
 * @param navigationMapper Maps [Action] to a [ComposableNavSpec.LaunchedNavigation] to be navigated to.
 */
class LaunchedComposableNavigator<Action : NavigationAction<*>>(navigationMapper: @Composable (Action) -> ComposableNavSpec.LaunchedNavigation) :
    ComposableNavigator<Action>(navigationMapper) {
    override val routeController: RouteController? = null
    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content -> content() }
}
