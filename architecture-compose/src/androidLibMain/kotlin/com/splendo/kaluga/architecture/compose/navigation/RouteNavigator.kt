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

/**
 * Controller for navigating to a [Route]
 */
sealed interface RouteController {
    /**
     * Navigates to a [Route]
     * @param newRoute The [Route] to navigate to
     */
    fun navigate(newRoute: Route)

    /**
     * Navigates back
     */
    fun back(result: Map<String, Any?> = emptyMap()): Boolean

    /**
     * Closes this RouteController
     */
    fun close()
}

internal val RouteController.navHostController get() = when (this) {
    is NavHostRouteController -> navHostController
    is BottomSheetSheetContentRouteController -> navHostController
}

/**
 * A [RouteController] managed by a [NavHostController]
 * @param navHostController The [NavHostController] managing the route stack
 * @param parentRouteController The [NavHostController] that is a parent to the [navHostController] if it exists
 */
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
                navHostController.getBackStackEntry(newRoute.route).savedStateHandle.let { savedStateHandle ->
                    newRoute.result.entries.forEach { (key, value) -> savedStateHandle[key] = value }
                }
                navHostController.popBackStack(newRoute.route, false)
            }
            is Route.PopToIncluding<*, *> -> {
                navHostController.popBackStack(newRoute.route, true)
            }
            is Route.Back -> back(newRoute.result)
            is Route.PopToRoot -> {
                navHostController.getBackStackEntry(ROOT_VIEW).savedStateHandle.let { savedStateHandle ->
                    newRoute.result.entries.forEach { (key, value) -> savedStateHandle[key] = value }
                }
                navHostController.popBackStack(ROOT_VIEW, false)
            }
            is Route.Close -> close()
            is Route.Launcher<*> -> newRoute.launch()
        }
    }

    override fun back(result: Map<String, Any?>): Boolean = if (navHostController.backQueue.isNotEmpty()){
        navHostController.previousBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
            result.entries.forEach { (key, value) -> savedStateHandle[key] = value }
        }
        navHostController.popBackStack()
    }
    else { parentRouteController?.back(result) ?: false }

    override fun close() {
        navHostController.popBackStack(ROOT_VIEW, true)
        parentRouteController?.close()
    }
}

typealias RouteRootContentBuilder = (NavHostController) -> Unit
typealias RouteContentBuilder = NavGraphBuilder.(NavHostController) -> Unit

/**
 * A Navigator managed by a [RouteController]
 * @param routeController The [RouteController] managing this navigation
 * @param navigationMapper A mapper that converts an [NavigationAction] handled by this navigator into a [Route]
 */
open class RouteNavigator<A : NavigationAction<*>>(
    internal val routeController: RouteController,
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

/**
 * Creates a a [NavHost] for a given [RouteNavigator]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun <A : NavigationAction<*>> RouteNavigator<A>.SetupNavHost(builder: RouteContentBuilder) = routeController.SetupNavHost(builder)

/**
 * Creates a a [NavHost] for a given [RouteController]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun RouteController.SetupNavHost(builder: RouteContentBuilder) {
    SetupNavHost(startDestination = ROOT_VIEW) {
        composable(ROOT_VIEW) {
            Spacer(modifier = Modifier.fillMaxWidth())
        }
        builder(navHostController)
    }
}

/**
 * Creates a a [NavHost] for a given [RouteNavigator]
 * @param rootView The [RouteRootContentBuilder] for the initial view of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun <A : NavigationAction<*>> RouteNavigator<A>.SetupNavHost(
    rootView: @Composable RouteRootContentBuilder,
    builder: RouteContentBuilder
) = routeController.SetupNavHost(rootView, builder)

/**
 * Creates a a [NavHost] for a given [RouteController]
 * @param rootView The [RouteRootContentBuilder] for the initial view of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun RouteController.SetupNavHost(
    rootView: @Composable RouteRootContentBuilder,
    builder: RouteContentBuilder
) {
    SetupNavHost(startDestination = ROOT_VIEW) {
        composable(ROOT_VIEW, content = { rootView(navHostController) })
        builder(navHostController)
    }
}

/**
 * Creates a a [NavHost] for a given [RouteNavigator]
 * @param startDestination The start destination of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun <A : NavigationAction<*>> RouteNavigator<A>.SetupNavHost(
    startDestination: String,
    builder: NavGraphBuilder.(RouteController) -> Unit
) = routeController.SetupNavHost(startDestination, builder)

/**
 * Creates a a [NavHost] for a given [RouteController]
 * @param startDestination The start destination of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
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
