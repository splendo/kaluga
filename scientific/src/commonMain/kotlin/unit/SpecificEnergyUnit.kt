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
 * Set of all [MetricSpecificEnergy]
 */
val MetricSpecificEnergyUnits: Set<MetricSpecificEnergy> get() = MetricEnergyUnits.flatMap { energy ->
    MetricWeightUnits.map { energy per it }
}.toSet()

/**
 * Set of all [ImperialSpecificEnergy]
 */
val ImperialSpecificEnergyUnits: Set<ImperialSpecificEnergy> get() = ImperialEnergyUnits.flatMap { energy ->
    ImperialWeightUnits.map { energy per it }
}.toSet()

/**
 * Set of all [UKImperialSpecificEnergy]
 */
val UKImperialSpecificEnergyUnits: Set<UKImperialSpecificEnergy> get() = ImperialEnergyUnits.flatMap { energy ->
    UKImperialWeightUnits.map { energy per it }
}.toSet()

/**
 * Set of all [USCustomarySpecificEnergy]
 */
val USCustomarySpecificEnergyUnits: Set<USCustomarySpecificEnergy> get() = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryWeightUnits.map { energy per it }
}.toSet()

/**
 * Set of all [SpecificEnergy]
 */
val SpecificEnergyUnits: Set<SpecificEnergy> get() = MetricSpecificEnergyUnits +
    ImperialSpecificEnergyUnits +
    UKImperialSpecificEnergyUnits.filter { it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomarySpecificEnergyUnits.filter { it.per !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.SpecificEnergy]
 * SI unit is `Joule per Kilogram`
 */
@Serializable
sealed class SpecificEnergy : AbstractScientificUnit<PhysicalQuantity.SpecificEnergy>() {

    /**
     * The [Energy] component
     */
    abstract val energy: Energy

    /**
     * The [Weight] component
     */
    abstract val per: Weight
    override val quantity = PhysicalQuantity.SpecificEnergy
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [SpecificEnergy] for [MeasurementSystem.Metric]
 * @param energy the [MetricEnergy] component
 * @param per the [MetricWeight] component
 */
@Serializable
data class MetricSpecificEnergy(override val energy: MetricEnergy, override val per: MetricWeight) : SpecificEnergy(), MetricScientificUnit<PhysicalQuantity.SpecificEnergy> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [SpecificEnergy] for [MeasurementSystem.Imperial]
 * @param energy the [ImperialEnergy] component
 * @param per the [ImperialWeight] component
 */
@Serializable
data class ImperialSpecificEnergy(
    override val energy: ImperialEnergy,
    override val per: ImperialWeight,
) : SpecificEnergy(), ImperialScientificUnit<PhysicalQuantity.SpecificEnergy> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialSpecificEnergy] equivalent to this [ImperialSpecificEnergy]
     */
    val ukImperial get() = energy per per.ukImperial

    /**
     * The [USCustomarySpecificEnergy] equivalent to this [ImperialSpecificEnergy]
     */
    val usCustomary get() = energy per per.usCustomary
}

/**
 * A [SpecificEnergy] for [MeasurementSystem.UKImperial]
 * @param energy the [ImperialEnergy] component
 * @param per the [UKImperialWeight] component
 */
@Serializable
data class UKImperialSpecificEnergy(
    override val energy: ImperialEnergy,
    override val per: UKImperialWeight,
) : SpecificEnergy(), UKImperialScientificUnit<PhysicalQuantity.SpecificEnergy> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [SpecificEnergy] for [MeasurementSystem.USCustomary]
 * @param energy the [ImperialEnergy] component
 * @param per the [USCustomaryWeight] component
 */
@Serializable
data class USCustomarySpecificEnergy(
    override val energy: ImperialEnergy,
    override val per: USCustomaryWeight,
) : SpecificEnergy(), USCustomaryScientificUnit<PhysicalQuantity.SpecificEnergy> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricSpecificEnergy] from a [MetricAndImperialEnergy] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricSpecificEnergy] represented by the units
 */
infix fun MetricAndImperialEnergy.per(weight: MetricWeight) = MetricSpecificEnergy(this.metric, weight)

/**
 * Gets an [ImperialSpecificEnergy] from a [MetricAndImperialEnergy] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [ImperialSpecificEnergy] represented by the units
 */
infix fun MetricAndImperialEnergy.per(weight: ImperialWeight) = ImperialSpecificEnergy(this.imperial, weight)

/**
 * Gets a [UKImperialSpecificEnergy] from a [MetricAndImperialEnergy] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialSpecificEnergy] represented by the units
 */
infix fun MetricAndImperialEnergy.per(weight: UKImperialWeight) = UKImperialSpecificEnergy(this.imperial, weight)

/**
 * Gets a [USCustomarySpecificEnergy] from a [MetricAndImperialEnergy] and a [USCustomaryWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomarySpecificEnergy] represented by the units
 */
infix fun MetricAndImperialEnergy.per(weight: USCustomaryWeight) = USCustomarySpecificEnergy(this.imperial, weight)

/**
 * Gets a [MetricSpecificEnergy] from a [MetricEnergy] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricSpecificEnergy] represented by the units
 */
infix fun MetricEnergy.per(weight: MetricWeight) = MetricSpecificEnergy(this, weight)

/**
 * Gets an [ImperialSpecificEnergy] from an [ImperialEnergy] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [ImperialSpecificEnergy] represented by the units
 */
infix fun ImperialEnergy.per(weight: ImperialWeight) = ImperialSpecificEnergy(this, weight)

/**
 * Gets a [UKImperialSpecificEnergy] from an [ImperialEnergy] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialSpecificEnergy] represented by the units
 */
infix fun ImperialEnergy.per(weight: UKImperialWeight) = UKImperialSpecificEnergy(this, weight)

/**
 * Gets a [USCustomarySpecificEnergy] from an [ImperialEnergy] and a [USCustomaryWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomarySpecificEnergy] represented by the units
 */
infix fun ImperialEnergy.per(weight: USCustomaryWeight) = USCustomarySpecificEnergy(this, weight)
