/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.featureList

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel

sealed class FeatureListNavigationAction : NavigationAction<Nothing>(null) {

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