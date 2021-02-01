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
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.ObservableOptional
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.observable.toSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.flow.HotFlowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class InputNavigation(inputDetails: InputDetails) : SingleValueNavigationAction<InputDetails>(
    inputDetails,
    NavigationBundleSpecType.SerializedType(InputDetails.serializer())
)

class ArchitectureInputViewModel(navigator: Navigator<SingleValueNavigationAction<InputDetails>>) : NavigatingViewModel<SingleValueNavigationAction<InputDetails>>(navigator) {

    val nameHeader = observableOf("Enter your Name")
    val numberHeader = observableOf("Enter a Number")

    private val _nameInput = HotFlowable("")
    val nameInput = _nameInput.toSubject(coroutineScope)

    private val _numberInput = HotFlowable("")
    val numberInput = _numberInput.toSubject(coroutineScope)

    private val _isNameValid: Flow<Boolean> get() { return _nameInput.flow().map {
        it.isNotEmpty()
    } }
    val isNameValid = _isNameValid.toObservable(coroutineScope)

    private val _isNumberValid: Flow<Boolean> get() { return _numberInput.flow().map {
        it.toIntOrNull() != null
    } }
    val isNumberValid = _isNumberValid.toObservable(coroutineScope)

    private val isValid = combine(_isNameValid, _isNumberValid) {
            validName, validNumber -> validName && validNumber
    }

    fun onShowDetailsPressed() {
        val nameResult: ObservableOptional<String> by nameInput
        val name: String? by nameResult
        val numberResult: ObservableOptional<String> by numberInput
        val number: String? by numberResult
        coroutineScope.launch {
            if (isValid.first()) {
                navigator.navigate(
                    InputNavigation(
                        InputDetails(name ?: "", number?.toIntOrNull() ?: 0)
                    )
                )
            }
        }
    }
}
