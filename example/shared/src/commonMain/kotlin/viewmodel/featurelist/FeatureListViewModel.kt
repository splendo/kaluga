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
    object Beacons : FeatureListNavigationAction()
    object Resources : FeatureListNavigationAction()
}

enum class Feature(private val titleKey: String) {
    ALERTS("feature_alerts"),
    ARCHITECTURE("feature_architecture"),
    BLUETOOTH("feature_bluetooth"),
    DATE_TIME_PICKER("feature_date_time_picker"),
    KEYBOARD("feature_keyboard"),
    LOADING_INDICATOR("feature_hud"),
    LOCATION("feature_location"),
    PERMISSIONS("feature_permissions"),
    LINKS("feature_links"),
    SYSTEM("feature_system"),
    BEACONS("feature_beacons"),
    RESOURCES("feature_resources");

    val title: String get() = titleKey.localized()
}

class FeatureListViewModel(navigator: Navigator<FeatureListNavigationAction>) : NavigatingViewModel<FeatureListNavigationAction>(navigator) {

    val feature = observableOf(Feature.values().toList())

    fun onFeaturePressed(feature: Feature) {
        navigator.navigate(
            when (feature) {
                Feature.ALERTS -> FeatureListNavigationAction.Alerts
                Feature.ARCHITECTURE -> FeatureListNavigationAction.Architecture
                Feature.BLUETOOTH -> FeatureListNavigationAction.Bluetooth
                Feature.DATE_TIME_PICKER -> FeatureListNavigationAction.DateTimePicker
                Feature.KEYBOARD -> FeatureListNavigationAction.Keyboard
                Feature.LINKS -> FeatureListNavigationAction.Links
                Feature.LOADING_INDICATOR -> FeatureListNavigationAction.LoadingIndicator
                Feature.LOCATION -> FeatureListNavigationAction.Location
                Feature.PERMISSIONS -> FeatureListNavigationAction.Permissions
                Feature.SYSTEM -> FeatureListNavigationAction.System
                Feature.BEACONS -> FeatureListNavigationAction.Beacons
                Feature.RESOURCES -> FeatureListNavigationAction.Resources
            }
        )
    }
}
