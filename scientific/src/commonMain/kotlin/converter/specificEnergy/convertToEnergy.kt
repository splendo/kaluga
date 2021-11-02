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

import com.splendo.kaluga.scientific.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricSpecificEnergy
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificEnergy
import com.splendo.kaluga.scientific.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.energy.energy
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("imperialSpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("imperialSpecificEnergyTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("imperialSpecificEnergyTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("ukImperialSpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("ukImperialSpecificEnergyTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("usCustomarySpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("usCustomarySpecificEnergyTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("specificEnergyTimesWeight")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, WeightUnit : Weight> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
