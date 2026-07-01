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
 * Set of all [MetricTorque]
 */
val MetricTorqueUnits: Set<MetricTorque> get() = MetricForceUnits.flatMap { force ->
    MetricLengthUnits.map {
        MetricTorque(force, it)
    }
}.toSet()

/**
 * Set of all [ImperialTorque]
 */
val ImperialTorqueUnits: Set<ImperialTorque> get() = ImperialForceUnits.flatMap { force ->
    ImperialLengthUnits.map {
        ImperialTorque(force, it)
    }
}.toSet()

/**
 * Set of all [UKImperialTorque]
 */
val UKImperialTorqueUnits: Set<UKImperialTorque> get() = UKImperialForceUnits.flatMap { force ->
    ImperialLengthUnits.map {
        UKImperialTorque(force, it)
    }
}.toSet()

/**
 * Set of all [USCustomaryTorque]
 */
val USCustomaryTorqueUnits: Set<USCustomaryTorque> get() = USCustomaryForceUnits.flatMap { force ->
    ImperialLengthUnits.map {
        USCustomaryTorque(force, it)
    }
}.toSet()

/**
 * Set of all [Torque]
 */
val TorqueUnits: Set<Torque> get() = MetricTorqueUnits +
    ImperialTorqueUnits +
    UKImperialTorqueUnits.filter { it.force !is UKImperialImperialForceWrapper }.toSet() +
    USCustomaryTorqueUnits.filter { it.force !is USCustomaryImperialForceWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Torque]
 * SI unit is `Newton x Meter`
 */
@Serializable
sealed class Torque : DefinedScientificUnit<PhysicalQuantity.Torque>() {

    /**
     * The [Force] component
     */
    abstract val force: Force

    /**
     * The [Length] component
     */
    abstract val length: Length
    override val quantity = PhysicalQuantity.Torque
    override val symbol: String by lazy { "${force.symbol}⋅${length.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = length.fromSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(length.toSIUnit(value))
}

/**
 * A [Torque] for [MeasurementSystem.Metric]
 * @param force the [MetricForce] component
 * @param length the [MetricLength] component
 */
@Serializable
data class MetricTorque(override val force: MetricForce, override val length: MetricLength) :
    Torque(),
    MetricScientificUnit<PhysicalQuantity.Torque> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Torque] for [MeasurementSystem.Imperial]
 * @param force the [ImperialForce] component
 * @param length the [ImperialLength] component
 */
@Serializable
data class ImperialTorque(override val force: ImperialForce, override val length: ImperialLength) :
    Torque(),
    ImperialScientificUnit<PhysicalQuantity.Torque> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialTorque] equivalent to this [ImperialTorque]
     */
    val ukImperial get() = UKImperialTorque(force.ukImperial, length)

    /**
     * The [USCustomaryTorque] equivalent to this [ImperialTorque]
     */
    val usCustomary get() = USCustomaryTorque(force.usCustomary, length)
}

/**
 * A [Torque] for [MeasurementSystem.UKImperial]
 * @param force the [UKImperialForce] component
 * @param length the [ImperialLength] component
 */
@Serializable
data class UKImperialTorque(override val force: UKImperialForce, override val length: ImperialLength) :
    Torque(),
    UKImperialScientificUnit<PhysicalQuantity.Torque> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [Torque] for [MeasurementSystem.USCustomary]
 * @param force the [USCustomaryForce] component
 * @param length the [ImperialLength] component
 */
@Serializable
data class USCustomaryTorque(override val force: USCustomaryForce, override val length: ImperialLength) :
    Torque(),
    USCustomaryScientificUnit<PhysicalQuantity.Torque> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricTorque] from a [MetricForce] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricTorque] represented by the units
 */
infix fun MetricForce.x(length: MetricLength) = MetricTorque(this, length)

/**
 * Gets an [ImperialTorque] from an [ImperialForce] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialTorque] represented by the units
 */
infix fun ImperialForce.x(length: ImperialLength) = ImperialTorque(this, length)

/**
 * Gets a [UKImperialTorque] from a [UKImperialForce] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [UKImperialTorque] represented by the units
 */
infix fun UKImperialForce.x(length: ImperialLength) = UKImperialTorque(this, length)

/**
 * Gets a [USCustomaryTorque] from a [USCustomaryForce] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [USCustomaryTorque] represented by the units
 */
infix fun USCustomaryForce.x(length: ImperialLength) = USCustomaryTorque(this, length)

internal fun SerializersModuleBuilder.setupForTorque() {
    polymorphic(Torque::class) {
        registerTorqueClasses()
    }
}

internal fun PolymorphicModuleBuilder<Torque>.registerTorqueClasses() {
    subclass(ImperialTorque::class, ImperialTorque.serializer())
    subclass(MetricTorque::class, MetricTorque.serializer())
    subclass(UKImperialTorque::class, UKImperialTorque.serializer())
    subclass(USCustomaryTorque::class, USCustomaryTorque.serializer())
}
