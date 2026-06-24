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
 * Set of all [MetricElectricChargeDensity]
 */
val MetricElectricChargeDensityUnits: Set<MetricElectricChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    MetricVolumeUnits.map { charge per it }
}.toSet()

/**
 * Set of all [ImperialElectricChargeDensity]
 */
val ImperialElectricChargeDensityUnits: Set<ImperialElectricChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    ImperialVolumeUnits.map { charge per it }
}.toSet()

/**
 * Set of all [UKImperialElectricChargeDensity]
 */
val UKImperialElectricChargeDensityUnits: Set<UKImperialElectricChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    UKImperialVolumeUnits.map { charge per it }
}.toSet()

/**
 * Set of all [USCustomaryElectricChargeDensity]
 */
val USCustomaryElectricChargeDensityUnits: Set<USCustomaryElectricChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    USCustomaryVolumeUnits.map { charge per it }
}.toSet()

/**
 * Set of all [ElectricChargeDensity]
 */
val ElectricChargeDensityUnits: Set<ElectricChargeDensity> get() = MetricElectricChargeDensityUnits +
    ImperialElectricChargeDensityUnits +
    UKImperialElectricChargeDensityUnits.filter { it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryElectricChargeDensityUnits.filter { it.per !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ElectricChargeDensity]
 * SI unit is `Coulomb per CubicMeter`
 */
@Serializable
sealed class ElectricChargeDensity : DefinedScientificUnit<PhysicalQuantity.ElectricChargeDensity>() {

    /**
     * The [ElectricCharge] component
     */
    abstract val charge: ElectricCharge

    /**
     * The [Volume] component
     */
    abstract val per: Volume
    override val symbol: String by lazy { "${charge.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.ElectricChargeDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(charge.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = charge.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [ElectricChargeDensity] for [MeasurementSystem.Metric]
 * @param charge the [ElectricCharge] component
 * @param per the [MetricVolume] component
 */
@Serializable
data class MetricElectricChargeDensity(override val charge: ElectricCharge, override val per: MetricVolume) :
    ElectricChargeDensity(),
    MetricScientificUnit<PhysicalQuantity.ElectricChargeDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [ElectricChargeDensity] for [MeasurementSystem.Imperial]
 * @param charge the [ElectricCharge] component
 * @param per the [ImperialVolume] component
 */
@Serializable
data class ImperialElectricChargeDensity(override val charge: ElectricCharge, override val per: ImperialVolume) :
    ElectricChargeDensity(),
    ImperialScientificUnit<PhysicalQuantity.ElectricChargeDensity> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialElectricChargeDensity] equivalent to this [ImperialElectricChargeDensity]
     */
    val ukImperial get() = charge per per.ukImperial

    /**
     * The [USCustomaryElectricChargeDensity] equivalent to this [ImperialElectricChargeDensity]
     */
    val usCustomary get() = charge per per.usCustomary
}

/**
 * An [ElectricChargeDensity] for [MeasurementSystem.USCustomary]
 * @param charge the [ElectricCharge] component
 * @param per the [USCustomaryVolume] component
 */
@Serializable
data class USCustomaryElectricChargeDensity(override val charge: ElectricCharge, override val per: USCustomaryVolume) :
    ElectricChargeDensity(),
    USCustomaryScientificUnit<PhysicalQuantity.ElectricChargeDensity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * An [ElectricChargeDensity] for [MeasurementSystem.UKImperial]
 * @param charge the [ElectricCharge] component
 * @param per the [UKImperialVolume] component
 */
@Serializable
data class UKImperialElectricChargeDensity(override val charge: ElectricCharge, override val per: UKImperialVolume) :
    ElectricChargeDensity(),
    UKImperialScientificUnit<PhysicalQuantity.ElectricChargeDensity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricElectricChargeDensity] from an [ElectricCharge] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricElectricChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(volume: MetricVolume) = MetricElectricChargeDensity(this, volume)

/**
 * Gets an [ImperialElectricChargeDensity] from an [ElectricCharge] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [ImperialElectricChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(volume: ImperialVolume) = ImperialElectricChargeDensity(this, volume)

/**
 * Gets a [USCustomaryElectricChargeDensity] from an [ElectricCharge] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryElectricChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(volume: USCustomaryVolume) = USCustomaryElectricChargeDensity(this, volume)

/**
 * Gets a [UKImperialElectricChargeDensity] from an [ElectricCharge] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialElectricChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(volume: UKImperialVolume) = UKImperialElectricChargeDensity(this, volume)

internal fun SerializersModuleBuilder.setupForElectricChargeDensity() {
    polymorphic(ElectricChargeDensity::class) {
        registerElectricChargeDensityClasses()
    }
}

internal fun PolymorphicModuleBuilder<ElectricChargeDensity>.registerElectricChargeDensityClasses() {
    subclass(ImperialElectricChargeDensity::class, ImperialElectricChargeDensity.serializer())
    subclass(MetricElectricChargeDensity::class, MetricElectricChargeDensity.serializer())
    subclass(UKImperialElectricChargeDensity::class, UKImperialElectricChargeDensity.serializer())
    subclass(USCustomaryElectricChargeDensity::class, USCustomaryElectricChargeDensity.serializer())
}
