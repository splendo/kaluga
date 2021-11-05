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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molality.molality
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeDivMolarVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.div(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>
) = (molarVolume.unit.per per unit.per).molality(this, molarVolume)

@JvmName("imperialSpecificVolumeDivMolarVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.div(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>
) = (molarVolume.unit.per per unit.per).molality(this, molarVolume)

@JvmName("ukImperialSpecificVolumeDivMolarVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.div(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>
) = (molarVolume.unit.per per unit.per).molality(this, molarVolume)

@JvmName("usCustomarySpecificVolumeDivMolarVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.div(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>
) = (molarVolume.unit.per per unit.per).molality(this, molarVolume)

@JvmName("specificVolumeDivMolarVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.div(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>
) = (molarVolume.unit.per per Kilogram).molality(this, molarVolume)

@JvmName("metricSpecificVolumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = molarity * this

@JvmName("imperialSpecificVolumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = molarity * this

@JvmName("ukImperialSpecificVolumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = molarity * this

@JvmName("usCustomarySpecificVolumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = molarity * this

@JvmName("specificVolumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = molarity * this
