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
 * Set of all [MetricRadiance]
 */
val MetricRadianceUnits: Set<MetricRadiance> get() = MetricRadiantIntensityUnits.flatMap { radiantIntensity ->
    MetricAreaUnits.map { radiantIntensity per it }
}.toSet()

/**
 * Set of all [ImperialRadiance]
 */
val ImperialRadianceUnits: Set<ImperialRadiance> get() = ImperialRadiantIntensityUnits.flatMap { radiantIntensity ->
    ImperialAreaUnits.map { radiantIntensity per it }
}.toSet()

/**
 * Set of all [Radiance]
 */
val RadianceUnits: Set<Radiance> get() = MetricRadianceUnits +
    ImperialRadianceUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Radiance]
 * SI unit is `Watt per Steradian per SquareMeter`
 */
@Serializable
sealed class Radiance : DefinedScientificUnit<PhysicalQuantity.Radiance>() {

    /**
     * The [RadiantIntensity] component
     */
    abstract val radiantIntensity: RadiantIntensity

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val quantity = PhysicalQuantity.Radiance
    override val symbol: String by lazy { "${radiantIntensity.power.symbol}/(${radiantIntensity.per.symbol}⋅${per.symbol})" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(radiantIntensity.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = radiantIntensity.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Radiance] for [MeasurementSystem.Metric]
 * @param radiantIntensity the [MetricRadiantIntensity] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricRadiance(override val radiantIntensity: MetricRadiantIntensity, override val per: MetricArea) :
    Radiance(),
    MetricScientificUnit<PhysicalQuantity.Radiance> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Radiance] for [MeasurementSystem.Imperial]
 * @param radiantIntensity the [ImperialRadiantIntensity] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialRadiance(override val radiantIntensity: ImperialRadiantIntensity, override val per: ImperialArea) :
    Radiance(),
    ImperialScientificUnit<PhysicalQuantity.Radiance> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricRadiance] from a [MetricAndImperialRadiantIntensity] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricRadiance] represented by the units
 */
infix fun MetricAndImperialRadiantIntensity.per(area: MetricArea) = MetricRadiance(this.metric, area)

/**
 * Gets an [ImperialRadiance] from a [MetricAndImperialRadiantIntensity] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialRadiance] represented by the units
 */
infix fun MetricAndImperialRadiantIntensity.per(area: ImperialArea) = ImperialRadiance(this.imperial, area)

/**
 * Gets a [MetricRadiance] from a [MetricRadiantIntensity] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricRadiance] represented by the units
 */
infix fun MetricRadiantIntensity.per(area: MetricArea) = MetricRadiance(this, area)

/**
 * Gets an [ImperialRadiance] from an [ImperialRadiantIntensity] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialRadiance] represented by the units
 */
infix fun ImperialRadiantIntensity.per(area: ImperialArea) = ImperialRadiance(this, area)

internal fun SerializersModuleBuilder.setupForRadiance() {
    polymorphic(Radiance::class) {
        registerRadianceClasses()
    }
}

internal fun PolymorphicModuleBuilder<Radiance>.registerRadianceClasses() {
    subclass(ImperialRadiance::class, ImperialRadiance.serializer())
    subclass(MetricRadiance::class, MetricRadiance.serializer())
}
