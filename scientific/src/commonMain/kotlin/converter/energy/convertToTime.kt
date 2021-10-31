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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.BritishThermalUnit
import com.splendo.kaluga.scientific.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.FootPoundForce
import com.splendo.kaluga.scientific.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.Horsepower
import com.splendo.kaluga.scientific.HorsepowerHour
import com.splendo.kaluga.scientific.Hour
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Minute
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Watt
import com.splendo.kaluga.scientific.WattHour
import com.splendo.kaluga.scientific.converter.time.time
import kotlin.jvm.JvmName

@JvmName("wattHourDivWatt")
infix operator fun ScientificValue<MeasurementType.Energy, WattHour>.div(power: ScientificValue<MeasurementType.Power, Watt>) = Hour.time(this, power)
@JvmName("wattHourMultipleDivWatt")
infix operator fun <WattHourUnit> ScientificValue<MeasurementType.Energy, WattHourUnit>.div(power: ScientificValue<MeasurementType.Power, Watt>) where WattHourUnit : Energy, WattHourUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, WattHour> = Hour.time(this, power)
@JvmName("wattHourDivWattMultiple")
infix operator fun <WattUnit> ScientificValue<MeasurementType.Energy, WattHour>.div(power: ScientificValue<MeasurementType.Power, WattUnit>) where WattUnit : Power, WattUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Power, Watt> = Hour.time(this, power)
@JvmName("wattHourMultipleDivWattMultiple")
infix operator fun <WattHourUnit, WattUnit> ScientificValue<MeasurementType.Energy, WattHourUnit>.div(power: ScientificValue<MeasurementType.Power, WattUnit>) where WattHourUnit : Energy, WattHourUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, WattHour>, WattUnit : Power, WattUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Power, Watt> = Hour.time(this, power)
@JvmName("horepowerHourDivHorsepower")
infix operator fun ScientificValue<MeasurementType.Energy, HorsepowerHour>.div(power: ScientificValue<MeasurementType.Power, Horsepower>) = Hour.time(this, power)
@JvmName("footPountForceDivFootPoundForcePerMinute")
infix operator fun ScientificValue<MeasurementType.Energy, FootPoundForce>.div(power: ScientificValue<MeasurementType.Power, FootPoundForcePerMinute>) = Minute.time(this, power)
@JvmName("britishThermalUnitDivBritishThermalUnitPerHour")
infix operator fun ScientificValue<MeasurementType.Energy, BritishThermalUnit>.div(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerHour>) = Hour.time(this, power)
@JvmName("britishThermalUnitDivBritishThermalUnitPerMinute")
infix operator fun ScientificValue<MeasurementType.Energy, BritishThermalUnit>.div(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerMinute>) = Minute.time(this, power)
@JvmName("britishThermalUnitDivBritishThermalUnitPerSecond")
infix operator fun ScientificValue<MeasurementType.Energy, BritishThermalUnit>.div(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerSecond>) = Second.time(this, power)
@JvmName("energyDivPower")
infix operator fun <EnergyUnit : Energy, PowerUnit : Power> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(power: ScientificValue<MeasurementType.Power, PowerUnit>) = Second.time(this, power)
