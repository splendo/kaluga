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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

val MetricAndImperialAccelerationUnits: Set<MetricAndImperialAcceleration> get() =
    setOf(GUnit, Nanog, Microg, Millig, Centig, Decig, Decag, Hectog, Kilog, Megag, Gigag)

/**
 * Set of all [MetricAcceleration]
 */
val MetricAccelerationUnits: Set<MetricAcceleration> get() = MetricSpeedUnits.flatMap { speed ->
    TimeUnits.map { speed per it }
}.toSet() +
    setOf(Gal, NanoGal, MicroGal, MilliGal, CentiGal, DeciGal, DecaGal, HectoGal, KiloGal, MegaGal, GigaGal) +
    MetricAndImperialAccelerationUnits.map { it.metric }

/**
 * Set of all [ImperialAcceleration]
 */
val ImperialAccelerationUnits: Set<ImperialAcceleration> get() = ImperialSpeedUnits.flatMap { speed ->
    TimeUnits.map { speed per it }
}.toSet() +
    MetricAndImperialAccelerationUnits.map { it.imperial }

/**
 * Set of all [Acceleration]
 */
val AccelerationUnits: Set<Acceleration> get() = MetricAndImperialAccelerationUnits +
    MetricAccelerationUnits.filter { it !is MetricMetricAndImperialAccelerationWrapper } +
    ImperialAccelerationUnits.filter { it !is ImperialMetricAndImperialAccelerationWrapper }

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Acceleration]
 * SI unit is `Meter per Second per Second`
 */
@Serializable
sealed class Acceleration : AbstractScientificUnit<PhysicalQuantity.Acceleration>() {

    /**
     * The [Speed] component
     */
    abstract val speed: Speed

    /**
     * The [Time] component
     */
    abstract val per: Time
    override val quantity = PhysicalQuantity.Acceleration
    override val symbol: String by lazy {
        defaultSymbol
    }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(speed.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = speed.toSIUnit(per.fromSIUnit(value))
}

internal val Acceleration.defaultSymbol: String get() = if (speed.per == per) {
    "${speed.distance.symbol}/${per.symbol}²"
} else {
    "${speed.distance.symbol}/${speed.per.symbol}·${per.symbol}"
}

@Serializable
sealed class MetricAndImperialAcceleration :
    Acceleration(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Acceleration> {
    override val system = MeasurementSystem.MetricAndImperial

    val metric get() = MetricMetricAndImperialAccelerationWrapper(this)
    val imperial get() = ImperialMetricAndImperialAccelerationWrapper(this)
}

/**
 * An [Acceleration] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricAcceleration :
    Acceleration(),
    MetricScientificUnit<PhysicalQuantity.Acceleration> {
    abstract override val speed: MetricSpeed
    override val system = MeasurementSystem.Metric
}

/**
 * A [MetricAcceleration] created from a combination of [MetricSpeed] and [Time]
 * @param speed the [MetricSpeed] component
 * @param per the [Time] component
 */
@Serializable
data class CombinedMetricAcceleration(override val speed: MetricSpeed, override val per: Time) : MetricAcceleration()

@Serializable
data class MetricMetricAndImperialAccelerationWrapper(val metricAndImperial: MetricAndImperialAcceleration) : MetricAcceleration() {
    override val speed = Meter per Second
    override val per: Time = metricAndImperial.per
    override val symbol: String = metricAndImperial.symbol

    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperial.toSIUnit(value)
}

@Serializable
data object Gal : MetricAcceleration(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration> {
    override val symbol: String = "Gal"
    override val speed = Centimeter per Second
    override val per: Time = Second
}

@Serializable
sealed class GalMultiple :
    MetricAcceleration(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> {
    override val speed = Centimeter per Second
    override val per: Time = Second
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object NanoGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Nano(Gal) {
    override val symbol: String = "nanog"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object MicroGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Micro(Gal) {
    override val symbol: String = "microg"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object MilliGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Milli(Gal) {
    override val symbol: String = "millig"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object CentiGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Centi(Gal) {
    override val symbol: String = "centig"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object DeciGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Deci(Gal) {
    override val symbol: String = "decig"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object DecaGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Deca(Gal) {
    override val symbol: String = "decag"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object HectoGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Hecto(Gal) {
    override val symbol: String = "hectog"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object KiloGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Kilo(Gal) {
    override val symbol: String = "kilog"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object MegaGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Mega(Gal) {
    override val symbol: String = "megag"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object GigaGal : GalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Acceleration, Gal> by Giga(Gal) {
    override val symbol: String = "gigag"
}

/**
 * An [Acceleration] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialAcceleration :
    Acceleration(),
    ImperialScientificUnit<PhysicalQuantity.Acceleration> {
    abstract override val speed: ImperialSpeed
    override val system = MeasurementSystem.Imperial
}

/**
 * An [ImperialAcceleration] created from a combination of [ImperialSpeed] and [Time]
 * @param speed the [ImperialSpeed] component
 * @param per the [Time] component
 */
@Serializable
data class CombinedImperialAcceleration(override val speed: ImperialSpeed, override val per: Time) : ImperialAcceleration()

@Serializable
data class ImperialMetricAndImperialAccelerationWrapper(val metricAndImperial: MetricAndImperialAcceleration) : ImperialAcceleration() {
    override val speed = Foot per Second
    override val per: Time = metricAndImperial.per
    override val symbol: String = metricAndImperial.symbol

    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperial.toSIUnit(value)
}

/**
 * Gets a [MetricAcceleration] from a [MetricSpeed] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAcceleration] represented by the units
 */
infix fun MetricSpeed.per(time: Time): MetricAcceleration = CombinedMetricAcceleration(this, time)

/**
 * Gets an [ImperialAcceleration] from an [ImperialSpeed] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialAcceleration] represented by the units
 */
infix fun ImperialSpeed.per(time: Time): ImperialAcceleration = CombinedImperialAcceleration(this, time)

@Serializable
data object GUnit : MetricAndImperialAcceleration(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration> {

    private val standardGravity = 9.80665.toDecimal()

    override val symbol: String = "g-unit"
    override val speed = Meter per Second
    override val per: Time = Second

    override fun toSIUnit(value: Decimal): Decimal = super.toSIUnit(value * standardGravity)

    override fun fromSIUnit(value: Decimal): Decimal = super.fromSIUnit(value / standardGravity)
}

@Serializable
sealed class GUnitMultiple :
    MetricAndImperialAcceleration(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> {
    override val speed = Meter per Second
    override val per: Time = Second
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Nanog : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Nano(GUnit) {
    override val symbol: String = "nanog"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Microg : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Micro(GUnit) {
    override val symbol: String = "microg"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Millig : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Milli(GUnit) {
    override val symbol: String = "millig"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Centig : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Centi(GUnit) {
    override val symbol: String = "centig"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Decig : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Deci(GUnit) {
    override val symbol: String = "decig"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Decag : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Deca(GUnit) {
    override val symbol: String = "decag"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Hectog : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Hecto(GUnit) {
    override val symbol: String = "hectog"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Kilog : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Kilo(GUnit) {
    override val symbol: String = "kilog"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Megag : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Mega(GUnit) {
    override val symbol: String = "megag"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
@Serializable
data object Gigag : GUnitMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Acceleration, GUnit> by Giga(GUnit) {
    override val symbol: String = "gigag"
}

/**
 * The standard [Acceleration] due to gravity in `Meter per Second per Second`
 */
val MetricStandardGravityAcceleration = 1(GUnit).convert(Meter per Second per Second)

/**
 * The standard [Acceleration] due to gravity in `Foot per Second per Second`
 */
val ImperialStandardGravityAcceleration = 1(GUnit).convert(Foot per Second per Second)
