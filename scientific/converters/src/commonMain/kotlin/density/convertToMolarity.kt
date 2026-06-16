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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarity.molarity
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>) =
    molality * this

@JvmName("imperialDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>) =
    molality * this

@JvmName("ukImperialDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = molality * this

@JvmName("usCustomaryDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = molality * this

@JvmName("densityTimesMolality")
infix operator fun <MolalityUnit : Molality, DensityUnit : Density> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = molality * this

@JvmName("metricDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.Density, MetricDensity>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (molarMass.unit.per per unit.per).molarity(this, molarMass)

@JvmName("imperialDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (molarMass.unit.per per unit.per).molarity(this, molarMass)

@JvmName("ukImperialDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (molarMass.unit.per per unit.per).molarity(this, molarMass)

@JvmName("usCustomaryDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (molarMass.unit.per per unit.per).molarity(this, molarMass)

@JvmName("densityDivMolarMass")
infix operator fun <DensityUnit : Density, MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.Density, DensityUnit>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (molarMass.unit.per per CubicMeter).molarity(this, molarMass)
