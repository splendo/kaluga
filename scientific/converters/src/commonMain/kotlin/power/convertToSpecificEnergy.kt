/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricAndImperialCombinedPower
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialCombinedPowerDivMetricMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>) =
    (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("metricAndImperialCombinedPowerDivImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>) =
    (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("metricAndImperialCombinedPowerDivUKImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>,
) = (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("metricAndImperialCombinedPowerDivUSCustomaryMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>,
) = (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("metricCombinedPowerDivMetricMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>) =
    (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("imperialCombinedPowerDivImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>) =
    (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("imperialCombinedPowerDivUKImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>) =
    (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("imperialCombinedPowerDivUSCustomaryMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>) =
    (unit.energy per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("metricPowerDivMetricMassFlowRate")
infix operator fun <PowerUnit : MetricPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>,
) = (Joule per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("imperialPowerDivImperialMassFlowRate")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>,
) = (FootPoundForce per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("imperialPowerDivUKImperialMassFlowRate")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>,
) = (FootPoundForce per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("imperialPowerDivUSCustomaryMassFlowRate")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>,
) = (FootPoundForce per massFlowRate.unit.weight).specificEnergy(this, massFlowRate)

@JvmName("powerDivMassFlowRate")
infix operator fun <PowerUnit : Power, MassFlowRateUnit : MassFlowRate> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>,
) = (Joule per Kilogram).specificEnergy(this, massFlowRate)
