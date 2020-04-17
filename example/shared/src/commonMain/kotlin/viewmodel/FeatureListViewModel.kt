package com.splendo.kaluga.example.shared.viewmodel

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
}

sealed class Feature {
    abstract val title: String

    object Location : Feature() {
        override val title: String = "Location"
    }
    object Permissions : Feature() {
        override val title: String = "Permissions"
    }
    object Alerts : Feature() {
        override val title: String = "Alerts"
    }
    object LoadingIndicator : Feature() {
        override val title: String = "Loading Indicator"
    }

}

class FeatureListViewModel(navigator: Navigator<FeatureListNavigationAction>) : NavigatingViewModel<FeatureListNavigationAction>(navigator) {

    val feature = observableOf(listOf(Feature.Location, Feature.Permissions, Feature.Alerts, Feature.LoadingIndicator))

    fun onFeaturePressed(feature: Feature) {
        navigator.navigate(when(feature) {
            is Feature.Location -> FeatureListNavigationAction.Location
            is Feature.Permissions -> FeatureListNavigationAction.Permissions
            is Feature.Alerts -> FeatureListNavigationAction.Alerts
            is Feature.LoadingIndicator -> FeatureListNavigationAction.LoadingIndicator
        })
    }

}