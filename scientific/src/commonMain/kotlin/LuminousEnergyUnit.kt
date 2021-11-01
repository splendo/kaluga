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

val LuminousEnergyUnits: Set<LuminousEnergy> = LuminousFluxUnits.flatMap { flux ->
    TimeUnits.map { flux x it }
}.toSet()

@Serializable
class LuminousEnergy(val luminousFlux: LuminousFlux, val time: Time) : MetricAndImperialScientificUnit<MeasurementType.LuminousEnergy> {
    override val symbol: String = "${luminousFlux.symbol}⋅${time.symbol}"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.LuminousEnergy
    override fun fromSIUnit(value: Decimal): Decimal = luminousFlux.fromSIUnit(time.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = time.toSIUnit(luminousFlux.toSIUnit(value))
}

infix fun LuminousFlux.x(time: Time) = LuminousEnergy(this, time)

@JvmName("luminousFluxFromLuminousEnergyAndTime")
fun <
    TimeUnit : Time,
    LuminousFluxUnit : LuminousFlux
    > LuminousFluxUnit.luminousFlux(
    luminousEnergy: ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(luminousEnergy, time)

@JvmName("luminousEnergyFromLuminousFluxAndTime")
fun <
    TimeUnit : Time,
    LuminousFluxUnit : LuminousFlux
    > LuminousEnergy.luminousEnergy(
    luminousFlux: ScientificValue<MeasurementType.LuminousFlux, LuminousFluxUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(luminousFlux, time)

@JvmName("timeFromLuminousEnergyAndFlux")
fun <
    TimeUnit : Time,
    LuminousFluxUnit : LuminousFlux
    > TimeUnit.time(
    luminousEnergy: ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>,
    luminousFlux: ScientificValue<MeasurementType.LuminousFlux, LuminousFluxUnit>
) = byDividing(luminousEnergy, luminousFlux)

@JvmName("luminousEnergyDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.luminousFlux.luminousFlux(this, time)
@JvmName("luminousEnergyDivLuminousFlux")
infix operator fun <LuminousFluxUnit : LuminousFlux> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousFlux: ScientificValue<MeasurementType.LuminousFlux, LuminousFluxUnit>) = unit.time.time(this, luminousFlux)
@JvmName("LuminousFluxTimesTime")
infix operator fun <LuminousFluxUnit : LuminousFlux, TimeUnit : Time> ScientificValue<MeasurementType.LuminousFlux, LuminousFluxUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).luminousEnergy(this, time)
@JvmName("timeTimesLuminousFlux")
infix operator fun <LuminousFluxUnit : LuminousFlux, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(luminousFlux: ScientificValue<MeasurementType.LuminousFlux, LuminousFluxUnit>) = luminousFlux * this
