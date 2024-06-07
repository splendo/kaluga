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
 * Set of all [MetricVolumetricFlow]
 */
val MetricVolumetricFlowUnits: Set<MetricVolumetricFlow> get() = MetricVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

/**
 * Set of all [ImperialVolumetricFlow]
 */
val ImperialVolumetricFlowUnits: Set<ImperialVolumetricFlow> get() = ImperialVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

/**
 * Set of all [UKImperialVolumetricFlow]
 */
val UKImperialVolumetricFlowUnits: Set<UKImperialVolumetricFlow> get() = UKImperialVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

/**
 * Set of all [USCustomaryVolumetricFlow]
 */
val USCustomaryVolumetricFlowUnits: Set<USCustomaryVolumetricFlow> get() = USCustomaryVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

/**
 * Set of all [VolumetricFlow]
 */
val VolumetricFlowUnits: Set<VolumetricFlow> get() = MetricVolumetricFlowUnits +
    ImperialVolumetricFlowUnits +
    UKImperialVolumetricFlowUnits.filter { it.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryVolumetricFlowUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.VolumetricFlow]
 * SI unit is `CubicMeter per Second`
 */
@Serializable
sealed class VolumetricFlow : AbstractScientificUnit<PhysicalQuantity.VolumetricFlow>() {

    /**
     * The [Volume] component
     */
    abstract val volume: Volume

    /**
     * The [Time] component
     */
    abstract val per: Time
    override val quantity = PhysicalQuantity.VolumetricFlow
    override val symbol: String by lazy { "${volume.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volume.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [VolumetricFlow] for [MeasurementSystem.Metric]
 * @param volume the [MetricVolume] component
 * @param per the [Time] component
 */
@Serializable
data class MetricVolumetricFlow(override val volume: MetricVolume, override val per: Time) : VolumetricFlow(), MetricScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [VolumetricFlow] for [MeasurementSystem.Imperial]
 * @param volume the [ImperialVolume] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialVolumetricFlow(override val volume: ImperialVolume, override val per: Time) : VolumetricFlow(), ImperialScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialVolumetricFlow] equivalent to this [ImperialVolumetricFlow]
     */
    val ukImperial get() = UKImperialVolumetricFlow(volume.ukImperial, per)

    /**
     * The [USCustomaryVolumetricFlow] equivalent to this [ImperialVolumetricFlow]
     */
    val usCustomary get() = USCustomaryVolumetricFlow(volume.usCustomary, per)
}

/**
 * A [VolumetricFlow] for [MeasurementSystem.UKImperial]
 * @param volume the [UKImperialVolume] component
 * @param per the [Time] component
 */
@Serializable
data class UKImperialVolumetricFlow(override val volume: UKImperialVolume, override val per: Time) : VolumetricFlow(), UKImperialScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [VolumetricFlow] for [MeasurementSystem.USCustomary]
 * @param volume the [USCustomaryVolume] component
 * @param per the [Time] component
 */
@Serializable
data class USCustomaryVolumetricFlow(
    override val volume: USCustomaryVolume,
    override val per: Time,
) : VolumetricFlow(), USCustomaryScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricVolumetricFlow] from a [MetricVolume] and a [Time]
 * @param time the [Time] component
 * @return the [MetricVolumetricFlow] represented by the units
 */
infix fun MetricVolume.per(time: Time) = MetricVolumetricFlow(this, time)

/**
 * Gets an [ImperialVolumetricFlow] from an [ImperialVolume] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialVolumetricFlow] represented by the units
 */
infix fun ImperialVolume.per(time: Time) = ImperialVolumetricFlow(this, time)

/**
 * Gets a [UKImperialVolumetricFlow] from a [UKImperialVolume] and a [Time]
 * @param time the [Time] component
 * @return the [UKImperialVolumetricFlow] represented by the units
 */
infix fun UKImperialVolume.per(time: Time) = UKImperialVolumetricFlow(this, time)

/**
 * Gets a [USCustomaryVolumetricFlow] from a [USCustomaryVolume] and a [Time]
 * @param time the [Time] component
 * @return the [USCustomaryVolumetricFlow] represented by the units
 */
infix fun USCustomaryVolume.per(time: Time) = USCustomaryVolumetricFlow(this, time)
