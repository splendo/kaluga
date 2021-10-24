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
data class AngularAcceleration(val angularVelocity: AngularVelocity, val per: Time) : AbstractScientificUnit<MeasurementType.AngularAcceleration>(), MetricAndImperialScientificUnit<MeasurementType.AngularAcceleration> {
    override val type = MeasurementType.AngularAcceleration
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy {
        if (angularVelocity.per == per) {
            "${angularVelocity.angle.symbol} / ${per.symbol}2"
        } else {
            "${angularVelocity.angle.symbol} / (${angularVelocity.per.symbol} * ${per.symbol})"
        }
    }
    override fun fromSIUnit(value: Decimal): Decimal = angularVelocity.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = angularVelocity.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

infix fun AngularVelocity.per(time: Time) = AngularAcceleration(this, time)

@JvmName("angularVelocityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.AngularVelocity, AngularVelocity>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (value / time.value)(unit per time.unit)
@JvmName("angularAccelerationTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.AngularAcceleration, AngularAcceleration>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (value * time.convertValue(unit.per))(unit.angularVelocity)
@JvmName("timeTimesAngularAcceleration")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(angularAcceleration: ScientificValue<MeasurementType.AngularAcceleration, AngularAcceleration>) = angularAcceleration * this
@JvmName("angularVelocityDivAngularAcceleration")
infix operator fun ScientificValue<MeasurementType.AngularVelocity, AngularVelocity>.div(angularAcceleration: ScientificValue<MeasurementType.AngularAcceleration, AngularAcceleration>) : ScientificValue<MeasurementType.Time, Time> = (convertValue(angularAcceleration.unit.angularVelocity) / angularAcceleration.value)(angularAcceleration.unit.per)
