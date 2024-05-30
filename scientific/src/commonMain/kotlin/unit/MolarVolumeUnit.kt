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
 * Set of all [MetricMolarVolume]
 */
val MetricMolarVolumeUnits: Set<MetricMolarVolume> get() = MetricVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

/**
 * Set of all [ImperialMolarVolume]
 */
val ImperialMolarVolumeUnits: Set<ImperialMolarVolume> get() = ImperialVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

/**
 * Set of all [UKImperialMolarVolume]
 */
val UKImperialMolarVolumeUnits: Set<UKImperialMolarVolume> get() = UKImperialVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

/**
 * Set of all [USCustomaryMolarVolume]
 */
val USCustomaryMolarVolumeUnits: Set<USCustomaryMolarVolume> get() = USCustomaryVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

/**
 * Set of all [MolarVolume]
 */
val MolarVolumeUnits: Set<MolarVolume> get() = MetricMolarVolumeUnits +
    ImperialMolarVolumeUnits +
    UKImperialMolarVolumeUnits.filter { it.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryMolarVolumeUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.MolarVolume]
 * SI unit is `CubicMeter per Mole`
 */
@Serializable
sealed class MolarVolume : AbstractScientificUnit<PhysicalQuantity.MolarVolume>() {

    /**
     * The [Volume] component
     */
    abstract val volume: Volume

    /**
     * The [AmountOfSubstance] component
     */
    abstract val per: AmountOfSubstance
    override val symbol: String by lazy { "${volume.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.MolarVolume
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volume.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MolarVolume] for [MeasurementSystem.Metric]
 * @param volume the [MetricVolume] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class MetricMolarVolume(override val volume: MetricVolume, override val per: AmountOfSubstance) : MolarVolume(), MetricScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MolarVolume] for [MeasurementSystem.Imperial]
 * @param volume the [ImperialVolume] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class ImperialMolarVolume(override val volume: ImperialVolume, override val per: AmountOfSubstance) : MolarVolume(), ImperialScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMolarVolume] equivalent to this [ImperialMolarVolume]
     */
    val ukImperial get() = volume.ukImperial per per

    /**
     * The [USCustomaryMolarVolume] equivalent to this [ImperialMolarVolume]
     */
    val usCustomary get() = volume.usCustomary per per
}

/**
 * A [MolarVolume] for [MeasurementSystem.USCustomary]
 * @param volume the [USCustomaryVolume] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class USCustomaryMolarVolume(
    override val volume: USCustomaryVolume,
    override val per: AmountOfSubstance,
) : MolarVolume(), USCustomaryScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [MolarVolume] for [MeasurementSystem.UKImperial]
 * @param volume the [UKImperialVolume] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class UKImperialMolarVolume(
    override val volume: UKImperialVolume,
    override val per: AmountOfSubstance,
) : MolarVolume(), UKImperialScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricMolarVolume] from a [MetricVolume] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [MetricMolarVolume] represented by the units
 */
infix fun MetricVolume.per(amountOfSubstance: AmountOfSubstance) = MetricMolarVolume(this, amountOfSubstance)

/**
 * Gets an [ImperialMolarVolume] from an [ImperialVolume] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [ImperialMolarVolume] represented by the units
 */
infix fun ImperialVolume.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarVolume(this, amountOfSubstance)

/**
 * Gets a [USCustomaryMolarVolume] from a [USCustomaryVolume] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [USCustomaryMolarVolume] represented by the units
 */
infix fun USCustomaryVolume.per(amountOfSubstance: AmountOfSubstance) = USCustomaryMolarVolume(this, amountOfSubstance)

/**
 * Gets a [UKImperialMolarVolume] from a [UKImperialVolume] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [UKImperialMolarVolume] represented by the units
 */
infix fun UKImperialVolume.per(amountOfSubstance: AmountOfSubstance) = UKImperialMolarVolume(this, amountOfSubstance)
