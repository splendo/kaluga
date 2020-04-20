package com.splendo.kaluga.example.shared.viewmodel.featureList

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel

sealed class FeatureListNavigationAction : NavigationAction<Nothing>() {

    override val bundle: NavigationBundle<Nothing>? = null

    object Location : FeatureListNavigationAction()
    object Permissions : FeatureListNavigationAction()
    object Alerts : FeatureListNavigationAction()
    object LoadingIndicator : FeatureListNavigationAction()
    object Architecture:  FeatureListNavigationAction()
}

sealed class Feature(val title: String) {
    object Location : Feature("Location")
    object Permissions : Feature("Permissions")
    object Alerts : Feature("Alerts")
    object LoadingIndicator : Feature("Loading Indicator")
    object Architecture : Feature("Architecture")
}

class FeatureListViewModel(navigator: Navigator<FeatureListNavigationAction>) : NavigatingViewModel<FeatureListNavigationAction>(navigator) {

    val feature = observableOf(listOf(
        Feature.Location,
        Feature.Permissions,
        Feature.Alerts,
        Feature.LoadingIndicator,
        Feature.Architecture
    ))

    fun onFeaturePressed(feature: Feature) {
        navigator.navigate(when(feature) {
            is Feature.Location -> FeatureListNavigationAction.Location
            is Feature.Permissions -> FeatureListNavigationAction.Permissions
            is Feature.Alerts -> FeatureListNavigationAction.Alerts
            is Feature.LoadingIndicator -> FeatureListNavigationAction.LoadingIndicator
            is Feature.Architecture -> FeatureListNavigationAction.Architecture
        })
    }

}