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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.temperature.temperature
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificHeatCapacity
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyDivMetricSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, MetricSpecificHeatCapacity>,
) = specificHeatCapacity.unit.heatCapacity.per.temperature(this, specificHeatCapacity)

@JvmName("imperialSpecificEnergyDivUKImperialSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>,
) = specificHeatCapacity.unit.heatCapacity.per.temperature(this, specificHeatCapacity)

@JvmName("imperialSpecificEnergyDivUSCustomarySpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>,
) = specificHeatCapacity.unit.heatCapacity.per.temperature(this, specificHeatCapacity)

@JvmName("ukImperialSpecificEnergyDivUKImperialSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>,
) = specificHeatCapacity.unit.heatCapacity.per.temperature(this, specificHeatCapacity)

@JvmName("usCustomarySpecificEnergyDivUSCustomarySpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>,
) = specificHeatCapacity.unit.heatCapacity.per.temperature(this, specificHeatCapacity)

@JvmName("specificEnergyDivTemperature")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, SpecificHeatCapacityUnit : SpecificHeatCapacity> ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>,
) = Kelvin.temperature(this, specificHeatCapacity)
