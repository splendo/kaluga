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

val MetricMassFlowRateUnits: Set<MetricMassFlowRate> get() = MetricWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

val ImperialMassFlowRateUnits: Set<ImperialMassFlowRate> get() = ImperialWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

val UKImperialMassFlowRateUnits: Set<UKImperialMassFlowRate> get() = UKImperialWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

val USCustomaryMassFlowRateUnits: Set<USCustomaryMassFlowRate> get() = USCustomaryWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

val MassFlowRateUnits: Set<MassFlowRate> get() = MetricMassFlowRateUnits +
    ImperialMassFlowRateUnits +
    UKImperialMassFlowRateUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMassFlowRateUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class MassFlowRate : AbstractScientificUnit<PhysicalQuantity.MassFlowRate>() {
    abstract val weight: Weight
    abstract val per: Time
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val quantity = PhysicalQuantity.MassFlowRate
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricMassFlowRate(override val weight: MetricWeight, override val per: Time) : MassFlowRate(), MetricScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMassFlowRate(override val weight: ImperialWeight, override val per: Time) : MassFlowRate(), ImperialScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryMassFlowRate(override val weight: USCustomaryWeight, override val per: Time) : MassFlowRate(), USCustomaryScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialMassFlowRate(override val weight: UKImperialWeight, override val per: Time) : MassFlowRate(), UKImperialScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(time: Time) = MetricMassFlowRate(this, time)
infix fun ImperialWeight.per(time: Time) = ImperialMassFlowRate(this, time)
infix fun USCustomaryWeight.per(time: Time) = USCustomaryMassFlowRate(this, time)
infix fun UKImperialWeight.per(time: Time) = UKImperialMassFlowRate(this, time)
