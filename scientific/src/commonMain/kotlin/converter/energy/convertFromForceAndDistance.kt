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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("energyFromForceAndDistanceDefault")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length
    > EnergyUnit.energy(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = energy(force, distance, ::DefaultScientificValue)

@JvmName("energyFromForceAndDistance")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length,
    Value : ScientificValue<PhysicalQuantity.Energy, EnergyUnit>
    > EnergyUnit.energy(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    factory: (Decimal, EnergyUnit) -> Value
) = byMultiplying(force, distance, factory)
