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
 * Set of all [MetricLinearMassDensity]
 */
val MetricLinearMassDensityUnits: Set<MetricLinearMassDensity> get() = MetricWeightUnits.flatMap { weight ->
    MetricLengthUnits.map { weight per it }
}.toSet()

/**
 * Set of all [ImperialLinearMassDensity]
 */
val ImperialLinearMassDensityUnits: Set<ImperialLinearMassDensity> get() = ImperialWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

/**
 * Set of all [UKImperialLinearMassDensity]
 */
val UKImperialLinearMassDensityUnits: Set<UKImperialLinearMassDensity> get() = UKImperialWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

/**
 * Set of all [USCustomaryLinearMassDensity]
 */
val USCustomaryLinearMassDensityUnits: Set<USCustomaryLinearMassDensity> get() = USCustomaryWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

/**
 * Set of all [LinearMassDensity]
 */
val LinearMassDensityUnits: Set<LinearMassDensity> get() = MetricLinearMassDensityUnits +
    ImperialLinearMassDensityUnits +
    UKImperialLinearMassDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryLinearMassDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.LinearMassDensity]
 * SI unit is `Kilogram per Meter`
 */
@Serializable
sealed class LinearMassDensity : AbstractScientificUnit<PhysicalQuantity.LinearMassDensity>() {

    /**
     * The [Weight] component
     */
    abstract val weight: Weight

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${weight.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.LinearMassDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [LinearMassDensity] for [MeasurementSystem.Metric]
 * @param weight the [MetricWeight] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricLinearMassDensity(
    override val weight: MetricWeight,
    override val per: MetricLength,
) : LinearMassDensity(), MetricScientificUnit<PhysicalQuantity.LinearMassDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [LinearMassDensity] for [MeasurementSystem.Imperial]
 * @param weight the [ImperialWeight] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialLinearMassDensity(
    override val weight: ImperialWeight,
    override val per: ImperialLength,
) : LinearMassDensity(), ImperialScientificUnit<PhysicalQuantity.LinearMassDensity> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialLinearMassDensity] equivalent to this [ImperialLinearMassDensity]
     */
    val ukImperial get() = weight.ukImperial per per

    /**
     * The [USCustomaryLinearMassDensity] equivalent to this [ImperialLinearMassDensity]
     */
    val usCustomary get() = weight.usCustomary per per
}

/**
 * A [LinearMassDensity] for [MeasurementSystem.USCustomary]
 * @param weight the [USCustomaryWeight] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class USCustomaryLinearMassDensity(
    override val weight: USCustomaryWeight,
    override val per: ImperialLength,
) : LinearMassDensity(), USCustomaryScientificUnit<PhysicalQuantity.LinearMassDensity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [LinearMassDensity] for [MeasurementSystem.UKImperial]
 * @param weight the [UKImperialWeight] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class UKImperialLinearMassDensity(
    override val weight: UKImperialWeight,
    override val per: ImperialLength,
) : LinearMassDensity(), UKImperialScientificUnit<PhysicalQuantity.LinearMassDensity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricLinearMassDensity] from a [MetricWeight] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricLinearMassDensity] represented by the units
 */
infix fun MetricWeight.per(length: MetricLength) = MetricLinearMassDensity(this, length)

/**
 * Gets an [ImperialLinearMassDensity] from an [ImperialWeight] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialLinearMassDensity] represented by the units
 */
infix fun ImperialWeight.per(length: ImperialLength) = ImperialLinearMassDensity(this, length)

/**
 * Gets a [USCustomaryLinearMassDensity] from a [USCustomaryWeight] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [USCustomaryLinearMassDensity] represented by the units
 */
infix fun USCustomaryWeight.per(length: ImperialLength) = USCustomaryLinearMassDensity(this, length)

/**
 * Gets a [UKImperialLinearMassDensity] from a [UKImperialWeight] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [UKImperialLinearMassDensity] represented by the units
 */
infix fun UKImperialWeight.per(length: ImperialLength) = UKImperialLinearMassDensity(this, length)
