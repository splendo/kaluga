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
sealed class Energy : AbstractScientificUnit<MeasurementType.Energy>()

@Serializable
sealed class MetricEnergy : Energy(), MetricScientificUnit<MeasurementType.Energy>
@Serializable
sealed class ImperialEnergy : Energy(), ImperialScientificUnit<MeasurementType.Energy>
@Serializable
sealed class MetricAndImperialEnergy : Energy(), MetricAndImperialScientificUnit<MeasurementType.Energy>

@Serializable
object Joule : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Energy> {
    override val symbol: String = "J"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}
@Serializable
object NanoJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Nano(Joule)
@Serializable
object MicroJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Micro(Joule)
@Serializable
object MilliJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Milli(Joule)
@Serializable
object CentiJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Centi(Joule)
@Serializable
object DeciJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Deci(Joule)
@Serializable
object DecaJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Deca(Joule)
@Serializable
object HectoJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Hecto(Joule)
@Serializable
object KiloJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Kilo(Joule)
@Serializable
object MegaJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Mega(Joule)
@Serializable
object GigaJoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Joule> by Giga(Joule)

@Serializable
object Erg : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Energy> {
    const val ERG_IN_JOULE = 10000000
    override val symbol: String = "erg"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value * ERG_IN_JOULE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / ERG_IN_JOULE.toDecimal()
}

@Serializable
object MicroErg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> by Micro(Erg)
@Serializable
object MilliErg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> by Milli(Erg)
@Serializable
object KiloErg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> by Kilo(Erg)
@Serializable
object MegaErg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> by Mega(Erg)

@Serializable
object WattHour : MetricAndImperialEnergy(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy> {
    override val symbol: String = "Wh"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Hour.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Hour.toSIUnit(value)
}
@Serializable
object NanoWattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, WattHour> by Nano(WattHour)
@Serializable
object MicroWattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, WattHour> by Micro(WattHour)
@Serializable
object MilliWattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, WattHour> by Milli(WattHour)
@Serializable
object KiloWattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, WattHour> by Kilo(WattHour)
@Serializable
object MegaWattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, WattHour> by Mega(WattHour)
@Serializable
object GigaWattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, WattHour> by Giga(WattHour)

@Serializable
object Electronvolt : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Energy> {
    override val symbol: String = "eV"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value / elementaryCharge.value
    override fun toSIUnit(value: Decimal): Decimal = value * elementaryCharge.value
}

@Serializable
object NanoElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Nano(Electronvolt)
@Serializable
object MicroElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Micro(Electronvolt)
@Serializable
object MilliElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Milli(Electronvolt)
@Serializable
object CentiElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Centi(Electronvolt)
@Serializable
object DeciElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Deci(Electronvolt)
@Serializable
object DecaElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Deca(Electronvolt)
@Serializable
object HectoElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Hecto(Electronvolt)
@Serializable
object KiloElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Kilo(Electronvolt)
@Serializable
object MegaElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Mega(Electronvolt)
@Serializable
object GigaElectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Electronvolt> by Giga(Electronvolt)

interface CalorieUnit : MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy>
@Serializable
object Calorie : MetricAndImperialEnergy(), CalorieUnit by CalorieBase(4.184.toDecimal()) {

    internal class CalorieBase(val jouleInCalorie: Decimal, symbolPostfix: String = "") : CalorieUnit {
        override val symbol: String = "Cal$symbolPostfix"
        override val system = MeasurementSystem.MetricAndImperial
        override val type = MeasurementType.Energy
        override fun fromSIUnit(value: Decimal): Decimal = value / jouleInCalorie
        override fun toSIUnit(value: Decimal): Decimal = value * jouleInCalorie
    }

    @Serializable
    object IT : MetricAndImperialEnergy(), CalorieUnit by CalorieBase(4.1868.toDecimal(), "-IT")
}
@Serializable
object MilliCalorie : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, Calorie> by Milli(Calorie) {
    @Serializable
    object IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, Calorie.IT> by Milli(Calorie.IT)
}
@Serializable
object KiloCalorie : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, Calorie> by Kilo(Calorie) {
    @Serializable
    object IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, Calorie.IT> by Kilo(Calorie.IT)
}
@Serializable
object MegaCalorie : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, Calorie> by Mega(Calorie) {
    @Serializable
    object IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Energy, Calorie.IT> by Mega(Calorie.IT)
}

@Serializable
object FootPoundal : ImperialEnergy() {
    override val symbol: String = "ftpdl"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(Poundal.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Poundal.toSIUnit(Foot.toSIUnit(value))
}
@Serializable
object FootPoundForce : ImperialEnergy() {
    override val symbol: String = "ftlbf"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(PoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(Foot.toSIUnit(value))
}
@Serializable
object InchPoundForce : ImperialEnergy() {
    override val symbol: String = "inlbf"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Inch.fromSIUnit(PoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(Inch.toSIUnit(value))
}
@Serializable
object InchOunceForce : ImperialEnergy() {
    override val symbol: String = "inozf"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Inch.fromSIUnit(OunceForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = OunceForce.toSIUnit(Inch.toSIUnit(value))
}
@Serializable
object HorsepowerHour : ImperialEnergy() {
    override val symbol: String = "hph"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = WattHour.fromSIUnit(Horsepower.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Horsepower.toSIUnit(WattHour.toSIUnit(value))
}
@Serializable
object BritishThermalUnit : ImperialEnergy(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Energy> by BritishThermalUnitBase(Calorie.IT)  {

    internal class BritishThermalUnitBase(private val calorieUnit: CalorieUnit, symbolPostfix: String = "") : SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Energy> {
        override val symbol: String = "Btu$symbolPostfix"
        override val system = MeasurementSystem.Imperial
        override val type = MeasurementType.Energy
        override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(Rankine.fromSIUnit(Kilo(calorieUnit).fromSIUnit(value)))
        override fun toSIUnit(value: Decimal): Decimal = Kilo(calorieUnit).toSIUnit(Rankine.toSIUnit(Pound.toSIUnit(value)))
    }

    @Serializable
    object Thermal : SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Energy> by BritishThermalUnitBase(Calorie, "-th")
}

@Serializable
data class MetricMetricAndImperialEnergyWrapper(val metricAndImperialEnergy: MetricAndImperialEnergy) : MetricEnergy() {
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Energy
    override val symbol: String = metricAndImperialEnergy.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.toSIUnit(value)
}

@Serializable
data class ImperialMetricAndImperialEnergyWrapper(val metricAndImperialEnergy: MetricAndImperialEnergy) : ImperialEnergy() {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Energy
    override val symbol: String = metricAndImperialEnergy.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.toSIUnit(value)
}

@JvmName("energyFromForceAndDistance")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length
    >
    EnergyUnit.energy(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    distance: ScientificValue<MeasurementType.Length, LengthUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(force, distance)

@JvmName("energyFromPressureAndVolume")
fun <
    EnergyUnit : Energy,
    PressureUnit : Pressure,
    VolumeUnit : Volume
    >
    EnergyUnit.energy(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(pressure, volume)

@JvmName("energyFromChargeAndVoltage")
fun <
    EnergyUnit : Energy,
    ChargeUnit : ElectricCharge,
    VoltageUnit : Voltage
    >
    EnergyUnit.energy(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(charge, voltage)

@JvmName("energyFromPowerAndTime")
fun <
    EnergyUnit : Energy,
    TimeUnit : Time,
    PowerUnit : Power
    > EnergyUnit.energy(
    power: ScientificValue<MeasurementType.Power, PowerUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(power, time)

@JvmName("energyFromFluxAndCurrent")
fun <
    EnergyUnit : Energy,
    CurrentUnit : ElectricCurrent,
    FluxUnit : MagneticFlux
    >
    EnergyUnit.energy(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) : ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(flux, current)

@JvmName("energyFromAbsorbedDoseAndWeight")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose
    >
    EnergyUnit.energy(
    absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) : ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(absorbedDose, weight)

@JvmName("energyFromEquivalentDoseAndWeight")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    EquivalentDoseUnit : IonizingRadiationEquivalentDose
    >
    EnergyUnit.energy(
    equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) : ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(equivalentDose, weight)


@JvmName("forceTimesLength")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = Joule.energy(this, distance)
@JvmName("lengthTimesForce")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("pressureTimesVolume")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Joule.energy(this, volume)
@JvmName("volumeTimesPressure")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = pressure * this
@JvmName("chargeTimesVoltage")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Joule.energy(this, voltage)
@JvmName("voltageTimesCharge")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = charge * this
@JvmName("powerTimesTime")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Joule.energy(this, time)
@JvmName("timeTimesPower")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = power * this
@JvmName("fluxTimesCurrent")
infix operator fun <FluxUnit : MagneticFlux, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Joule.energy(this, current)
@JvmName("currentTimesFlux")
infix operator fun <FluxUnit : MagneticFlux, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>) = flux * this
@JvmName("radTimesWeight")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Erg.energy(this, weight)
@JvmName("weightTimesRad")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>) = absorbedDose * this
@JvmName("radMultipleTimesWeight")
infix operator fun <AbsorbedDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) where AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> = Erg.energy(this, weight)
@JvmName("weightTimesRadMultiple")
infix operator fun <AbsorbedDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) where AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad>  = absorbedDose * this
@JvmName("absorbedDoseTimesWeight")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Joule.energy(this, weight)
@JvmName("weightTimesAbsorbedDose")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) = absorbedDose * this
@JvmName("roentgenEquivalentManTimesWeight")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Erg.energy(this, weight)
@JvmName("weightTimesRoentgenEquivalentMan")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) = equivalentDose * this
@JvmName("roentgenEquivalentManMultipleTimesWeight")
infix operator fun <EquivalentDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) where EquivalentDoseUnit : IonizingRadiationEquivalentDose, EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> = Erg.energy(this, weight)
@JvmName("weightTimesRoentgenEquivalentManMultiple")
infix operator fun <EquivalentDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) where EquivalentDoseUnit : IonizingRadiationEquivalentDose, EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>  = equivalentDose * this
@JvmName("equivalentDoseTimesWeight")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Joule.energy(this, weight)
@JvmName("weightTimesEquivalentDose")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) = equivalentDose * this

