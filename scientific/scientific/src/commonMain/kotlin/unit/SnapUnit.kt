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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MetricSnap]
 */
val MetricSnapUnits: Set<MetricSnap> get() = MetricJoltUnits.flatMap { jolt ->
    TimeUnits.map { jolt per it }
}.toSet()

/**
 * Set of all [ImperialSnap]
 */
val ImperialSnapUnits: Set<ImperialSnap> get() = ImperialJoltUnits.flatMap { jolt ->
    TimeUnits.map { jolt per it }
}.toSet()

/**
 * Set of all [Snap]
 */
val SnapUnits: Set<Snap> get() = MetricSnapUnits +
    ImperialSnapUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Snap]
 * SI unit is `Meter per Second per Second per Second per Second`
 */
@Serializable
sealed class Snap : DefinedScientificUnit<PhysicalQuantity.Snap>() {
    abstract val jolt: Jolt
    abstract val per: Time
    override val quantity = PhysicalQuantity.Snap
    override val symbol: String by lazy { "${jolt.acceleration.speed.distance.symbol}/${per.symbol}⁴" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(jolt.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = jolt.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Snap] for [MeasurementSystem.MetricAndImperial]
 * @param jolt the [MetricAndImperialJolt] component
 * @param per the [Time] component
 */
@Serializable
data class MetricAndImperialSnap(override val jolt: MetricAndImperialJolt, override val per: Time) :
    Snap(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Snap> {
    override val system = MeasurementSystem.MetricAndImperial

    val metric get() = jolt.metric per per
    val imperial get() = jolt.imperial per per
}

/**
 * A [Snap] for [MeasurementSystem.Metric]
 * @param jolt the [MetricJolt] component
 * @param per the [Time] component
 */
@Serializable
data class MetricSnap(override val jolt: MetricJolt, override val per: Time) :
    Snap(),
    MetricScientificUnit<PhysicalQuantity.Snap> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Snap] for [MeasurementSystem.Imperial]
 * @param jolt the [ImperialJolt] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialSnap(override val jolt: ImperialJolt, override val per: Time) :
    Snap(),
    ImperialScientificUnit<PhysicalQuantity.Snap> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAndImperialSnap] from a [MetricAndImperialJolt] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAndImperialSnap] represented by the units
 */
infix fun MetricAndImperialJolt.per(time: Time) = MetricAndImperialSnap(this, time)

/**
 * Gets a [MetricSnap] from a [MetricJolt] and a [Time]
 * @param time the [Time] component
 * @return the [MetricSnap] represented by the units
 */
infix fun MetricJolt.per(time: Time) = MetricSnap(this, time)

/**
 * Gets an [ImperialSnap] from an [ImperialJolt] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialSnap] represented by the units
 */
infix fun ImperialJolt.per(time: Time) = ImperialSnap(this, time)

internal fun SerializersModuleBuilder.setupForSnap() {
    polymorphic(Snap::class) {
        registerSnapClasses()
    }
}

internal fun PolymorphicModuleBuilder<Snap>.registerSnapClasses() {
    subclass(ImperialSnap::class, ImperialSnap.serializer())
    subclass(MetricAndImperialSnap::class, MetricAndImperialSnap.serializer())
    subclass(MetricSnap::class, MetricSnap.serializer())
}
