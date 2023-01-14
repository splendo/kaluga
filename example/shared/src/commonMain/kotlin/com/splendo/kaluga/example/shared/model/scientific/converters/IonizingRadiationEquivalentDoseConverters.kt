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

package com.splendo.kaluga.example.shared.model.scientific.converters

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.times
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentManMultiple
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight

val PhysicalQuantity.IonizingRadiationEquivalentDose.converters get() = listOf<QuantityConverter<PhysicalQuantity.IonizingRadiationEquivalentDose, *>>(
    QuantityConverterWithOperator("Energy from Weight", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is RoentgenEquivalentMan && rightUnit is Gram -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is RoentgenEquivalentManMultiple && rightUnit is Gram -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is IonizingRadiationEquivalentDose && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is IonizingRadiationEquivalentDose && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is IonizingRadiationEquivalentDose && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is IonizingRadiationEquivalentDose && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
