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
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.example.shared.model.scientific.converters.QuantityConverter
import com.splendo.kaluga.example.shared.model.scientific.details
import com.splendo.kaluga.example.shared.model.scientific.name
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object CloseScientificConverterNavigationAction : SingleValueNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType)

class ScientificConverterViewModel internal constructor(
    private val leftUnits: Set<ScientificUnit<*>>,
    private val converter: QuantityConverter<*, *, *>?,
    val alertPresenterBuilder: BaseAlertPresenter.Builder,
    navigator: Navigator<CloseScientificConverterNavigationAction>
) : NavigatingViewModel<CloseScientificConverterNavigationAction>(navigator) {

    companion object {
        val numberFormatterDecimal = NumberFormatter(style = NumberFormatStyle.Decimal(maxIntegerDigits = 10U, minIntegerDigits = 1U, minFractionDigits = 0U, maxFractionDigits = 10U))
        val numberFormatterScientific = NumberFormatter(style = NumberFormatStyle.Scientific())
    }

    @kotlinx.serialization.Serializable
    data class Arguments(val physicalQuantity: PhysicalQuantity, val converterIndex: Int)

    private val rightUnits = converter?.rightUnits ?: emptySet()

    private val currentLeftUnit = MutableStateFlow(leftUnits.firstOrNull())
    val currentLeftUnitButton = currentLeftUnit.map { createLeftUnitButton(it) }.toInitializedObservable(createLeftUnitButton(null), coroutineScope)
    private val currentRightUnit = MutableStateFlow(rightUnits.firstOrNull())
    val currentRightUnitButton = currentRightUnit.map { createRightUnitButton(it) }.toInitializedObservable(createRightUnitButton(null), coroutineScope)
    private val _leftValue = MutableStateFlow("0.0")
    val leftValue = _leftValue.toInitializedSubject(coroutineScope)
    private val _rightValue = MutableStateFlow("0.0")
    val rightValue = _rightValue.toInitializedSubject(coroutineScope)
    val calculateOperatorSymbol = converter?.type?.operatorSymbol.orEmpty()
    private val _resultValue = MutableStateFlow("")
    val resultValue = _resultValue.toInitializedObservable(coroutineScope)

    val calculateButton = KalugaButton.Plain("Calculate", ButtonStyles.default) {
        val left = numberFormatterScientific.parse(_leftValue.value)?.toDecimal()
        val leftUnit = currentLeftUnit.value
        val right = numberFormatterScientific.parse(_rightValue.value)?.toDecimal()
        val rightUnit = currentRightUnit.value
        _resultValue.value = if (left != null && leftUnit != null && right != null && rightUnit != null) {
            converter?.convert(left, leftUnit, right, rightUnit)?.let { result ->
                val formattedDecimal = numberFormatterDecimal.format(result.value)
                val parsedDecimal = numberFormatterDecimal.parse(formattedDecimal)
                val formatter = if (parsedDecimal?.toDouble() == result.value.toDouble()) {
                    numberFormatterDecimal
                } else {
                    numberFormatterScientific
                }
                "${formatter.format(result.value)} ${result.unit.symbol}"
            }.orEmpty()
        } else {
            ""
        }
    }

    private fun createLeftUnitButton(currentUnit: ScientificUnit<*>?) = KalugaButton.Plain(currentUnit?.symbol.orEmpty(), ButtonStyles.default) { selectLeftUnit() }

    private fun selectLeftUnit() {
        coroutineScope.launch {
            alertPresenterBuilder.buildActionSheet(coroutineScope) {
                setTitle("Select Unit")
                addActions(leftUnits.map { Alert.Action(it.name) { currentLeftUnit.value = it } })
            }.show()
        }
    }

    private fun createRightUnitButton(currentUnit: ScientificUnit<*>?) = KalugaButton.Plain(currentUnit?.symbol.orEmpty(), ButtonStyles.default) { selectRightUnit() }

    private fun selectRightUnit() {
        coroutineScope.launch {
            alertPresenterBuilder.buildActionSheet(coroutineScope) {
                setTitle("Select Unit")
                addActions(rightUnits.map { Alert.Action(it.name) { currentRightUnit.value = it } })
            }.show()
        }
    }
}

fun ScientificConverterViewModel(arguments: ScientificConverterViewModel.Arguments, alertPresenterBuilder: BaseAlertPresenter.Builder, navigator: Navigator<CloseScientificConverterNavigationAction>) = arguments.physicalQuantity.details?.let { details ->
    details.converters.getOrNull(arguments.converterIndex)?.let { converter ->
        ScientificConverterViewModel(details.units, converter, alertPresenterBuilder, navigator)
    }
} ?: ScientificConverterViewModel(emptySet(), null, alertPresenterBuilder, navigator)
