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
    override fun fromSIUnit(value: Decimal): Decimal = speed.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = speed.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricAcceleration(override val speed: MetricSpeed, override val per: Time) : Acceleration(), MetricScientificUnit<MeasurementType.Acceleration> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialAcceleration(override val speed: ImperialSpeed, override val per: Time) : Acceleration(), ImperialScientificUnit<MeasurementType.Acceleration> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricSpeed.per(time: Time) = MetricAcceleration(this, time)
infix fun ImperialSpeed.per(time: Time) = ImperialAcceleration(this, time)

val MetricStandardGravityAcceleration = 9.80665(Meter per Second per Second)
val ImperialStandardGravityAcceleration = MetricStandardGravityAcceleration.convert(Foot per Second per Second)

@JvmName("accelerationFromSpeedAndTime")
fun <
    SpeedUnit : Speed,
    TimeUnit : Time,
    AccelerationUnit : Acceleration
    > AccelerationUnit.acceleration(
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>,
    time : ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(speed, time)

@JvmName("accelerationFromForceAndMass")
fun <
    MassUnit : ScientificUnit<MeasurementType.Weight>,
    AccelerationUnit : ScientificUnit<MeasurementType.Acceleration>,
    ForceUnit : ScientificUnit<MeasurementType.Force>
    > AccelerationUnit.acceleration(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    mass: ScientificValue<MeasurementType.Weight, MassUnit>
) : ScientificValue<MeasurementType.Acceleration, AccelerationUnit> = byDividing(force, mass)

@JvmName("metricSpeedDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, MetricSpeed>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).acceleration(this, time)
@JvmName("imperialSpeedDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, ImperialSpeed>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).acceleration(this, time)
@JvmName("speedDivTime")
infix operator fun <SpeedUnit : Speed, TimeUnit : Time> ScientificValue<MeasurementType.Speed, SpeedUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Meter per Second per time.unit).acceleration(this, time)

@JvmName("speedDivAcceleration")
infix operator fun <
    SpeedUnit : Speed,
    AccelerationUnit : Acceleration
    >  ScientificValue<MeasurementType.Speed, SpeedUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>) : ScientificValue<MeasurementType.Time, Time> = (convertValue(acceleration.unit.speed) / acceleration.value)(acceleration.unit.per)

@JvmName("newtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Newton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("newtonMultipleDivKilogram")
operator fun <M : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton>> ScientificValue<MeasurementType.Force, M>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("dyneDivGram")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("dyneMultipleDivGram")
operator fun <M : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne>> ScientificValue<MeasurementType.Force, M>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("poundalDivPound")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("poundForceDivPound")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("ounceForceDivOunce")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(mass: ScientificValue<MeasurementType.Weight, Ounce>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("grainForceDivGrain")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(mass: ScientificValue<MeasurementType.Weight, Grain>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("kipDivPound")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("usTonForceDivUsTon")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(mass: ScientificValue<MeasurementType.Weight, UsTon>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("imperialTonForceDivImperialTon")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("forceDivWeight")
operator fun <ForceUnit : Force, WeightUnit : Weight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Meter per Second per Second).acceleration(this, mass)
