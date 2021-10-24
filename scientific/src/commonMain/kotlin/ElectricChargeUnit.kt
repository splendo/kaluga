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
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
sealed class ElectricCharge : AbstractScientificUnit<MeasurementType.ElectricCharge>(), MetricAndImperialScientificUnit<MeasurementType.ElectricCharge>

@Serializable
object Coulomb : ElectricCharge(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge> {
    override val symbol = "C"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricCharge
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Nano(Coulomb)
@Serializable
object MicroCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Micro(Coulomb)
@Serializable
object MilliCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Milli(Coulomb)
@Serializable
object CentiCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Centi(Coulomb)
@Serializable
object DeciCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Deci(Coulomb)
@Serializable
object DecaCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Deca(Coulomb)
@Serializable
object HectoCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Hecto(Coulomb)
@Serializable
object KiloCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Kilo(Coulomb)
@Serializable
object MegaCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Mega(Coulomb)
@Serializable
object GigaCoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCharge, Coulomb> by Giga(Coulomb)

val elementaryCharge = 1.602176634e-19(Coulomb)

@JvmName("chargeFromCurrentAndTime")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
    >
    ChargeUnit.charge(
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.ElectricCharge, ChargeUnit> = byMultiplying(current, time)

@JvmName("timeFromChargeAndCurrent")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
    >
    TimeUnit.duration(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) : ScientificValue<MeasurementType.Time, TimeUnit> = byDividing(charge, current)

@JvmName("chargeFromEnergyAndVoltage")
fun <
    VoltageUnit : Voltage,
    EnergyUnit : Energy,
    ChargeUnit : ElectricCharge
    > ChargeUnit.charge(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.ElectricCharge, ChargeUnit> = byDividing(energy, voltage)

@JvmName("capacitanceFromChargeAndVoltage")
fun <
    ChargeUnit : ElectricCharge,
    VoltageUnit : Voltage,
    CapacitanceUnit : ElectricCapacitance
    >
    ChargeUnit.charge(
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
) : ScientificValue<MeasurementType.ElectricCharge, ChargeUnit> = byMultiplying(capacitance, voltage)

@JvmName("chargeFromFluxAndResistance")
fun <
    ResistanceUnit : ElectricResistance,
    ChargeUnit : ElectricCharge,
    FluxUnit : MagneticFlux
    >
    ChargeUnit.charge(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
) : ScientificValue<MeasurementType.ElectricCharge, ChargeUnit> = byDividing(flux, resistance)

@JvmName("currentTimesTime")
infix operator fun <CurrentUnit : ElectricCurrent, TimeUnit : Time> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Coulomb.charge(this, time)
@JvmName("timeTimesCurrent")
infix operator fun <CurrentUnit : ElectricCurrent, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = current * this
@JvmName("chargeDivCurrent")
infix operator fun <ChargeUnit : ElectricCharge, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Second.duration(this, current)
@JvmName("energyDivVoltage")
infix operator fun <EnergyUnit : Energy, VoltageUnit : Voltage> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Coulomb.charge(this, voltage)
@JvmName("capacitanceTimesVoltage")
infix operator fun <CapacitanceUnit : ElectricCapacitance, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Coulomb.charge(this, voltage)
@JvmName("voltageTimesCapacitance")
infix operator fun <CapacitanceUnit : ElectricCapacitance, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>) = capacitance * this
@JvmName("fluxDivResistance")
infix operator fun <FluxUnit : MagneticFlux, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = Coulomb.charge(this, resistance)
