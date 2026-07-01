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
import com.splendo.kaluga.scientific.converter.electricFieldStrength.electricFieldStrength
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialForceDivElectricCharge")
infix operator fun <ForceUnit, ChargeUnit> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) where ForceUnit : ImperialForce, ChargeUnit : ElectricCharge = (Volt per Foot).electricFieldStrength(this, charge)

@JvmName("ukImperialForceDivElectricCharge")
infix operator fun <ForceUnit, ChargeUnit> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) where ForceUnit : UKImperialForce, ChargeUnit : ElectricCharge = (Volt per Foot).electricFieldStrength(this, charge)

@JvmName("usCustomaryForceDivElectricCharge")
infix operator fun <ForceUnit, ChargeUnit> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) where ForceUnit : USCustomaryForce, ChargeUnit : ElectricCharge = (Volt per Foot).electricFieldStrength(this, charge)

@JvmName("forceDivElectricCharge")
infix operator fun <ForceUnit : Force, ChargeUnit : ElectricCharge> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) = (Volt per Meter).electricFieldStrength(this, charge)
