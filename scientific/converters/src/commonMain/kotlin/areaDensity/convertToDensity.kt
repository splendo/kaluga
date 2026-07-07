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

package com.splendo.kaluga.scientific.converter.areaDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAreaDensityDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>.div(length: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)

@JvmName("imperialAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)

@JvmName("ukImperialAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)

@JvmName("usCustomaryAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)

@JvmName("areaDensityDivLength")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (Kilogram per CubicMeter).density(this, length)
