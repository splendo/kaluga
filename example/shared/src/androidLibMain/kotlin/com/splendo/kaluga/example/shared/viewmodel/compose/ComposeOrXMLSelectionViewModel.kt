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

sealed class ComposeOrXMLNavigationAction : SingleValueNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType) {
    data object Compose : ComposeOrXMLNavigationAction()
    data object XML : ComposeOrXMLNavigationAction()
}

sealed class UIType(val title: String) {
    data object Compose : UIType("android_ui_compose".localized())
    data object XML : UIType("android_ui_xml".localized())
}

class ComposeOrXMLSelectionViewModel(
    navigator: Navigator<ComposeOrXMLNavigationAction>,
) : NavigatingViewModel<ComposeOrXMLNavigationAction>(navigator) {

    val uiTypes = observableOf(
        listOf(
            UIType.Compose,
            UIType.XML,
        ),
    )

    fun onUITypePressed(feature: UIType) {
        navigator.navigate(
            when (feature) {
                is UIType.Compose -> ComposeOrXMLNavigationAction.Compose
                is UIType.XML -> ComposeOrXMLNavigationAction.XML
            },
        )
    }
}
