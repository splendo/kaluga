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

@Serializable
sealed class Speed<System : MeasurementSystem, LengthUnit : Length<System>> : AbstractScientificUnit<System, MeasurementType.Speed>() {
    abstract val distance: LengthUnit
    abstract val per: Time
    override val symbol: String by lazy { "${distance.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = distance.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = distance.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricSpeed(override val distance: MetricLength, override val per: Time) : Speed<MeasurementSystem.Metric, MetricLength>()

@Serializable
data class ImperialSpeed(override val distance: ImperialLength, override val per: Time) : Speed<MeasurementSystem.Imperial, ImperialLength>()

infix fun MetricLength.per(time: Time) = MetricSpeed(this, time)
infix fun ImperialLength.per(time: Time) = ImperialSpeed(this, time)
infix fun Length<*>.per(time: Time) = when (this) {
    is MetricLength -> this per time
    is ImperialLength -> this per time
}

inline infix fun <
    System : MeasurementSystem,
    LengthUnit : Length<System>,
    reified SpeedUnit : Speed<System, LengthUnit>,
    > ScientificValue<
    System,
    MeasurementType.Length,
    LengthUnit,
    >.per(time: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Time>): ScientificValue<System, MeasurementType.Speed, SpeedUnit> {
    val speedUnit = (unit per time.unit) as SpeedUnit
    return ScientificValue(value / time.value, speedUnit)
}

infix operator fun <
    System : MeasurementSystem,
    LengthUnit : Length<System>,
    SpeedUnit : Speed<System, LengthUnit>
    >  ScientificValue<System, MeasurementType.Speed, SpeedUnit>.times(time: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Time>) : ScientificValue<System, MeasurementType.Length, LengthUnit> {
        return ScientificValue(value * time.convertValue(unit.per), unit.distance)
    }

infix operator fun <
    LengthSystem : MeasurementSystem,
    LengthUnit : Length<LengthSystem>,
    SpeedSystem : MeasurementSystem,
    SpeedLengthUnit : Length<SpeedSystem>,
    SpeedUnit : Speed<SpeedSystem, SpeedLengthUnit>
    >  ScientificValue<LengthSystem, MeasurementType.Length, LengthUnit>.div(speed: ScientificValue<SpeedSystem, MeasurementType.Speed, SpeedUnit>) : ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Time> {
    return ScientificValue(convertValue(speed.unit.distance) / speed.value, speed.unit.per)
}
