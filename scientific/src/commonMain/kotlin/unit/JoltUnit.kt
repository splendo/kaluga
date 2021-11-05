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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val MetricJoltUnits: Set<MetricJolt> get() = MetricAccelerationUnits.flatMap { acceleration ->
    TimeUnits.map { acceleration per it }
}.toSet()

val ImperialJoltUnits: Set<ImperialJolt> get() = ImperialAccelerationUnits.flatMap { acceleration ->
    TimeUnits.map { acceleration per it }
}.toSet()

val JoltUnits: Set<Jolt> get() = MetricJoltUnits +
    ImperialJoltUnits

@Serializable
sealed class Jolt : AbstractScientificUnit<PhysicalQuantity.Jolt>() {
    abstract val acceleration: Acceleration
    abstract val per: Time
    override val type = PhysicalQuantity.Jolt
    override val symbol: String by lazy {
        val perSymbol = when {
            per == acceleration.per && acceleration.speed.per == per -> "${per.symbol}3"
            per == acceleration.per -> "${per.symbol}2*${acceleration.speed.per}"
            per == acceleration.speed.per -> "${per.symbol}2*${acceleration.per}"
            acceleration.per == acceleration.speed.per -> "${per.symbol}*${acceleration.per}2"
            else -> "${per.symbol}*${acceleration.per.symbol}*${acceleration.speed.per.symbol}"
        }
        "${acceleration.speed.distance.symbol} / $perSymbol"
    }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(acceleration.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = acceleration.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricJolt(override val acceleration: MetricAcceleration, override val per: Time) : Jolt(), MetricScientificUnit<PhysicalQuantity.Jolt> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialJolt(override val acceleration: ImperialAcceleration, override val per: Time) : Jolt(), ImperialScientificUnit<PhysicalQuantity.Jolt> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricAcceleration.per(time: Time) = MetricJolt(this, time)
infix fun ImperialAcceleration.per(time: Time) = ImperialJolt(this, time)
infix fun Acceleration.per(time: Time): Jolt = when (this) {
    is MetricAcceleration -> this per time
    is ImperialAcceleration -> this per time
}
