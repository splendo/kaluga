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
sealed class Frequency : ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency>

object Hertz : Frequency(), BaseMetricUnit<MeasurementType.Frequency, MeasurementSystem.Global> {
    override val symbol: String = "Hz"
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanohertz : Frequency(), ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency> by Nano(Hertz)
@Serializable
object Microhertz : Frequency(), ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency> by Micro(Hertz)
@Serializable
object Millihertz : Frequency(), ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency> by Milli(Hertz)
@Serializable
object Kilohertz : Frequency(), ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency> by Kilo(Hertz)
@Serializable
object Megahertz : Frequency(), ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency> by Mega(Hertz)
@Serializable
object Gigahertz : Frequency(), ScientificUnit<MeasurementSystem.Global, MeasurementType.Frequency> by Giga(Hertz)
@Serializable
object BeatsPerMinute : Frequency() {
    override val symbol: String = "bpm"
    override fun fromSIUnit(value: Decimal): Decimal = value * 60.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 60.0.toDecimal()
}
@Serializable
object RoundsPerMinute : Frequency() {
    override val symbol: String = "rpm"
    override fun fromSIUnit(value: Decimal): Decimal = value * 60.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 60.0.toDecimal()
}

operator fun Decimal.div(second: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second>): ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Hertz> = ScientificValue(this / second.value, Hertz)
operator fun Decimal.div(minute: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Minute>): ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, BeatsPerMinute> = ScientificValue(this / minute.value, BeatsPerMinute)
fun <FrequencyUnit : Frequency, TimeUnit : Time> FrequencyUnit.frequency(cycle: Decimal, per: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, FrequencyUnit> = ScientificValue(cycle / per.convertValue(Second), Hertz).convert(this)

operator fun <FrequencyUnit : Frequency, TimeUnit : Time> ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, FrequencyUnit>.times(time: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, TimeUnit>): Decimal = convertValue(Hertz) * time.convertValue(Second)

operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Hertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.value, Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Nanohertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Microhertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Millihertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Kilohertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Megahertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, Gigahertz>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, BeatsPerMinute>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Hour> = ScientificValue(this / frequency.value, Hour)
operator fun Decimal.div(frequency: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, RoundsPerMinute>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Hour> = ScientificValue(this / frequency.value, Hour)
fun <FrequencyUnit : Frequency, TimeUnit : Time> TimeUnit.time(cycle: Decimal, at: ScientificValue<MeasurementSystem.Global, MeasurementType.Frequency, FrequencyUnit>): ScientificValue<MeasurementSystem.Global, MeasurementType.Time, TimeUnit> = ScientificValue(cycle / at.convertValue(Hertz), Second).convert(this)
