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

sealed class BottomSheetRoute {
    data class SheetContent(val route: Route) : BottomSheetRoute()
    data class Content(val route: Route) : BottomSheetRoute()
}

val Route.bottomSheetSheetContent get() = BottomSheetRoute.SheetContent(this)
val Route.bottomSheetContent get() = BottomSheetRoute.Content(this)

class BottomSheetRouteController(
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

    override fun back(): Boolean = navHostController.popBackStack() || kotlin.run {
        close()
        true
    }

    override fun close() {
        coroutineScope.launch {
            sheetState.animateTo(ModalBottomSheetValue.Hidden)
        }
    }
}

/** Navigator for [ModalBottomSheetLayout]. */
class ModalBottomSheetNavigator<A : NavigationAction<*>>(
    internal val contentRouteController: RouteController,
    internal val sheetContentRouteController: BottomSheetRouteController,
    private val navigationMapper: (A) -> BottomSheetRoute
) : Navigator<A> {

    constructor(
        contentNavHostController: NavHostController,
        sheetContentNavHostController: NavHostController,
        sheetState: ModalBottomSheetState,
        coroutineScope: CoroutineScope,
        navigationMapper: (A) -> BottomSheetRoute
    ) : this(
        NavHostRouteController(contentNavHostController),
        BottomSheetRouteController(sheetContentNavHostController, sheetState, coroutineScope),
        navigationMapper
    )

    override fun navigate(action: A) {
        when (val bottomSheetRoute = navigationMapper(action)) {
            is BottomSheetRoute.SheetContent -> sheetContentRouteController.navigate(
                bottomSheetRoute.route
            )
            is BottomSheetRoute.Content -> contentRouteController.navigate(bottomSheetRoute.route)
        }
    }
}

typealias BottomSheetContentBuilder = NavGraphBuilder.(contentNavHostController: NavHostController, sheetContentNavHostController: NavHostController, sheetState: ModalBottomSheetState) -> Unit

@Composable
fun <A : NavigationAction<*>> NavigatingModalBottomSheetLayout(
    navigator: ModalBottomSheetNavigator<A>,
    sheetContent: BottomSheetContentBuilder,
    contentRoot: @Composable BottomSheetContentBuilder,
    content: BottomSheetContentBuilder,
) = ModalBottomSheetLayout(
    sheetContent = {
        if (navigator.sheetContentRouteController.sheetState.isVisible) {
            HardwareBackButtonNavigation(onBackButtonClickHandler = navigator.sheetContentRouteController::close)
        }
        Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
            navigator.sheetContentRouteController.SetupNavHost {
                sheetContent(
                    navigator.contentRouteController.navHostController,
                    navigator.sheetContentRouteController.navHostController,
                    navigator.sheetContentRouteController.sheetState
                )
            }
        }
    },
    sheetState = navigator.sheetContentRouteController.sheetState,
) {
    navigator.contentRouteController.SetupNavHost(
        rootView = {
            contentRoot(
                navigator.contentRouteController.navHostController,
                navigator.sheetContentRouteController.navHostController,
                navigator.sheetContentRouteController.sheetState
            )
        }
    ) {
        content(
            navigator.contentRouteController.navHostController,
            navigator.sheetContentRouteController.navHostController,
            navigator.sheetContentRouteController.sheetState
        )
    }
}
