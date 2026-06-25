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
import com.splendo.kaluga.scientific.converter.thermalConductance.thermalConductance
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

@JvmName("metricAndImperialPowerDivMetricAndUKImperialTemperature")
infix operator fun <PowerUnit : MetricAndImperialPower, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = (unit per temperature.unit).thermalConductance(this, temperature)

@JvmName("metricPowerDivMetricAndUKImperialTemperature")
infix operator fun <PowerUnit : MetricPower, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = (unit per temperature.unit).thermalConductance(this, temperature)

@JvmName("imperialPowerDivMetricAndUKImperialTemperature")
infix operator fun <PowerUnit : ImperialPower, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = (unit per temperature.unit).thermalConductance(this, temperature)

@JvmName("metricAndImperialPowerDivUSCustomaryTemperature")
infix operator fun <PowerUnit : MetricAndImperialPower, TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = (unit per temperature.unit).thermalConductance(this, temperature)

@JvmName("imperialPowerDivUSCustomaryTemperature")
infix operator fun <PowerUnit : ImperialPower, TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = (unit per temperature.unit).thermalConductance(this, temperature)

@JvmName("powerDivTemperature")
infix operator fun <PowerUnit : Power, TemperatureUnit : Temperature> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = (Watt per Kelvin).thermalConductance(this, temperature)
