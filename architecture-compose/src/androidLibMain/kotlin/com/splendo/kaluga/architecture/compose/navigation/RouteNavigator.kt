package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlin.reflect.KClass

const val BACK_ROUTE = "back"
private const val EMPTY_ROUTE = "empty"

/** @return a route represented by the [NavigationAction]. */
inline fun <reified T : NavigationAction<*>> route(vararg arguments: String): String =
    route(T::class, *arguments)

/** @return a route represented by this [NavigationAction]. */
fun NavigationAction<*>.route(vararg arguments: String): String = route(this::class, *arguments)

/** @return a route represented by [navigationActionClass] and [arguments]. */
fun route(navigationActionClass: KClass<out NavigationAction<*>>, vararg arguments: String): String {
    var route = navigationActionClass.simpleName!!
    for (argument in arguments) {
        route += "/$argument"
    }

    return route
}

/**
 * Routes [Navigator] calls to [NavHostController].
 */
open class RouteNavigator<A : NavigationAction<*>>(
    protected val navController: NavHostController,
    private val navigationMapper: (A) -> String,
    private val parentNavigator: RouteNavigator<*>? = null
) : Navigator<A> {

    override fun navigate(action: A) {
        val newRoute = navigationMapper(action)
        if (isNewRoute(newRoute)) {
            if (newRoute == BACK_ROUTE) {
                back()
            } else {
                navigate(newRoute)
            }
        }
    }

    fun back() {
        parentNavigator?.back() ?: navigate(EMPTY_ROUTE)
    }

    @Composable
    open fun SetupNavHost(builder: NavGraphBuilder.() -> Unit) {
        SetupNavHost(startDestination = EMPTY_ROUTE) {
            composable(EMPTY_ROUTE) {}
            builder()
        }
    }

    @Composable
    fun SetupNavHost(
        startDestination: String,
        builder: NavGraphBuilder.() -> Unit
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            builder = builder
        )
    }

    protected fun isNewRoute(route: String): Boolean {
        val backStackEntry = navController.currentBackStackEntry ?: return false
        return backStackEntry.destination.route != route
    }

    protected fun navigate(route: String) {
        navController.popBackStack()
        navController.navigate(route)
    }
}
