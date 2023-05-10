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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.temperature.temperature
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivMetricAndUKImperialHeatCapacity")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, MetricAndUKImperialHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("metricAndImperialEnergyDivMetricHeatCapacity")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, MetricHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("metricAndImperialEnergyDivUKImperialHeatCapacity")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, UKImperialHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("metricAndImperialEnergyDivUSCustomaryHeatCapacity")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, USCustomaryHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("metricEnergyDivMetricHeatCapacity")
infix operator fun <EnergyUnit : MetricEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, MetricHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("imperialEnergyDivMetricAndImperialHeatCapacity")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, MetricAndUKImperialHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("imperialEnergyDivUKImperialHeatCapacity")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, UKImperialHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("imperialEnergyDivUSCustomaryHeatCapacity")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, USCustomaryHeatCapacity>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)

@JvmName("energyDivHeatCapacity")
infix operator fun <EnergyUnit : Energy, HeatCapacityUnit : HeatCapacity> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>,
) = heatCapacity.unit.per.temperature(this, heatCapacity)
