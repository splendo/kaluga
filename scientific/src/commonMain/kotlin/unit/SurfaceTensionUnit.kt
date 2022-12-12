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

val MetricSurfaceTensionUnits: Set<MetricSurfaceTension> get() = MetricForceUnits.flatMap { force ->
    MetricLengthUnits.map { force per it }
}.toSet()

val ImperialSurfaceTensionUnits: Set<ImperialSurfaceTension> get() = ImperialForceUnits.flatMap { force ->
    ImperialLengthUnits.map { force per it }
}.toSet()

val UKImperialSurfaceTensionUnits: Set<UKImperialSurfaceTension> get() = UKImperialForceUnits.flatMap { force ->
    ImperialLengthUnits.map { force per it }
}.toSet()

val USCustomarySurfaceTensionUnits: Set<USCustomarySurfaceTension> get() = USCustomaryForceUnits.flatMap { force ->
    ImperialLengthUnits.map { force per it }
}.toSet()

val SurfaceTensionUnits: Set<SurfaceTension> get() = MetricSurfaceTensionUnits +
    ImperialSurfaceTensionUnits +
    UKImperialSurfaceTensionUnits.filter { it.force !is UKImperialImperialForceWrapper }.toSet() +
    USCustomarySurfaceTensionUnits.filter { it.force !is USCustomaryImperialForceWrapper }.toSet()

@Serializable
sealed class SurfaceTension : AbstractScientificUnit<PhysicalQuantity.SurfaceTension>() {
    abstract val force: Force
    abstract val per: Length
    override val symbol: String by lazy { "${force.symbol} / ${per.symbol}" }
    override val quantity = PhysicalQuantity.SurfaceTension
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricSurfaceTension(override val force: MetricForce, override val per: MetricLength) : SurfaceTension(), MetricScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialSurfaceTension(override val force: ImperialForce, override val per: ImperialLength) : SurfaceTension(), ImperialScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = force.ukImperial per per
    val usCustomary get() = force.usCustomary per per
}
@Serializable
data class USCustomarySurfaceTension(override val force: USCustomaryForce, override val per: ImperialLength) : SurfaceTension(), USCustomaryScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialSurfaceTension(override val force: UKImperialForce, override val per: ImperialLength) : SurfaceTension(), UKImperialScientificUnit<PhysicalQuantity.SurfaceTension> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricForce.per(length: MetricLength) = MetricSurfaceTension(this, length)
infix fun ImperialForce.per(length: ImperialLength) = ImperialSurfaceTension(this, length)
infix fun USCustomaryForce.per(length: ImperialLength) = USCustomarySurfaceTension(this, length)
infix fun UKImperialForce.per(length: ImperialLength) = UKImperialSurfaceTension(this, length)
