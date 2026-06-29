/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.converter.massFlowRate.times
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyTimesMetricMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>.times(massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>) =
    massFlowRate * this

@JvmName("imperialSpecificEnergyTimesImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>,
) = massFlowRate * this

@JvmName("ukImperialSpecificEnergyTimesImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>,
) = massFlowRate * this

@JvmName("usCustomarySpecificEnergyTimesImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>,
) = massFlowRate * this

@JvmName("imperialSpecificEnergyTimesUKImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>,
) = massFlowRate * this

@JvmName("ukImperialSpecificEnergyTimesUKImperialMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>,
) = massFlowRate * this

@JvmName("imperialSpecificEnergyTimesUSCustomaryMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>,
) = massFlowRate * this

@JvmName("usCustomarySpecificEnergyTimesUSCustomaryMassFlowRate")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>,
) = massFlowRate * this

@JvmName("sSpecificEnergyTimesMassFlowRate")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MassFlowRateUnit : MassFlowRate> ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>.times(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>,
) = massFlowRate * this
