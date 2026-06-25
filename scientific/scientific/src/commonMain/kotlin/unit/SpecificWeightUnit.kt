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
 * Set of all [MetricSpecificWeight]
 */
val MetricSpecificWeightUnits: Set<MetricSpecificWeight> get() = MetricForceUnits.flatMap { force ->
    MetricVolumeUnits.map { force per it }
}.toSet()

/**
 * Set of all [ImperialSpecificWeight]
 */
val ImperialSpecificWeightUnits: Set<ImperialSpecificWeight> get() = ImperialForceUnits.flatMap { force ->
    ImperialVolumeUnits.map { force per it }
}.toSet()

/**
 * Set of all [UKImperialSpecificWeight]
 */
val UKImperialSpecificWeightUnits: Set<UKImperialSpecificWeight> get() = UKImperialForceUnits.flatMap { force ->
    UKImperialVolumeUnits.map { force per it }
}.toSet()

/**
 * Set of all [USCustomarySpecificWeight]
 */
val USCustomarySpecificWeightUnits: Set<USCustomarySpecificWeight> get() = USCustomaryForceUnits.flatMap { force ->
    USCustomaryVolumeUnits.map { force per it }
}.toSet()

/**
 * Set of all [SpecificWeight]
 */
val SpecificWeightUnits: Set<SpecificWeight> get() = MetricSpecificWeightUnits +
    ImperialSpecificWeightUnits +
    UKImperialSpecificWeightUnits.filter { it.force !is UKImperialImperialForceWrapper || it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomarySpecificWeightUnits.filter { it.force !is USCustomaryImperialForceWrapper || it.per !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.SpecificWeight]
 * SI unit is `Newton per CubicMeter`
 */
@Serializable
sealed class SpecificWeight : DefinedScientificUnit<PhysicalQuantity.SpecificWeight>() {

    /**
     * The [Force] component
     */
    abstract val force: Force

    /**
     * The [Volume] component
     */
    abstract val per: Volume
    override val symbol: String by lazy { "${force.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.SpecificWeight
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [SpecificWeight] for [MeasurementSystem.Metric]
 * @param force the [MetricForce] component
 * @param per the [MetricVolume] component
 */
@Serializable
data class MetricSpecificWeight(override val force: MetricForce, override val per: MetricVolume) :
    SpecificWeight(),
    MetricScientificUnit<PhysicalQuantity.SpecificWeight> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [SpecificWeight] for [MeasurementSystem.Imperial]
 * @param force the [ImperialForce] component
 * @param per the [ImperialVolume] component
 */
@Serializable
data class ImperialSpecificWeight(override val force: ImperialForce, override val per: ImperialVolume) :
    SpecificWeight(),
    ImperialScientificUnit<PhysicalQuantity.SpecificWeight> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = force.ukImperial per per.ukImperial
    val usCustomary get() = force.usCustomary per per.usCustomary
}

/**
 * A [SpecificWeight] for [MeasurementSystem.USCustomary]
 * @param force the [USCustomaryForce] component
 * @param per the [USCustomaryVolume] component
 */
@Serializable
data class USCustomarySpecificWeight(override val force: USCustomaryForce, override val per: USCustomaryVolume) :
    SpecificWeight(),
    USCustomaryScientificUnit<PhysicalQuantity.SpecificWeight> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [SpecificWeight] for [MeasurementSystem.UKImperial]
 * @param force the [UKImperialForce] component
 * @param per the [UKImperialVolume] component
 */
@Serializable
data class UKImperialSpecificWeight(override val force: UKImperialForce, override val per: UKImperialVolume) :
    SpecificWeight(),
    UKImperialScientificUnit<PhysicalQuantity.SpecificWeight> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricSpecificWeight] from a [MetricForce] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricSpecificWeight] represented by the units
 */
infix fun MetricForce.per(volume: MetricVolume) = MetricSpecificWeight(this, volume)

/**
 * Gets an [ImperialSpecificWeight] from an [ImperialForce] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [MetricAreaSpecificWeight] represented by the units
 */
infix fun ImperialForce.per(volume: ImperialVolume) = ImperialSpecificWeight(this, volume)

/**
 * Gets a [UKImperialSpecificWeight] from an [ImperialForce] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialSpecificWeight] represented by the units
 */
infix fun ImperialForce.per(volume: UKImperialVolume) = UKImperialSpecificWeight(this.ukImperial, volume)

/**
 * Gets a [USCustomarySpecificWeight] from an [ImperialForce] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomarySpecificWeight] represented by the units
 */
infix fun ImperialForce.per(volume: USCustomaryVolume) = USCustomarySpecificWeight(this.usCustomary, volume)

/**
 * Gets a [USCustomarySpecificWeight] from a [USCustomaryForce] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomarySpecificWeight] represented by the units
 */
infix fun USCustomaryForce.per(volume: USCustomaryVolume) = USCustomarySpecificWeight(this, volume)

/**
 * Gets a [USCustomarySpecificWeight] from a [USCustomaryForce] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [USCustomarySpecificWeight] represented by the units
 */
infix fun USCustomaryForce.per(volume: ImperialVolume) = USCustomarySpecificWeight(this, volume.usCustomary)

/**
 * Gets a [UKImperialSpecificWeight] from a [UKImperialForce] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [UKImperialSpecificWeight] represented by the units
 */
infix fun UKImperialForce.per(volume: ImperialVolume) = UKImperialSpecificWeight(this, volume.ukImperial)

/**
 * Gets a [UKImperialSpecificWeight] from a [UKImperialForce] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialSpecificWeight] represented by the units
 */
infix fun UKImperialForce.per(volume: UKImperialVolume) = UKImperialSpecificWeight(this, volume)

internal fun SerializersModuleBuilder.setupForSpecificWeight() {
    polymorphic(SpecificWeight::class) {
        registerSpecificWeightClasses()
    }
}

internal fun PolymorphicModuleBuilder<SpecificWeight>.registerSpecificWeightClasses() {
    subclass(ImperialSpecificWeight::class, ImperialSpecificWeight.serializer())
    subclass(MetricSpecificWeight::class, MetricSpecificWeight.serializer())
    subclass(UKImperialSpecificWeight::class, UKImperialSpecificWeight.serializer())
    subclass(USCustomarySpecificWeight::class, USCustomarySpecificWeight.serializer())
}
