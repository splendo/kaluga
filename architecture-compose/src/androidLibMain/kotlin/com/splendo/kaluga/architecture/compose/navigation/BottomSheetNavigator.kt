package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.compose.navigation.result.NavHostResultHandler
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A Navigator managed by a [BottomSheetRouteController]
 * @param navigationMapper A mapper that converts an [NavigationAction] handled by this navigator into a [BottomSheetRoute]
 */
sealed class BottomSheetNavigator<A : NavigationAction<*>>(
    private val navigationMapper: @Composable (A) -> BottomSheetComposableNavSpec
) : Navigator<A>, ComposableLifecycleSubscribable {

    abstract val routeController: BottomSheetRouteController

    private val actionState = MutableStateFlow<A?>(null)

    override fun navigate(action: A) {
        actionState.value = action
    }

    final override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        SetupNavigationAction()
        contentModifier(content)
    }

    abstract val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit

    @Composable
    protected fun BaseLifecycleViewModel.SetupNavigationAction() {
        val currentAction by actionState.collectAsState()
        val onDispose: () -> Unit = { actionState.compareAndSet(currentAction, null) }
        currentAction?.let { action ->
            when (val spec = navigationMapper(action)) {
                is BottomSheetRoute -> LaunchedEffect(spec) {
                    routeController.navigate(spec)
                    onDispose()
                }
                is BottomSheetLaunchedNavigation -> spec.launchedNavigation.Launch(this, onDispose)
            }
        }
    }
}

/**
 * A [BottomSheetNavigator] that creates and manages a [ModalBottomSheetLayout]. This should be on top of any [ModalBottomSheetNavigator]
 * @param navigationMapper Maps [A] to a [BottomSheetComposableNavSpec] to be navigated to.
 * @param initialSheetValue The [ModalBottomSheetValue] the [ModalBottomSheetLayout] should default to.
 * @param parentRouteController A [RouteController] managing the parent view of the [ModalBottomSheetLayout]
 * @param contentRootResultHandlers A list of [NavHostResultHandler] to be added to the root view of the content.
 * @param contentBuilder The [BottomSheetContentBuilder] describing the navigation graph of the content.
 * @param sheetContentBuilder The [BottomSheetContentBuilder] describing the navigation graph of the sheet content.
 */
class RootModalBottomSheetNavigator<A : NavigationAction<*>>(
    navigationMapper: @Composable (A) -> BottomSheetComposableNavSpec,
    private val initialSheetValue: ModalBottomSheetValue = ModalBottomSheetValue.Hidden,
    parentRouteController: RouteController? = null,
    contentRootResultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    contentBuilder: BottomSheetContentBuilder,
    sheetContentBuilder: BottomSheetContentBuilder
) : BottomSheetNavigator<A>(navigationMapper) {

    private val bottomSheetNavigationState = MutableStateFlow<BottomSheetNavigatorState?>(null)
    private val sheetStateCoroutineScope = MutableStateFlow<CoroutineScope?>(null)
    override val routeController = BottomSheetRouteController(
        BottomSheetContentRouteController(bottomSheetNavigationState, parentRouteController),
        BottomSheetSheetContentRouteController(
            bottomSheetNavigationState,
            sheetStateCoroutineScope
        )
    )

    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
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

/**
 * A [BottomSheetNavigator] that is managed by a [ModalBottomSheetLayout].
 * @param bottomSheetNavigatorState The [BottomSheetNavigatorState] controlling the routes.
 * @param contentResultHandlers A list of [NavHostResultHandler] to be added to the content of this navigator.
 * @param sheetContentResultHandlers A list of [NavHostResultHandler] to be added to the sheet content of this navigator.
 * @param navigationMapper Maps [A] to a [BottomSheetComposableNavSpec] to be navigated to.
 */
class ModalBottomSheetNavigator<A : NavigationAction<*>>(
    bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    contentResultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    sheetContentResultHandlers: List<NavHostResultHandler<*, *>> = emptyList(),
    navigationMapper: @Composable (A) -> BottomSheetComposableNavSpec
) : BottomSheetNavigator<A>(navigationMapper) {

    private val sheetStateCoroutineScope = MutableStateFlow<CoroutineScope?>(null)

    override val routeController = BottomSheetRouteController(
        BottomSheetContentRouteController(bottomSheetNavigatorState),
        BottomSheetSheetContentRouteController(
            bottomSheetNavigatorState,
            sheetStateCoroutineScope
        )
    )

    override val contentModifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        sheetStateCoroutineScope.tryEmit(rememberCoroutineScope())
        routeController.contentRouteController.AddResultHandlers(viewModel = this, resultHandlers = contentResultHandlers)
        routeController.sheetContentRouteController.sheetContentRouteController.AddResultHandlers(viewModel = this, resultHandlers = sheetContentResultHandlers)
        content()
    }
}

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
                    ) { state ->
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
