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
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricCombinedPowerDivDyne")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(force: ScientificValue<PhysicalQuantity.Force, Dyne>) = (
    when (unit.energy) {
        is Erg -> Centimeter
        is ErgMultiple -> Centimeter
        else -> Meter
    } per unit.per
    ).speed(this, force)

@JvmName("metricCombinedPowerDivDyneMultiple")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(force: ScientificValue<PhysicalQuantity.Force, DyneUnit>) = (
    when (unit.energy) {
        is Erg -> Centimeter
        is ErgMultiple -> Centimeter
        else -> Meter
    } per unit.per
    ).speed(this, force)

@JvmName("metricPowerDivMetricForce")
infix operator fun <PowerUnit : MetricPower, ForceUnit : Force> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    (Meter per Second).speed(this, force)

@JvmName("imperialCombinedPowerDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) = (
    when (unit.energy) {
        FootPoundForce -> Foot
        FootPoundal -> Foot
        InchPoundForce -> Inch
        InchOunceForce -> Inch
        BritishThermalUnit -> Foot
        BritishThermalUnit.Thermal -> Foot
    } per unit.per
    ).speed(this, force)

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
