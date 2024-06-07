/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.scientific

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.example.shared.model.scientific.name
import com.splendo.kaluga.example.shared.model.scientific.quantityDetails
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class ScientificUnitSelectionAction<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    data object Cancelled : ScientificUnitSelectionAction<Unit>(Unit, NavigationBundleSpecType.UnitType)
    data class DidSelect(val index: Int) : ScientificUnitSelectionAction<Int>(index, NavigationBundleSpecType.IntegerType)
}

class ScientificUnitSelectionViewModel(
    private val quantity: PhysicalQuantity,
    navigator: Navigator<ScientificUnitSelectionAction<*>>,
) : NavigatingViewModel<ScientificUnitSelectionAction<*>>(navigator) {

    private val allUnits = MutableStateFlow<List<String>>(emptyList())
    private val _filter = MutableStateFlow("")
    val filter = _filter.toInitializedSubject(coroutineScope)

    init {
        coroutineScope.launch(Dispatchers.Default) {
            allUnits.value = quantity.quantityDetails?.units?.map { it.name }.orEmpty()
        }
    }

    val currentUnits = combine(allUnits, _filter) { units, filterToApply ->
        units.withIndex()
            .filter { it.value.lowercase().contains(filterToApply.lowercase()) }
            .map { KalugaButton.Plain(it.value, ButtonStyles.default) { navigator.navigate(ScientificUnitSelectionAction.DidSelect(it.index)) } }
    }.toInitializedObservable(emptyList(), coroutineScope)

    fun onCancel() {
        navigator.navigate(ScientificUnitSelectionAction.Cancelled)
    }
}
