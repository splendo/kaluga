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
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import kotlin.jvm.JvmName

@JvmName("metricAreaDensityTimesMetricSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>.times(specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>) =
    specificVolume * this

@JvmName("imperialAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("imperialAreaDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume * this

@JvmName("imperialAreaDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume * this

@JvmName("ukImperialAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("ukImperialAreaDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume * this

@JvmName("usCustomaryAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("usCustomaryAreaDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume * this

@JvmName("areaDensityTimesSpecificVolume")
infix operator fun <AreaDensityUnit : AreaDensity, SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = specificVolume * this

@JvmName("metricAreaDensityDivMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("imperialAreaDensityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("imperialAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("imperialAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("ukImperialAreaDensityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("ukImperialAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("usCustomaryAreaDensityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("usCustomaryAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("areaDensityDivDensity")
infix operator fun <AreaDensityUnit : AreaDensity, DensityUnit : Density> ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
