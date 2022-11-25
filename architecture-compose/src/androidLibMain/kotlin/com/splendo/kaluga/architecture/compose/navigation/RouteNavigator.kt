package com.splendo.kaluga.architecture.compose.navigation

import android.os.Bundle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer

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

sealed class NavHostProvidingRouteController<Provider>(
    private val provider: StateFlow<Provider?>,
    private val parentRouteController: RouteController? = null
) : RouteController {

    abstract fun Provider.provide(): NavHostController

    override fun navigate(newRoute: Route) {
        val navHostController = provider.value?.provide() ?: return
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
        val navHostController = provider.value?.provide()
        return if (navHostController != null && navHostController.backQueue.isNotEmpty()) {
            navHostController.previousBackStackEntry?.setResult(result)
            navHostController.popBackStack()
        } else {
            parentRouteController?.back(result) ?: false
        }
    }

    override fun close() {
        provider.value?.provide()?.popBackStack(ROOT_VIEW, true)
        parentRouteController?.close()
    }
}

class NavHostRouteController(
    provider: StateFlow<NavHostController?>,
    parentRouteController: RouteController? = null
) : NavHostProvidingRouteController<NavHostController>(provider, parentRouteController) {
    override fun NavHostController.provide(): NavHostController = this
}

typealias RouteContentBuilder = NavGraphBuilder.(StateFlow<NavHostController?>) -> Unit

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
    rootResultHandlers: List<NavHostRootResultHandler<*, *>> = emptyList(),
    builder: RouteContentBuilder
) : RouteNavigator<A>(navigationMapper) {

    private val navHostController = MutableStateFlow<NavHostController?>(null)
    override val routeController: RouteController = NavHostRouteController(navHostController, parentRouteController)

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        val navController = rememberNavController()
        navHostController.tryEmit(navController)
        SetupNavHost(
            navHostController = navHostController,
            rootView = {
                rootResultHandlers.forEach { resultHandler ->
                    resultHandler.HandleResult(viewModel = this, navHostController = navController)
                }
                content()
                       },
            builder = builder
        )
    }
}

class NavHostProvidingContentRouteNavigator<A : NavigationAction<*>, Provider>(
    override val routeController: NavHostProvidingRouteController<Provider>,
    navigationMapper: (A) -> Route
) : RouteNavigator<A>(navigationMapper) {

    override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = @Composable { content ->
        content()
    }
}

fun <A : NavigationAction<*>> NavHostContentRouteNavigator(
    navHostController: StateFlow<NavHostController?>,
    parentRouteController: RouteController? = null,
    navigationMapper: (A) -> Route
): NavHostProvidingContentRouteNavigator<A, NavHostController> = NavHostProvidingContentRouteNavigator(
    NavHostRouteController(navHostController, parentRouteController),
    navigationMapper
)

@Composable
fun SetupNavHost(
    navHostController: StateFlow<NavHostController?>,
    builder: RouteContentBuilder
) = SetupNavHost(
    navHostController = navHostController,
    rootView = { Spacer(modifier = Modifier.fillMaxWidth()) },
    builder
)

@Composable
fun SetupNavHost(
    navHostController: StateFlow<NavHostController?>,
    rootView: @Composable () -> Unit,
    builder: RouteContentBuilder
) {
    val currentNavHostController by navHostController.collectAsState()
    currentNavHostController?.let {
        SetupNavHost(
            navHostController = it,
            startDestination = ROOT_VIEW
        ) {
            composable(ROOT_VIEW, content = { rootView() })
            builder(navHostController)
        }
    }
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

sealed class NavHostRootResultHandler<ViewModel: BaseLifecycleViewModel, R> {
    abstract val viewModelClass: Class<ViewModel>
    abstract val retain: Boolean
    abstract val onResult: ViewModel.(R) -> Unit
    @Composable
    internal abstract fun NavHostController.HandleResult(callback: (R) -> Unit)

    data class Bundle<ViewModel: BaseLifecycleViewModel, R : NavigationBundleSpecRow<*>>(
        override val viewModelClass: Class<ViewModel>,
        val spec: NavigationBundleSpec<R>,
        override val retain: Boolean = false,
        override val onResult: ViewModel.(NavigationBundle<R>) -> Unit
    ) : NavHostRootResultHandler<ViewModel, NavigationBundle<R>>() {
        @Composable
        override fun NavHostController.HandleResult(callback: (NavigationBundle<R>) -> Unit) = HandleResult(spec, retain) { callback(this) }
    }

    data class Type<ViewModel: BaseLifecycleViewModel, R>(
        override val viewModelClass: Class<ViewModel>,
        val spec: NavigationBundleSpecType<R>,
        override val retain: Boolean,
        override val onResult: ViewModel.(R) -> Unit
    ) : NavHostRootResultHandler<ViewModel, R>() {

        @Composable
        override fun NavHostController.HandleResult(callback: (R) -> Unit) = HandleResult(spec, retain) { callback(this) }
    }

    @Composable
    internal fun HandleResult(viewModel: BaseLifecycleViewModel, navHostController: NavHostController) {
        viewModelClass.cast(viewModel)?.let { vm ->
            navHostController.HandleResult { vm.onResult(it) }
        }
    }
}

inline fun <reified ViewModel : BaseLifecycleViewModel, R : NavigationBundleSpecRow<*>> NavigationBundleSpec<R>.NavHostRootResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(NavigationBundle<R>) -> Unit
) = NavHostRootResultHandler.Bundle(ViewModel::class.java, this, retain, onResult)

inline fun <reified ViewModel : BaseLifecycleViewModel, R> NavigationBundleSpecType<R>.NavHostRootResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(R) -> Unit
) = NavHostRootResultHandler.Type(ViewModel::class.java, this, retain, onResult)

inline fun <reified ViewModel : BaseLifecycleViewModel, R> KSerializer<R>.NavHostRootResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(R) -> Unit
) = NavHostRootResultHandler.Type(ViewModel::class.java, NavigationBundleSpecType.SerializedType(this), retain, onResult)

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
    onResult: NavigationBundle<R>.() -> Unit
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
    onResult: R.() -> Unit
) = HandleResult(retain) { toTypedProperty(type).onResult() }

@Composable
internal fun NavHostController.HandleResult(retain: Boolean = false, onResult: Bundle.() -> Unit) {
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
