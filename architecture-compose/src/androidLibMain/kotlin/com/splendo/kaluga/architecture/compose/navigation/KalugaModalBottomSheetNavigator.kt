package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

operator fun ((NavigationAction<*>) -> String?).plus(otherMapper: (NavigationAction<*>) -> String?): (NavigationAction<*>) -> String? {
    return CombinedNavigationMapper(this, otherMapper)::map
}

private class CombinedNavigationMapper(
    private val firstMapper: (NavigationAction<*>) -> String?,
    private val secondMapper: (NavigationAction<*>) -> String?
) {
    fun map(action: NavigationAction<*>): String? = firstMapper(action) ?: secondMapper(action)
}

/** A [composable] that also expands bottom sheet upon re-composition. */
@ExperimentalMaterialApi
fun NavGraphBuilder.bottomSheetComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    modalBottomSheetState: ModalBottomSheetState,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = emptyList()
    ) {
        linkHwBackButtonNavigation {
            modalBottomSheetState.hide()
        }

        content(it)

        LaunchedEffect(Unit) {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    }
}

object CloseBottomSheetNavigationAction : NavigationAction<Nothing>(null)

/** Navigator for [ModalBottomSheetLayout]. */
@ExperimentalMaterialApi
class KalugaModalBottomSheetNavigator(
    private val coroutineScope: CoroutineScope,
    private val modalBottomSheetState: ModalBottomSheetState,
    navController: NavHostController,
    navigationMapper: (NavigationAction<*>) -> String?
) : KalugaNavigatorComposeAdapter<NavigationAction<*>>(navController, navigationMapper) {

    override fun navigate(action: NavigationAction<*>) {
        if (action is CloseBottomSheetNavigationAction) {
            action.route().takeIf(::isNewRoute)?.let(::navigate)
        } else {
            super.navigate(action)
        }
    }

    fun hide() {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }

    @Composable
    override fun SetupNavHost(builder: NavGraphBuilder.() -> Unit) {
        val hiddenRoute = CloseBottomSheetNavigationAction.route()
        SetupNavHost(
            startDestination = hiddenRoute
        ) {
            composable(hiddenRoute) {
                LaunchedEffect(Unit) {
                    modalBottomSheetState.hide()
                }
            }
            builder()
        }

        if (!modalBottomSheetState.isVisible) {
            syncHiddenState()
        }
    }

    private fun syncHiddenState() {
        navigate(CloseBottomSheetNavigationAction)
    }

}
