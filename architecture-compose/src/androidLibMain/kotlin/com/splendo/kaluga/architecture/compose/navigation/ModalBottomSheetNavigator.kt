package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
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

/**
 * A [RouteController] used for navigating the sheet content in a [ModalBottomSheetLayout]
 * @param navHostController The [NavHostController] to navigate the sheet content
 * @param sheetState The [ModalBottomSheetState] associated with the [ModalBottomSheetLayout]
 * @param coroutineScope A [CoroutineScope] used for navigation actions
 */
class BottomSheetSheetContentRouteController(
    internal val navHostController: NavHostController,
    internal val sheetState: ModalBottomSheetState,
    private val coroutineScope: CoroutineScope
) : RouteController {

    private val sheetContentRouteController = NavHostRouteController(navHostController, this)

    override fun navigate(newRoute: Route) {
        if (!sheetState.isVisible) {
            coroutineScope.launch {
                sheetState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }
        sheetContentRouteController.navigate(newRoute)
    }

    override fun back(result: Map<String, Any?>): Boolean = if (navHostController.backQueue.isNotEmpty()) {
        navHostController.previousBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
            result.entries.forEach { (key, value) -> savedStateHandle[key] = value }
        }
        navHostController.popBackStack()
    } else {
        close()
        true
    }

    override fun close() {
        coroutineScope.launch {
            sheetState.hide()
        }
    }
}

/**
 * A [Navigator] for a [ModalBottomSheetLayout]
 * @param routeController The [BottomSheetRouteController] used for navigating.
 * @param navigationMapper A mapper that converts an [NavigationAction] handled by this navigator into a [BottomSheetRoute]
 */
class ModalBottomSheetNavigator<A : NavigationAction<*>>(
    internal val routeController: BottomSheetRouteController,
    private val navigationMapper: (A) -> BottomSheetRoute
) : Navigator<A> {

    constructor(
        contentNavHostController: NavHostController,
        sheetContentNavHostController: NavHostController,
        sheetState: ModalBottomSheetState,
        coroutineScope: CoroutineScope,
        navigationMapper: (A) -> BottomSheetRoute
    ) : this(
        BottomSheetRouteController(
            NavHostRouteController(contentNavHostController),
            BottomSheetSheetContentRouteController(
                sheetContentNavHostController,
                sheetState,
                coroutineScope
            )
        ),
        navigationMapper
    )

    constructor(
        contentRouteController: RouteController,
        sheetContentRouteController: BottomSheetSheetContentRouteController,
        navigationMapper: (A) -> BottomSheetRoute
    ) : this(
        BottomSheetRouteController(contentRouteController, sheetContentRouteController),
        navigationMapper
    )

    override fun navigate(action: A) = routeController.navigate(navigationMapper(action))
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
fun <A : NavigationAction<*>> ModalBottomSheetNavigator<A>.NavigatingModalBottomSheetLayout(
    sheetContent: BottomSheetContentBuilder,
    contentRoot: @Composable BottomSheetRootContentBuilder,
    content: BottomSheetContentBuilder
) = routeController.NavigatingModalBottomSheetLayout(
    sheetContent = sheetContent,
    contentRoot = contentRoot,
    content = content
)

/**
 * Creates a [ModalBottomSheetLayout] that supports navigation using a [ModalBottomSheetNavigator]
 * @param sheetContent The [BottomSheetContentBuilder] for building the navigation sheet content of the [ModalBottomSheetLayout]
 * @param contentRoot The [BottomSheetRootContentBuilder] for building the root view of the content of the [ModalBottomSheetLayout]
 * @param content The [BottomSheetContentBuilder] for building the navigation content of the [ModalBottomSheetLayout]
 */
@Composable
fun BottomSheetRouteController.NavigatingModalBottomSheetLayout(
    sheetContent: BottomSheetContentBuilder,
    contentRoot: @Composable BottomSheetRootContentBuilder,
    content: BottomSheetContentBuilder
) = ModalBottomSheetLayout(
    sheetContent = {
        if (sheetContentRouteController.sheetState.isVisible) {
            HardwareBackButtonNavigation(onBackButtonClickHandler = sheetContentRouteController::close)
        }
        Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
            sheetContentRouteController.SetupNavHost { sheetContentNavHostController ->
                sheetContent(
                    contentRouteController.navHostController,
                    sheetContentNavHostController,
                    sheetContentRouteController.sheetState
                )
            }
        }
    },
    sheetState = sheetContentRouteController.sheetState,
) {
    contentRouteController.SetupNavHost(
        rootView = { navHostController ->
            contentRoot(
                navHostController,
                sheetContentRouteController.navHostController,
                sheetContentRouteController.sheetState
            )
        }
    ) { navHostController ->
        content(
            navHostController,
            sheetContentRouteController.navHostController,
            sheetContentRouteController.sheetState
        )
    }
}
