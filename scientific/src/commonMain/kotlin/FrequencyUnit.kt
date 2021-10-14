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
sealed class Frequency : ScientificUnit<MeasurementType.Frequency>, SIScientificUnit<MeasurementType.Frequency>

object Hertz : Frequency(), BaseMetricUnit<MeasurementType.Frequency, MeasurementSystem.SI> {
    override val symbol: String = "Hz"
    override val system = MeasurementSystem.SI
    override val type = MeasurementType.Frequency
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanohertz : Frequency(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Frequency> by Nano(Hertz)
@Serializable
object Microhertz : Frequency(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Frequency> by Micro(Hertz)
@Serializable
object Millihertz : Frequency(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Frequency> by Milli(Hertz)
@Serializable
object Kilohertz : Frequency(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Frequency> by Kilo(Hertz)
@Serializable
object Megahertz : Frequency(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Frequency> by Mega(Hertz)
@Serializable
object Gigahertz : Frequency(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Frequency> by Giga(Hertz)
@Serializable
object BeatsPerMinute : Frequency() {
    override val symbol: String = "bpm"
    override val system = MeasurementSystem.SI
    override val type = MeasurementType.Frequency
    override fun fromSIUnit(value: Decimal): Decimal = value * 60.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 60.0.toDecimal()
}
@Serializable
object RoundsPerMinute : Frequency() {
    override val symbol: String = "rpm"
    override val system = MeasurementSystem.SI
    override val type = MeasurementType.Frequency
    override fun fromSIUnit(value: Decimal): Decimal = value * 60.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 60.0.toDecimal()
}

@JvmName("decimalDivSecond")
operator fun Decimal.div(second: ScientificValue<MeasurementType.Time, Second>): ScientificValue<MeasurementType.Frequency, Hertz> = ScientificValue(this / second.value, Hertz)
@JvmName("decimalDivMinute")
operator fun Decimal.div(minute: ScientificValue<MeasurementType.Time, Minute>): ScientificValue<MeasurementType.Frequency, BeatsPerMinute> = ScientificValue(this / minute.value, BeatsPerMinute)
fun <FrequencyUnit : Frequency, TimeUnit : Time> FrequencyUnit.frequency(cycle: Decimal, per: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Frequency, FrequencyUnit> = ScientificValue(cycle / per.convertValue(Second), Hertz).convert(this)

operator fun <FrequencyUnit : Frequency, TimeUnit : Time> ScientificValue<MeasurementType.Frequency, FrequencyUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>): Decimal = convertValue(Hertz) * time.convertValue(Second)

@JvmName("decimalDivHertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Hertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.value, Second)
@JvmName("decimalDivNanohertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Nanohertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
@JvmName("decimalDivMicrohertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Microhertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
@JvmName("decimalDivMillihertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Millihertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
@JvmName("decimalDivKilohertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Kilohertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
@JvmName("decimalDivMegahertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Megahertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
@JvmName("decimalDivGigahertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Gigahertz>): ScientificValue<MeasurementType.Time, Second> = ScientificValue(this / frequency.convertValue(Hertz), Second)
@JvmName("decimalDivBPM")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, BeatsPerMinute>): ScientificValue<MeasurementType.Time, Hour> = ScientificValue(this / frequency.value, Hour)
@JvmName("decimalDivRPM")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, RoundsPerMinute>): ScientificValue<MeasurementType.Time, Hour> = ScientificValue(this / frequency.value, Hour)
fun <FrequencyUnit : Frequency, TimeUnit : Time> TimeUnit.time(cycle: Decimal, at: ScientificValue<MeasurementType.Frequency, FrequencyUnit>): ScientificValue<MeasurementType.Time, TimeUnit> = ScientificValue(cycle / at.convertValue(Hertz), Second).convert(this)
