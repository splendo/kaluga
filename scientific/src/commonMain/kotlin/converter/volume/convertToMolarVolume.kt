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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarVolume.molarVolume
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricVolumeDivAmountOfSubstance")
infix operator fun <VolumeUnit : MetricVolume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>
) = (unit per amountOfSubstance.unit).molarVolume(this, amountOfSubstance)

@JvmName("imperialVolumeDivAmountOfSubstance")
infix operator fun <VolumeUnit : ImperialVolume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>
) = (unit per amountOfSubstance.unit).molarVolume(this, amountOfSubstance)

@JvmName("ukImperialVolumeDivAmountOfSubstance")
infix operator fun <VolumeUnit : UKImperialVolume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>
) = (unit per amountOfSubstance.unit).molarVolume(this, amountOfSubstance)

@JvmName("usCustomaryVolumeDivAmountOfSubstance")
infix operator fun <VolumeUnit : USCustomaryVolume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>
) = (unit per amountOfSubstance.unit).molarVolume(this, amountOfSubstance)

@JvmName("volumeDivAmountOfSubstance")
infix operator fun <VolumeUnit : Volume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>
) = (CubicMeter per Mole).molarVolume(this, amountOfSubstance)
