package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator

sealed interface RouteController {
    fun navigate(newRoute: Route)
    fun back(): Boolean
    fun close()
}

internal val RouteController.navHostController get() = when (this) {
    is NavHostRouteController -> navHostController
    is BottomSheetRouteController -> navHostController
}

class NavHostRouteController(
    internal val navHostController: NavHostController,
    private val parentRouteController: RouteController? = null
) : RouteController {
    override fun navigate(newRoute: Route) {
        when (newRoute) {
            is Route.NextRoute<*, *> -> navHostController.navigate(newRoute.route) {
                launchSingleTop = true
            }
            is Route.FromRoute<*, *> -> navHostController.navigate(newRoute.route) {
                popUpTo(newRoute.from)
            }
            is Route.Replace<*, *> -> {
                navHostController.popBackStack()
                navHostController.navigate(newRoute.route)
            }
            is Route.PopTo<*, *> -> {
                navHostController.popBackStack(newRoute.route, false)
            }
            is Route.PopToIncluding<*, *> -> {
                navHostController.popBackStack(newRoute.route, true)
            }
            is Route.Back -> back()
            is Route.PopToRoot -> navHostController.popBackStack(ROOT_VIEW, false)
            is Route.Close -> close()
        }
    }

    override fun back(): Boolean = navHostController.popBackStack() || parentRouteController?.back() ?: false
    override fun close() {
        navHostController.popBackStack(ROOT_VIEW, true)
        parentRouteController?.close()
    }
}

/**
 * Routes [Navigator] calls to [NavHostController].
 */
open class RouteNavigator<A : NavigationAction<*>>(
    private val routeController: RouteController,
    private val navigationMapper: (A) -> Route
) : Navigator<A> {

    constructor(
        navHostController: NavHostController,
        navigationMapper: (A) -> Route
    ) : this(NavHostRouteController(navHostController), navigationMapper)

    override fun navigate(action: A) {
        routeController.navigate(navigationMapper(action))
    }
}

@Composable
fun RouteController.SetupNavHost(builder: NavGraphBuilder.(RouteController) -> Unit) {
    SetupNavHost(startDestination = ROOT_VIEW) {
        composable(ROOT_VIEW) {
            Spacer(modifier = Modifier.fillMaxWidth())
        }
        builder(this@SetupNavHost)
    }
}

@Composable
fun RouteController.SetupNavHost(
    rootView: @Composable NavGraphBuilder.(RouteController) -> Unit,
    builder: NavGraphBuilder.(RouteController) -> Unit
) {
    SetupNavHost(startDestination = ROOT_VIEW) {
        composable(ROOT_VIEW, content = { rootView(this@SetupNavHost) })
        builder(this@SetupNavHost)
    }
}

@Composable
fun RouteController.SetupNavHost(
    startDestination: String,
    builder: NavGraphBuilder.(RouteController) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        builder = { builder(this@SetupNavHost) }
    )
}
