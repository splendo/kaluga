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
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class FeatureListNavigationAction : NavigationAction<Nothing>(null) {
    object Location : FeatureListNavigationAction()
    object Permissions : FeatureListNavigationAction()
    object Alerts : FeatureListNavigationAction()
    object DateTimePicker : FeatureListNavigationAction()
    object LoadingIndicator : FeatureListNavigationAction()
    object Architecture : FeatureListNavigationAction()
    object Bluetooth : FeatureListNavigationAction()
    object Keyboard : FeatureListNavigationAction()
    object Links : FeatureListNavigationAction()
    object System : FeatureListNavigationAction()
    object ServiceMonitor : FeatureListNavigationAction()
    object Beacons : FeatureListNavigationAction()
    object Resources : FeatureListNavigationAction()
    object PlatformSpecific : FeatureListNavigationAction()
}

sealed class Feature(val title: String) {
    object Alerts : Feature("feature_alerts".localized())
    object Architecture : Feature("feature_architecture".localized())
    object Bluetooth : Feature("feature_bluetooth".localized())
    object DateTimePicker : Feature("feature_date_time_picker".localized())
    object Keyboard : Feature("feature_keyboard".localized())
    object LoadingIndicator : Feature("feature_hud".localized())
    object Location : Feature("feature_location".localized())
    object Permissions : Feature("feature_permissions".localized())
    object Links : Feature("feature_links".localized())
    object System : Feature("feature_system".localized())
    object ServiceMonitor : Feature("feature_service_monitor".localized())
    object Beacons : Feature("feature_beacons".localized())
    object Resource : Feature("feature_resources".localized())
    object PlatformSpecific : Feature("feature_platform_specific".localized())
}

class FeatureListViewModel(navigator: Navigator<FeatureListNavigationAction>) : NavigatingViewModel<FeatureListNavigationAction>(navigator) {

    val feature = observableOf(
        listOfNotNull(
            Feature.Alerts,
            Feature.Architecture,
            Feature.Bluetooth,
            Feature.DateTimePicker,
            Feature.Keyboard,
            Feature.Links,
            Feature.LoadingIndicator,
            Feature.Location,
            Feature.Permissions,
            Feature.ServiceMonitor,
            Feature.System,
            Feature.Beacons,
            Feature.Resource,
            Feature.PlatformSpecific.takeIf { showPlatformSpecificFeatures }
        )
    )

    fun onFeaturePressed(feature: Feature) {
        navigator.navigate(
            when (feature) {
                is Feature.Alerts -> FeatureListNavigationAction.Alerts
                is Feature.Architecture -> FeatureListNavigationAction.Architecture
                is Feature.Beacons -> FeatureListNavigationAction.Beacons
                is Feature.Bluetooth -> FeatureListNavigationAction.Bluetooth
                is Feature.DateTimePicker -> FeatureListNavigationAction.DateTimePicker
                is Feature.Keyboard -> FeatureListNavigationAction.Keyboard
                is Feature.Links -> FeatureListNavigationAction.Links
                is Feature.LoadingIndicator -> FeatureListNavigationAction.LoadingIndicator
                is Feature.Location -> FeatureListNavigationAction.Location
                is Feature.Permissions -> FeatureListNavigationAction.Permissions
                is Feature.ServiceMonitor -> FeatureListNavigationAction.ServiceMonitor
                is Feature.System -> FeatureListNavigationAction.System
                is Feature.Beacons -> FeatureListNavigationAction.Beacons
                is Feature.Resource -> FeatureListNavigationAction.Resources
                is Feature.PlatformSpecific -> FeatureListNavigationAction.PlatformSpecific
            }
        )
    }
}
