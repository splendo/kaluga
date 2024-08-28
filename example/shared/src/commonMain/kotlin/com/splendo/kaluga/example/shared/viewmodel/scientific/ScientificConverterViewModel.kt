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
import com.splendo.kaluga.architecture.observable.BaseInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.example.shared.model.scientific.converters.QuantityConverter
import com.splendo.kaluga.example.shared.model.scientific.name
import com.splendo.kaluga.example.shared.model.scientific.quantityDetails
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

sealed class ScientificConverterNavigationAction<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    data object Close : ScientificConverterNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType)
    sealed class SelectUnit(quantity: PhysicalQuantity) :
        ScientificConverterNavigationAction<PhysicalQuantity>(
            quantity,
            NavigationBundleSpecType.SerializedType(PhysicalQuantity.serializer()),
        ) {
        data class Left(val quantity: PhysicalQuantity) : SelectUnit(quantity)
        data class Right(val quantity: PhysicalQuantity) : SelectUnit(quantity)
    }
}

class ScientificConverterViewModel internal constructor(
    private val leftUnits: Set<ScientificUnit<*>>,
    private val converter: QuantityConverter<*, *>?,
    navigator: Navigator<ScientificConverterNavigationAction<*>>,
) : NavigatingViewModel<ScientificConverterNavigationAction<*>>(navigator) {

    companion object {
        val numberFormatterDecimal = NumberFormatter(
            style = NumberFormatStyle.Decimal(maxIntegerDigits = 10U, minIntegerDigits = 1U, minFractionDigits = 0U, maxFractionDigits = 10U),
        )
        val numberFormatterScientific = NumberFormatter(style = NumberFormatStyle.Scientific())
    }

    @kotlinx.serialization.Serializable
    data class Arguments(val physicalQuantity: PhysicalQuantity, val converterIndex: Int) {
        val id: String get() = "${physicalQuantity.name}_$converterIndex"
    }

    private val currentLeftUnit = MutableStateFlow(leftUnits.firstOrNull())
    val currentLeftUnitButton: BaseInitializedObservable<KalugaButton> = currentLeftUnit.map {
        createLeftUnitButton(it)
    }.toInitializedObservable(createLeftUnitButton(null), coroutineScope)
    val isRightUnitSelectable = converter is QuantityConverter.WithOperator<*, *, *>
    private val currentRightUnit = MutableStateFlow((converter as? QuantityConverter.WithOperator<*, *, *>)?.rightQuantity?.quantityDetails?.units?.firstOrNull())
    val currentRightUnitButton: BaseInitializedObservable<KalugaButton> = currentRightUnit.map {
        createRightUnitButton(it)
    }.toInitializedObservable(createRightUnitButton(null), coroutineScope)
    private val _leftValue = MutableStateFlow(numberFormatterDecimal.format(0.0))
    val leftValue = _leftValue.toInitializedSubject(coroutineScope)
    private val _rightValue = MutableStateFlow(numberFormatterDecimal.format(0.0))
    val rightValue = _rightValue.toInitializedSubject(coroutineScope)
    val calculateOperatorSymbol = (converter as? QuantityConverter.WithOperator<*, *, *>)?.type?.operatorSymbol.orEmpty()
    private val _resultValue = MutableStateFlow("")
    val resultValue = _resultValue.toInitializedObservable(coroutineScope)

    val calculateButton = KalugaButton.Plain("Calculate", ButtonStyles.default) {
        val left = numberFormatterScientific.parse(_leftValue.value)?.toDecimal()
        val leftUnit = currentLeftUnit.value
        val right = numberFormatterScientific.parse(_rightValue.value)?.toDecimal()
        val rightUnit = currentRightUnit.value
        _resultValue.value = when (converter) {
            is QuantityConverter.Single<*, *> -> {
                if (left != null && leftUnit != null) {
                    converter.convert(left, leftUnit)
                } else {
                    null
                }
            }
            is QuantityConverter.WithOperator<*, *, *> -> {
                if (left != null && leftUnit != null && right != null && rightUnit != null) {
                    converter.convert(left, leftUnit, right, rightUnit)
                } else {
                    null
                }
            }
            null -> {
                null
            }
        }?.let { result ->
            val formattedDecimal = numberFormatterDecimal.format(result.value)
            val parsedDecimal = numberFormatterDecimal.parse(formattedDecimal)
            val formatter = if (parsedDecimal?.toDouble() == result.value.toDouble()) {
                numberFormatterDecimal
            } else {
                numberFormatterScientific
            }
            "${formatter.format(result.value)} ${result.unit.symbol}"
        } ?: "Invalid Result"
    }

    fun didSelectLeftUnit(unitIndex: Int) {
        currentLeftUnit.value = leftUnits.toList().getOrNull(unitIndex)
    }

    fun didSelectRightUnit(unitIndex: Int) {
        currentRightUnit.value = (converter as? QuantityConverter.WithOperator<*, *, *>)?.rightQuantity?.quantityDetails?.units?.toList()?.getOrNull(unitIndex)
    }

    fun onClosePressed() {
        navigator.navigate(ScientificConverterNavigationAction.Close)
    }

    private fun createLeftUnitButton(currentUnit: ScientificUnit<*>?) = KalugaButton.Plain(currentUnit?.symbol.orEmpty(), ButtonStyles.default) {
        currentLeftUnit.value?.let {
            navigator.navigate(ScientificConverterNavigationAction.SelectUnit.Left(it.quantity))
        }
    }

    private fun createRightUnitButton(currentUnit: ScientificUnit<*>?) = KalugaButton.Plain(currentUnit?.symbol.orEmpty(), ButtonStyles.default) {
        currentRightUnit.value?.let {
            navigator.navigate(ScientificConverterNavigationAction.SelectUnit.Right(it.quantity))
        }
    }
}

fun ScientificConverterViewModel(arguments: ScientificConverterViewModel.Arguments, navigator: Navigator<ScientificConverterNavigationAction<*>>) =
    arguments.physicalQuantity.quantityDetails?.let { details ->
        details.converters.getOrNull(arguments.converterIndex)?.let { converter ->
            ScientificConverterViewModel(details.units, converter, navigator)
        }
    } ?: ScientificConverterViewModel(emptySet(), null, navigator)
