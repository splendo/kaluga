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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("imperialSpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("imperialSpecificEnergyTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("imperialSpecificEnergyTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("ukImperialSpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("ukImperialSpecificEnergyTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("usCustomarySpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("usCustomarySpecificEnergyTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)

@JvmName("specificEnergyTimesWeight")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, WeightUnit : Weight> ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy).energy(this, weight)
