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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("lengthFromEnergyAndForceDefault")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length,
    > LengthUnit.distance(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = distance(energy, force, ::DefaultScientificValue)

@JvmName("lengthFromEnergyAndForce")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length,
    Value : ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    > LengthUnit.distance(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    factory: (Decimal, LengthUnit) -> Value,
) = byDividing(energy, force, factory)
