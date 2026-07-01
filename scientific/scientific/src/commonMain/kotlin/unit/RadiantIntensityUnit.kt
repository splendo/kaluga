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
 * Set of all [MetricAndImperialRadiantIntensity]
 */
val MetricAndImperialRadiantIntensityUnits: Set<MetricAndImperialRadiantIntensity> get() = MetricAndImperialPowerUnits.flatMap { power ->
    SolidAngleUnits.map { power per it }
}.toSet()

/**
 * Set of all [MetricRadiantIntensity]
 */
val MetricRadiantIntensityUnits: Set<MetricRadiantIntensity> get() = MetricPowerUnits.flatMap { power ->
    SolidAngleUnits.map { power per it }
}.toSet()

/**
 * Set of all [ImperialRadiantIntensity]
 */
val ImperialRadiantIntensityUnits: Set<ImperialRadiantIntensity> get() = ImperialPowerUnits.flatMap { power ->
    SolidAngleUnits.map { power per it }
}.toSet()

/**
 * Set of all [RadiantIntensity]
 */
val RadiantIntensityUnits: Set<RadiantIntensity> get() = MetricAndImperialRadiantIntensityUnits +
    MetricRadiantIntensityUnits.filter { it.power !is MetricMetricAndImperialPowerWrapper }.toSet() +
    ImperialRadiantIntensityUnits.filter { it.power !is ImperialMetricAndImperialPowerWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.RadiantIntensity]
 * SI unit is `Watt per Steradian`
 */
@Serializable
sealed class RadiantIntensity : DefinedScientificUnit<PhysicalQuantity.RadiantIntensity>() {

    /**
     * The [Power] component
     */
    abstract val power: Power

    /**
     * The [SolidAngle] component
     */
    abstract val per: SolidAngle
    override val quantity = PhysicalQuantity.RadiantIntensity
    override val symbol: String by lazy { "${power.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(power.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = power.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [RadiantIntensity] for [MeasurementSystem.MetricAndImperial]
 * @param power the [MetricAndImperialPower] component
 * @param per the [SolidAngle] component
 */
@Serializable
data class MetricAndImperialRadiantIntensity(override val power: MetricAndImperialPower, override val per: SolidAngle) :
    RadiantIntensity(),
    MetricAndImperialScientificUnit<PhysicalQuantity.RadiantIntensity> {
    override val system = MeasurementSystem.MetricAndImperial

    /**
     * The [MetricRadiantIntensity] equivalent to this [MetricAndImperialRadiantIntensity]
     */
    val metric get() = power.metric per per

    /**
     * The [ImperialRadiantIntensity] equivalent to this [MetricAndImperialRadiantIntensity]
     */
    val imperial get() = power.imperial per per
}

/**
 * A [RadiantIntensity] for [MeasurementSystem.Metric]
 * @param power the [MetricPower] component
 * @param per the [SolidAngle] component
 */
@Serializable
data class MetricRadiantIntensity(override val power: MetricPower, override val per: SolidAngle) :
    RadiantIntensity(),
    MetricScientificUnit<PhysicalQuantity.RadiantIntensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [RadiantIntensity] for [MeasurementSystem.Imperial]
 * @param power the [ImperialPower] component
 * @param per the [SolidAngle] component
 */
@Serializable
data class ImperialRadiantIntensity(override val power: ImperialPower, override val per: SolidAngle) :
    RadiantIntensity(),
    ImperialScientificUnit<PhysicalQuantity.RadiantIntensity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAndImperialRadiantIntensity] from a [MetricAndImperialPower] and a [SolidAngle]
 * @param solidAngle the [SolidAngle] component
 * @return the [MetricAndImperialRadiantIntensity] represented by the units
 */
infix fun MetricAndImperialPower.per(solidAngle: SolidAngle) = MetricAndImperialRadiantIntensity(this, solidAngle)

/**
 * Gets a [MetricRadiantIntensity] from a [MetricPower] and a [SolidAngle]
 * @param solidAngle the [SolidAngle] component
 * @return the [MetricRadiantIntensity] represented by the units
 */
infix fun MetricPower.per(solidAngle: SolidAngle) = MetricRadiantIntensity(this, solidAngle)

/**
 * Gets an [ImperialRadiantIntensity] from an [ImperialPower] and a [SolidAngle]
 * @param solidAngle the [SolidAngle] component
 * @return the [ImperialRadiantIntensity] represented by the units
 */
infix fun ImperialPower.per(solidAngle: SolidAngle) = ImperialRadiantIntensity(this, solidAngle)

internal fun SerializersModuleBuilder.setupForRadiantIntensity() {
    polymorphic(RadiantIntensity::class) {
        registerRadiantIntensityClasses()
    }
}

internal fun PolymorphicModuleBuilder<RadiantIntensity>.registerRadiantIntensityClasses() {
    subclass(ImperialRadiantIntensity::class, ImperialRadiantIntensity.serializer())
    subclass(MetricAndImperialRadiantIntensity::class, MetricAndImperialRadiantIntensity.serializer())
    subclass(MetricRadiantIntensity::class, MetricRadiantIntensity.serializer())
}
