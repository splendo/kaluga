/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.system

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class SystemFeatures(val name: String) {
    object Network : SystemFeatures("network_feature".localized())
}

sealed class SystemNavigationActions<Value>(
    value: Value,
    type: NavigationBundleSpecType<Value>
) : SingleValueNavigationAction<Value>(value, type) {
    object Network : SystemNavigationActions<Unit>(Unit, NavigationBundleSpecType.UnitType)
}

class SystemViewModel(
    navigator: Navigator<SystemNavigationActions<Unit>>
) : NavigatingViewModel<SystemNavigationActions<Unit>>(navigator) {

    val modules =
        observableOf(
            listOf(
                SystemFeatures.Network
        )
    )

    fun onButtonTapped(systemFeatures: SystemFeatures) {
        when (systemFeatures) {
            is SystemFeatures.Network -> navigator.navigate(SystemNavigationActions.Network)
        }
    }
}