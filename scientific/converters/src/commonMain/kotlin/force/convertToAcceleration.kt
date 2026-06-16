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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.acceleration.acceleration
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("dyneDivGram")
infix operator fun ScientificValue<PhysicalQuantity.Force, Dyne>.div(mass: ScientificValue<PhysicalQuantity.Weight, Gram>) =
    (Centimeter per Second per Second).acceleration(this, mass)

@JvmName("dyneMultipleDivGram")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Force, DyneUnit>.div(mass: ScientificValue<PhysicalQuantity.Weight, Gram>) =
    (Centimeter per Second per Second).acceleration(this, mass)

@JvmName("imperialForceDivImperialWeight")
infix operator fun <ForceUnit : ImperialForce, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("imperialForceDivUKImperialWeight")
infix operator fun <ForceUnit : ImperialForce, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("imperialForceDivUSCustomaryWeight")
infix operator fun <ForceUnit : ImperialForce, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("ukImperialForceDivImperialWeight")
infix operator fun <ForceUnit : UKImperialForce, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("ukImperialForceDivUKImperialWeight")
infix operator fun <ForceUnit : UKImperialForce, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("usCustomaryForceDivImperialWeight")
infix operator fun <ForceUnit : USCustomaryForce, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("usCustomaryForceDivUSCustomaryWeight")
infix operator fun <ForceUnit : USCustomaryForce, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("forceDivWeight")
infix operator fun <ForceUnit : Force, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    (Meter per Second per Second).acceleration(this, mass)
