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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.speed.speed
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("ergPerSecondDivDyne")
infix operator fun ScientificValue<MeasurementType.Power, ErgPerSecond>.div(force: ScientificValue<MeasurementType.Force, Dyne>) = (Centimeter per Second).speed(this, force)
@JvmName("ergPerSecondDivDyneMultiple")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Power, ErgPerSecond>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : Force, DyneUnit :MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = (Centimeter per Second).speed(this, force)
@JvmName("metricPowerDivMetricForce")
infix operator fun <PowerUnit : MetricPower, ForceUnit : Force> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Meter per Second).speed(this, force)
@JvmName("footPoundForcePerSecondDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Power, FootPoundForcePerSecond>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("footPoundForcePerMinuteDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Power, FootPoundForcePerMinute>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Minute).speed(this, force)
@JvmName("britishThermalUnitPerSecondDivPoundForce")
infix operator fun ScientificValue<MeasurementType.Power, BritishThermalUnitPerSecond>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = (Foot per Second).speed(this, force)
@JvmName("britishThermalUnitPerMinuteDivPoundForce")
infix operator fun ScientificValue<MeasurementType.Power, BritishThermalUnitPerMinute>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = (Foot per Minute).speed(this, force)
@JvmName("britishThermalUnitPerHourDivPoundForce")
infix operator fun ScientificValue<MeasurementType.Power, BritishThermalUnitPerHour>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = (Foot per Hour).speed(this, force)
@JvmName("imperialPowerDivImperialForce")
infix operator fun <PowerUnit : ImperialPower, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("imperialPowerDivUKImperialForce")
infix operator fun <PowerUnit : ImperialPower, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("imperialPowerDivUSCustomaryForce")
infix operator fun <PowerUnit : ImperialPower, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("metricAndImperialPowerDivImperialForce")
infix operator fun <PowerUnit : MetricAndImperialPower, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("metricAndImperialPowerDivUKImperialForce")
infix operator fun <PowerUnit : MetricAndImperialPower, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("metricAndImperialPowerDivUSCustomaryForce")
infix operator fun <PowerUnit : MetricAndImperialPower, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Foot per Second).speed(this, force)
@JvmName("powerDivForce")
infix operator fun <PowerUnit : Power, ForceUnit : Force> ScientificValue<MeasurementType.Power, PowerUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = (Meter per Second).speed(this, force)
