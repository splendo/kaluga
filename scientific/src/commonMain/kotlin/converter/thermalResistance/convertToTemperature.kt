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

package com.splendo.kaluga.scientific.converter.thermalResistance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.temperature.temperature
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.MetricThermalResistance
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.ThermalResistance
import com.splendo.kaluga.scientific.unit.UKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.USCustomaryThermalResistance
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialThermalResistanceTimesPower")
infix operator fun <PowerUnit : Power> ScientificValue<PhysicalQuantity.ThermalResistance, MetricAndUKImperialThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("metricThermalResistanceTimesMetricAndImperialPower")
infix operator fun <PowerUnit : MetricAndImperialPower> ScientificValue<PhysicalQuantity.ThermalResistance, MetricThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("metricThermalResistanceTimesMetricPower")
infix operator fun <PowerUnit : MetricPower> ScientificValue<PhysicalQuantity.ThermalResistance, MetricThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("ukImperialThermalResistanceTimesMetricAndImperialPower")
infix operator fun <PowerUnit : MetricAndImperialPower> ScientificValue<PhysicalQuantity.ThermalResistance, UKImperialThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("ukImperialThermalResistanceTimesImperialPower")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.ThermalResistance, UKImperialThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("usCustomaryThermalResistanceTimesMetricAndImperialPower")
infix operator fun <PowerUnit : MetricAndImperialPower> ScientificValue<PhysicalQuantity.ThermalResistance, USCustomaryThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("usCustomaryThermalResistanceTimesImperialPower")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.ThermalResistance, USCustomaryThermalResistance>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = unit.temperature.temperature(this, power)

@JvmName("thermalResistanceTimesPower")
infix operator fun <ThermalResistanceUnit : ThermalResistance, PowerUnit : Power> ScientificValue<PhysicalQuantity.ThermalResistance, ThermalResistanceUnit>.times(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = Kelvin.temperature(this, power)
