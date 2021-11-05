/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("lengthFromWeightAndLinearMassDensityDefault")
fun <
    WeightUnit : Weight,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LengthUnit.length(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>
) = length(weight, linearMassDensity, ::DefaultScientificValue)

@JvmName("lengthFromWeightAndLinearMassDensity")
fun <
    WeightUnit : Weight,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity,
    Value : ScientificValue<MeasurementType.Length, LengthUnit>
    > LengthUnit.length(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    factory: (Decimal, LengthUnit) -> Value
) = byDividing(weight, linearMassDensity, factory)
