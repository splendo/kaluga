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

package com.splendo.kaluga.scientific.converter.magneticInduction

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.MagneticFieldStrength
import com.splendo.kaluga.scientific.unit.MagneticInduction
import com.splendo.kaluga.scientific.unit.Permeability
import kotlin.jvm.JvmName

@JvmName("inductionFromPermeabilityAndMagneticFieldStrengthDefault")
fun <
    MagneticInductionUnit : MagneticInduction,
    PermeabilityUnit : Permeability,
    MagneticFieldStrengthUnit : MagneticFieldStrength,
    > MagneticInductionUnit.induction(
    permeability: ScientificValue<PhysicalQuantity.Permeability, PermeabilityUnit>,
    magneticFieldStrength: ScientificValue<PhysicalQuantity.MagneticFieldStrength, MagneticFieldStrengthUnit>,
) = induction(permeability, magneticFieldStrength, ::DefaultScientificValue)

@JvmName("inductionFromPermeabilityAndMagneticFieldStrength")
fun <
    MagneticInductionUnit : MagneticInduction,
    PermeabilityUnit : Permeability,
    MagneticFieldStrengthUnit : MagneticFieldStrength,
    Value : ScientificValue<PhysicalQuantity.MagneticInduction, MagneticInductionUnit>,
    > MagneticInductionUnit.induction(
    permeability: ScientificValue<PhysicalQuantity.Permeability, PermeabilityUnit>,
    magneticFieldStrength: ScientificValue<PhysicalQuantity.MagneticFieldStrength, MagneticFieldStrengthUnit>,
    factory: (Decimal, MagneticInductionUnit) -> Value,
) = byMultiplying(permeability, magneticFieldStrength, factory)
