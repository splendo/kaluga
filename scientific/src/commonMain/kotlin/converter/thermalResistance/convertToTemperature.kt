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

package com.splendo.kaluga.scientific.converter.thermalResistance

import com.splendo.kaluga.scientific.ImperialPower
import com.splendo.kaluga.scientific.Kelvin
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndImperialPower
import com.splendo.kaluga.scientific.MetricAndUKImperialThermalResistance
import com.splendo.kaluga.scientific.MetricPower
import com.splendo.kaluga.scientific.MetricThermalResistance
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.ThermalResistance
import com.splendo.kaluga.scientific.UKImperialThermalResistance
import com.splendo.kaluga.scientific.USCustomaryThermalResistance
import com.splendo.kaluga.scientific.converter.temperature.temperature
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialThermalResistanceTimesPower")
infix operator fun <PowerUnit : Power> ScientificValue<MeasurementType.ThermalResistance, MetricAndUKImperialThermalResistance>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = unit.temperature.temperature(this, power)
@JvmName("metricThermalResistanceTimesMetricAndImperialPower")
infix operator fun ScientificValue<MeasurementType.ThermalResistance, MetricThermalResistance>.times(power: ScientificValue<MeasurementType.Power, MetricAndImperialPower>) = unit.temperature.temperature(this, power)
@JvmName("metricThermalResistanceTimesMetricPower")
infix operator fun ScientificValue<MeasurementType.ThermalResistance, MetricThermalResistance>.times(power: ScientificValue<MeasurementType.Power, MetricPower>) = unit.temperature.temperature(this, power)
@JvmName("ukImperialThermalResistanceTimesMetricAndImperialPower")
infix operator fun ScientificValue<MeasurementType.ThermalResistance, UKImperialThermalResistance>.times(power: ScientificValue<MeasurementType.Power, MetricAndImperialPower>) = unit.temperature.temperature(this, power)
@JvmName("ukImperialThermalResistanceTimesImperialPower")
infix operator fun ScientificValue<MeasurementType.ThermalResistance, UKImperialThermalResistance>.times(power: ScientificValue<MeasurementType.Power, ImperialPower>) = unit.temperature.temperature(this, power)
@JvmName("usCustomaryThermalResistanceTimesMetricAndImperialPower")
infix operator fun ScientificValue<MeasurementType.ThermalResistance, USCustomaryThermalResistance>.times(power: ScientificValue<MeasurementType.Power, MetricAndImperialPower>) = unit.temperature.temperature(this, power)
@JvmName("usCustomaryThermalResistanceTimesImperialPower")
infix operator fun ScientificValue<MeasurementType.ThermalResistance, USCustomaryThermalResistance>.times(power: ScientificValue<MeasurementType.Power, ImperialPower>) = unit.temperature.temperature(this, power)
@JvmName("thermalResistanceTimesPower")
infix operator fun <ThermalResistanceUnit : ThermalResistance, PowerUnit : Power> ScientificValue<MeasurementType.ThermalResistance, ThermalResistanceUnit>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = Kelvin.temperature(this, power)
