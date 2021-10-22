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
sealed class Frequency : ScientificUnit<MeasurementType.Frequency>, MetricAndImperialScientificUnit<MeasurementType.Frequency>

object Hertz : Frequency(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency> {
    override val symbol: String = "Hz"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Frequency
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanohertz : Frequency(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz> by Nano(Hertz)
@Serializable
object Microhertz : Frequency(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz> by Micro(Hertz)
@Serializable
object Millihertz : Frequency(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz> by Milli(Hertz)
@Serializable
object Kilohertz : Frequency(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz> by Kilo(Hertz)
@Serializable
object Megahertz : Frequency(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz> by Mega(Hertz)
@Serializable
object Gigahertz : Frequency(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz> by Giga(Hertz)
@Serializable
object BeatsPerMinute : Frequency() {
    override val symbol: String = "bpm"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Frequency
    override fun fromSIUnit(value: Decimal): Decimal = value * 60.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 60.0.toDecimal()
}
@Serializable
object RoundsPerMinute : Frequency() {
    override val symbol: String = "rpm"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Frequency
    override fun fromSIUnit(value: Decimal): Decimal = value * 60.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 60.0.toDecimal()
}

fun <
    TimeUnit : Time,
    FrequencyUnit : Frequency
    > FrequencyUnit.frequency(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Frequency, FrequencyUnit> = byInverting(time)

fun <
    FrequencyUnit : Frequency,
    TimeUnit : Time
    > FrequencyUnit.frequency(cycle: Decimal, per: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Frequency, FrequencyUnit> = (cycle / per.convertValue(Second))(Hertz).convert(this)


fun <
    TimeUnit : Time,
    FrequencyUnit : Frequency
    > TimeUnit.time(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>): ScientificValue<MeasurementType.Time, TimeUnit> = byInverting(frequency)

fun <
    FrequencyUnit : Frequency,
    TimeUnit : Time
    > TimeUnit.time(cycle: Decimal, at: ScientificValue<MeasurementType.Frequency, FrequencyUnit>): ScientificValue<MeasurementType.Time, TimeUnit> = (cycle / at.convertValue(Hertz))(Second).convert(this)

@JvmName("frequencyFromConductanceAndCapacity")
fun <
    CapacitanceUnit : ElectricCapacitance,
    ConductanceUnit : ElectricConductance,
    FrequencyUnit : Frequency
    > FrequencyUnit.frequency(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>
): ScientificValue<MeasurementType.Frequency, FrequencyUnit> = byDividing(conductance, capacitance)

@JvmName("frequencyFromReistanceAndInductance")
fun <
    ResistanceUnit : ElectricResistance,
    FrequencyUnit : Frequency,
    InductanceUnit : ElectricInductance
    >
    FrequencyUnit.frequency(
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>,
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>
) : ScientificValue<MeasurementType.Frequency, FrequencyUnit> = byDividing(resistance, inductance)

@JvmName("decimalDivSecond")
operator fun Decimal.div(second: ScientificValue<MeasurementType.Time, Second>): ScientificValue<MeasurementType.Frequency, Hertz> = (this / second.value)(Hertz)
@JvmName("decimalDivMinute")
operator fun Decimal.div(minute: ScientificValue<MeasurementType.Time, Minute>): ScientificValue<MeasurementType.Frequency, BeatsPerMinute> = (this / minute.value)(BeatsPerMinute)

@JvmName("frequencyTimesTime")
operator fun <FrequencyUnit : Frequency, TimeUnit : Time> ScientificValue<MeasurementType.Frequency, FrequencyUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>): Decimal = convertValue(Hertz) * time.convertValue(Second)
@JvmName("timeTimesFrequency")
operator fun <FrequencyUnit : Frequency, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>): Decimal = frequency * this

@JvmName("decimalDivHertz")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, Hertz>): ScientificValue<MeasurementType.Time, Second> = (this / frequency.value)(Second)
@JvmName("decimalDivHertzMultiple")
operator fun <M : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Frequency, Hertz>> Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, M>): ScientificValue<MeasurementType.Time, Second> = (this / frequency.convertValue(Hertz))(Second)
@JvmName("decimalDivBPM")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, BeatsPerMinute>): ScientificValue<MeasurementType.Time, Hour> = (this / frequency.value)(Hour)
@JvmName("decimalDivRPM")
operator fun Decimal.div(frequency: ScientificValue<MeasurementType.Frequency, RoundsPerMinute>): ScientificValue<MeasurementType.Time, Hour> = (this / frequency.value)(Hour)

@JvmName("conductanceDivCapacitance")
operator fun <ConductanceUnit : ElectricConductance, CapacitanceUnit : ElectricCapacitance> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.div(capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>) = Hertz.frequency(this, capacitance)
@JvmName("resistanceDivInductance")
operator fun <ResistanceUnit : ElectricResistance, InductanceUnit : ElectricInductance> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.div(inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>) = Hertz.frequency(this, inductance)
