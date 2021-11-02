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

package com.splendo.kaluga.scientific.converter.molarMass

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialMolarMass
import com.splendo.kaluga.scientific.ImperialMolarVolume
import com.splendo.kaluga.scientific.ImperialMolarity
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMolarMass
import com.splendo.kaluga.scientific.MetricMolarVolume
import com.splendo.kaluga.scientific.MetricMolarity
import com.splendo.kaluga.scientific.MolarMass
import com.splendo.kaluga.scientific.MolarVolume
import com.splendo.kaluga.scientific.Molarity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMolarMass
import com.splendo.kaluga.scientific.UKImperialMolarVolume
import com.splendo.kaluga.scientific.UKImperialMolarity
import com.splendo.kaluga.scientific.USCustomaryMolarMass
import com.splendo.kaluga.scientific.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.USCustomaryMolarity
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.density
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricMolarMassTimesMetricMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, MetricMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("imperialMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("imperialMolarMassTimesUKImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("imperialMolarMassTimesUSCustomaryMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("ukImperialMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("ukImperialMolarMassTimesUKImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("usCustomaryMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("usCustomaryMolarMassTimesUSCustomaryMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("molarMassTimesMolarity")
infix operator fun <MolarMassUnit : MolarMass, MolarityUnit : Molarity> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (Kilogram per CubicMeter).density(this, molarity)

@JvmName("metricMolarMassDivMetricMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, MetricMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("imperialMolarMassDivImperialMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("imperialMolarMassDivUKImperialMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("imperialMolarMassDivUSCustomaryMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("ukImperialMolarMassDivImperialMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("ukImperialMolarMassDivUKImperialMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("usCustomaryMolarMassDivImperialMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("usCustomaryMolarMassDivUSCustomaryMolarVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>) = (unit.weight per molarVolume.unit.volume).density(this, molarVolume)
@JvmName("molarMassDivMolarVolume")
infix operator fun <MolarMassUnit : MolarMass, MolarVolumeUnit : MolarVolume> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.div(molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>) = (Kilogram per CubicMeter).density(this, molarVolume)

