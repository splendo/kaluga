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
 * Set of all [MetricDynamicViscosity]
 */
val MetricDynamicViscosityUnits: Set<MetricDynamicViscosity> get() = MetricPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

/**
 * Set of all [ImperialDynamicViscosity]
 */
val ImperialDynamicViscosityUnits: Set<ImperialDynamicViscosity> get() = ImperialPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

/**
 * Set of all [UKImperialDynamicViscosity]
 */
val UKImperialDynamicViscosityUnits: Set<UKImperialDynamicViscosity> get() = UKImperialPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

/**
 * Set of all [USCustomaryDynamicViscosity]
 */
val USCustomaryDynamicViscosityUnits: Set<USCustomaryDynamicViscosity> get() = USCustomaryPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

/**
 * Set of all [DynamicViscosity]
 */
val DynamicViscosityUnits: Set<DynamicViscosity> get() = MetricDynamicViscosityUnits +
    ImperialDynamicViscosityUnits +
    UKImperialDynamicViscosityUnits.filter { it.pressure !is UKImperialPressureWrapper }.toSet() +
    USCustomaryDynamicViscosityUnits.filter { it.pressure !is USCustomaryImperialPressureWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.DynamicViscosity]
 * SI unit is `Pascal per Second`
 */
@Serializable
sealed class DynamicViscosity : AbstractScientificUnit<PhysicalQuantity.DynamicViscosity>() {

    /**
     * The [Pressure] component
     */
    abstract val pressure: Pressure

    /**
     * The [Time] component
     */
    abstract val time: Time
    override val quantity = PhysicalQuantity.DynamicViscosity
    override val symbol: String by lazy { "${pressure.symbol}⋅${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.fromSIUnit(pressure.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = pressure.toSIUnit(time.toSIUnit(value))
}

/**
 * A [DynamicViscosity] for [MeasurementSystem.Metric]
 * @param pressure the [MetricPressure] component
 * @param time the [Time] component
 */
@Serializable
data class MetricDynamicViscosity(override val pressure: MetricPressure, override val time: Time) :
    DynamicViscosity(),
    MetricScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [DynamicViscosity] for [MeasurementSystem.Imperial]
 * @param pressure the [ImperialPressure] component
 * @param time the [Time] component
 */
@Serializable
data class ImperialDynamicViscosity(override val pressure: ImperialPressure, override val time: Time) :
    DynamicViscosity(),
    ImperialScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialDynamicViscosity] equivalent to this [ImperialDynamicViscosity]
     */
    val ukImperial get() = pressure.ukImperial x time

    /**
     * The [USCustomaryDynamicViscosity] equivalent to this [ImperialDynamicViscosity]
     */
    val usCustomary get() = pressure.usCustomary x time
}

/**
 * A [DynamicViscosity] for [MeasurementSystem.UKImperial]
 * @param pressure the [UKImperialPressure] component
 * @param time the [Time] component
 */
@Serializable
data class UKImperialDynamicViscosity(override val pressure: UKImperialPressure, override val time: Time) :
    DynamicViscosity(),
    UKImperialScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [DynamicViscosity] for [MeasurementSystem.USCustomary]
 * @param pressure the [USCustomaryPressure] component
 * @param time the [Time] component
 */
@Serializable
data class USCustomaryDynamicViscosity(override val pressure: USCustomaryPressure, override val time: Time) :
    DynamicViscosity(),
    USCustomaryScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricDynamicViscosity] from a [MetricPressure] and a [Time]
 * @param time the [Time] component
 * @return the [MetricDynamicViscosity] represented by the units
 */
infix fun MetricPressure.x(time: Time) = MetricDynamicViscosity(this, time)

/**
 * Gets an [ImperialDynamicViscosity] from an [ImperialPressure] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialDynamicViscosity] represented by the units
 */
infix fun ImperialPressure.x(time: Time) = ImperialDynamicViscosity(this, time)

/**
 * Gets a [UKImperialDynamicViscosity] from a [UKImperialPressure] and a [Time]
 * @param time the [Time] component
 * @return the [UKImperialDynamicViscosity] represented by the units
 */
infix fun UKImperialPressure.x(time: Time) = UKImperialDynamicViscosity(this, time)

/**
 * Gets a [USCustomaryDynamicViscosity] from a [USCustomaryPressure] and a [Time]
 * @param time the [Time] component
 * @return the [USCustomaryDynamicViscosity] represented by the units
 */
infix fun USCustomaryPressure.x(time: Time) = USCustomaryDynamicViscosity(this, time)
