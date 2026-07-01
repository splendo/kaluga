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

package com.splendo.kaluga.scientific.converter.electricFieldStrength

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialElectricFieldStrength
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import kotlin.jvm.JvmName

@JvmName("imperialElectricFieldStrengthTimesElectricCharge")
infix operator fun <ChargeUnit : ElectricCharge> ScientificValue<PhysicalQuantity.ElectricFieldStrength, ImperialElectricFieldStrength>.times(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) = PoundForce.force(this, charge)

@JvmName("electricFieldStrengthTimesElectricCharge")
infix operator fun <ElectricFieldStrengthUnit, ChargeUnit> ScientificValue<PhysicalQuantity.ElectricFieldStrength, ElectricFieldStrengthUnit>.times(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) where ElectricFieldStrengthUnit : ElectricFieldStrength, ChargeUnit : ElectricCharge = Newton.force(this, charge)
