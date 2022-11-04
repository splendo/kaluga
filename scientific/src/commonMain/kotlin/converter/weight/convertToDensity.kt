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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricVolume")
infix operator fun <WeightUnit : MetricWeight, VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("imperialWeightDivImperialVolume")
infix operator fun <WeightUnit : ImperialWeight, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("imperialWeightDivUKImperialVolume")
infix operator fun <WeightUnit : ImperialWeight, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("imperialWeightDivUSCustomaryVolume")
infix operator fun <WeightUnit : ImperialWeight, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("ukImperialWeightDivImperialVolume")
infix operator fun <WeightUnit : UKImperialWeight, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("ukImperialWeightDivUKImperialVolume")
infix operator fun <WeightUnit : UKImperialWeight, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("usCustomaryWeightDivImperialVolume")
infix operator fun <WeightUnit : USCustomaryWeight, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("usCustomaryWeightDivUSCustomaryVolume")
infix operator fun <WeightUnit : USCustomaryWeight, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).density(this, volume)

@JvmName("weightDivVolume")
infix operator fun <WeightUnit : Weight, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (Kilogram per CubicMeter).density(this, volume)
