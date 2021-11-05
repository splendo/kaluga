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
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMolarVolume
import com.splendo.kaluga.scientific.unit.ImperialMolarity
import com.splendo.kaluga.scientific.unit.MetricMolarVolume
import com.splendo.kaluga.scientific.unit.MetricMolarity
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.UKImperialMolarVolume
import com.splendo.kaluga.scientific.unit.UKImperialMolarity
import com.splendo.kaluga.scientific.unit.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.unit.USCustomaryMolarity
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceDivMetricMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MetricMolarity>
) = molarity.unit.per.volume(this, molarity)

@JvmName("amountOfSubstanceDivImperialMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>
) = molarity.unit.per.volume(this, molarity)

@JvmName("amountOfSubstanceDivUKImperialMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>
) = molarity.unit.per.volume(this, molarity)

@JvmName("amountOfSubstanceDivUSCustomaryMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>
) = molarity.unit.per.volume(this, molarity)

@JvmName("amountOfSubstanceDivMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = CubicMeter.volume(this, molarity)

@JvmName("amountOfSubstanceTimesMetricMolarVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>
) = molarVolume * this

@JvmName("amountOfSubstanceTimesImperialMolarVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>
) = molarVolume * this

@JvmName("amountOfSubstanceTimesUKImperialMolarVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>
) = molarVolume * this

@JvmName("amountOfSubstanceTimesUSCustomaryMolarVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>
) = molarVolume * this

@JvmName("amountOfSubstanceTimesMolarVolume")
infix operator fun <MolarVolumeUnit : MolarVolume, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>
) = molarVolume * this
