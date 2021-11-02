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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricSpecificEnergyUnits: Set<MetricSpecificEnergy> = MetricEnergyUnits.flatMap { energy ->
    MetricWeightUnits.map { energy per it }
}.toSet()

val ImperialSpecificEnergyUnits: Set<ImperialSpecificEnergy> = ImperialEnergyUnits.flatMap { energy ->
    ImperialWeightUnits.map { energy per it }
}.toSet()

val UKImperialSpecificEnergyUnits: Set<UKImperialSpecificEnergy> = ImperialEnergyUnits.flatMap { energy ->
    UKImperialWeightUnits.map { energy per it }
}.toSet()

val USCustomarySpecificEnergyUnits: Set<USCustomarySpecificEnergy> = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryWeightUnits.map { energy per it }
}.toSet()

val SpecificEnergyUnits: Set<SpecificEnergy> = MetricSpecificEnergyUnits +
    ImperialSpecificEnergyUnits +
    UKImperialSpecificEnergyUnits.filter { it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomarySpecificEnergyUnits.filter { it.per !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class SpecificEnergy : AbstractScientificUnit<MeasurementType.SpecificEnergy>() {
    abstract val energy: Energy
    abstract val per: Weight
    override val type = MeasurementType.SpecificEnergy
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricSpecificEnergy(override val energy: MetricEnergy, override val per: MetricWeight) : SpecificEnergy(), MetricScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialSpecificEnergy(override val energy: ImperialEnergy, override val per: ImperialWeight) : SpecificEnergy(), ImperialScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = energy per per.ukImperial
    val usCustomary get() = energy per per.usCustomary
}
@Serializable
data class UKImperialSpecificEnergy(override val energy: ImperialEnergy, override val per: UKImperialWeight) : SpecificEnergy(), UKImperialScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomarySpecificEnergy(override val energy: ImperialEnergy, override val per: USCustomaryWeight) : SpecificEnergy(), USCustomaryScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricAndImperialEnergy.per(weight: MetricWeight) = MetricSpecificEnergy(this.metric, weight)
infix fun MetricAndImperialEnergy.per(weight: ImperialWeight) = ImperialSpecificEnergy(this.imperial, weight)
infix fun MetricAndImperialEnergy.per(weight: UKImperialWeight) = UKImperialSpecificEnergy(this.imperial, weight)
infix fun MetricAndImperialEnergy.per(weight: USCustomaryWeight) = USCustomarySpecificEnergy(this.imperial, weight)
infix fun MetricEnergy.per(weight: MetricWeight) = MetricSpecificEnergy(this, weight)
infix fun ImperialEnergy.per(weight: ImperialWeight) = ImperialSpecificEnergy(this, weight)
infix fun ImperialEnergy.per(weight: UKImperialWeight) = UKImperialSpecificEnergy(this, weight)
infix fun ImperialEnergy.per(weight: USCustomaryWeight) = USCustomarySpecificEnergy(this, weight)
