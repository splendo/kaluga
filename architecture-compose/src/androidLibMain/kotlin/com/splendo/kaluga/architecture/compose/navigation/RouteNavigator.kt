package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlin.reflect.KClass

/**
 * Routes [Navigator] calls to [NavHostController].
 */
open class RouteNavigator<A : NavigationAction<*>>(
    protected val navController: NavHostController,
    private val navigationMapper: (A) -> Route,
    private val parentNavigator: RouteNavigator<*>? = null
) : Navigator<A> {

    override fun navigate(action: A) {
        navigate(navigationMapper(action))
    }

    protected fun navigate(newRoute: Route) {
        when (newRoute) {
            is Route.NextRoute<*, *> -> {
                val currentDestination = navController.currentBackStackEntry?.destination
                if (currentDestination?.route?.stripArguments() != newRoute.route.stripArguments()) {
                    navController.navigate(newRoute.route)
                }
            }
            is Route.FromRoute<*, *> -> navController.navigate(newRoute.route) {
                popUpTo(newRoute.from)
            }
            is Route.Replace<*, *> -> {
                navController.popBackStack()
                navController.navigate(newRoute.route)
            }
            is Route.PopTo<*, *> -> {
                navController.popBackStack(newRoute.route, false)
            }
            is Route.PopToIncluding<*, *> -> {
                navController.popBackStack(newRoute.route, true)
            }
            is Route.Back -> back()
            is Route.PopToRoot -> navController.popBackStack(ROOT_VIEW, false)
        }
    }

    fun back(): Boolean = navController.popBackStack() || parentNavigator?.back() ?: false

    private fun String.stripArguments(): String {
        val components = split("/")

        val strippedComponents = components.lastOrNull()?.let {
            if (it.startsWith("{") && it.endsWith("}")) {
                components.dropLast(1)
            } else {
                components
            }
        } ?: components
        return strippedComponents.joinToString("/")
    }
}

@Composable
fun NavHostController.SetupNavHost(builder: NavGraphBuilder.() -> Unit) {
    SetupNavHost(startDestination = ROOT_VIEW) {
        composable(ROOT_VIEW) {}
        builder()
    }
}

@Composable
fun NavHostController.SetupNavHost(
    rootView: @Composable (NavBackStackEntry) -> Unit,
    builder: NavGraphBuilder.() -> Unit
) {
    SetupNavHost(startDestination = ROOT_VIEW) {
        composable(ROOT_VIEW, content = rootView)
        builder()
    }
}

@Composable
fun NavHostController.SetupNavHost(
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = this,
        startDestination = startDestination,
        builder = builder
    )
}
