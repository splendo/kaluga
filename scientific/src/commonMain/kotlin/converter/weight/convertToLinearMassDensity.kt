/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.linearMassDensity.linearMassDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricLength")
infix operator fun <WeightUnit : MetricWeight, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).linearMassDensity(this, length)

@JvmName("imperialWeightDivImperialLength")
infix operator fun <WeightUnit : ImperialWeight, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).linearMassDensity(this, length)

@JvmName("ukImperialWeightDivImperialLength")
infix operator fun <WeightUnit : UKImperialWeight, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).linearMassDensity(this, length)

@JvmName("usCustomaryWeightDivImperialLength")
infix operator fun <WeightUnit : USCustomaryWeight, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).linearMassDensity(this, length)

@JvmName("weightDivLength")
infix operator fun <WeightUnit : Weight, LengthUnit : Length> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (Kilogram per Meter).linearMassDensity(this, length)
