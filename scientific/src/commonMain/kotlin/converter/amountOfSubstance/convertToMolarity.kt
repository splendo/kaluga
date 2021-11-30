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

package com.splendo.kaluga.scientific.converter.amountOfSubstance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarity.molarity
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceDivMetricVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).molarity(this, volume)

@JvmName("amountOfSubstanceDivImperialVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).molarity(this, volume)

@JvmName("amountOfSubstanceDivUKImperialVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).molarity(this, volume)

@JvmName("amountOfSubstanceDivUSCustomaryVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per volume.unit).molarity(this, volume)

@JvmName("amountOfSubstanceDivVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>
) = (unit per CubicMeter).molarity(this, volume)
