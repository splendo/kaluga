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
 * Set of all [MetricAndImperialMolarEnergy]
 */
val MetricAndImperialMolarEnergyUnits: Set<MetricAndImperialMolarEnergy> get() = MetricAndImperialEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

/**
 * Set of all [MetricMolarEnergy]
 */
val MetricMolarEnergyUnits: Set<MetricMolarEnergy> get() = MetricEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

/**
 * Set of all [ImperialMolarEnergy]
 */
val ImperialMolarEnergyUnits: Set<ImperialMolarEnergy> get() = ImperialEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

/**
 * Set of all [MolarEnergy]
 */
val MolarEnergyUnits: Set<MolarEnergy> get() = MetricAndImperialMolarEnergyUnits +
    MetricMolarEnergyUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    ImperialMolarEnergyUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.MolarEnergy]
 * SI unit is `Joule per Mole`
 */
@Serializable
sealed class MolarEnergy : AbstractScientificUnit<PhysicalQuantity.MolarEnergy>() {

    /**
     * The [Energy] component
     */
    abstract val energy: Energy

    /**
     * The [AmountOfSubstance] component
     */
    abstract val per: AmountOfSubstance
    override val quantity = PhysicalQuantity.MolarEnergy
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MolarEnergy] for [MeasurementSystem.MetricAndImperial]
 * @param energy the [MetricAndImperialEnergy] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class MetricAndImperialMolarEnergy(override val energy: MetricAndImperialEnergy, override val per: AmountOfSubstance) :
    MolarEnergy(),
    MetricAndImperialScientificUnit<PhysicalQuantity.MolarEnergy> {
    override val system = MeasurementSystem.MetricAndImperial

    /**
     * The [MetricMolarEnergy] equivalent to this [MetricAndImperialMolarEnergy]
     */
    val metric get() = energy.metric per per

    /**
     * The [ImperialMolarEnergy] equivalent to this [MetricAndImperialMolarEnergy]
     */
    val imperial get() = energy.imperial per per
}

/**
 * A [MolarEnergy] for [MeasurementSystem.Metric]
 * @param energy the [MetricEnergy] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class MetricMolarEnergy(override val energy: MetricEnergy, override val per: AmountOfSubstance) :
    MolarEnergy(),
    MetricScientificUnit<PhysicalQuantity.MolarEnergy> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MolarEnergy] for [MeasurementSystem.Imperial]
 * @param energy the [ImperialEnergy] component
 * @param per the [AmountOfSubstance] component
 */
@Serializable
data class ImperialMolarEnergy(override val energy: ImperialEnergy, override val per: AmountOfSubstance) :
    MolarEnergy(),
    ImperialScientificUnit<PhysicalQuantity.MolarEnergy> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAndImperialMolarEnergy] from a [MetricAndImperialEnergy] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [MetricAndImperialMolarEnergy] represented by the units
 */
infix fun MetricAndImperialEnergy.per(amountOfSubstance: AmountOfSubstance) = MetricAndImperialMolarEnergy(this, amountOfSubstance)

/**
 * Gets a [MetricMolarEnergy] from a [MetricEnergy] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [MetricMolarEnergy] represented by the units
 */
infix fun MetricEnergy.per(amountOfSubstance: AmountOfSubstance) = MetricMolarEnergy(this, amountOfSubstance)

/**
 * Gets an [ImperialMolarEnergy] from an [ImperialEnergy] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [ImperialMolarEnergy] represented by the units
 */
infix fun ImperialEnergy.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarEnergy(this, amountOfSubstance)
