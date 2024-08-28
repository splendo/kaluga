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
 * Set of all [MetricAreaDensity]
 */
val MetricAreaDensityUnits: Set<MetricAreaDensity> get() = MetricWeightUnits.flatMap { weight ->
    MetricAreaUnits.map { weight per it }
}.toSet()

/**
 * Set of all [ImperialAreaDensity]
 */
val ImperialAreaDensityUnits: Set<ImperialAreaDensity> get() = ImperialWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight per it }
}.toSet()

/**
 * Set of all [UKImperialAreaDensity]
 */
val UKImperialAreaDensityUnits: Set<UKImperialAreaDensity> get() = UKImperialWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight per it }
}.toSet()

/**
 * Set of all [USCustomaryAreaDensity]
 */
val USCustomaryAreaDensityUnits: Set<USCustomaryAreaDensity> get() = USCustomaryWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight per it }
}.toSet()

/**
 * Set of all [AreaDensity]
 */
val AreaDensityUnits: Set<AreaDensity> get() = MetricAreaDensityUnits +
    ImperialAreaDensityUnits +
    UKImperialAreaDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryAreaDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.AreaDensity]
 * SI unit is `Kilogram per SquareMeter`
 */
@Serializable
sealed class AreaDensity : AbstractScientificUnit<PhysicalQuantity.AreaDensity>() {
    /**
     * The [Weight] component
     */
    abstract val weight: Weight

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val symbol: String by lazy { "${weight.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.AreaDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [AreaDensity] for [MeasurementSystem.Metric]
 * @param weight the [MetricWeight] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricAreaDensity(override val weight: MetricWeight, override val per: MetricArea) :
    AreaDensity(),
    MetricScientificUnit<PhysicalQuantity.AreaDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [AreaDensity] for [MeasurementSystem.Imperial]
 * @param weight the [ImperialWeight] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialAreaDensity(override val weight: ImperialWeight, override val per: ImperialArea) :
    AreaDensity(),
    ImperialScientificUnit<PhysicalQuantity.AreaDensity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}

/**
 * An [AreaDensity] for [MeasurementSystem.USCustomary]
 * @param weight the [USCustomaryWeight] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class USCustomaryAreaDensity(override val weight: USCustomaryWeight, override val per: ImperialArea) :
    AreaDensity(),
    USCustomaryScientificUnit<PhysicalQuantity.AreaDensity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * An [AreaDensity] for [MeasurementSystem.UKImperial]
 * @param weight the [UKImperialWeight] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class UKImperialAreaDensity(override val weight: UKImperialWeight, override val per: ImperialArea) :
    AreaDensity(),
    UKImperialScientificUnit<PhysicalQuantity.AreaDensity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricAreaDensity] from a [MetricWeight] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricAreaDensity] represented by the units
 */
infix fun MetricWeight.per(area: MetricArea) = MetricAreaDensity(this, area)

/**
 * Gets an [ImperialAreaDensity] from an [ImperialWeight] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialAreaDensity] represented by the units
 */
infix fun ImperialWeight.per(area: ImperialArea) = ImperialAreaDensity(this, area)

/**
 * Gets a [USCustomaryAreaDensity] from a [USCustomaryWeight] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [USCustomaryAreaDensity] represented by the units
 */
infix fun USCustomaryWeight.per(area: ImperialArea) = USCustomaryAreaDensity(this, area)

/**
 * Gets a [UKImperialAreaDensity] from a [UKImperialWeight] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [UKImperialAreaDensity] represented by the units
 */
infix fun UKImperialWeight.per(area: ImperialArea) = UKImperialAreaDensity(this, area)
