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
 * Set of all [MetricIrradiance]
 */
val MetricIrradianceUnits: Set<MetricIrradiance> get() = MetricPowerUnits.flatMap { power ->
    MetricAreaUnits.map { power per it }
}.toSet()

/**
 * Set of all [ImperialIrradiance]
 */
val ImperialIrradianceUnits: Set<ImperialIrradiance> get() = ImperialPowerUnits.flatMap { power ->
    ImperialAreaUnits.map { power per it }
}.toSet()

/**
 * Set of all [Irradiance]
 */
val IrradianceUnits: Set<Irradiance> get() = MetricIrradianceUnits +
    ImperialIrradianceUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Irradiance]
 * SI unit is `Watt per SquareMeter`
 */
@Serializable
sealed class Irradiance : DefinedScientificUnit<PhysicalQuantity.Irradiance>() {

    /**
     * The [Power] component
     */
    abstract val power: Power

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val quantity = PhysicalQuantity.Irradiance
    override val symbol: String by lazy { "${power.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(power.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = power.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [Irradiance] for [MeasurementSystem.Metric]
 * @param power the [MetricPower] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricIrradiance(override val power: MetricPower, override val per: MetricArea) :
    Irradiance(),
    MetricScientificUnit<PhysicalQuantity.Irradiance> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [Irradiance] for [MeasurementSystem.Imperial]
 * @param power the [ImperialPower] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialIrradiance(override val power: ImperialPower, override val per: ImperialArea) :
    Irradiance(),
    ImperialScientificUnit<PhysicalQuantity.Irradiance> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricIrradiance] from a [MetricAndImperialPower] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricIrradiance] represented by the units
 */
infix fun MetricAndImperialPower.per(area: MetricArea) = MetricIrradiance(this.metric, area)

/**
 * Gets an [ImperialIrradiance] from a [MetricAndImperialPower] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialIrradiance] represented by the units
 */
infix fun MetricAndImperialPower.per(area: ImperialArea) = ImperialIrradiance(this.imperial, area)

/**
 * Gets a [MetricIrradiance] from a [MetricPower] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricIrradiance] represented by the units
 */
infix fun MetricPower.per(area: MetricArea) = MetricIrradiance(this, area)

/**
 * Gets an [ImperialIrradiance] from an [ImperialPower] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialIrradiance] represented by the units
 */
infix fun ImperialPower.per(area: ImperialArea) = ImperialIrradiance(this, area)

internal fun SerializersModuleBuilder.setupForIrradiance() {
    polymorphic(Irradiance::class) {
        registerIrradianceClasses()
    }
}

internal fun PolymorphicModuleBuilder<Irradiance>.registerIrradianceClasses() {
    subclass(ImperialIrradiance::class, ImperialIrradiance.serializer())
    subclass(MetricIrradiance::class, MetricIrradiance.serializer())
}
