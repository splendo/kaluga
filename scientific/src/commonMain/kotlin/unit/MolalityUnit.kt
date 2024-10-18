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
 * Set of all [MetricMolality]
 */
val MetricMolalityUnits: Set<MetricMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    MetricWeightUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [ImperialMolality]
 */
val ImperialMolalityUnits: Set<ImperialMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    ImperialWeightUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [UKImperialMolality]
 */
val UKImperialMolalityUnits: Set<UKImperialMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    UKImperialWeightUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [USCustomaryMolality]
 */
val USCustomaryMolalityUnits: Set<USCustomaryMolality> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    USCustomaryWeightUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [Molality]
 */
val MolalityUnits: Set<Molality> get() = MetricMolalityUnits +
    ImperialMolalityUnits +
    UKImperialMolalityUnits.filter { it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMolalityUnits.filter { it.per !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Molality]
 * SI unit is `Mole per Kilogram`
 */
@Serializable
sealed class Molality : AbstractScientificUnit<PhysicalQuantity.Molality>() {

    /**
     * The [AmountOfSubstance] component
     */
    abstract val amountOfSubstance: AmountOfSubstance

    /**
     * The [Weight] component
     */
    abstract val per: Weight
    override val symbol: String by lazy { "${amountOfSubstance.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.Molality
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(amountOfSubstance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = amountOfSubstance.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Molality] for [MeasurementSystem.Metric]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [MetricWeight] component
 */
@Serializable
data class MetricMolality(override val amountOfSubstance: AmountOfSubstance, override val per: MetricWeight) :
    Molality(),
    MetricScientificUnit<PhysicalQuantity.Molality> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Molality] for [MeasurementSystem.Imperial]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [ImperialWeight] component
 */
@Serializable
data class ImperialMolality(override val amountOfSubstance: AmountOfSubstance, override val per: ImperialWeight) :
    Molality(),
    ImperialScientificUnit<PhysicalQuantity.Molality> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMolality] equivalent to this [ImperialMolality]
     */
    val ukImperial get() = amountOfSubstance per per.ukImperial

    /**
     * The [USCustomaryMolality] equivalent to this [ImperialMolality]
     */
    val usCustomary get() = amountOfSubstance per per.usCustomary
}

/**
 * A [Molality] for [MeasurementSystem.USCustomary]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [USCustomaryWeight] component
 */
@Serializable
data class USCustomaryMolality(override val amountOfSubstance: AmountOfSubstance, override val per: USCustomaryWeight) :
    Molality(),
    USCustomaryScientificUnit<PhysicalQuantity.Molality> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [Molality] for [MeasurementSystem.UKImperial]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [UKImperialWeight] component
 */
@Serializable
data class UKImperialMolality(override val amountOfSubstance: AmountOfSubstance, override val per: UKImperialWeight) :
    Molality(),
    UKImperialScientificUnit<PhysicalQuantity.Molality> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricMolality] from an [AmountOfSubstance] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricMolality] represented by the units
 */
infix fun AmountOfSubstance.per(weight: MetricWeight) = MetricMolality(this, weight)

/**
 * Gets an [ImperialMolality] from an [AmountOfSubstance] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [ImperialMolality] represented by the units
 */
infix fun AmountOfSubstance.per(weight: ImperialWeight) = ImperialMolality(this, weight)

/**
 * Gets a [UKImperialMolality] from an [AmountOfSubstance] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialMolality] represented by the units
 */
infix fun AmountOfSubstance.per(weight: UKImperialWeight) = UKImperialMolality(this, weight)

/**
 * Gets a [USCustomaryMolality] from an [AmountOfSubstance] and a [USCustomaryWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomaryMolality] represented by the units
 */
infix fun AmountOfSubstance.per(weight: USCustomaryWeight) = USCustomaryMolality(this, weight)
