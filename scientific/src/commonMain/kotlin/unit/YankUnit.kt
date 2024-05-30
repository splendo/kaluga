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
 * Set of all [MetricYank]
 */
val MetricYankUnits: Set<MetricYank> get() = MetricForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

/**
 * Set of all [ImperialYank]
 */
val ImperialYankUnits: Set<ImperialYank> get() = ImperialForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

/**
 * Set of all [UKImperialYank]
 */
val UKImperialYankUnits: Set<UKImperialYank> get() = UKImperialForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

/**
 * Set of all [USCustomaryYank]
 */
val USCustomaryYankUnits: Set<USCustomaryYank> get() = USCustomaryForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

/**
 * Set of all [Yank]
 */
val YankUnits: Set<Yank> get() = MetricYankUnits +
    ImperialYankUnits +
    UKImperialYankUnits.filter { it.force !is UKImperialImperialForceWrapper }.toSet() +
    USCustomaryYankUnits.filter { it.force !is USCustomaryImperialForceWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Yank]
 * SI unit is `Newton per Second`
 */
@Serializable
sealed class Yank : AbstractScientificUnit<PhysicalQuantity.Yank>() {

    /**
     * The [Force] component
     */
    abstract val force: Force

    /**
     * The [Time] component
     */
    abstract val per: Time
    override val quantity = PhysicalQuantity.Yank
    override val symbol: String by lazy { "${force.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Yank] for [MeasurementSystem.Metric]
 * @param force the [MetricForce] component
 * @param per the [Time] component
 */
@Serializable
data class MetricYank(override val force: MetricForce, override val per: Time) : Yank(), MetricScientificUnit<PhysicalQuantity.Yank> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Yank] for [MeasurementSystem.Imperial]
 * @param force the [ImperialForce] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialYank(override val force: ImperialForce, override val per: Time) : Yank(), ImperialScientificUnit<PhysicalQuantity.Yank> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialYank] equivalent to this [ImperialYank]
     */
    val ukImperial get() = UKImperialYank(force.ukImperial, per)

    /**
     * The [USCustomaryYank] equivalent to this [ImperialYank]
     */
    val usCustomary get() = USCustomaryYank(force.usCustomary, per)
}

/**
 * A [Yank] for [MeasurementSystem.UKImperial]
 * @param force the [UKImperialForce] component
 * @param per the [Time] component
 */
@Serializable
data class UKImperialYank(override val force: UKImperialForce, override val per: Time) : Yank(), UKImperialScientificUnit<PhysicalQuantity.Yank> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [Yank] for [MeasurementSystem.USCustomary]
 * @param force the [USCustomaryForce] component
 * @param per the [Time] component
 */
@Serializable
data class USCustomaryYank(override val force: USCustomaryForce, override val per: Time) : Yank(), USCustomaryScientificUnit<PhysicalQuantity.Yank> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricYank] from a [MetricForce] and a [Time]
 * @param time the [Time] component
 * @return the [MetricYank] represented by the units
 */
infix fun MetricForce.per(time: Time) = MetricYank(this, time)

/**
 * Gets an [ImperialYank] from an [ImperialForce] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialYank] represented by the units
 */
infix fun ImperialForce.per(time: Time) = ImperialYank(this, time)

/**
 * Gets a [UKImperialYank] from a [UKImperialForce] and a [Time]
 * @param time the [Time] component
 * @return the [UKImperialYank] represented by the units
 */
infix fun UKImperialForce.per(time: Time) = UKImperialYank(this, time)

/**
 * Gets a [USCustomaryYank] from a [USCustomaryForce] and a [Time]
 * @param time the [Time] component
 * @return the [USCustomaryYank] represented by the units
 */
infix fun USCustomaryForce.per(time: Time) = USCustomaryYank(this, time)
