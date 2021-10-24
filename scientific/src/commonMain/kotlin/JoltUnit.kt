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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
sealed class Jolt : AbstractScientificUnit<MeasurementType.Jolt>() {
    abstract val acceleration: Acceleration
    abstract val per: Time
    override val type = MeasurementType.Jolt
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
    override fun fromSIUnit(value: Decimal): Decimal = acceleration.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = acceleration.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricJolt(override val acceleration: MetricAcceleration, override val per: Time) : Jolt(), MetricScientificUnit<MeasurementType.Jolt> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialJolt(override val acceleration: ImperialAcceleration, override val per: Time) : Jolt(), ImperialScientificUnit<MeasurementType.Jolt> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricAcceleration.per(time: Time) = MetricJolt(this, time)
infix fun ImperialAcceleration.per(time: Time) = ImperialJolt(this, time)
infix fun Acceleration.per(time: Time): Jolt = when (this) {
    is MetricAcceleration -> this per time
    is ImperialAcceleration -> this per time
}

@JvmName("metricAccelerationDivTime")
infix operator fun ScientificValue<
    MeasurementType.Acceleration,
    MetricAcceleration,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.Jolt, MetricJolt> = (value / time.value)(unit per time.unit)

@JvmName("imperialAccelerationDivTime")
infix operator fun ScientificValue<
    MeasurementType.Acceleration,
    ImperialAcceleration,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.Jolt, ImperialJolt> = (value / time.value)(unit per time.unit)

@JvmName("speedDivTime")
infix operator fun <
    AccelerationUnit : Acceleration,
    > ScientificValue<
    MeasurementType.Acceleration,
    AccelerationUnit,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.Jolt, Jolt> = (value / time.value)(unit per time.unit)

@JvmName("metricJoltTimesTime")
infix operator fun ScientificValue<MeasurementType.Jolt, MetricJolt>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Acceleration, MetricAcceleration> = (value * time.convertValue(unit.per))(unit.acceleration)
@JvmName("timeTimesMetricJolt")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(jolt: ScientificValue<MeasurementType.Jolt, MetricJolt>) : ScientificValue<MeasurementType.Acceleration, MetricAcceleration> = jolt * this
@JvmName("imperialJoltTimesTime")
infix operator fun  ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Acceleration, ImperialAcceleration> = (value * time.convertValue(unit.per))(unit.acceleration)
@JvmName("timeTimesImperialJolt")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) : ScientificValue<MeasurementType.Acceleration, ImperialAcceleration> = jolt * this
@JvmName("speedTimesTime")
infix operator fun  ScientificValue<MeasurementType.Jolt, Jolt>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Acceleration, Acceleration> = (value * time.convertValue(unit.per))(unit.acceleration)
@JvmName("timeTimesJolt")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(jolt: ScientificValue<MeasurementType.Jolt, Jolt>) : ScientificValue<MeasurementType.Acceleration, Acceleration> = jolt * this

@JvmName("accelerationDivJolt")
infix operator fun <
    AccelerationUnit : Acceleration,
    JoltUnit : Jolt
    >  ScientificValue<MeasurementType.Acceleration, AccelerationUnit>.div(jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>) : ScientificValue<MeasurementType.Time, Time> = (convertValue(jolt.unit.acceleration) / jolt.value)(jolt.unit.per)