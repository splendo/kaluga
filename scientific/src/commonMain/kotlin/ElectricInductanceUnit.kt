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
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
sealed class ElectricInductance : AbstractScientificUnit<MeasurementType.ElectricInductance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricInductance>

@Serializable
object Henry : ElectricInductance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance> {
    override val symbol = "H"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricInductance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Nano(Henry)
@Serializable
object MicroHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Micro(Henry)
@Serializable
object MilliHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Milli(Henry)
@Serializable
object CentiHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Centi(Henry)
@Serializable
object DeciHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Deci(Henry)
@Serializable
object DecaHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Deca(Henry)
@Serializable
object HectoHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Hecto(Henry)
@Serializable
object KiloHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Kilo(Henry)
@Serializable
object MegaHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Mega(Henry)
@Serializable
object GigaHenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Giga(Henry)

@JvmName("inductanceFromFluxAndCurrent")
fun <
    FluxUnit : MagneticFlux,
    CurrentUnit : ElectricCurrent,
    InductanceUnit : ElectricInductance
    >
    InductanceUnit.inductance(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) : ScientificValue<MeasurementType.ElectricInductance, InductanceUnit> = byDividing(flux, current)

@JvmName("inductanceFromResistanceAndFrequency")
fun <
    ResistanceUnit : ElectricResistance,
    FrequencyUnit : Frequency,
    InductanceUnit : ElectricInductance
    >
    InductanceUnit.inductance(
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>,
    frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>
) : ScientificValue<MeasurementType.ElectricInductance, InductanceUnit> = byDividing(resistance, frequency)

@JvmName("inductanceFromResistanceAndTime")
fun <
    ResistanceUnit : ElectricResistance,
    TimeUnit : Time,
    InductanceUnit : ElectricInductance
    >
    InductanceUnit.inductance(
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.ElectricInductance, InductanceUnit> = byMultiplying(resistance, time)

@JvmName("timeFromInductanceAndResistance")
fun <
    ResistanceUnit : ElectricResistance,
    TimeUnit : Time,
    InductanceUnit : ElectricInductance
    >
    TimeUnit.time(
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>,
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
) : ScientificValue<MeasurementType.Time, TimeUnit> = byDividing(inductance, resistance)

@JvmName("fluxDivCurrent")
infix operator fun <FluxUnit : MagneticFlux, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Henry.inductance(this, current)
@JvmName("resistanceDivFrequency")
infix operator fun <ResistanceUnit : ElectricResistance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.div(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Henry.inductance(this, frequency)
@JvmName("resistanceTimesTime")
infix operator fun <ResistanceUnit : ElectricResistance, TimeUnit : Time> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Henry.inductance(this, time)
@JvmName("timeTimesResistance")
infix operator fun <ResistanceUnit : ElectricResistance, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = resistance * this
@JvmName("inductanceDivResistance")
infix operator fun <InductanceUnit : ElectricInductance, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.div(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = Second.time(this, resistance)
