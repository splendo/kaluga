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
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivMetricWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("metricAndImperialEnergyDivImperialWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("metricAndImperialEnergyDivUKImperialWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("metricAndImperialEnergyDivUSCustomaryWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("metricEnergyDivMetricWeight")
infix operator fun <EnergyUnit : MetricEnergy, WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("imperialEnergyDivImperialWeight")
infix operator fun <EnergyUnit : ImperialEnergy, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("imperialEnergyDivUKImperialWeight")
infix operator fun <EnergyUnit : ImperialEnergy, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("imperialEnergyDivUSCustomaryWeight")
infix operator fun <EnergyUnit : ImperialEnergy, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit per weight.unit).specificEnergy(this, weight)

@JvmName("energyDivWeight")
infix operator fun <EnergyUnit : Energy, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (Joule per Kilogram).specificEnergy(this, weight)
