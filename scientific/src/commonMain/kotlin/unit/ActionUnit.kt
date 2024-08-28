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
 * Set of all [MetricAndImperialAction]
 */
val MetricAndImperialActionUnits: Set<MetricAndImperialAction> get() = MetricAndImperialEnergyUnits.flatMap { energy ->
    TimeUnits.map { energy x it }
}.toSet()

/**
 * Set of all [MetricAction]
 */
val MetricActionUnits: Set<MetricAction> get() = MetricEnergyUnits.flatMap { energy ->
    TimeUnits.map { energy x it }
}.toSet()

/**
 * Set of all [ImperialAction]
 */
val ImperialActionUnits: Set<ImperialAction> get() = ImperialEnergyUnits.flatMap { energy ->
    TimeUnits.map { energy x it }
}.toSet()

/**
 * Set of all [Action]
 */
val ActionUnits: Set<Action> get() = MetricAndImperialActionUnits +
    MetricActionUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    ImperialActionUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Acceleration]
 * SI unit is `Joule x Second`
 */
@Serializable
sealed class Action : AbstractScientificUnit<PhysicalQuantity.Action>() {

    /**
     * The [Energy] component
     */
    abstract val energy: Energy

    /**
     * The [Time] component
     */
    abstract val time: Time
    override val quantity = PhysicalQuantity.Action
    override val symbol: String by lazy { "${energy.symbol}⋅${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.fromSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(time.toSIUnit(value))
}

/**
 * An [Action] for [MeasurementSystem.MetricAndImperial]
 * @param energy the [MetricAndImperialEnergy] component
 * @param time the [Time] component
 */
@Serializable
data class MetricAndImperialAction(override val energy: MetricAndImperialEnergy, override val time: Time) :
    Action(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Action> {
    override val system = MeasurementSystem.MetricAndImperial
    val metric get() = energy.metric x time
    val imperial get() = energy.imperial x time
}

/**
 * An [Action] for [MeasurementSystem.Metric]
 * @param energy the [MetricEnergy] component
 * @param time the [Time] component
 */
@Serializable
data class MetricAction(override val energy: MetricEnergy, override val time: Time) :
    Action(),
    MetricScientificUnit<PhysicalQuantity.Action> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [Action] for [MeasurementSystem.Imperial]
 * @param energy the [ImperialEnergy] component
 * @param time the [Time] component
 */
@Serializable
data class ImperialAction(override val energy: ImperialEnergy, override val time: Time) :
    Action(),
    ImperialScientificUnit<PhysicalQuantity.Action> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAndImperialAction] from a [MetricAndImperialEnergy] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAndImperialAction] represented by the units
 */
infix fun MetricAndImperialEnergy.x(time: Time) = MetricAndImperialAction(this, time)

/**
 * Gets a [MetricAction] from a [MetricEnergy] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAction] represented by the units
 */
infix fun MetricEnergy.x(time: Time) = MetricAction(this, time)

/**
 * Gets an [ImperialAction] from an [ImperialEnergy] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialAction] represented by the units
 */
infix fun ImperialEnergy.x(time: Time) = ImperialAction(this, time)
