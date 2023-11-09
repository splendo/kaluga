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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.areaDensity.areaDensity
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricLengthTimesMetricDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    density * this

@JvmName("imperialLengthTimesImperialDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density * this

@JvmName("imperialLengthTimesUKImperialDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>,
) = density * this

@JvmName("imperialLengthTimesUSCustomaryDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>,
) = density * this

@JvmName("lengthTimesDensity")
infix operator fun <DensityUnit : Density, LengthUnit : Length> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = density * this

@JvmName("metricLengthDivMetricSpecificVolume")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(
    this,
    specificVolume,
)

@JvmName("imperialLengthDivImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(
    this,
    specificVolume,
)

@JvmName("imperialLengthDivUKImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(
    this,
    specificVolume,
)

@JvmName("imperialLengthDivUSCustomarySpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(
    this,
    specificVolume,
)

@JvmName("lengthDivSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LengthUnit : Length> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = (Kilogram per SquareMeter).areaDensity(this, specificVolume)
