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

package com.splendo.kaluga.example.shared.viewmodel.architecture

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

@Serializable
data class InputDetails(
    val name: String,
    val number: Int,
)

sealed class ArchitectureDetailsNavigationAction<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    data object Close : ArchitectureDetailsNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType)
    class FinishWithDetails(details: InputDetails) : ArchitectureDetailsNavigationAction<InputDetails>(details, NavigationBundleSpecType.SerializedType(InputDetails.serializer()))
}

class ArchitectureDetailsViewModel(
    initialDetail: InputDetails,
    navigator: Navigator<ArchitectureDetailsNavigationAction<*>>,
) : NavigatingViewModel<ArchitectureDetailsNavigationAction<*>>(navigator) {

    private val _name = MutableStateFlow(initialDetail.name)
    val name = _name.toInitializedObservable(coroutineScope)
    private val _number = MutableStateFlow(initialDetail.number.toString())
    val number = _number.toInitializedObservable(coroutineScope)

    val inverseButton = KalugaButton.Plain("architecture_details_inverse".localized(), ButtonStyles.default) {
        _name.value = _name.value.reversed()
        _number.value = _number.value.reversed()
    }

    val finishButton = KalugaButton.Plain("architecture_finish".localized(), ButtonStyles.default) {
        navigator.navigate(ArchitectureDetailsNavigationAction.FinishWithDetails(InputDetails(_name.value, _number.value.toIntOrNull() ?: 0)))
    }

    fun onBackPressed() {
        navigator.navigate(ArchitectureDetailsNavigationAction.Close)
    }
}
