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

val MetricMolarMassUnits: Set<MetricMolarMass> = MetricWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val ImperialMolarMassUnits: Set<ImperialMolarMass> = ImperialWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val UKImperialMolarMassUnits: Set<UKImperialMolarMass> = UKImperialWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val USCustomaryMolarMassUnits: Set<USCustomaryMolarMass> = USCustomaryWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

val MolarMassUnits: Set<MolarMass> = MetricMolarMassUnits +
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

fun <
    MolalityUnit : Molality,
    MolarMassUnit : MolarMass
    > MolalityUnit.molality(
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>
) = byInverting(molarMass)

fun <
    MolalityUnit : Molality,
    MolarMassUnit : MolarMass
    > MolarMassUnit.molarMass(
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>
) = byInverting(molality)

@JvmName("molarMassFromAmountOfSubstanceAndArea")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolarMassUnit : MolarMass
    > MolarMassUnit.molarMass(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = byDividing(weight, amountOfSubstance)

@JvmName("amountOfSubstanceFromMolarMassAndWeight")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolarMassUnit : MolarMass
    > AmountOfSubstanceUnit.amountOfSubstance(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>,
) = byDividing(weight, molarMass)

@JvmName("weightFromAmountOfSubstanceAndMolarMass")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolarMassUnit : MolarMass
    > WeightUnit.mass(
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>,
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = byMultiplying(molarMass, amountOfSubstance)

@JvmName("metricMolarMassMolality")
fun ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.molality() = (unit.per per unit.weight).molality(this)
@JvmName("imperialMolarMassMolality")
fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.molality() = (unit.per per unit.weight).molality(this)
@JvmName("ukImperialMolarMassMolality")
fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.molality() = (unit.per per unit.weight).molality(this)
@JvmName("usCustomaryMolarMassMolality")
fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.molality() = (unit.per per unit.weight).molality(this)
@JvmName("molarMassMolality")
fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.molality() = (Mole per Kilogram).molality(this)

@JvmName("metricMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, MetricMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("imperialMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, ImperialMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("ukImperialMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, UKImperialMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("usCustomaryMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, USCustomaryMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("molalityMolarMass")
fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.molarMass() = (Kilogram per Mole).molarMass(this)

@JvmName("metricWeightDivAmountOfSubstance")
infix operator fun <WeightUnit : MetricWeight, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Weight, WeightUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarMass(this, amountOfSubstance)
@JvmName("imperialWeightDivAmountOfSubstance")
infix operator fun <WeightUnit : ImperialWeight, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Weight, WeightUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarMass(this, amountOfSubstance)
@JvmName("ukImperialWeightDivAmountOfSubstance")
infix operator fun <WeightUnit : UKImperialWeight, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Weight, WeightUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarMass(this, amountOfSubstance)
@JvmName("usCustomaryWeightDivAmountOfSubstance")
infix operator fun <WeightUnit : USCustomaryWeight, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Weight, WeightUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarMass(this, amountOfSubstance)
@JvmName("weightDivAmountOfSubstance")
infix operator fun <WeightUnit : Weight, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Weight, WeightUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (Kilogram per Mole).molarMass(this, amountOfSubstance)

@JvmName("metricMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.weight.mass(this, amountOfSubstance)
@JvmName("imperialMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.weight.mass(this, amountOfSubstance)
@JvmName("ukImperialMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.weight.mass(this, amountOfSubstance)
@JvmName("usCustomaryMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = unit.weight.mass(this, amountOfSubstance)
@JvmName("molarMassTimesAmountOfSubstance")
infix operator fun <MolarMassUnit : MolarMass, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = Kilogram.mass(this, amountOfSubstance)

@JvmName("amountOfSubstanceTimesMetricMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = molarMass * this
@JvmName("amountOfSubstanceTimesImperialMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("amountOfSubstanceTimesUKImperialMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("amountOfSubstanceTimesUSCustomaryMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("amountOfSubstanceTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = molarMass * this

@JvmName("weightDivMolarMass")
infix operator fun <WeightUnit : Weight, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Weight, WeightUnit>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = molarMass.unit.per.amountOfSubstance(this, molarMass)
