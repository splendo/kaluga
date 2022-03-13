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

package com.splendo.kaluga.scientific.converter.temperature

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.thermalResistance.thermalResistance
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialTemperatureDivMetricAndImperialPower")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, MetricAndImperialPower>
) = (unit per power.unit).thermalResistance(this, power)

@JvmName("metricAndUKImperialTemperatureDivMetricPower")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, MetricPower>
) = (unit per power.unit).thermalResistance(this, power)

@JvmName("metricAndUKImperialTemperatureDivImperialPower")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, ImperialPower>
) = (unit per power.unit).thermalResistance(this, power)

@JvmName("usCustomaryTemperatureDivMetricAndImperialPower")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, MetricAndImperialPower>
) = (unit per power.unit).thermalResistance(this, power)

@JvmName("usCustomaryTemperatureDivImperialPower")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, ImperialPower>
) = (unit per power.unit).thermalResistance(this, power)

@JvmName("temperatureDivPower")
infix operator fun <TemperatureUnit : Temperature, PowerUnit : Power> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.div(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>
) = (Kelvin per Watt).thermalResistance(this, power)
