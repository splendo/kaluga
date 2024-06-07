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
 * Set of all [MetricDensity]
 */
val MetricDensityUnits: Set<MetricDensity> get() = MetricWeightUnits.flatMap { weight ->
    MetricVolumeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [ImperialDensity]
 */
val ImperialDensityUnits: Set<ImperialDensity> get() = ImperialWeightUnits.flatMap { weight ->
    ImperialVolumeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [UKImperialDensity]
 */
val UKImperialDensityUnits: Set<UKImperialDensity> get() = UKImperialWeightUnits.flatMap { weight ->
    UKImperialVolumeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [USCustomaryDensity]
 */
val USCustomaryDensityUnits: Set<USCustomaryDensity> get() = USCustomaryWeightUnits.flatMap { weight ->
    USCustomaryVolumeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [Density]
 */
val DensityUnits: Set<Density> get() = MetricDensityUnits +
    ImperialDensityUnits +
    UKImperialDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper || it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper || it.per !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Density]
 * SI unit is `Kilogram per CubicMeter`
 */
@Serializable
sealed class Density : AbstractScientificUnit<PhysicalQuantity.Density>() {

    /**
     * The [Weight] component
     */
    abstract val weight: Weight

    /**
     * The [Volume] component
     */
    abstract val per: Volume
    override val symbol: String by lazy { "${weight.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.Density
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Density] for [MeasurementSystem.Metric]
 * @param weight the [MetricWeight] component
 * @param per the [MetricVolume] component
 */
@Serializable
data class MetricDensity(override val weight: MetricWeight, override val per: MetricVolume) : Density(), MetricScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Density] for [MeasurementSystem.Imperial]
 * @param weight the [ImperialWeight] component
 * @param per the [ImperialVolume] component
 */
@Serializable
data class ImperialDensity(override val weight: ImperialWeight, override val per: ImperialVolume) : Density(), ImperialScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per.ukImperial
    val usCustomary get() = weight.usCustomary per per.usCustomary
}

/**
 * A [Density] for [MeasurementSystem.USCustomary]
 * @param weight the [USCustomaryWeight] component
 * @param per the [USCustomaryVolume] component
 */
@Serializable
data class USCustomaryDensity(override val weight: USCustomaryWeight, override val per: USCustomaryVolume) : Density(), USCustomaryScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [Density] for [MeasurementSystem.UKImperial]
 * @param weight the [UKImperialWeight] component
 * @param per the [UKImperialVolume] component
 */
@Serializable
data class UKImperialDensity(override val weight: UKImperialWeight, override val per: UKImperialVolume) : Density(), UKImperialScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricDensity] from a [MetricWeight] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricDensity] represented by the units
 */
infix fun MetricWeight.per(volume: MetricVolume) = MetricDensity(this, volume)

/**
 * Gets an [ImperialDensity] from an [ImperialWeight] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [MetricAreaDensity] represented by the units
 */
infix fun ImperialWeight.per(volume: ImperialVolume) = ImperialDensity(this, volume)

/**
 * Gets a [UKImperialDensity] from an [ImperialWeight] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialDensity] represented by the units
 */
infix fun ImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this.ukImperial, volume)

/**
 * Gets a [USCustomaryDensity] from an [ImperialWeight] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryDensity] represented by the units
 */
infix fun ImperialWeight.per(volume: USCustomaryVolume) = USCustomaryDensity(this.usCustomary, volume)

/**
 * Gets a [USCustomaryDensity] from a [USCustomaryWeight] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryDensity] represented by the units
 */
infix fun USCustomaryWeight.per(volume: USCustomaryVolume) = USCustomaryDensity(this, volume)

/**
 * Gets a [USCustomaryDensity] from a [USCustomaryWeight] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [USCustomaryDensity] represented by the units
 */
infix fun USCustomaryWeight.per(volume: ImperialVolume) = USCustomaryDensity(this, volume.usCustomary)

/**
 * Gets a [UKImperialDensity] from a [UKImperialWeight] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [UKImperialDensity] represented by the units
 */
infix fun UKImperialWeight.per(volume: ImperialVolume) = UKImperialDensity(this, volume.ukImperial)

/**
 * Gets a [UKImperialDensity] from a [UKImperialWeight] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialDensity] represented by the units
 */
infix fun UKImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this, volume)
