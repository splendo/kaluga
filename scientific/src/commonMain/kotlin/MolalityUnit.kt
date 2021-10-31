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

val MetricMolalityUnits = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    MetricWeightUnits.map { amountOfSubstance per it }
}.toSet()

val ImperialMolalityUnits = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    ImperialWeightUnits.map { amountOfSubstance per it  }
}.toSet()

val UKImperialMolalityUnits = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    UKImperialWeightUnits.map { amountOfSubstance per it  }
}.toSet()

val USCustomaryMolalityUnits = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    USCustomaryWeightUnits.map { amountOfSubstance per it  }
}.toSet()

val MolalityUnits: Set<Molality> = MetricMolalityUnits +
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

@JvmName("molalityFromAmountOfSubstanceAndArea")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolalityUnit : Molality
    > MolalityUnit.molality(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byDividing(amountOfSubstance, weight)

@JvmName("amountOfSubstanceFromMolalityAndWeight")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolalityUnit : Molality
    > AmountOfSubstanceUnit.amountOfSubstance(
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byMultiplying(molality, weight)

@JvmName("weightFromAmountOfSubstanceAndMolality")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolalityUnit : Molality
    > WeightUnit.weight(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>
) = byDividing(amountOfSubstance, molality)

@JvmName("amountOfSubstanceDivMetricWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : MetricWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivImperialWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivUKImperialWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivUSCustomaryWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).molality(this, weight)
@JvmName("amountOfSubstanceDivWeight")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, WeightUnit : Weight> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Mole per Kilogram).molality(this, weight)

@JvmName("molalityTimesWeight")
infix operator fun <MolalityUnit : Molality, WeightUnit : Weight> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.amountOfSubstance.amountOfSubstance(this, weight)

@JvmName("weightTimesMolality")
infix operator fun <MolalityUnit : Molality, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this

@JvmName("amountOfSubstanceDivMetricMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = molality.unit.per.weight(this, molality)
@JvmName("amountOfSubstanceDivImperialMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = molality.unit.per.weight(this, molality)
@JvmName("amountOfSubstanceDivUKImperialMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = molality.unit.per.weight(this, molality)
@JvmName("amountOfSubstanceDivUSCustomaryMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = molality.unit.per.weight(this, molality)
@JvmName("amountOfSubstanceDivMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, MolalityUnit : Molality> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = Kilogram.weight(this, molality)
