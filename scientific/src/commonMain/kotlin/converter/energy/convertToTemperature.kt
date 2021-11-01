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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.HeatCapacity
import com.splendo.kaluga.scientific.ImperialEnergy
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.MetricEnergy
import com.splendo.kaluga.scientific.MetricHeatCapacity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.converter.temperature.temperature
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivMetricAndUKImperialHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("metricAndImperialEnergyDivMetricHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("metricAndImperialEnergyDivUKImperialHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("metricEnergyDivMetricHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("ukImperialEnergyDivUKImperialHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("usCustomaryEnergyDivUSCustomaryHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("energyDivHeatCapacity")
infix operator fun <EnergyUnit : Energy, HeatCapacityUnit : HeatCapacity> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>) = heatCapacity.unit.per.temperature(this, heatCapacity)
