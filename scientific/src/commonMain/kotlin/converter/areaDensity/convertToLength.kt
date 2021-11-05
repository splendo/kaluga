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

package com.splendo.kaluga.scientific.converter.areaDensity

import com.splendo.kaluga.scientific.MeasurementType
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
infix operator fun ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>
) = specificVolume * this

@JvmName("imperialAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>
) = specificVolume * this

@JvmName("imperialAreaDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>
) = specificVolume * this

@JvmName("imperialAreaDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>
) = specificVolume * this

@JvmName("ukImperialAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>
) = specificVolume * this

@JvmName("ukImperialAreaDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>
) = specificVolume * this

@JvmName("usCustomaryAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>
) = specificVolume * this

@JvmName("usCustomaryAreaDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>
) = specificVolume * this

@JvmName("areaDensityTimesSpecificVolume")
infix operator fun <AreaDensityUnit : AreaDensity, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.times(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>
) = specificVolume * this

@JvmName("metricAreaDensityDivMetricDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("imperialAreaDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("imperialAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("imperialAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("ukImperialAreaDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("ukImperialAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("usCustomaryAreaDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("usCustomaryAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("areaDensityDivDensity")
infix operator fun <AreaDensityUnit : AreaDensity, DensityUnit : Density> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.div(
    density: ScientificValue<MeasurementType.Density, DensityUnit>
) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
