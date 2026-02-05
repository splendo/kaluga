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
import com.splendo.kaluga.scientific.converter.massFlowRate.massFlowRate
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndImperialCombinedPower
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialCombinedPowerDivMetricSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>,
) = (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("metricAndImperialCombinedPowerDivImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("metricAndImperialCombinedPowerDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("metricAndImperialCombinedPowerDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("metricCombinedPowerDivMetricSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>) =
    (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("imperialCombinedPowerDivImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>) =
    (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("imperialCombinedPowerDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>) =
    (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("imperialCombinedPowerDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>) =
    (specificEnergy.unit.per per unit.per).massFlowRate(this, specificEnergy)

@JvmName("metricPowerDivMetricSpecificEnergy")
infix operator fun <PowerUnit : MetricPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>,
) = (specificEnergy.unit.per per Second).massFlowRate(this, specificEnergy)

@JvmName("imperialPowerDivImperialSpecificEnergy")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = (specificEnergy.unit.per per Second).massFlowRate(this, specificEnergy)

@JvmName("imperialPowerDivUKImperialSpecificEnergy")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = (specificEnergy.unit.per per Second).massFlowRate(this, specificEnergy)

@JvmName("imperialPowerDivUSCustomarySpecificEnergy")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = (specificEnergy.unit.per per Second).massFlowRate(this, specificEnergy)

@JvmName("powerDivSpecificEnergy")
infix operator fun <PowerUnit : Power, SpecificEnergyUnit : SpecificEnergy> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
) = (Kilogram per Second).massFlowRate(this, specificEnergy)
