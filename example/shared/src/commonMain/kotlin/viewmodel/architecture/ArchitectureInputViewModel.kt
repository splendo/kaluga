package com.splendo.kaluga.example.shared.viewmodel.architecture

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.observable.ObservableResult
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.observable.toSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class InputNavigation(val name: String, val number: Int): NavigationAction<DetailsSpecRow<*>>() {

    private val dialogSpec = DetailsSpec()
    override val bundle: NavigationBundle<DetailsSpecRow<*>>?
        get() = dialogSpec.toBundle { row ->
            when (row) {
                is DetailsSpecRow.NameRow -> row.associatedType.convertValue(name)
                is DetailsSpecRow.NumberRow -> row.associatedType.convertValue(number)
            }
        }
}

class ArchitectureInputViewModel(navigator: Navigator<InputNavigation>) : NavigatingViewModel<InputNavigation>(navigator) {

    val nameHeader = observableOf("Enter your Name")
    val numberHeader = observableOf("Enter a Number")

    private val _nameInput  = BaseFlowable<String>().apply { runBlocking{set("")} }
    val nameInput = _nameInput.toSubject(coroutineScope)

    private val _numberInput  = BaseFlowable<String>().apply { runBlocking{set("")} }
    val numberInput = _numberInput.toSubject(coroutineScope)

    private val _isNameValid: Flow<Boolean> get() {return _nameInput.flow().map { it.isNotEmpty() }}
    val isNameValid = _isNameValid.toObservable(coroutineScope)

    private val _isNumberValid: Flow<Boolean> get() {return _numberInput.flow().map { it.toIntOrNull() != null  } }
    val isNumberValid = _isNumberValid.toObservable(coroutineScope)

    val isValid = combine(_isNameValid, _isNumberValid) {validName, validNumber -> validName && validNumber}.toObservable(coroutineScope)

    fun onShowDetailsPressed() {
        val nameResult: ObservableResult<String> by nameInput
        val name: String? by nameResult
        val numberResult: ObservableResult<String> by numberInput
        val number: String? by numberResult
        val isValid: ObservableResult<Boolean>  by isValid
        when(val valid = isValid) {
            is ObservableResult.Result -> {
                if (valid.value) {
                    navigator.navigate(InputNavigation(name ?: "", number?.toIntOrNull() ?: 0))
                }
            }
        }
    }

}