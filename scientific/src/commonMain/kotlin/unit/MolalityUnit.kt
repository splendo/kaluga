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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.MeasurementType
import kotlinx.serialization.Serializable

val MetricMolalityUnits: Set<MetricMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    MetricWeightUnits.map { amountOfSubstance per it }
}.toSet()

val ImperialMolalityUnits: Set<ImperialMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    ImperialWeightUnits.map { amountOfSubstance per it  }
}.toSet()

val UKImperialMolalityUnits: Set<UKImperialMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    UKImperialWeightUnits.map { amountOfSubstance per it  }
}.toSet()

val USCustomaryMolalityUnits: Set<USCustomaryMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    USCustomaryWeightUnits.map { amountOfSubstance per it  }
}.toSet()

val MolalityUnits: Set<Molality> get() = MetricMolalityUnits +
    ImperialMolalityUnits +
    UKImperialMolalityUnits.filter { it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMolalityUnits.filter { it.per !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class Molality : AbstractScientificUnit<MeasurementType.Molality>() {
    abstract val amountOfSubstance: AmountOfSubstance
    abstract val per: Weight
    override val symbol: String by lazy { "${amountOfSubstance.symbol} / ${per.symbol}" }
    override val type = MeasurementType.Molality
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(amountOfSubstance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = amountOfSubstance.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricMolality(override val amountOfSubstance: AmountOfSubstance, override val per: MetricWeight) : Molality(), MetricScientificUnit<MeasurementType.Molality> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMolality(override val amountOfSubstance: AmountOfSubstance, override val per: ImperialWeight) : Molality(), ImperialScientificUnit<MeasurementType.Molality> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = amountOfSubstance per per.ukImperial
    val usCustomary get() = amountOfSubstance per per.usCustomary
}
@Serializable
data class USCustomaryMolality(override val amountOfSubstance: AmountOfSubstance, override val per: USCustomaryWeight) : Molality(), USCustomaryScientificUnit<MeasurementType.Molality> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialMolality(override val amountOfSubstance: AmountOfSubstance, override val per: UKImperialWeight) : Molality(), UKImperialScientificUnit<MeasurementType.Molality> {
    override val system = MeasurementSystem.UKImperial
}

infix fun AmountOfSubstance.per(weight: MetricWeight) = MetricMolality(this, weight)
infix fun AmountOfSubstance.per(weight: ImperialWeight) = ImperialMolality(this, weight)
infix fun AmountOfSubstance.per(weight: UKImperialWeight) = UKImperialMolality(this, weight)
infix fun AmountOfSubstance.per(weight: USCustomaryWeight) = USCustomaryMolality(this, weight)
