/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.scientific

import androidx.lifecycle.ViewModel
import com.splendo.kaluga.base.formatting.NumberFormatStyle
import com.splendo.kaluga.base.formatting.NumberFormatter
import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.example.feature.scientific.converters.QuantityConverter
import com.splendo.kaluga.example.feature.scientific.model.QuantityDetails
import com.splendo.kaluga.example.feature.scientific.model.allPhysicalQuantities
import com.splendo.kaluga.example.feature.scientific.model.name
import com.splendo.kaluga.example.feature.scientific.model.quantityDetails
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.formatter.CommonScientificValueFormatter
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScientificViewModel : ViewModel() {

    companion object {
        private val formatter = CommonScientificValueFormatter.with {
            defaultValueFormatter = NumberFormatter(
                style = NumberFormatStyle.Scientific(
                    maxIntegerDigits = 4U,
                    minFractionDigits = 0U,
                    minExponent = 1U,
                    maxExponentForDecimalNotation = 6U,
                ),
            )
        }

        val quantities: List<QuantityDetails<*>> = allPhysicalQuantities
            .mapNotNull { it.quantityDetails }
            .filter { it.units.isNotEmpty() }
            .sortedBy { it.quantity.name }
    }

    private val _selectedQuantity = MutableStateFlow(quantities.firstOrNull())
    val selectedQuantity: StateFlow<QuantityDetails<*>?> = _selectedQuantity.asStateFlow()

    fun selectQuantity(details: QuantityDetails<*>) {
        _selectedQuantity.value = details
    }

    fun <Q : PhysicalQuantity> convert(details: QuantityDetails<Q>, rawValue: String, from: AbstractScientificUnit<Q>?, to: AbstractScientificUnit<Q>?): String {
        if (from == null || to == null) return "Pick units"
        val decimal = rawValue.toDoubleOrNull()?.toDecimal() ?: return "Enter a number"
        return details.convert(decimal, from, to)?.let(::format) ?: "Cannot convert"
    }

    fun convertSingle(converter: QuantityConverter.Single<*, *>, rawValue: String, leftUnit: ScientificUnit<*>): String {
        val decimal = rawValue.toDoubleOrNull()?.toDecimal() ?: return "Enter a value above"
        return converter.convert(decimal, leftUnit)?.let(::format) ?: "Cannot convert"
    }

    fun convertWithOperator(
        converter: QuantityConverter.WithOperator<*, *, *>,
        leftRaw: String,
        leftUnit: ScientificUnit<*>,
        rightRaw: String,
        rightUnit: ScientificUnit<*>?,
    ): String {
        val left = leftRaw.toDoubleOrNull()?.toDecimal()
        val right = rightRaw.toDoubleOrNull()?.toDecimal()
        if (left == null || right == null || rightUnit == null) return "Enter values"
        return converter.convert(left, leftUnit, right, rightUnit)?.let(::format) ?: "Cannot convert"
    }

    private fun format(value: ScientificValue<*, *>): String = formatter.format(value)
}
