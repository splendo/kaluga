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

package com.splendo.kaluga.example.feature.scientific.converters

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.converter.molarEntropy.times
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialMolarEntropy
import com.splendo.kaluga.scientific.unit.MetricMolarEntropy
import com.splendo.kaluga.scientific.unit.MolarEntropy
import com.splendo.kaluga.scientific.unit.UKImperialMolarEntropy
import com.splendo.kaluga.scientific.unit.USCustomaryMolarEntropy

val PhysicalQuantity.MolarEntropy.converters get() = listOf<QuantityConverter<PhysicalQuantity.MolarEntropy, *>>(
    QuantityConverterWithOperator(
        "Heat Capacity from Amount Of Substance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.AmountOfSubstance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndUKImperialMolarEntropy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricMolarEntropy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialMolarEntropy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryMolarEntropy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MolarEntropy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
