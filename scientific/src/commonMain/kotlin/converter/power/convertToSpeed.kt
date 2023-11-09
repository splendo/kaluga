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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.speed.speed
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.InchPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
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
infix operator fun ScientificValue<PhysicalQuantity.Power, ErgPerSecond>.div(force: ScientificValue<PhysicalQuantity.Force, Dyne>) = (Centimeter per Second).speed(this, force)

@JvmName("ergPerSecondDivDyneMultiple")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Power, ErgPerSecond>.div(force: ScientificValue<PhysicalQuantity.Force, DyneUnit>) =
    (Centimeter per Second).speed(this, force)

@JvmName("metricPowerDivMetricForce")
infix operator fun <PowerUnit : MetricPower, ForceUnit : Force> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Meter per Second).speed(this, force)

@JvmName("footPoundForcePerSecondDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, FootPoundForcePerSecond>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Foot per Second).speed(this, force)

@JvmName("footPoundForcePerMinuteDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, FootPoundForcePerMinute>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Foot per Minute).speed(this, force)

@JvmName("inchPoundForcePerSecondDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, InchPoundForcePerSecond>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Inch per Second).speed(this, force)

@JvmName("inchPoundForcePerMinuteDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, InchPoundForcePerMinute>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Inch per Minute).speed(this, force)

@JvmName("britishThermalUnitPerSecondDivPoundForce")
infix operator fun ScientificValue<PhysicalQuantity.Power, BritishThermalUnitPerSecond>.div(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) =
    (Foot per Second).speed(this, force)

@JvmName("britishThermalUnitPerMinuteDivPoundForce")
infix operator fun ScientificValue<PhysicalQuantity.Power, BritishThermalUnitPerMinute>.div(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) =
    (Foot per Minute).speed(this, force)

@JvmName("britishThermalUnitPerHourDivPoundForce")
infix operator fun ScientificValue<PhysicalQuantity.Power, BritishThermalUnitPerHour>.div(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) =
    (Foot per Hour).speed(this, force)

@JvmName("imperialPowerDivImperialForce")
infix operator fun <PowerUnit : ImperialPower, ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = (Foot per Second).speed(this, force)

@JvmName("imperialPowerDivUKImperialForce")
infix operator fun <PowerUnit : ImperialPower, ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = (Foot per Second).speed(this, force)

@JvmName("imperialPowerDivUSCustomaryForce")
infix operator fun <PowerUnit : ImperialPower, ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = (Foot per Second).speed(this, force)

@JvmName("metricAndImperialPowerDivImperialForce")
infix operator fun <PowerUnit : MetricAndImperialPower, ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = (Foot per Second).speed(this, force)

@JvmName("metricAndImperialPowerDivUKImperialForce")
infix operator fun <PowerUnit : MetricAndImperialPower, ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = (Foot per Second).speed(this, force)

@JvmName("metricAndImperialPowerDivUSCustomaryForce")
infix operator fun <PowerUnit : MetricAndImperialPower, ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = (Foot per Second).speed(this, force)

@JvmName("powerDivForce")
infix operator fun <PowerUnit : Power, ForceUnit : Force> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Meter per Second).speed(this, force)
