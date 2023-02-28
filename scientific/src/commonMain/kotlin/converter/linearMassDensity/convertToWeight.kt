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

package com.splendo.kaluga.scientific.converter.linearMassDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.weight.mass(this, length)

@JvmName("imperialLinearMassDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.weight.mass(this, length)

@JvmName("ukImperialLinearMassDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.weight.mass(this, length)

@JvmName("usCustomaryLinearMassDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.weight.mass(this, length)

@JvmName("linearMassDensityTimesLength")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = Kilogram.mass(this, length)
