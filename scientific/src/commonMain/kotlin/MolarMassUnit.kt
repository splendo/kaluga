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

val MetricMolarMassUnits: Set<MetricMolarMass> get() = MetricWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val ImperialMolarMassUnits: Set<ImperialMolarMass> get() = ImperialWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val UKImperialMolarMassUnits: Set<UKImperialMolarMass> get() = UKImperialWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val USCustomaryMolarMassUnits: Set<USCustomaryMolarMass> get() = USCustomaryWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val MolarMassUnits: Set<MolarMass> get() = MetricMolarMassUnits +
    ImperialMolarMassUnits +
    UKImperialMolarMassUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMolarMassUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class MolarMass : AbstractScientificUnit<MeasurementType.MolarMass>() {
    abstract val weight: Weight
    abstract val per: AmountOfSubstance
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.MolarMass
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricMolarMass(override val weight: MetricWeight, override val per: AmountOfSubstance) : MolarMass(), MetricScientificUnit<MeasurementType.MolarMass> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMolarMass(override val weight: ImperialWeight, override val per: AmountOfSubstance) : MolarMass(), ImperialScientificUnit<MeasurementType.MolarMass> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryMolarMass(override val weight: USCustomaryWeight, override val per: AmountOfSubstance) : MolarMass(), USCustomaryScientificUnit<MeasurementType.MolarMass> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialMolarMass(override val weight: UKImperialWeight, override val per: AmountOfSubstance) : MolarMass(), UKImperialScientificUnit<MeasurementType.MolarMass> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(amountOfSubstance: AmountOfSubstance) = MetricMolarMass(this, amountOfSubstance)
infix fun ImperialWeight.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarMass(this, amountOfSubstance)
infix fun USCustomaryWeight.per(amountOfSubstance: AmountOfSubstance) = USCustomaryMolarMass(this, amountOfSubstance)
infix fun UKImperialWeight.per(amountOfSubstance: AmountOfSubstance) = UKImperialMolarMass(this, amountOfSubstance)
