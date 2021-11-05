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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import kotlin.jvm.JvmName

@JvmName("wattHourDivWatt")
infix operator fun ScientificValue<MeasurementType.Energy, WattHour>.div(power: ScientificValue<MeasurementType.Power, Watt>) =
    Hour.time(this, power)

@JvmName("wattHourMultipleDivWatt")
infix operator fun <WattHourUnit> ScientificValue<MeasurementType.Energy, WattHourUnit>.div(power: ScientificValue<MeasurementType.Power, Watt>) where WattHourUnit : Energy, WattHourUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, WattHour> =
    Hour.time(this, power)

@JvmName("wattHourDivWattMultiple")
infix operator fun <WattUnit> ScientificValue<MeasurementType.Energy, WattHour>.div(power: ScientificValue<MeasurementType.Power, WattUnit>) where WattUnit : Power, WattUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Power, Watt> =
    Hour.time(this, power)

@JvmName("wattHourMultipleDivWattMultiple")
infix operator fun <WattHourUnit, WattUnit> ScientificValue<MeasurementType.Energy, WattHourUnit>.div(
    power: ScientificValue<MeasurementType.Power, WattUnit>
) where WattHourUnit : Energy, WattHourUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, WattHour>, WattUnit : Power, WattUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Power, Watt> =
    Hour.time(this, power)

@JvmName("horepowerHourDivHorsepower")
infix operator fun ScientificValue<MeasurementType.Energy, HorsepowerHour>.div(power: ScientificValue<MeasurementType.Power, Horsepower>) =
    Hour.time(this, power)

@JvmName("footPountForceDivFootPoundForcePerMinute")
infix operator fun ScientificValue<MeasurementType.Energy, FootPoundForce>.div(power: ScientificValue<MeasurementType.Power, FootPoundForcePerMinute>) =
    Minute.time(this, power)

@JvmName("britishThermalUnitDivBritishThermalUnitPerHour")
infix operator fun ScientificValue<MeasurementType.Energy, BritishThermalUnit>.div(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerHour>) =
    Hour.time(this, power)

@JvmName("britishThermalUnitDivBritishThermalUnitPerMinute")
infix operator fun ScientificValue<MeasurementType.Energy, BritishThermalUnit>.div(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerMinute>) =
    Minute.time(this, power)

@JvmName("britishThermalUnitDivBritishThermalUnitPerSecond")
infix operator fun ScientificValue<MeasurementType.Energy, BritishThermalUnit>.div(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerSecond>) =
    Second.time(this, power)

@JvmName("energyDivPower")
infix operator fun <EnergyUnit : Energy, PowerUnit : Power> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    power: ScientificValue<MeasurementType.Power, PowerUnit>
) = Second.time(this, power)
