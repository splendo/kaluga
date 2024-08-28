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
 * Set of all [MetricSurfaceTension]
 */
val MetricSurfaceTensionUnits: Set<MetricSurfaceTension> get() = MetricForceUnits.flatMap { force ->
    MetricLengthUnits.map { force per it }
}.toSet()

/**
 * Set of all [ImperialSurfaceTension]
 */
val ImperialSurfaceTensionUnits: Set<ImperialSurfaceTension> get() = ImperialForceUnits.flatMap { force ->
    ImperialLengthUnits.map { force per it }
}.toSet()

/**
 * Set of all [UKImperialSurfaceTension]
 */
val UKImperialSurfaceTensionUnits: Set<UKImperialSurfaceTension> get() = UKImperialForceUnits.flatMap { force ->
    ImperialLengthUnits.map { force per it }
}.toSet()

/**
 * Set of all [USCustomarySurfaceTension]
 */
val USCustomarySurfaceTensionUnits: Set<USCustomarySurfaceTension> get() = USCustomaryForceUnits.flatMap { force ->
    ImperialLengthUnits.map { force per it }
}.toSet()

/**
 * Set of all [SurfaceTension]
 */
val SurfaceTensionUnits: Set<SurfaceTension> get() = MetricSurfaceTensionUnits +
    ImperialSurfaceTensionUnits +
    UKImperialSurfaceTensionUnits.filter { it.force !is UKImperialImperialForceWrapper }.toSet() +
    USCustomarySurfaceTensionUnits.filter { it.force !is USCustomaryImperialForceWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.SurfaceTension]
 * SI unit is `Newton per Meter`
 */
@Serializable
sealed class SurfaceTension : AbstractScientificUnit<PhysicalQuantity.SurfaceTension>() {

    /**
     * The [Force] component
     */
    abstract val force: Force

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${force.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.SurfaceTension
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [SurfaceTension] for [MeasurementSystem.Metric]
 * @param force the [MetricForce] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricSurfaceTension(override val force: MetricForce, override val per: MetricLength) :
    SurfaceTension(),
    MetricScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [SurfaceTension] for [MeasurementSystem.Imperial]
 * @param force the [ImperialForce] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialSurfaceTension(override val force: ImperialForce, override val per: ImperialLength) :
    SurfaceTension(),
    ImperialScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialSurfaceTension] equivalent to this [ImperialSurfaceTension]
     */
    val ukImperial get() = force.ukImperial per per

    /**
     * The [USCustomarySurfaceTension] equivalent to this [ImperialSurfaceTension]
     */
    val usCustomary get() = force.usCustomary per per
}

/**
 * A [SurfaceTension] for [MeasurementSystem.USCustomary]
 * @param force the [USCustomaryForce] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class USCustomarySurfaceTension(override val force: USCustomaryForce, override val per: ImperialLength) :
    SurfaceTension(),
    USCustomaryScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [SurfaceTension] for [MeasurementSystem.UKImperial]
 * @param force the [UKImperialForce] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class UKImperialSurfaceTension(override val force: UKImperialForce, override val per: ImperialLength) :
    SurfaceTension(),
    UKImperialScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricSurfaceTension] from a [MetricForce] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricSurfaceTension] represented by the units
 */
infix fun MetricForce.per(length: MetricLength) = MetricSurfaceTension(this, length)

/**
 * Gets an [ImperialSurfaceTension] from an [ImperialForce] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialSurfaceTension] represented by the units
 */
infix fun ImperialForce.per(length: ImperialLength) = ImperialSurfaceTension(this, length)

/**
 * Gets a [USCustomarySurfaceTension] from a [USCustomaryForce] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [USCustomarySurfaceTension] represented by the units
 */
infix fun USCustomaryForce.per(length: ImperialLength) = USCustomarySurfaceTension(this, length)

/**
 * Gets a [UKImperialSurfaceTension] from a [UKImperialForce] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [UKImperialSurfaceTension] represented by the units
 */
infix fun UKImperialForce.per(length: ImperialLength) = UKImperialSurfaceTension(this, length)
