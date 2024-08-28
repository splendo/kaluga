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
 * Set of all [MetricKinematicViscosity]
 */
val MetricKinematicViscosityUnits: Set<MetricKinematicViscosity> get() = MetricAreaUnits.flatMap { area ->
    TimeUnits.map { area per it }
}.toSet()

/**
 * Set of all [ImperialKinematicViscosity]
 */
val ImperialKinematicViscosityUnits: Set<ImperialKinematicViscosity> get() = ImperialAreaUnits.flatMap { area ->
    TimeUnits.map { area per it }
}.toSet()

/**
 * Set of all [KinematicViscosity]
 */
val KinematicViscosityUnits: Set<KinematicViscosity> get() = MetricKinematicViscosityUnits +
    ImperialKinematicViscosityUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.KinematicViscosity]
 * SI unit is `SquareMeter per Second`
 */
@Serializable
sealed class KinematicViscosity : AbstractScientificUnit<PhysicalQuantity.KinematicViscosity>() {

    /**
     * The [Area] component
     */
    abstract val area: Area

    /**
     * The [Time] component
     */
    abstract val time: Time
    override val quantity = PhysicalQuantity.KinematicViscosity
    override val symbol: String by lazy { "${area.symbol}/${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.toSIUnit(area.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = area.toSIUnit(time.fromSIUnit(value))
}

/**
 * A [KinematicViscosity] for [MeasurementSystem.Metric]
 * @param area the [MetricArea] component
 * @param time the [Time] component
 */
@Serializable
data class MetricKinematicViscosity(override val area: MetricArea, override val time: Time) :
    KinematicViscosity(),
    MetricScientificUnit<PhysicalQuantity.KinematicViscosity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [KinematicViscosity] for [MeasurementSystem.Imperial]
 * @param area the [ImperialArea] component
 * @param time the [Time] component
 */
@Serializable
data class ImperialKinematicViscosity(override val area: ImperialArea, override val time: Time) :
    KinematicViscosity(),
    ImperialScientificUnit<PhysicalQuantity.KinematicViscosity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricKinematicViscosity] from a [MetricArea] and a [Time]
 * @param time the [Time] component
 * @return the [MetricKinematicViscosity] represented by the units
 */
infix fun MetricArea.per(time: Time) = MetricKinematicViscosity(this, time)

/**
 * Gets an [ImperialKinematicViscosity] from an [ImperialArea] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialKinematicViscosity] represented by the units
 */
infix fun ImperialArea.per(time: Time) = ImperialKinematicViscosity(this, time)
