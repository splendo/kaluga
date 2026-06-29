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

package com.splendo.kaluga.scientific.converter.massFlowRate

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.ImperialNamedEnergyUnit
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.MetricNamedEnergyUnit
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMassFlowRateTimesMetricSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is MetricNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (Joule per unit.per)
}.power(this, specificEnergy)

@JvmName("imperialMassFlowRateTimesImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("imperialMassFlowRateTimesUKImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("imperialMassFlowRateTimesUSCustomarySpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("ukImperialMassFlowRateTimesImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("ukImperialMassFlowRateTimesUKImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("usCustomaryMassFlowRateTimesImperialSpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("usCustomaryMassFlowRateTimesUSCustomarySpecificEnergy")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = when (val energyUnit = specificEnergy.unit.energy) {
    is ImperialNamedEnergyUnit -> (energyUnit per unit.per)
    else -> (FootPoundForce per unit.per)
}.power(this, specificEnergy)

@JvmName("massFlowRateTimesSpecificEnergy")
infix operator fun <MassFlowRateUnit : MassFlowRate, SpecificEnergyUnit : SpecificEnergy> ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
) = Watt.power(this, specificEnergy)
