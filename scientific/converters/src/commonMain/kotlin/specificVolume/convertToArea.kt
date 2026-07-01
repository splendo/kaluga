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
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMetricLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("imperialSpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("imperialSpecificVolumeTimesUKImperialLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("imperialSpecificVolumeTimesUSCustomaryLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("ukImperialSpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("ukImperialSpecificVolumeTimesUKImperialLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("usCustomarySpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("usCustomarySpecificVolumeTimesUSCustomaryLinearMassDensity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>,
) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)

@JvmName("specificVolumeTimesLinearMassDensity")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LinearMassDensityUnit : LinearMassDensity> ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>.times(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>,
) = SquareMeter.area(this, linearMassDensity)
