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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.molarMass
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialMolarVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricMolarVolume
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialMolarVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Density, MetricDensity>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)

@JvmName("imperialDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)

@JvmName("ukImperialDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)

@JvmName("usCustomaryDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)

@JvmName("densityDivMolarity")
infix operator fun <DensityUnit : Density, MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Density, DensityUnit>.div(
    molarity: ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>
) = (Kilogram per molarity.unit.amountOfSubstance).molarMass(this, molarity)

@JvmName("metricDensityTimesMetricMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>) =
    molarVolume * this

@JvmName("imperialDensityTimesImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>) =
    molarVolume * this

@JvmName("imperialDensityTimesUKImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>) =
    molarVolume * this

@JvmName("imperialDensityTimesUSCustomaryMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>) =
    molarVolume * this

@JvmName("ukImperialDensityTimesImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>) =
    molarVolume * this

@JvmName("ukImperialDensityTimesUKImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>) =
    molarVolume * this

@JvmName("usCustomaryDensityTimesImperialMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>) =
    molarVolume * this

@JvmName("usCustomaryDensityTimesUSCustomaryMolarVolume")
infix operator fun ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>) =
    molarVolume * this

@JvmName("densityTimesMolarVolume")
infix operator fun <DensityUnit : Density, MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    molarVolume: ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>
) = molarVolume * this
