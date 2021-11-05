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

package com.splendo.kaluga.scientific.converter.amountOfSubstance

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molality.molality
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceDivMetricWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : MetricWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivImperialWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivUKImperialWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivUSCustomaryWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : Weight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Mole per Kilogram).molality(this, weight)
