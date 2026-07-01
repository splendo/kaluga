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
 * Set of all [MetricEnergyDensity]
 */
val MetricEnergyDensityUnits: Set<MetricEnergyDensity> get() = MetricEnergyUnits.flatMap { energy ->
    MetricVolumeUnits.map { energy per it }
}.toSet()

/**
 * Set of all [ImperialEnergyDensity]
 */
val ImperialEnergyDensityUnits: Set<ImperialEnergyDensity> get() = ImperialEnergyUnits.flatMap { energy ->
    ImperialVolumeUnits.map { energy per it }
}.toSet()

/**
 * Set of all [UKImperialEnergyDensity]
 */
val UKImperialEnergyDensityUnits: Set<UKImperialEnergyDensity> get() = ImperialEnergyUnits.flatMap { energy ->
    UKImperialVolumeUnits.map { energy per it }
}.toSet()

/**
 * Set of all [USCustomaryEnergyDensity]
 */
val USCustomaryEnergyDensityUnits: Set<USCustomaryEnergyDensity> get() = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryVolumeUnits.map { energy per it }
}.toSet()

/**
 * Set of all [EnergyDensity]
 */
val EnergyDensityUnits: Set<EnergyDensity> get() = MetricEnergyDensityUnits +
    ImperialEnergyDensityUnits +
    UKImperialEnergyDensityUnits.filter { it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryEnergyDensityUnits.filter { it.per !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.EnergyDensity]
 * SI unit is `Joule per CubicMeter`
 */
@Serializable
sealed class EnergyDensity : DefinedScientificUnit<PhysicalQuantity.EnergyDensity>() {

    /**
     * The [Energy] component
     */
    abstract val energy: Energy

    /**
     * The [Volume] component
     */
    abstract val per: Volume
    override val quantity = PhysicalQuantity.EnergyDensity
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [EnergyDensity] for [MeasurementSystem.Metric]
 * @param energy the [MetricEnergy] component
 * @param per the [MetricVolume] component
 */
@Serializable
data class MetricEnergyDensity(override val energy: MetricEnergy, override val per: MetricVolume) :
    EnergyDensity(),
    MetricScientificUnit<PhysicalQuantity.EnergyDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [EnergyDensity] for [MeasurementSystem.Imperial]
 * @param energy the [ImperialEnergy] component
 * @param per the [ImperialVolume] component
 */
@Serializable
data class ImperialEnergyDensity(override val energy: ImperialEnergy, override val per: ImperialVolume) :
    EnergyDensity(),
    ImperialScientificUnit<PhysicalQuantity.EnergyDensity> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialEnergyDensity] equivalent to this [ImperialEnergyDensity]
     */
    val ukImperial get() = energy per per.ukImperial

    /**
     * The [USCustomaryEnergyDensity] equivalent to this [ImperialEnergyDensity]
     */
    val usCustomary get() = energy per per.usCustomary
}

/**
 * An [EnergyDensity] for [MeasurementSystem.UKImperial]
 * @param energy the [ImperialEnergy] component
 * @param per the [UKImperialVolume] component
 */
@Serializable
data class UKImperialEnergyDensity(override val energy: ImperialEnergy, override val per: UKImperialVolume) :
    EnergyDensity(),
    UKImperialScientificUnit<PhysicalQuantity.EnergyDensity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * An [EnergyDensity] for [MeasurementSystem.USCustomary]
 * @param energy the [ImperialEnergy] component
 * @param per the [USCustomaryVolume] component
 */
@Serializable
data class USCustomaryEnergyDensity(override val energy: ImperialEnergy, override val per: USCustomaryVolume) :
    EnergyDensity(),
    USCustomaryScientificUnit<PhysicalQuantity.EnergyDensity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricEnergyDensity] from a [MetricAndImperialEnergy] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricEnergyDensity] represented by the units
 */
infix fun MetricAndImperialEnergy.per(volume: MetricVolume) = MetricEnergyDensity(this.metric, volume)

/**
 * Gets an [ImperialEnergyDensity] from a [MetricAndImperialEnergy] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [ImperialEnergyDensity] represented by the units
 */
infix fun MetricAndImperialEnergy.per(volume: ImperialVolume) = ImperialEnergyDensity(this.imperial, volume)

/**
 * Gets a [UKImperialEnergyDensity] from a [MetricAndImperialEnergy] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialEnergyDensity] represented by the units
 */
infix fun MetricAndImperialEnergy.per(volume: UKImperialVolume) = UKImperialEnergyDensity(this.imperial, volume)

/**
 * Gets a [USCustomaryEnergyDensity] from a [MetricAndImperialEnergy] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryEnergyDensity] represented by the units
 */
infix fun MetricAndImperialEnergy.per(volume: USCustomaryVolume) = USCustomaryEnergyDensity(this.imperial, volume)

/**
 * Gets a [MetricEnergyDensity] from a [MetricEnergy] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricEnergyDensity] represented by the units
 */
infix fun MetricEnergy.per(volume: MetricVolume) = MetricEnergyDensity(this, volume)

/**
 * Gets an [ImperialEnergyDensity] from an [ImperialEnergy] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [ImperialEnergyDensity] represented by the units
 */
infix fun ImperialEnergy.per(volume: ImperialVolume) = ImperialEnergyDensity(this, volume)

/**
 * Gets a [UKImperialEnergyDensity] from an [ImperialEnergy] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialEnergyDensity] represented by the units
 */
infix fun ImperialEnergy.per(volume: UKImperialVolume) = UKImperialEnergyDensity(this, volume)

/**
 * Gets a [USCustomaryEnergyDensity] from an [ImperialEnergy] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryEnergyDensity] represented by the units
 */
infix fun ImperialEnergy.per(volume: USCustomaryVolume) = USCustomaryEnergyDensity(this, volume)

internal fun SerializersModuleBuilder.setupForEnergyDensity() {
    polymorphic(EnergyDensity::class) {
        registerEnergyDensityClasses()
    }
}

internal fun PolymorphicModuleBuilder<EnergyDensity>.registerEnergyDensityClasses() {
    subclass(ImperialEnergyDensity::class, ImperialEnergyDensity.serializer())
    subclass(MetricEnergyDensity::class, MetricEnergyDensity.serializer())
    subclass(UKImperialEnergyDensity::class, UKImperialEnergyDensity.serializer())
    subclass(USCustomaryEnergyDensity::class, USCustomaryEnergyDensity.serializer())
}
