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
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.resources.localized

sealed class FeatureListNavigationAction : NavigationAction<Nothing>(null) {

    object Alerts : FeatureListNavigationAction()
    object Architecture : FeatureListNavigationAction()
    object Bluetooth : FeatureListNavigationAction()
    object Beacons : FeatureListNavigationAction()
    object Keyboard : FeatureListNavigationAction()
    object LoadingIndicator : FeatureListNavigationAction()
    object Location : FeatureListNavigationAction()
    object Permissions : FeatureListNavigationAction()
}

sealed class Feature(val title: String) {
    object Alerts : Feature("feature_alerts".localized())
    object Architecture : Feature("feature_architecture".localized())
    object Bluetooth : Feature("feature_bluetooth".localized())
    object Beacons: Feature("feature_beacons".localized())
    object Keyboard : Feature("feature_keyboard".localized())
    object LoadingIndicator : Feature("feature_hud".localized())
    object Location : Feature("feature_location".localized())
    object Permissions : Feature("feature_permissions".localized())
}

class FeatureListViewModel(navigator: Navigator<FeatureListNavigationAction>) : NavigatingViewModel<FeatureListNavigationAction>(navigator) {

    val feature: Observable<List<Feature>> = observableOf(listOf(
        Feature.Alerts,
        Feature.Architecture,
        Feature.Bluetooth,
        Feature.Beacons,
        Feature.Keyboard,
        Feature.LoadingIndicator,
        Feature.Location,
        Feature.Permissions
    ))

    fun onFeaturePressed(feature: Feature) {
        navigator.navigate(when (feature) {
            is Feature.Alerts -> FeatureListNavigationAction.Alerts
            is Feature.Architecture -> FeatureListNavigationAction.Architecture
            is Feature.Bluetooth -> FeatureListNavigationAction.Bluetooth
            is Feature.Beacons -> FeatureListNavigationAction.Beacons
            is Feature.Keyboard -> FeatureListNavigationAction.Keyboard
            is Feature.LoadingIndicator -> FeatureListNavigationAction.LoadingIndicator
            is Feature.Location -> FeatureListNavigationAction.Location
            is Feature.Permissions -> FeatureListNavigationAction.Permissions
        })
    }
}
