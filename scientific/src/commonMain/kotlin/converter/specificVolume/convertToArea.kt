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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.scientific.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.ImperialSpecificVolume
import com.splendo.kaluga.scientific.LinearMassDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricLinearMassDensity
import com.splendo.kaluga.scientific.MetricSpecificVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMetricLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("imperialSpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("imperialSpecificVolumeTimesUKImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("imperialSpecificVolumeTimesUSCustomaryLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("ukImperialSpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("ukImperialSpecificVolumeTimesUKImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("usCustomarySpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("usCustomarySpecificVolumeTimesUSCustomaryLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.area(this, linearMassDensity)
@JvmName("specificVolumeTimesLinearMassDensity")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LinearMassDensityUnit : LinearMassDensity> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = SquareMeter.area(this, linearMassDensity)
