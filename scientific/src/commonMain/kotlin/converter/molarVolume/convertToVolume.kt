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

package com.splendo.kaluga.scientific.converter.molarVolume

import com.splendo.kaluga.scientific.AmountOfSubstance
import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialMolarVolume
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMolarVolume
import com.splendo.kaluga.scientific.MolarVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMolarVolume
import com.splendo.kaluga.scientific.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.converter.volume.volume
import kotlin.jvm.JvmName

@JvmName("metricMolarVolumeTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarVolume, MetricMolarVolume>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.volume.volume(this, amountOfSubstance)
@JvmName("imperialMolarVolumeTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.volume.volume(this, amountOfSubstance)
@JvmName("ukImperialMolarVolumeTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.volume.volume(this, amountOfSubstance)
@JvmName("usCustomaryMolarVolumeTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.volume.volume(this, amountOfSubstance)
@JvmName("molarVolumeTimesAmountOfSubstance")
infix operator fun <MolarVolumeUnit : MolarVolume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = CubicMeter.volume(this, amountOfSubstance)
