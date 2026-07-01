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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.magneticFieldStrength.magneticFieldStrength
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialPermeability
import com.splendo.kaluga.scientific.unit.MagneticInduction
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Permeability
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("magneticInductionDivImperialPermeability")
infix operator fun <MagneticInductionUnit : MagneticInduction> ScientificValue<PhysicalQuantity.MagneticInduction, MagneticInductionUnit>.div(
    permeability: ScientificValue<PhysicalQuantity.Permeability, ImperialPermeability>,
) = (Ampere per Foot).magneticFieldStrength(this, permeability)

@JvmName("magneticInductionDivPermeability")
infix operator fun <MagneticInductionUnit, PermeabilityUnit> ScientificValue<PhysicalQuantity.MagneticInduction, MagneticInductionUnit>.div(
    permeability: ScientificValue<PhysicalQuantity.Permeability, PermeabilityUnit>,
) where MagneticInductionUnit : MagneticInduction, PermeabilityUnit : Permeability = (Ampere per Meter).magneticFieldStrength(this, permeability)
