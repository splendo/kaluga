package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlin.reflect.KClass

/** Reserved route name for the back button navigation. */
const val BACK_ROUTE = "com.splendo.kaluga.architecture.compose.navigation.back_route"
private const val EMPTY_ROUTE = "com.splendo.kaluga.architecture.compose.navigation.empty_route"

/** @return a route represented by the [NavigationAction]. */
inline fun <reified T : NavigationAction<*>> route(vararg arguments: String): String =
    route(T::class, *arguments)

/** @return a route represented by this [NavigationAction]. */
fun NavigationAction<*>.route(vararg arguments: String): String = route(this::class, *arguments)

/** @return a route represented by [navigationActionClass] and [arguments]. */
fun route(navigationActionClass: KClass<out NavigationAction<*>>, vararg arguments: String): String =
    arguments.takeIf(Array<*>::isNotEmpty)
        ?.joinToString(separator = "/", prefix = "${navigationActionClass.simpleName!!}/")
        ?: navigationActionClass.simpleName!!

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

    fun back(): Boolean = navController.popBackStack() || parentNavigator?.back() ?: false

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
