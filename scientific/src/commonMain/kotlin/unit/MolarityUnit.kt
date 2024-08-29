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
 * Set of all [MetricMolarity]
 */
val MetricMolarityUnits: Set<MetricMolarity> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    MetricVolumeUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [ImperialMolarity]
 */
val ImperialMolarityUnits: Set<ImperialMolarity> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    ImperialVolumeUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [UKImperialMolarity]
 */
val UKImperialMolarityUnits: Set<UKImperialMolarity> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    UKImperialVolumeUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [USCustomaryMolarity]
 */
val USCustomaryMolarityUnits: Set<USCustomaryMolarity> get() = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    USCustomaryVolumeUnits.map { amountOfSubstance per it }
}.toSet()

/**
 * Set of all [Molarity]
 */
val MolarityUnits: Set<Molarity> get() = MetricMolarityUnits +
    ImperialMolarityUnits +
    UKImperialMolarityUnits.filter { it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryMolarityUnits.filter { it.per !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Molarity]
 * SI unit is `Mole per CubicMeter`
 */
@Serializable
sealed class Molarity : AbstractScientificUnit<PhysicalQuantity.Molarity>() {

    /**
     * The [AmountOfSubstance] component
     */
    abstract val amountOfSubstance: AmountOfSubstance

    /**
     * The [Volume] component
     */
    abstract val per: Volume
    override val symbol: String by lazy { "${amountOfSubstance.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.Molarity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(amountOfSubstance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = amountOfSubstance.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Molarity] for [MeasurementSystem.Metric]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [MetricVolume] component
 */
@Serializable
data class MetricMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: MetricVolume) :
    Molarity(),
    MetricScientificUnit<PhysicalQuantity.Molarity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Molarity] for [MeasurementSystem.Imperial]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [ImperialVolume] component
 */
@Serializable
data class ImperialMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: ImperialVolume) :
    Molarity(),
    ImperialScientificUnit<PhysicalQuantity.Molarity> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMolarity] equivalent to this [ImperialMolarity]
     */
    val ukImperial get() = amountOfSubstance per per.ukImperial

    /**
     * The [USCustomaryMolarity] equivalent to this [ImperialMolarity]
     */
    val usCustomary get() = amountOfSubstance per per.usCustomary
}

/**
 * A [Molarity] for [MeasurementSystem.USCustomary]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [USCustomaryVolume] component
 */
@Serializable
data class USCustomaryMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: USCustomaryVolume) :
    Molarity(),
    USCustomaryScientificUnit<PhysicalQuantity.Molarity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [Molarity] for [MeasurementSystem.UKImperial]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @param per the [UKImperialVolume] component
 */
@Serializable
data class UKImperialMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: UKImperialVolume) :
    Molarity(),
    UKImperialScientificUnit<PhysicalQuantity.Molarity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricMolarity] from an [AmountOfSubstance] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricMolarity] represented by the units
 */
infix fun AmountOfSubstance.per(volume: MetricVolume) = MetricMolarity(this, volume)

/**
 * Gets an [ImperialMolarity] from an [AmountOfSubstance] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [ImperialMolarity] represented by the units
 */
infix fun AmountOfSubstance.per(volume: ImperialVolume) = ImperialMolarity(this, volume)

/**
 * Gets a [USCustomaryMolarity] from an [AmountOfSubstance] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryMolarity] represented by the units
 */
infix fun AmountOfSubstance.per(volume: USCustomaryVolume) = USCustomaryMolarity(this, volume)

/**
 * Gets a [UKImperialMolarity] from an [AmountOfSubstance] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialMolarity] represented by the units
 */
infix fun AmountOfSubstance.per(volume: UKImperialVolume) = UKImperialMolarity(this, volume)
