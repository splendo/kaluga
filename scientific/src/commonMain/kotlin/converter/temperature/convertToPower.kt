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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.MetricThermalResistance
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.ThermalResistance
import com.splendo.kaluga.scientific.unit.UKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import com.splendo.kaluga.scientific.unit.USCustomaryThermalResistance
import com.splendo.kaluga.scientific.unit.Watt
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialTemperatureDivMetricAndUKImperialThermalResistance")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.div(thermalResistance: ScientificValue<MeasurementType.ThermalResistance, MetricAndUKImperialThermalResistance>) = thermalResistance.unit.per.power(this, thermalResistance)
@JvmName("metricAndUKImperialTemperatureDivMetricThermalResistance")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.div(thermalResistance: ScientificValue<MeasurementType.ThermalResistance, MetricThermalResistance>) = thermalResistance.unit.per.power(this, thermalResistance)
@JvmName("metricAndUKImperialTemperatureDivUKImperialThermalResistance")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.div(thermalResistance: ScientificValue<MeasurementType.ThermalResistance, UKImperialThermalResistance>) = thermalResistance.unit.per.power(this, thermalResistance)
@JvmName("usCustomaryTemperatureDivUSCustomaryThermalResistance")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.div(thermalResistance: ScientificValue<MeasurementType.ThermalResistance, USCustomaryThermalResistance>) = thermalResistance.unit.per.power(this, thermalResistance)
@JvmName("temperatureDivThermalResistance")
infix operator fun <TemperatureUnit : Temperature, ThermalResistanceUnit : ThermalResistance> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.div(thermalResistance: ScientificValue<MeasurementType.ThermalResistance, ThermalResistanceUnit>) = Watt.power(this, thermalResistance)
