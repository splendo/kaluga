/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands
 
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
import com.splendo.kaluga.scientific.MeasurementType
import kotlinx.serialization.Serializable

val MetricYankUnits: Set<MetricYank> get() = MetricForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

val ImperialYankUnits: Set<ImperialYank> get() = ImperialForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

val UKImperialYankUnits: Set<UKImperialYank> get() = UKImperialForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

val USCustomaryYankUnits: Set<USCustomaryYank> get() = USCustomaryForceUnits.flatMap { force ->
    TimeUnits.map { force per it }
}.toSet()

val YankUnits: Set<Yank> get() = MetricYankUnits +
    ImperialYankUnits +
    UKImperialYankUnits.filter { it.force !is UKImperialImperialForceWrapper }.toSet() +
    USCustomaryYankUnits.filter { it.force !is USCustomaryImperialForceWrapper }.toSet()

@Serializable
sealed class Yank : AbstractScientificUnit<MeasurementType.Yank>() {
    abstract val force: Force
    abstract val per: Time
    override val type = MeasurementType.Yank
    override val symbol: String by lazy { "${force.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricYank(override val force: MetricForce, override val per: Time) : Yank(), MetricScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialYank(override val force: ImperialForce, override val per: Time) : Yank(), ImperialScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = UKImperialYank(force.ukImperial, per)
    val usCustomary get() = USCustomaryYank(force.usCustomary, per)
}
@Serializable
data class UKImperialYank(override val force: UKImperialForce, override val per: Time) : Yank(), UKImperialScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryYank(override val force: USCustomaryForce, override val per: Time) : Yank(), USCustomaryScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricForce.per(time: Time) = MetricYank(this, time)
infix fun ImperialForce.per(time: Time) = ImperialYank(this, time)
infix fun UKImperialForce.per(time: Time) = UKImperialYank(this, time)
infix fun USCustomaryForce.per(time: Time) = USCustomaryYank(this, time)
