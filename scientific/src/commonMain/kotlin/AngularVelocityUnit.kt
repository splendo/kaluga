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
data class AngularVelocity(val angle: Angle, val per: Time) : AbstractScientificUnit<MeasurementType.AngularVelocity>(), MetricAndImperialScientificUnit<MeasurementType.AngularVelocity> {
    override val type = MeasurementType.AngularVelocity
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy { "${angle.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = angle.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = angle.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

infix fun Angle.per(time: Time) = AngularVelocity(this, time)

@JvmName("angularVelocityFromAngleAndTime")
fun <
    AngleUnit : Angle,
    TimeUnit : Time,
    VelocityUnit : AngularVelocity
    > VelocityUnit.velocity(
    angle: ScientificValue<MeasurementType.Angle, AngleUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(angle, time)

@JvmName("timeFromAngleAndAngularVelocity")
fun <
    AngleUnit : Angle,
    TimeUnit : Time
    > TimeUnit.time(
    angle: ScientificValue<MeasurementType.Angle, AngleUnit>,
    velocity: ScientificValue<MeasurementType.AngularVelocity, AngularVelocity>
) = byDividing(angle, velocity)

@JvmName("angularVelocityFromAngularAccelerationAndTime")
fun <
    TimeUnit : Time,
    > AngularVelocity.velocity(
    acceleration: ScientificValue<MeasurementType.AngularAcceleration, AngularAcceleration>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(acceleration, time)

@JvmName("angleDivTime")
infix operator fun <AngleUnit : Angle , TimeUnit : Time> ScientificValue<MeasurementType.Angle, AngleUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).velocity(this, time)
@JvmName("angleDivAngularVelocity")
infix operator fun <AngleUnit : Angle> ScientificValue<MeasurementType.Angle, AngleUnit>.div(angularVelocity: ScientificValue<MeasurementType.AngularVelocity, AngularVelocity>) = Second.time(this, angularVelocity)

@JvmName("angularAccelerationTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.AngularAcceleration, AngularAcceleration>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.angularVelocity.velocity(this, time)
@JvmName("timeTimesAngularAcceleration")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(angularAcceleration: ScientificValue<MeasurementType.AngularAcceleration, AngularAcceleration>) = angularAcceleration * this
