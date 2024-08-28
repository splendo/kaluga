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
 * Set of all [MetricSpecificVolume]
 */
val MetricSpecificVolumeUnits: Set<MetricSpecificVolume> get() = MetricVolumeUnits.flatMap { volume ->
    MetricWeightUnits.map { volume per it }
}.toSet()

/**
 * Set of all [ImperialSpecificVolume]
 */
val ImperialSpecificVolumeUnits: Set<ImperialSpecificVolume> get() = ImperialVolumeUnits.flatMap { volume ->
    ImperialWeightUnits.map { volume per it }
}.toSet()

/**
 * Set of all [UKImperialSpecificVolume]
 */
val UKImperialSpecificVolumeUnits: Set<UKImperialSpecificVolume> get() = UKImperialVolumeUnits.flatMap { volume ->
    UKImperialWeightUnits.map { volume per it }
}.toSet()

/**
 * Set of all [USCustomarySpecificVolume]
 */
val USCustomarySpecificVolumeUnits: Set<USCustomarySpecificVolume> get() = USCustomaryVolumeUnits.flatMap { volume ->
    USCustomaryWeightUnits.map { volume per it }
}.toSet()

/**
 * Set of all [SpecificVolume]
 */
val SpecificVolumeUnits: Set<SpecificVolume> get() = MetricSpecificVolumeUnits +
    ImperialSpecificVolumeUnits +
    UKImperialSpecificVolumeUnits.filter { it.volume !is UKImperialImperialVolumeWrapper || it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomarySpecificVolumeUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper || it.per !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.SpecificVolume]
 * SI unit is `CubicMeter per Kilogram`
 */
@Serializable
sealed class SpecificVolume : AbstractScientificUnit<PhysicalQuantity.SpecificVolume>() {

    /**
     * The [Volume] component
     */
    abstract val volume: Volume

    /**
     * The [Weight] component
     */
    abstract val per: Weight
    override val symbol: String by lazy { "${volume.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.SpecificVolume
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volume.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [SpecificVolume] for [MeasurementSystem.Metric]
 * @param volume the [MetricVolume] component
 * @param per the [MetricWeight] component
 */
@Serializable
data class MetricSpecificVolume(override val volume: MetricVolume, override val per: MetricWeight) :
    SpecificVolume(),
    MetricScientificUnit<PhysicalQuantity.SpecificVolume> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [SpecificVolume] for [MeasurementSystem.Imperial]
 * @param volume the [ImperialVolume] component
 * @param per the [ImperialWeight] component
 */
@Serializable
data class ImperialSpecificVolume(override val volume: ImperialVolume, override val per: ImperialWeight) :
    SpecificVolume(),
    ImperialScientificUnit<PhysicalQuantity.SpecificVolume> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialSpecificVolume] equivalent to this [ImperialSpecificVolume]
     */
    val ukImperial get() = volume.ukImperial per per.ukImperial

    /**
     * The [USCustomarySpecificVolume] equivalent to this [ImperialSpecificVolume]
     */
    val usCustomary get() = volume.usCustomary per per.usCustomary
}

/**
 * A [SpecificVolume] for [MeasurementSystem.USCustomary]
 * @param volume the [USCustomaryVolume] component
 * @param per the [USCustomaryWeight] component
 */
@Serializable
data class USCustomarySpecificVolume(override val volume: USCustomaryVolume, override val per: USCustomaryWeight) :
    SpecificVolume(),
    USCustomaryScientificUnit<PhysicalQuantity.SpecificVolume> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [SpecificVolume] for [MeasurementSystem.UKImperial]
 * @param volume the [UKImperialVolume] component
 * @param per the [UKImperialWeight] component
 */
@Serializable
data class UKImperialSpecificVolume(override val volume: UKImperialVolume, override val per: UKImperialWeight) :
    SpecificVolume(),
    UKImperialScientificUnit<PhysicalQuantity.SpecificVolume> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricSpecificVolume] from a [MetricVolume] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricSpecificVolume] represented by the units
 */
infix fun MetricVolume.per(weight: MetricWeight) = MetricSpecificVolume(this, weight)

/**
 * Gets a [MetricSpecificVolume] from an [ImperialVolume] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [MetricSpecificVolume] represented by the units
 */
infix fun ImperialVolume.per(weight: ImperialWeight) = ImperialSpecificVolume(this, weight)

/**
 * Gets a [UKImperialSpecificVolume] from an [ImperialVolume] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialSpecificVolume] represented by the units
 */
infix fun ImperialVolume.per(weight: UKImperialWeight) = UKImperialSpecificVolume(this.ukImperial, weight)

/**
 * Gets a [USCustomarySpecificVolume] from an [ImperialVolume] and a [USCustomaryWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomarySpecificVolume] represented by the units
 */
infix fun ImperialVolume.per(weight: USCustomaryWeight) = USCustomarySpecificVolume(this.usCustomary, weight)

/**
 * Gets a [USCustomarySpecificVolume] from a [USCustomaryVolume] and a [USCustomaryWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomarySpecificVolume] represented by the units
 */
infix fun USCustomaryVolume.per(weight: USCustomaryWeight) = USCustomarySpecificVolume(this, weight)

/**
 * Gets a [USCustomarySpecificVolume] from a [USCustomaryVolume] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [USCustomarySpecificVolume] represented by the units
 */
infix fun USCustomaryVolume.per(weight: ImperialWeight) = USCustomarySpecificVolume(this, weight.usCustomary)

/**
 * Gets a [UKImperialSpecificVolume] from a [UKImperialVolume] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [UKImperialSpecificVolume] represented by the units
 */
infix fun UKImperialVolume.per(weight: ImperialWeight) = UKImperialSpecificVolume(this, weight.ukImperial)

/**
 * Gets a [UKImperialSpecificVolume] from a [UKImperialVolume] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialSpecificVolume] represented by the units
 */
infix fun UKImperialVolume.per(weight: UKImperialWeight) = UKImperialSpecificVolume(this, weight)
