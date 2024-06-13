/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.result.NavHostResultHandler
import com.splendo.kaluga.architecture.compose.navigation.result.setResult
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Controller for navigating to a [Route]
 */
sealed interface RouteController {

    /**
     * Adds a list of [NavHostResultHandler] to handles results for this route controller.
     */
    @Composable
    fun AddResultHandlers(viewModel: BaseLifecycleViewModel, resultHandlers: List<NavHostResultHandler<*, *>>)

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
 * Abstract [RouteController] that navigates on a [NavHostController] provided by a [Provider]
 * @param Provider a type of class that can provide a [NavHostController]
 * @param provider A [StateFlow] of [Provider].
 * @param parentRouteController An optional parent [RouteController] managing this controller.
 */
sealed class ProvidingNavHostRouteController<Provider>(
    private val provider: StateFlow<Provider?>,
    private val parentRouteController: RouteController? = null,
) : RouteController {

    /**
     * Converts [Provider] into a [NavHostController]
     */
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
        }
    }

    override fun back(result: Route.Result): Boolean {
        val navHostController = provider.value?.provide()
        return if (navHostController?.currentBackStackEntry != null) {
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

    @Composable
    override fun AddResultHandlers(viewModel: BaseLifecycleViewModel, resultHandlers: List<NavHostResultHandler<*, *>>) {
        if (resultHandlers.isNotEmpty()) {
            val currentProvider by provider.collectAsState()
            currentProvider?.provide()?.let { currentNavHostController ->
                resultHandlers.forEach {
                    it.HandleResult(viewModel = viewModel, navHostController = currentNavHostController)
                }
            }
        }
    }
}

/**
 * Simple [ProvidingNavHostRouteController] where the provider is a flow of [NavHostController]
 * @param provider The flow of [NavHostController] to be managed by this Route Controller
 * @param parentRouteController An optional parent [RouteController] managing this controller.
 */
class NavHostRouteController(
    provider: StateFlow<NavHostController?>,
    parentRouteController: RouteController? = null,
) : ProvidingNavHostRouteController<NavHostController>(provider, parentRouteController) {
    override fun NavHostController.provide(): NavHostController = this
}

/**
 * [ProvidingNavHostRouteController] where the [NavHostController] is provided by the content of a [BottomSheetNavigatorState]
 * @param provider The flow of [BottomSheetNavigatorState] where the [BottomSheetNavigatorState.contentNavHostController] is to be managed by this Route Controller
 * @param parentRouteController An optional parent [RouteController] managing this controller.
 */
class BottomSheetContentRouteController(
    provider: StateFlow<BottomSheetNavigatorState?>,
    parentRouteController: RouteController? = null,
) : ProvidingNavHostRouteController<BottomSheetNavigatorState>(provider, parentRouteController) {
    override fun BottomSheetNavigatorState.provide(): NavHostController = contentNavHostController
}

/**
 * [RouteController] for the sheet content of a [BottomSheetNavigatorState]
 * @param bottomSheetNavigatorState The flow of [BottomSheetNavigatorState] where the [BottomSheetNavigatorState.sheetContentNavHostController] is to be managed by this Route Controller
 * @param coroutineScope A flow of [CoroutineScope] for managing the [BottomSheetScaffold].
 */
class BottomSheetSheetContentRouteController(
    private val bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    private val coroutineScope: StateFlow<CoroutineScope?>,
) : RouteController {

    inner class NavHostRouteController : ProvidingNavHostRouteController<BottomSheetNavigatorState>(bottomSheetNavigatorState) {
        override fun BottomSheetNavigatorState.provide(): NavHostController = sheetContentNavHostController
    }

    internal val sheetContentRouteController = NavHostRouteController()

    override fun navigate(newRoute: Route) {
        bottomSheetNavigatorState.value?.let { (_, _, scaffoldState) ->
            if (!scaffoldState.bottomSheetState.hasExpandedState) {
                coroutineScope.value?.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            }
        }

        // We should call our own back/close mechanisms since the ROOT_VIEW needs to be retained.
        when (newRoute) {
            is Route.Close -> close()
            is Route.Back -> back(newRoute.result)
            is Route.PopToRoot -> close()
            else -> sheetContentRouteController.navigate(newRoute)
        }
    }

    override fun back(result: Route.Result): Boolean {
        val navHostController = bottomSheetNavigatorState.value?.sheetContentNavHostController
        val rootId = NavDestination("").apply {
            route = ROOT_VIEW
        }.id
        return if (navHostController?.currentBackStackEntry != null && navHostController.previousBackStackEntry?.destination?.id != rootId) {
            navHostController.previousBackStackEntry?.setResult(result)
            navHostController.popBackStack()
        } else {
            close()
            true
        }
    }

    override fun close() {
        bottomSheetNavigatorState.value?.let { (_, sheetContentNavHostController, scaffoldState) ->
            // Make sure we're back at the ROOT_VIEW before hiding the sheet
            sheetContentNavHostController.popBackStack(ROOT_VIEW, false)
            coroutineScope.value?.launch {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    @Composable
    override fun AddResultHandlers(viewModel: BaseLifecycleViewModel, resultHandlers: List<NavHostResultHandler<*, *>>) {
        sheetContentRouteController.AddResultHandlers(viewModel = viewModel, resultHandlers = resultHandlers)
    }
}
