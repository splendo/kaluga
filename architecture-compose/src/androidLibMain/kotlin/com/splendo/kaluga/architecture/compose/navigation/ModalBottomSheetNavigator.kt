package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class BottomSheetRoute {
    data class SheetContent(val route: Route): BottomSheetRoute()
    data class Content(val route: Route): BottomSheetRoute()
}

val Route.bottomSheetSheetContent get() = BottomSheetRoute.SheetContent(this)
val Route.bottomSheetContent get() = BottomSheetRoute.Content(this)

class BottomSheetRouteController(
    private val navHostController: NavHostController,
    private val sheetState: ModalBottomSheetState,
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
    private val contentRouteController: RouteController,
    private val sheetContentRouteController: BottomSheetRouteController,
    private val navigationMapper: (A) -> BottomSheetRoute
) : Navigator<A> {

    override fun navigate(action: A) {
        when (val bottomSheetRoute = navigationMapper(action)) {
            is BottomSheetRoute.SheetContent -> sheetContentRouteController.navigate(bottomSheetRoute.route)
            is BottomSheetRoute.Content -> contentRouteController.navigate(bottomSheetRoute.route)
        }
    }
}
