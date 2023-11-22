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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.InchPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.WattHourMultiple
import com.splendo.kaluga.scientific.unit.WattMultiple
import kotlin.jvm.JvmName

@JvmName("wattHourDivWatt")
infix operator fun ScientificValue<PhysicalQuantity.Energy, WattHour>.div(power: ScientificValue<PhysicalQuantity.Power, Watt>) = Hour.time(this, power)

@JvmName("wattHourMultipleDivWatt")
infix operator fun <WattHourUnit : WattHourMultiple> ScientificValue<PhysicalQuantity.Energy, WattHourUnit>.div(power: ScientificValue<PhysicalQuantity.Power, Watt>) =
    Hour.time(this, power)

@JvmName("wattHourDivWattMultiple")
infix operator fun <WattUnit : WattMultiple> ScientificValue<PhysicalQuantity.Energy, WattHour>.div(power: ScientificValue<PhysicalQuantity.Power, WattUnit>) =
    Hour.time(this, power)

@JvmName("wattHourMultipleDivWattMultiple")
infix operator fun <WattHourUnit : WattHourMultiple, WattUnit : WattMultiple> ScientificValue<PhysicalQuantity.Energy, WattHourUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, WattUnit>,
) = Hour.time(this, power)

@JvmName("horsepowerHourDivHorsepower")
infix operator fun ScientificValue<PhysicalQuantity.Energy, HorsepowerHour>.div(power: ScientificValue<PhysicalQuantity.Power, Horsepower>) = Hour.time(this, power)

@JvmName("footPoundForceDivFootPoundForcePerMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundForce>.div(power: ScientificValue<PhysicalQuantity.Power, FootPoundForcePerMinute>) = Minute.time(this, power)

@JvmName("inchPoundForceDivInchPoundForcePerMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(power: ScientificValue<PhysicalQuantity.Power, InchPoundForcePerMinute>) = Minute.time(this, power)

@JvmName("britishThermalUnitDivBritishThermalUnitPerHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, BritishThermalUnit>.div(power: ScientificValue<PhysicalQuantity.Power, BritishThermalUnitPerHour>) =
    Hour.time(this, power)

@JvmName("britishThermalUnitDivBritishThermalUnitPerMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, BritishThermalUnit>.div(power: ScientificValue<PhysicalQuantity.Power, BritishThermalUnitPerMinute>) =
    Minute.time(this, power)

@JvmName("energyDivPower")
infix operator fun <EnergyUnit : Energy, PowerUnit : Power> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(power: ScientificValue<PhysicalQuantity.Power, PowerUnit>) =
    Second.time(this, power)
