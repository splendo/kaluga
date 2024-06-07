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

/**
 * Set of all [MetricMolarMass]
 */
val MetricMolarMassUnits: Set<MetricMolarMass> get() = MetricWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

/**
 * Set of all [ImperialMolarMass]
 */
val ImperialMolarMassUnits: Set<ImperialMolarMass> get() = ImperialWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

/**
 * Set of all [UKImperialMolarMass]
 */
val UKImperialMolarMassUnits: Set<UKImperialMolarMass> get() = UKImperialWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

/**
 * Set of all [USCustomaryMolarMass]
 */
val USCustomaryMolarMassUnits: Set<USCustomaryMolarMass> get() = USCustomaryWeightUnits.flatMap { weight ->
    AmountOfSubstanceUnits.map { weight per it }
}.toSet()

/**
 * Set of all [MolarMass]
 */
val MolarMassUnits: Set<MolarMass> get() = MetricMolarMassUnits +
    ImperialMolarMassUnits +
    UKImperialMolarMassUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMolarMassUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.MolarMass]
 * SI unit is `Kilogram per Mole`
 */
@Serializable
sealed class MolarMass : AbstractScientificUnit<PhysicalQuantity.MolarMass>() {

    /**
     * The [Weight] component
     */
    abstract val weight: Weight

    /**
     * The [AmountOfSubstance] component
     */
    abstract val per: AmountOfSubstance
    override val symbol: String by lazy { "${weight.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.MolarMass
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MolarMass] for [MeasurementSystem.Metric]
 * @param weight the [MetricWeight] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class MetricMolarMass(override val weight: MetricWeight, override val per: AmountOfSubstance) : MolarMass(), MetricScientificUnit<PhysicalQuantity.MolarMass> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MolarMass] for [MeasurementSystem.Imperial]
 * @param weight the [ImperialWeight] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class ImperialMolarMass(override val weight: ImperialWeight, override val per: AmountOfSubstance) : MolarMass(), ImperialScientificUnit<PhysicalQuantity.MolarMass> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMolarMass] equivalent to this [ImperialMolarMass]
     */
    val ukImperial get() = weight.ukImperial per per

    /**
     * The [USCustomaryMolarMass] equivalent to this [ImperialMolarMass]
     */
    val usCustomary get() = weight.usCustomary per per
}

/**
 * A [MolarMass] for [MeasurementSystem.USCustomary]
 * @param weight the [USCustomaryWeight] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class USCustomaryMolarMass(override val weight: USCustomaryWeight, override val per: AmountOfSubstance) : MolarMass(), USCustomaryScientificUnit<PhysicalQuantity.MolarMass> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [MolarMass] for [MeasurementSystem.UKImperial]
 * @param weight the [UKImperialWeight] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class UKImperialMolarMass(override val weight: UKImperialWeight, override val per: AmountOfSubstance) : MolarMass(), UKImperialScientificUnit<PhysicalQuantity.MolarMass> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricMolarMass] from a [MetricWeight] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [MetricMolarMass] represented by the units
 */
infix fun MetricWeight.per(amountOfSubstance: AmountOfSubstance) = MetricMolarMass(this, amountOfSubstance)

/**
 * Gets an [ImperialMolarMass] from an [ImperialWeight] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [ImperialMolarMass] represented by the units
 */
infix fun ImperialWeight.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarMass(this, amountOfSubstance)

/**
 * Gets a [USCustomaryMolarMass] from a [USCustomaryWeight] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [USCustomaryMolarMass] represented by the units
 */
infix fun USCustomaryWeight.per(amountOfSubstance: AmountOfSubstance) = USCustomaryMolarMass(this, amountOfSubstance)

/**
 * Gets a [UKImperialMolarMass] from a [UKImperialWeight] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [UKImperialMolarMass] represented by the units
 */
infix fun UKImperialWeight.per(amountOfSubstance: AmountOfSubstance) = UKImperialMolarMass(this, amountOfSubstance)
