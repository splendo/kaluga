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

package com.splendo.kaluga.example.shared.viewmodel.architecture

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SimpleNavigationAction
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.ObservableOptional
import com.splendo.kaluga.architecture.observable.subjectOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import kotlinx.serialization.Serializable

@Serializable
data class InputDetails(
    val name: String,
    val number: Int
)

class CloseDetailsNavigation(inputDetails: InputDetails) : SimpleNavigationAction<InputDetails>(
    inputDetails,
    NavigationBundleSpecType.SerializedType(InputDetails.serializer())
)

class ArchitectureDetailsViewModel(initialDetail: InputDetails, navigator: Navigator<CloseDetailsNavigation>) : NavigatingViewModel<CloseDetailsNavigation>(navigator) {

    private val _name = subjectOf(initialDetail.name, coroutineScope)
    val name: Observable<String> = _name
    private val _number = subjectOf(initialDetail.number.toString(), coroutineScope)
    val number: Observable<String> = _number

    private var nameResult: ObservableOptional<String> by _name
    private var numberResult: ObservableOptional<String> by _number

    fun onInversePressed() {
        val newName: String? by nameResult
        val newNumber: String? by numberResult

        newName?.let {
            nameResult = ObservableOptional.Value(it.reversed())
        }
        newNumber?.let {
            numberResult = ObservableOptional.Value(it.reversed())
        }
    }

    fun onClosePressed() {
        val name: String? by nameResult
        val number: String? by numberResult
        navigator.navigate(CloseDetailsNavigation(InputDetails(name ?: "", number?.toIntOrNull() ?: 0)))
    }
}
