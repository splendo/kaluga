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
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable
import kotlin.native.concurrent.ThreadLocal

val MetricAccelerationUnits: Set<MetricAcceleration> get() = MetricSpeedUnits.flatMap { speed ->
    TimeUnits.map { speed per it }
}.toSet()

val ImperialAccelerationUnits: Set<ImperialAcceleration> get() = ImperialSpeedUnits.flatMap { speed ->
    TimeUnits.map { speed per it }
}.toSet()

val AccelerationUnits: Set<Acceleration> get() = MetricAccelerationUnits +
    ImperialAccelerationUnits

@Serializable
sealed class Acceleration : AbstractScientificUnit<MeasurementType.Acceleration>() {
    abstract val speed: Speed
    abstract val per: Time
    override val type = MeasurementType.Acceleration
    override val symbol: String by lazy { if (speed.per == per) {
        "${speed.distance.symbol} / ${per.symbol}2"
    } else {
        "${speed.distance.symbol} / (${speed.per.symbol} * ${per.symbol})"
    }
    }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(speed.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = speed.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricAcceleration(override val speed: MetricSpeed, override val per: Time) : Acceleration(),
    MetricScientificUnit<MeasurementType.Acceleration> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialAcceleration(override val speed: ImperialSpeed, override val per: Time) : Acceleration(),
    ImperialScientificUnit<MeasurementType.Acceleration> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricSpeed.per(time: Time) = MetricAcceleration(this, time)
infix fun ImperialSpeed.per(time: Time) = ImperialAcceleration(this, time)

@ThreadLocal
val MetricStandardGravityAcceleration = 9.80665(Meter per Second per Second)
@ThreadLocal
val ImperialStandardGravityAcceleration = MetricStandardGravityAcceleration.convert(Foot per Second per Second)
