package com.splendo.kaluga.architecture.compose.navigation

import android.os.Bundle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationSpec
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.navigation.toNavigationBundle
import com.splendo.kaluga.architecture.navigation.toTypedProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    fun back(result: Route.Result): Boolean

    /**
     * Closes this RouteController
     */
    fun close()
}

/**
 * A [RouteController] managed by a [NavHostController]
 * @param navHostController A [StateFlow] indicating the current [NavHostController] managing the route stack
 * @param parentRouteController The [NavHostController] that is a parent to the [navHostController] if it exists
 */
class NavHostRouteController(
    internal val navHostController: StateFlow<NavHostController?>,
    private val parentRouteController: RouteController? = null
) : RouteController {

    override fun navigate(newRoute: Route) {
        val navHostController = navHostController.value ?: return
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
                navHostController.getBackStackEntry(newRoute.route).setResult(newRoute.result)
                navHostController.popBackStack(newRoute.route, false)
            }
            is Route.PopToIncluding<*, *> -> {
                navHostController.popBackStack(newRoute.route, true)
            }
            is Route.Back -> back(newRoute.result)
            is Route.PopToRoot -> {
                navHostController.getBackStackEntry(ROOT_VIEW).setResult(newRoute.result)
                navHostController.popBackStack(ROOT_VIEW, false)
            }
            is Route.Close -> close()
            is Route.Launcher<*> -> newRoute.launch()
        }
    }

    override fun back(result: Route.Result): Boolean {
        val navHostController = navHostController.value
        return if (navHostController != null && navHostController.backQueue.isNotEmpty()) {
            navHostController.previousBackStackEntry?.setResult(result)
            navHostController.popBackStack()
        } else {
            parentRouteController?.back(result) ?: false
        }
    }

    override fun close() {
        navHostController.value?.popBackStack(ROOT_VIEW, true)
        parentRouteController?.close()
    }
}

typealias RouteRootContentBuilder = (NavHostController) -> Unit
typealias RouteContentBuilder = NavGraphBuilder.(NavHostController) -> Unit

/**
 * A Navigator managed by a [RouteController]
 * @param navigationMapper A mapper that converts an [NavigationAction] handled by this navigator into a [Route]
 */
sealed class RouteNavigator<A : NavigationAction<*>>(
    private val navigationMapper: (A) -> Route
) : Navigator<A>, ComposableLifecycleSubscribable {

    abstract val routeController: RouteController

    override fun navigate(action: A) {
        routeController.navigate(navigationMapper(action))
    }
}

class NavHostRouteNavigator<A : NavigationAction<*>>(
    navigationMapper: (A) -> Route,
    parentRouteController: RouteController? = null,
    builder: RouteContentBuilder
) : RouteNavigator<A>(navigationMapper) {

    private val navHostController = MutableStateFlow<NavHostController?>(null)
    override val routeController: RouteController = NavHostRouteController(navHostController, parentRouteController)

    override val modifier: @Composable (@Composable () -> Unit) -> Unit = @Composable { content ->
        val navController = rememberNavController()
        navHostController.tryEmit(navController)
        SetupNavHost(
            navHostController = navController,
            rootView = content,
            builder = builder
        )
    }
}

class NavHostContentRouteNavigator<A : NavigationAction<*>>(
    navHostController: NavHostController,
    parentRouteController: RouteController? = null,
    navigationMapper: (A) -> Route
) : RouteNavigator<A>(navigationMapper) {

    override val routeController: RouteController = NavHostRouteController(MutableStateFlow(navHostController), parentRouteController)

    override val modifier: @Composable (@Composable () -> Unit) -> Unit = @Composable { content ->
        content()
    }
}

@Composable
fun SetupNavHost(
    navHostController: NavHostController,
    builder: RouteContentBuilder
) = SetupNavHost(
    navHostController = navHostController,
    rootView = { Spacer(modifier = Modifier.fillMaxWidth()) },
    builder
)

@Composable
fun SetupNavHost(
    navHostController: NavHostController,
    rootView: @Composable () -> Unit,
    builder: RouteContentBuilder
) = SetupNavHost(
    navHostController = navHostController,
    startDestination = ROOT_VIEW
) {
    composable(ROOT_VIEW, content = { rootView() })
    builder(navHostController)
}

/**
 * Creates a a [NavHost] for a given [RouteController]
 * @param startDestination The start destination of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun SetupNavHost(
    navHostController: NavHostController,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        builder = { builder() }
    )
}


/**
 * Handles a [Route.Result] matching a given [NavigationBundleSpec]
 * @param spec The [NavigationBundleSpec] used to create the [Route.Result]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 */
@Composable
fun <R : NavigationBundleSpecRow<*>> NavHostController.HandleResult(
    spec: NavigationBundleSpec<R>,
    retain: Boolean = false,
    onResult: @Composable NavigationBundle<R>.() -> Unit
) = HandleResult(retain) { toNavigationBundle(spec).onResult() }

/**
 * Handles a [Route.Result] matching a given [NavigationBundleSpecType]
 * Requires that the [Route.Result] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @param type The [NavigationBundleSpecType] stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 */
@Composable
fun <R> NavHostController.HandleResult(
    type: NavigationBundleSpecType<R>,
    retain: Boolean = false,
    onResult: @Composable R.() -> Unit
) = HandleResult(retain) { toTypedProperty(type).onResult() }

@Composable
internal fun NavHostController.HandleResult(retain: Boolean = false, onResult: @Composable Bundle.() -> Unit) {
    val result = currentBackStackEntry?.savedStateHandle?.getStateFlow<Bundle?>(Route.Result.KEY, null)?.collectAsState()
    result?.value?.let {
        onResult(it)
        if (!retain) {
            currentBackStackEntry?.savedStateHandle?.remove<Bundle>(Route.Result.KEY)
        }
    }
}

internal fun NavBackStackEntry.setResult(result: Route.Result) = when (result) {
    is Route.Result.Empty -> savedStateHandle.remove(Route.Result.KEY)
    is Route.Result.Data<*, *> -> savedStateHandle[Route.Result.KEY] = result.bundle.toBundle()
}
