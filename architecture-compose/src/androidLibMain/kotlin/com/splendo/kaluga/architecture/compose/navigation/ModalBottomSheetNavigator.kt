package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

class BottomSheetSheetContentRouteController(
    val navHostController: StateFlow<NavHostController?>,
    private val sheetState: SheetState
) : RouteController {

    private val sheetContentRouteController = NavHostRouteController(navHostController, this)

    override fun navigate(newRoute: Route) {
        if (!sheetState.isVisible) {
            sheetState.show(ModalBottomSheetValue.Expanded)
        }
        sheetContentRouteController.navigate(newRoute)
    }

    override fun back(result: Route.Result): Boolean {
        val navHostController = navHostController.value
        return if (navHostController != null && navHostController.backQueue.isNotEmpty()) {
            navHostController.previousBackStackEntry?.setResult(result)
            navHostController.popBackStack()
        } else {
            close()
            true
        }
    }

    override fun close() {
        sheetState.hide()
    }
}

interface SheetState {
    val isVisible: Boolean
    fun show(value: ModalBottomSheetValue)
    fun hide()
}

private object EmptySheetState : SheetState {
    override val isVisible: Boolean = false
    override fun show(value: ModalBottomSheetValue) {}
    override fun hide() {}
}

private data class SheetStateState(
    val state: ModalBottomSheetState,
    val coroutineScope: CoroutineScope?
) : SheetState {
    override val isVisible: Boolean get() = state.isVisible
    override fun show(value: ModalBottomSheetValue) {
        coroutineScope?.launch { state.animateTo(value)  }
    }

    override fun hide() {
        coroutineScope?.launch { state.hide() }
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
    sheetContentBuilder: BottomSheetContentBuilder,
    private val initialSheetValue: ModalBottomSheetValue = ModalBottomSheetValue.Hidden
) : ModalBottomSheetNavigator<A>(navigationMapper) {
    private val contentNavHostController = MutableStateFlow<NavHostController?>(null)
    private val sheetContentNavHostController = MutableStateFlow<NavHostController?>(null)
    private val sheetState = MutableStateFlow<SheetState>(EmptySheetState)
    override val routeController = BottomSheetRouteController(
        NavHostRouteController(contentNavHostController),
        BottomSheetSheetContentRouteController(
            sheetContentNavHostController,
            object : SheetState {
                override val isVisible: Boolean = sheetState.value.isVisible
                override fun show(value: ModalBottomSheetValue) = sheetState.value.show(value)
                override fun hide() = sheetState.value.hide()
            }
        )
    )

    override val modifier: @Composable (@Composable () -> Unit) -> Unit = @Composable { content ->
        val contentNavController = rememberNavController()
        val sheetContentNavController = rememberNavController()
        val modalBottomSheetState = rememberModalBottomSheetState(initialValue = initialSheetValue)
        val coroutineScope = rememberCoroutineScope()
        contentNavHostController.tryEmit(contentNavController)
        sheetContentNavHostController.tryEmit(sheetContentNavController)
        sheetState.tryEmit(SheetStateState(modalBottomSheetState, coroutineScope))
        SetupNavigatingModalBottomSheetLayout(
            routeController.sheetContentRouteController,
            contentNavController,
            sheetContentNavController,
            sheetContentBuilder,
            content,
            contentBuilder,
            modalBottomSheetState
        )
    }
}

class NavHostModalBottomSheetContentNavigator<A : NavigationAction<*>>(
    contentNavHostController: NavHostController,
    sheetContentNavHostController: NavHostController,
    sheetState: ModalBottomSheetState,
    navigationMapper: (A) -> BottomSheetRoute,
) : ModalBottomSheetNavigator<A>(navigationMapper) {

    private val sheetStateState = MutableStateFlow<SheetState>(SheetStateState(sheetState, null))
    override val routeController = BottomSheetRouteController(
        NavHostRouteController(MutableStateFlow(contentNavHostController)),
        BottomSheetSheetContentRouteController(
            MutableStateFlow(sheetContentNavHostController),
            object : SheetState {
                override val isVisible: Boolean = sheetStateState.value.isVisible
                override fun show(value: ModalBottomSheetValue) = sheetStateState.value.show(value)
                override fun hide() = sheetStateState.value.hide()
            }
        )
    )

    override val modifier: @Composable (@Composable () -> Unit) -> Unit = @Composable { content ->
        sheetStateState.tryEmit(SheetStateState(sheetState, rememberCoroutineScope()))
        content()
    }
}

typealias BottomSheetRootContentBuilder = (contentNavHostController: NavHostController, sheetContentNavHostController: NavHostController, sheetState: ModalBottomSheetState) -> Unit
typealias BottomSheetContentBuilder = NavGraphBuilder.(contentNavHostController: NavHostController, sheetContentNavHostController: NavHostController, sheetState: ModalBottomSheetState) -> Unit

/**
 * Creates a [ModalBottomSheetLayout] that supports navigation using a [ModalBottomSheetNavigator]
 * @param sheetContent The [BottomSheetContentBuilder] for building the navigation sheet content of the [ModalBottomSheetLayout]
 * @param contentRoot The [BottomSheetRootContentBuilder] for building the root view of the content of the [ModalBottomSheetLayout]
 * @param content The [BottomSheetContentBuilder] for building the navigation content of the [ModalBottomSheetLayout]
 */
@Composable
private fun SetupNavigatingModalBottomSheetLayout(
    sheetContentRouteController: RouteController,
    contentNavHostController: NavHostController,
    sheetContentNavHostController: NavHostController,
    sheetContent: BottomSheetContentBuilder,
    contentRoot: @Composable () -> Unit,
    content: BottomSheetContentBuilder,
    sheetState: ModalBottomSheetState
) = ModalBottomSheetLayout(
    sheetContent = {
        if (sheetState.isVisible) {
            HardwareBackButtonNavigation(onBackButtonClickHandler = sheetContentRouteController::close)
        }
        Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
            SetupNavHost(sheetContentNavHostController) { navHostController ->
                sheetContent(
                    contentNavHostController,
                    navHostController,
                    sheetState
                )
            }
        }
    },
    sheetState = sheetState,
) {
    SetupNavHost(
        navHostController = contentNavHostController,
        rootView = contentRoot
    ) { navHostController ->
        content(
            navHostController,
            sheetContentNavHostController,
            sheetState
        )
    }
}
