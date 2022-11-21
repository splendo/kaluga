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

package com.splendo.kaluga.example.shared.viewmodel.compose

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class ComposeOrAndroidUINavigationAction : SingleValueNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType) {
    object Compose : ComposeOrAndroidUINavigationAction()
    object AndroidUI : ComposeOrAndroidUINavigationAction()
}

sealed class UIType(val title: String) {
    object Compose : UIType("android_ui_compose".localized())
    object AndroidUI : UIType("android_ui_legacy".localized())
}

class ComposeOrAndroidUISelectionViewModel(
    navigator: Navigator<ComposeOrAndroidUINavigationAction>
) : NavigatingViewModel<ComposeOrAndroidUINavigationAction>(navigator) {

    val uiTypes = observableOf(
        listOf(
            UIType.Compose,
            UIType.AndroidUI
        )
    )

    fun onUITypePressed(feature: UIType) {
        navigator.navigate(
            when (feature) {
                is UIType.Compose -> ComposeOrAndroidUINavigationAction.Compose
                is UIType.AndroidUI -> ComposeOrAndroidUINavigationAction.AndroidUI
            }
        )
    }
}
