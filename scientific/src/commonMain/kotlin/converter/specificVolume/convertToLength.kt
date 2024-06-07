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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMetricAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>.times(areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>) =
    (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("imperialSpecificVolumeTimesImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("imperialSpecificVolumeTimesUKImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("imperialSpecificVolumeTimesUSCustomaryAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("ukImperialSpecificVolumeTimesImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("ukImperialSpecificVolumeTimesUKImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("usCustomarySpecificVolumeTimesImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("usCustomarySpecificVolumeTimesUSCustomaryAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>,
) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)

@JvmName("specificVolumeTimesAreaDensity")
infix operator fun <SpecificVolumeUnit : SpecificVolume, AreaDensityUnit : AreaDensity> ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>,
) = Meter.length(this, areaDensity)
