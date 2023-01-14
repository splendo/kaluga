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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val MetricAndImperialMolarEnergyUnits: Set<MetricAndImperialMolarEnergy> get() = MetricAndImperialEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

val MetricMolarEnergyUnits: Set<MetricMolarEnergy> get() = MetricEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

val ImperialMolarEnergyUnits: Set<ImperialMolarEnergy> get() = ImperialEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

val MolarEnergyUnits: Set<MolarEnergy> get() = MetricAndImperialMolarEnergyUnits +
    MetricMolarEnergyUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    ImperialMolarEnergyUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet()

@Serializable
sealed class MolarEnergy : AbstractScientificUnit<PhysicalQuantity.MolarEnergy>() {
    abstract val energy: Energy
    abstract val per: AmountOfSubstance
    override val quantity = PhysicalQuantity.MolarEnergy
    override val symbol: String by lazy { "${energy.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricAndImperialMolarEnergy(override val energy: MetricAndImperialEnergy, override val per: AmountOfSubstance) : MolarEnergy(), MetricAndImperialScientificUnit<PhysicalQuantity.MolarEnergy> {
    override val system = MeasurementSystem.MetricAndImperial
    val metric get() = energy.metric per per
    val imperial get() = energy.imperial per per
}
@Serializable
data class MetricMolarEnergy(override val energy: MetricEnergy, override val per: AmountOfSubstance) : MolarEnergy(), MetricScientificUnit<PhysicalQuantity.MolarEnergy> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMolarEnergy(override val energy: ImperialEnergy, override val per: AmountOfSubstance) : MolarEnergy(), ImperialScientificUnit<PhysicalQuantity.MolarEnergy> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricAndImperialEnergy.per(amountOfSubstance: AmountOfSubstance) = MetricAndImperialMolarEnergy(this, amountOfSubstance)
infix fun MetricEnergy.per(amountOfSubstance: AmountOfSubstance) = MetricMolarEnergy(this, amountOfSubstance)
infix fun ImperialEnergy.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarEnergy(this, amountOfSubstance)
