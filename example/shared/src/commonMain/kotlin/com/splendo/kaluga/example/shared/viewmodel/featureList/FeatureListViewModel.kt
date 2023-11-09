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

package com.splendo.kaluga.example.shared.viewmodel.featureList

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class FeatureListNavigationAction : NavigationAction<Nothing>(null) {
    data object Location : FeatureListNavigationAction()
    data object Permissions : FeatureListNavigationAction()
    data object Alerts : FeatureListNavigationAction()
    data object DateTime : FeatureListNavigationAction()
    data object DateTimePicker : FeatureListNavigationAction()
    data object LoadingIndicator : FeatureListNavigationAction()
    data object Architecture : FeatureListNavigationAction()
    data object Bluetooth : FeatureListNavigationAction()
    data object Keyboard : FeatureListNavigationAction()
    data object Links : FeatureListNavigationAction()
    data object Media : FeatureListNavigationAction()
    data object System : FeatureListNavigationAction()
    data object Beacons : FeatureListNavigationAction()
    data object Resources : FeatureListNavigationAction()
    data object Scientific : FeatureListNavigationAction()
    data object PlatformSpecific : FeatureListNavigationAction()
}

sealed class Feature(val title: String) {
    data object Alerts : Feature("feature_alerts".localized())
    data object Architecture : Feature("feature_architecture".localized())
    data object Bluetooth : Feature("feature_bluetooth".localized())
    data object DateTime : Feature("feature_date_time".localized())
    data object DateTimePicker : Feature("feature_date_time_picker".localized())
    data object Keyboard : Feature("feature_keyboard".localized())
    data object LoadingIndicator : Feature("feature_hud".localized())
    data object Location : Feature("feature_location".localized())
    data object Media : Feature("feature_media".localized())
    data object Permissions : Feature("feature_permissions".localized())
    data object Links : Feature("feature_links".localized())
    data object System : Feature("feature_system".localized())
    data object Beacons : Feature("feature_beacons".localized())
    data object Resource : Feature("feature_resources".localized())
    data object Scientific : Feature("feature_scientific".localized())
    data object PlatformSpecific : Feature("feature_platform_specific".localized())
}

class FeatureListViewModel(navigator: Navigator<FeatureListNavigationAction>) : NavigatingViewModel<FeatureListNavigationAction>(navigator) {

    val feature = observableOf(
        listOfNotNull(
            Feature.Alerts,
            Feature.Architecture,
            Feature.Bluetooth,
            Feature.DateTime,
            Feature.DateTimePicker,
            Feature.Keyboard,
            Feature.Links,
            Feature.LoadingIndicator,
            Feature.Location,
            Feature.Media,
            Feature.Permissions,
            Feature.System,
            Feature.Beacons,
            Feature.Resource,
            Feature.Scientific,
            Feature.PlatformSpecific.takeIf { showPlatformSpecificFeatures },
        ),
    )

    fun onFeaturePressed(feature: Feature) {
        navigator.navigate(
            when (feature) {
                is Feature.Alerts -> FeatureListNavigationAction.Alerts
                is Feature.Architecture -> FeatureListNavigationAction.Architecture
                is Feature.Bluetooth -> FeatureListNavigationAction.Bluetooth
                is Feature.DateTime -> FeatureListNavigationAction.DateTime
                is Feature.DateTimePicker -> FeatureListNavigationAction.DateTimePicker
                is Feature.Keyboard -> FeatureListNavigationAction.Keyboard
                is Feature.Links -> FeatureListNavigationAction.Links
                is Feature.LoadingIndicator -> FeatureListNavigationAction.LoadingIndicator
                is Feature.Location -> FeatureListNavigationAction.Location
                is Feature.Media -> FeatureListNavigationAction.Media
                is Feature.Permissions -> FeatureListNavigationAction.Permissions
                is Feature.System -> FeatureListNavigationAction.System
                is Feature.Beacons -> FeatureListNavigationAction.Beacons
                is Feature.Resource -> FeatureListNavigationAction.Resources
                is Feature.Scientific -> FeatureListNavigationAction.Scientific
                is Feature.PlatformSpecific -> FeatureListNavigationAction.PlatformSpecific
            },
        )
    }
}
