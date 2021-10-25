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
sealed class Speed : AbstractScientificUnit<MeasurementType.Speed>() {
    abstract val distance: Length
    abstract val per: Time
    override val type = MeasurementType.Speed
    override val symbol: String by lazy { "${distance.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = distance.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = distance.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricSpeed(override val distance: MetricLength, override val per: Time) : Speed(), MetricScientificUnit<MeasurementType.Speed> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialSpeed(override val distance: ImperialLength, override val per: Time) : Speed(), ImperialScientificUnit<MeasurementType.Speed> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricLength.per(time: Time) = MetricSpeed(this, time)
infix fun ImperialLength.per(time: Time) = ImperialSpeed(this, time)

val MetricSpeedOfLight = 299792458(Meter per Second)
val ImperialSpeedOfLight = MetricSpeedOfLight.convert(Foot per Second)

@JvmName("speedFromDistanceAndTime")
fun <
    LengthUnit : Length,
    TimeUnit : Time,
    SpeedUnit : Speed
    > SpeedUnit.speed(
    distance: ScientificValue<MeasurementType.Length, LengthUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(distance, time)

@JvmName("timeFromDistanceAndSpeed")
fun <
    LengthUnit : Length,
    TimeUnit : Time,
    SpeedUnit : Speed
    > TimeUnit.time(
    distance: ScientificValue<MeasurementType.Length, LengthUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>
) = byDividing(distance, speed)

@JvmName("speedFromMomentumAndMass")
fun <
    WeightUnit : Weight,
    SpeedUnit : Speed,
    MomentumUnit : Momentum
    > SpeedUnit.speed(
    momentum: ScientificValue<MeasurementType.Momentum, MomentumUnit>,
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byDividing(momentum, mass)

@JvmName("speedFromAccelerationAndTime")
fun <
    AccelerationUnit : Acceleration,
    TimeUnit : Time,
    SpeedUnit : Speed
    > SpeedUnit.speed(
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(acceleration, time)

@JvmName("metricLengthDivTime")
operator fun <LengthUnit : MetricLength, > ScientificValue<MeasurementType.Length, LengthUnit>.div(time: ScientificValue<MeasurementType.Time, Time>) = (unit per time.unit).speed(this, time)
@JvmName("imperialLengthDivTime")
operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(time: ScientificValue<MeasurementType.Time, Time>) = (unit per time.unit).speed(this, time)
@JvmName("lengthDivTime")
operator fun <LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.div(time: ScientificValue<MeasurementType.Time, Time>) = (Meter per Second).speed(this, time)

@JvmName("lengthDivSpeed")
infix operator fun <LengthUnit : Length, SpeedUnit : Speed> ScientificValue<MeasurementType.Length, LengthUnit>.div(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = speed.unit.per.time(this, speed)

@JvmName("metricMomentumDivMetricMass")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.speed.speed(this, mass)
@JvmName("imperialMomentumDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.speed.speed(this, mass)
@JvmName("ukImperialMomentumDivUKImperialMass")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.speed.speed(this, mass)
@JvmName("usCustomaryMomentumDivUSCustomaryMass")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.speed.speed(this, mass)
@JvmName("momentumDivMass")
infix operator fun <MomentumUnit : Momentum, WeightUnit : Weight> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Meter per Second).speed(this, mass)

@JvmName("metricAccelerationTimesTime")
infix operator fun <TimeUnit : Time>  ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.speed.speed(this, time)
@JvmName("timeTimesMetricAcceleration")
infix operator fun <TimeUnit : Time>  ScientificValue<MeasurementType.Time, TimeUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = acceleration * this
@JvmName("imperialAccelerationTimesTime")
infix operator fun <TimeUnit : Time>  ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.speed.speed(this, time)
@JvmName("timeTimesImperialAcceleration")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = acceleration * this
@JvmName("speedTimesTime")
infix operator fun <AccelerationUnit : Acceleration, TimeUnit : Time> ScientificValue<MeasurementType.Acceleration, AccelerationUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Meter per Second).speed(this, time)
@JvmName("timeTimesAcceleration")
infix operator fun <AccelerationUnit : Acceleration, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>) = acceleration * this
