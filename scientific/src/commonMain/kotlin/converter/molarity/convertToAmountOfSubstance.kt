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

package com.splendo.kaluga.scientific.converter.molarity

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.amountOfSubstance.amountOfSubstance
import com.splendo.kaluga.scientific.unit.ImperialMolarity
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.MetricMolarity
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.UKImperialMolarity
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryMolarity
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("metricMolarityTimesMetricVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Molarity, MetricMolarity>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = unit.amountOfSubstance.amountOfSubstance(this, volume)

@JvmName("imperialMolarityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = unit.amountOfSubstance.amountOfSubstance(this, volume)

@JvmName("ukImperialMolarityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = unit.amountOfSubstance.amountOfSubstance(this, volume)

@JvmName("ukImperialMolarityTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = unit.amountOfSubstance.amountOfSubstance(this, volume)

@JvmName("usCustomaryMolarityTimesUKImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = unit.amountOfSubstance.amountOfSubstance(this, volume)

@JvmName("usCustomaryMolarityTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = unit.amountOfSubstance.amountOfSubstance(this, volume)

@JvmName("molarityTimesVolume")
infix operator fun <MolarityUnit : Molarity, VolumeUnit : Volume> ScientificValue<MeasurementType.Molarity, MolarityUnit>.times(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = Mole.amountOfSubstance(this, volume)
