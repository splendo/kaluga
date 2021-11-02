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

package com.splendo.kaluga.scientific.converter.volume

import com.splendo.kaluga.scientific.ImperialMolarity
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMolarity
import com.splendo.kaluga.scientific.MetricVolume
import com.splendo.kaluga.scientific.MolarVolume
import com.splendo.kaluga.scientific.Molarity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMolarity
import com.splendo.kaluga.scientific.UKImperialVolume
import com.splendo.kaluga.scientific.USCustomaryMolarity
import com.splendo.kaluga.scientific.USCustomaryVolume
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.converter.amountOfSubstance.amountOfSubstance
import com.splendo.kaluga.scientific.converter.molarity.times
import kotlin.jvm.JvmName

@JvmName("metricVolumeTimesMetricMolarity")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, MetricMolarity>) = molarity * this
@JvmName("imperialVolumeTimesImperialMolarity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = molarity * this
@JvmName("imperialVolumeTimesUKImperialMolarity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = molarity * this
@JvmName("imperialVolumeTimesUSCustomaryMolarity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = molarity * this
@JvmName("ukImperialVolumeTimesUKImperialMolarity")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = molarity * this
@JvmName("usCustomaryVolumeTimesUSCustomaryMolarity")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = molarity * this
@JvmName("volumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity, VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = molarity * this

@JvmName("volumeDivMolarVolume")
infix operator fun <VolumeUnit : Volume, MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>) = molarVolume.unit.per.amountOfSubstance(this, molarVolume)
