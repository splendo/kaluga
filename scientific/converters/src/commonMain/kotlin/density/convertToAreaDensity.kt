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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.areaDensity.areaDensity
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(length: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)

@JvmName("imperialDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(length: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)

@JvmName("ukImperialDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(length: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)

@JvmName("usCustomaryDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)

@JvmName("densityTimesLength")
infix operator fun <DensityUnit : Density, LengthUnit : Length> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (Kilogram per SquareMeter).areaDensity(this, length)
