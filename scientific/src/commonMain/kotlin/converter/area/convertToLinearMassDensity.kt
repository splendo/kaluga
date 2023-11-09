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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.linearMassDensity.linearMassDensity
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAreaTimesMetricDensity")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    density * this

@JvmName("imperialAreaTimesImperialDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density * this

@JvmName("imperialAreaTimesUKImperialDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    density * this

@JvmName("imperialAreaTimesUSCustomaryDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    density * this

@JvmName("areaTimesDensity")
infix operator fun <DensityUnit : Density, AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = density * this

@JvmName("metricAreaDivMetricSpecificVolume")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(
    this,
    specificVolume,
)

@JvmName("imperialAreaDivImperialSpecificVolume")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(
    this,
    specificVolume,
)

@JvmName("imperialAreaDivUKImperialSpecificVolume")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(
    this,
    specificVolume,
)

@JvmName("imperialAreaDivUSCustomarySpecificVolume")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(
    this,
    specificVolume,
)

@JvmName("areaDivSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = (Kilogram per Meter).linearMassDensity(this, specificVolume)
