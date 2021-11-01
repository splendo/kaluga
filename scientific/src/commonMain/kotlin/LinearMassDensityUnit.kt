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

val MetricLinearMassDensityUnits: Set<MetricLinearMassDensity> = MetricWeightUnits.flatMap { weight ->
    MetricLengthUnits.map { weight per it }
}.toSet()

val ImperialLinearMassDensityUnits: Set<ImperialLinearMassDensity> = ImperialWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

val UKImperialLinearMassDensityUnits: Set<UKImperialLinearMassDensity> = UKImperialWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

val USCustomaryLinearMassDensityUnits: Set<USCustomaryLinearMassDensity> = USCustomaryWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

val LinearMassDensityUnits: Set<LinearMassDensity> = MetricLinearMassDensityUnits +
    ImperialLinearMassDensityUnits +
    UKImperialLinearMassDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryLinearMassDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class LinearMassDensity : AbstractScientificUnit<MeasurementType.LinearMassDensity>() {
    abstract val weight: Weight
    abstract val per: Length
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.LinearMassDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricLinearMassDensity(override val weight: MetricWeight, override val per: MetricLength) : LinearMassDensity(), MetricScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialLinearMassDensity(override val weight: ImperialWeight, override val per: ImperialLength) : LinearMassDensity(), ImperialScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryLinearMassDensity(override val weight: USCustomaryWeight, override val per: ImperialLength) : LinearMassDensity(), USCustomaryScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialLinearMassDensity(override val weight: UKImperialWeight, override val per: ImperialLength) : LinearMassDensity(), UKImperialScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(length: MetricLength) = MetricLinearMassDensity(this, length)
infix fun ImperialWeight.per(length: ImperialLength) = ImperialLinearMassDensity(this, length)
infix fun USCustomaryWeight.per(length: ImperialLength) = USCustomaryLinearMassDensity(this, length)
infix fun UKImperialWeight.per(length: ImperialLength) = UKImperialLinearMassDensity(this, length)
