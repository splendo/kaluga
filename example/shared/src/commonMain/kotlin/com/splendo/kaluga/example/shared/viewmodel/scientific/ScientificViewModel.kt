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

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.BaseInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.example.shared.model.scientific.QuantityDetails
import com.splendo.kaluga.example.shared.model.scientific.allPhysicalQuantities
import com.splendo.kaluga.example.shared.model.scientific.quantityDetails
import com.splendo.kaluga.example.shared.model.scientific.name
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScientificNavigationAction<T>(value: T, spec: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, spec) {
    sealed class SelectUnit(quantity: PhysicalQuantity) :
        ScientificNavigationAction<PhysicalQuantity>(
            quantity,
            NavigationBundleSpecType.SerializedType(PhysicalQuantity.serializer()),
        ) {
        data class Left(val quantity: PhysicalQuantity) : SelectUnit(quantity)
        data class Right(val quantity: PhysicalQuantity) : SelectUnit(quantity)
    }
    data class Converter(val quantity: PhysicalQuantity, val index: Int) :
        ScientificNavigationAction<ScientificConverterViewModel.Arguments>(
            ScientificConverterViewModel.Arguments(quantity, index),
            NavigationBundleSpecType.SerializedType(ScientificConverterViewModel.Arguments.serializer()),
        )
}

class ScientificViewModel(private val alertPresenterBuilder: BaseAlertPresenter.Builder, navigator: Navigator<ScientificNavigationAction<*>>) :
    NavigatingViewModel<ScientificNavigationAction<*>>(navigator, alertPresenterBuilder) {

    data class Button(val name: String, val quantity: PhysicalQuantity, val index: Int, val navigator: Navigator<in ScientificNavigationAction.Converter>) {
        val id: String get() = "${quantity.name}_$index"
        val button: KalugaButton.Plain get() = KalugaButton.Plain(name, ButtonStyles.default) {
            navigator.navigate(ScientificNavigationAction.Converter(quantity, index))
        }
    }

    companion object {
        val numberFormatterDecimal = NumberFormatter(
            style = NumberFormatStyle.Decimal(maxIntegerDigits = 10U, minIntegerDigits = 1U, minFractionDigits = 0U, maxFractionDigits = 10U),
        )
        val numberFormatterScientific = NumberFormatter(style = NumberFormatStyle.Scientific())
    }

    private val quantityDetails = allPhysicalQuantities.mapNotNull { it.quantityDetails }
    private val currentQuantityDetails = MutableStateFlow(quantityDetails.firstOrNull())
    val quantityDetailsButton: BaseInitializedObservable<KalugaButton> = currentQuantityDetails.map {
        createQuantityButton(it)
    }.toInitializedObservable(createQuantityButton(null), coroutineScope)
    private val units = currentQuantityDetails.map { it?.units ?: emptySet() }.stateIn(coroutineScope, SharingStarted.Eagerly, emptySet())
    private val currentLeftUnit = MutableStateFlow<AbstractScientificUnit<*>?>(null)
    val currentLeftUnitButton: BaseInitializedObservable<KalugaButton> = currentLeftUnit.map {
        createLeftUnitButton(it)
    }.toInitializedObservable(createLeftUnitButton(null), coroutineScope)
    private val currentRightUnit = MutableStateFlow<AbstractScientificUnit<*>?>(null)
    val currentRightUnitButton: BaseInitializedObservable<KalugaButton> = currentRightUnit.map {
        createRightUnitButton(it)
    }.toInitializedObservable(createRightUnitButton(null), coroutineScope)
    private val _leftValue = MutableStateFlow(numberFormatterDecimal.format(0.0))
    val leftValue = _leftValue.toInitializedSubject(coroutineScope)
    private val _rightValue = MutableStateFlow(numberFormatterDecimal.format(0.0))
    val rightValue = _rightValue.toInitializedObservable(coroutineScope)

    val converters = currentQuantityDetails.map { details ->
        details?.let {
            details.converters.mapIndexed { index, converter ->
                Button(converter.name, details.quantity, index, navigator)
            }
        }.orEmpty()
    }.toInitializedObservable(emptyList(), coroutineScope)

    init {
        coroutineScope.launch {
            units.collect {
                currentLeftUnit.value = it.firstOrNull()
                currentRightUnit.value = it.firstOrNull()
            }
        }
    }

    val calculateButton = KalugaButton.Plain("Calculate", ButtonStyles.default) {
        val details = currentQuantityDetails.value
        val left = numberFormatterScientific.parse(_leftValue.value)?.toDecimal()
        val leftUnit = currentLeftUnit.value
        val rightUnit = currentRightUnit.value
        _rightValue.value = if (details != null && left != null && leftUnit != null && rightUnit != null) {
            details.convert(left, leftUnit, rightUnit)?.let { result ->
                val formattedDecimal = numberFormatterDecimal.format(result.value)
                val parsedDecimal = numberFormatterDecimal.parse(formattedDecimal)
                val formatter = if (parsedDecimal?.toDouble() == result.value.toDouble()) {
                    numberFormatterDecimal
                } else {
                    numberFormatterScientific
                }
                formatter.format(result.value)
            }
        } else {
            null
        } ?: "Invalid Result"
    }

    private fun createQuantityButton(currentQuantityDetails: QuantityDetails<*>?) = KalugaButton.Plain(
        currentQuantityDetails?.let {
            it.quantity::class.simpleName
        }.orEmpty(),
        ButtonStyles.default,
    ) { selectQuantity() }

    private fun selectQuantity() {
        coroutineScope.launch {
            alertPresenterBuilder.buildActionSheet(coroutineScope) {
                setTitle("Select Quantity")
                addActions(quantityDetails.map { Alert.Action(it.quantity.name) { currentQuantityDetails.value = it } })
            }.show()
        }
    }

    fun didSelectLeftUnit(unitIndex: Int) {
        currentLeftUnit.value = currentQuantityDetails.value?.units?.withIndex()?.toList()?.getOrNull(unitIndex)?.value
    }

    fun didSelectRightUnit(unitIndex: Int) {
        currentRightUnit.value = currentQuantityDetails.value?.units?.withIndex()?.toList()?.getOrNull(unitIndex)?.value
    }

    private fun createLeftUnitButton(currentUnit: ScientificUnit<*>?) = KalugaButton.Plain(currentUnit?.symbol.orEmpty(), ButtonStyles.default) {
        currentQuantityDetails.value?.let {
            navigator.navigate(ScientificNavigationAction.SelectUnit.Left(it.quantity))
        }
    }

    private fun createRightUnitButton(currentUnit: ScientificUnit<*>?) = KalugaButton.Plain(currentUnit?.symbol.orEmpty(), ButtonStyles.default) {
        currentQuantityDetails.value?.let {
            navigator.navigate(ScientificNavigationAction.SelectUnit.Right(it.quantity))
        }
    }
}
