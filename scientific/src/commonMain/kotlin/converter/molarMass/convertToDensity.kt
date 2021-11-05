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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.ImperialMolarVolume
import com.splendo.kaluga.scientific.unit.ImperialMolarity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MetricMolarVolume
import com.splendo.kaluga.scientific.unit.MetricMolarity
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolarVolume
import com.splendo.kaluga.scientific.unit.UKImperialMolarity
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.unit.USCustomaryMolarity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMolarMassTimesMetricMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, MetricMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("imperialMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("imperialMolarMassTimesUKImperialMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("imperialMolarMassTimesUSCustomaryMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("ukImperialMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("ukImperialMolarMassTimesUKImperialMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("usCustomaryMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("usCustomaryMolarMassTimesUSCustomaryMolarity")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>.times(molarity: ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>) =
    (unit.weight per molarity.unit.per).density(this, molarity)

@JvmName("molarMassTimesMolarity")
infix operator fun <MolarMassUnit : MolarMass, MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.times(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = (Kilogram per CubicMeter).density(this, molarity)

@JvmName("metricMolarMassDivMetricMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("imperialMolarMassDivImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("imperialMolarMassDivUKImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("imperialMolarMassDivUSCustomaryMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("ukImperialMolarMassDivImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("ukImperialMolarMassDivUKImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("usCustomaryMolarMassDivImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("usCustomaryMolarMassDivUSCustomaryMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>.div(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>) =
    (unit.weight per molarVolume.unit.volume).density(this, molarVolume)

@JvmName("molarMassDivMolarVolume")
infix operator fun <MolarMassUnit : MolarMass, MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.div(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>
) = (Kilogram per CubicMeter).density(this, molarVolume)
