/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricEnergy]
 */
val MetricEnergyUnits: Set<MetricEnergy> get() = setOf(
    Joule,
    Nanojoule,
    Microjoule,
    Millijoule,
    Centijoule,
    Decijoule,
    Decajoule,
    Hectojoule,
    Kilojoule,
    Megajoule,
    Gigajoule,
    Erg,
    Nanoerg,
    Microerg,
    Millierg,
    Centierg,
    Decierg,
    Decaerg,
    Hectoerg,
    Kiloerg,
    Megaerg,
    Gigaerg,
    Electronvolt,
    Nanoelectronvolt,
    Microelectronvolt,
    Millielectronvolt,
    Centielectronvolt,
    Decielectronvolt,
    Decaelectronvolt,
    Hectoelectronvolt,
    Kiloelectronvolt,
    Megaelectronvolt,
    Gigaelectronvolt
) + MetricAndImperialEnergyUnits.map { it.metric }.toSet()

/**
 * Set of all [MetricAndImperialEnergy]
 */
val MetricAndImperialEnergyUnits: Set<MetricAndImperialEnergy> get() = setOf(
    WattHour,
    NanowattHour,
    MicrowattHour,
    MilliwattHour,
    CentiwattHour,
    DeciwattHour,
    DecawattHour,
    HectowattHour,
    KilowattHour,
    MegawattHour,
    GigawattHour,
    Calorie,
    Calorie.IT,
    Millicalorie,
    Millicalorie.IT,
    Kilocalorie,
    Kilocalorie.IT,
    Megacalorie,
    Megacalorie.IT
)

/**
 * Set of all [ImperialEnergy]
 */
val ImperialEnergyUnits: Set<ImperialEnergy> get() = setOf(
    FootPoundal,
    FootPoundForce,
    InchPoundForce,
    InchOunceForce,
    HorsepowerHour,
    BritishThermalUnit,
    BritishThermalUnit.Thermal
) + MetricAndImperialEnergyUnits.map { it.imperial }.toSet()

/**
 * Set of all [Energy]
 */
val EnergyUnits: Set<Energy> get() = MetricAndImperialEnergyUnits + MetricEnergyUnits.filter { it !is MetricMetricAndImperialEnergyWrapper }.toSet() + ImperialEnergyUnits.filter { it !is ImperialMetricAndImperialEnergyWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Energy]
 * SI unit is [Joule]
 */
@Serializable
sealed class Energy : AbstractScientificUnit<PhysicalQuantity.Energy>()

/**
 * An [Energy] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricEnergy : Energy(), MetricScientificUnit<PhysicalQuantity.Energy>

/**
 * An [Energy] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialEnergy : Energy(), ImperialScientificUnit<PhysicalQuantity.Energy>

/**
 * An [Energy] for [MeasurementSystem.MetricAndImperial]
 */
@Serializable
sealed class MetricAndImperialEnergy : Energy(), MetricAndImperialScientificUnit<PhysicalQuantity.Energy>

@Serializable
object Joule : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    override val symbol: String = "J"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class JouleMultiple : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule>

@Serializable
object Nanojoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Nano(Joule)

@Serializable
object Microjoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Micro(Joule)

@Serializable
object Millijoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Milli(Joule)

@Serializable
object Centijoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Centi(Joule)

@Serializable
object Decijoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Deci(Joule)

@Serializable
object Decajoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Deca(Joule)

@Serializable
object Hectojoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Hecto(Joule)

@Serializable
object Kilojoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Kilo(Joule)

@Serializable
object Megajoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Mega(Joule)

@Serializable
object Gigajoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Giga(Joule)

@Serializable
object Erg : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    const val ERG_IN_JOULE = 10000000
    override val symbol: String = "erg"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value * ERG_IN_JOULE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / ERG_IN_JOULE.toDecimal()
}

@Serializable
sealed class ErgMultiple : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg>

@Serializable
object Nanoerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Nano(Erg)

@Serializable
object Microerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Micro(Erg)

@Serializable
object Millierg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Milli(Erg)

@Serializable
object Centierg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Centi(Erg)

@Serializable
object Decierg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Deci(Erg)

@Serializable
object Decaerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Deca(Erg)

@Serializable
object Hectoerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Hecto(Erg)

@Serializable
object Kiloerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Kilo(Erg)

@Serializable
object Megaerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Mega(Erg)

@Serializable
object Gigaerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Giga(Erg)

@Serializable
object WattHour : MetricAndImperialEnergy(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy> {
    override val symbol: String = "Wh"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Hour.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Hour.toSIUnit(value)
}

@Serializable
sealed class WattHourMultiple : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour>

@Serializable
object NanowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Nano(WattHour)

@Serializable
object MicrowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Micro(WattHour)

@Serializable
object MilliwattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Milli(WattHour)

@Serializable
object CentiwattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Centi(WattHour)

@Serializable
object DeciwattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Deci(WattHour)

@Serializable
object DecawattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Deca(WattHour)

@Serializable
object HectowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Hecto(WattHour)

@Serializable
object KilowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Kilo(WattHour)

@Serializable
object MegawattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Mega(WattHour)

@Serializable
object GigawattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Giga(WattHour)

@Serializable
object Electronvolt : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    override val symbol: String = "eV"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value.div(elementaryCharge.decimalValue, 29)
    override fun toSIUnit(value: Decimal): Decimal = value.times(elementaryCharge.decimalValue, 29)
}

@Serializable
sealed class ElectronvoltMultiple : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt>

@Serializable
object Nanoelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Nano(Electronvolt)

@Serializable
object Microelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Micro(Electronvolt)

@Serializable
object Millielectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Milli(Electronvolt)

@Serializable
object Centielectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Centi(Electronvolt)

@Serializable
object Decielectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Deci(Electronvolt)

@Serializable
object Decaelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Deca(Electronvolt)

@Serializable
object Hectoelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Hecto(Electronvolt)

@Serializable
object Kiloelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Kilo(Electronvolt)

@Serializable
object Megaelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Mega(Electronvolt)

@Serializable
object Gigaelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Giga(Electronvolt)

interface CalorieUnit : MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy>

@Serializable
object Calorie : MetricAndImperialEnergy(), CalorieUnit by CalorieBase(4.184.toDecimal()) {

    internal class CalorieBase(val jouleInCalorie: Decimal, symbolPostfix: String = "") : CalorieUnit {
        override val symbol: String = "Cal$symbolPostfix"
        override val system = MeasurementSystem.MetricAndImperial
        override val quantity = PhysicalQuantity.Energy
        override fun fromSIUnit(value: Decimal): Decimal = value / jouleInCalorie
        override fun toSIUnit(value: Decimal): Decimal = value * jouleInCalorie
    }

    @Serializable
    object IT : MetricAndImperialEnergy(), CalorieUnit by CalorieBase(4.1868.toDecimal(), "-IT")
}

@Serializable
sealed class CalorieMultiple : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> {

    @Serializable
    sealed class IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT>
}

@Serializable
object Millicalorie : CalorieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Milli(Calorie) {

    @Serializable
    object IT : CalorieMultiple.IT(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Milli(Calorie.IT)
}

@Serializable
object Kilocalorie : CalorieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Kilo(Calorie) {

    @Serializable
    object IT : CalorieMultiple.IT(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Kilo(Calorie.IT)
}

@Serializable
object Megacalorie : CalorieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Mega(Calorie) {

    @Serializable
    object IT : CalorieMultiple.IT(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Mega(Calorie.IT)
}

@Serializable
object FootPoundal : ImperialEnergy() {
    override val symbol: String = "ftpdl"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(Poundal.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Poundal.toSIUnit(Foot.toSIUnit(value))
}

@Serializable
object FootPoundForce : ImperialEnergy() {
    override val symbol: String = "ftlbf"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(PoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(Foot.toSIUnit(value))
}

@Serializable
object InchPoundForce : ImperialEnergy() {
    override val symbol: String = "inlbf"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Inch.fromSIUnit(PoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(Inch.toSIUnit(value))
}

@Serializable
object InchOunceForce : ImperialEnergy() {
    override val symbol: String = "inozf"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Inch.fromSIUnit(OunceForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = OunceForce.toSIUnit(Inch.toSIUnit(value))
}

@Serializable
object HorsepowerHour : ImperialEnergy() {
    override val symbol: String = "hph"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = WattHour.fromSIUnit(Horsepower.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Horsepower.toSIUnit(WattHour.toSIUnit(value))
}

@Serializable
object BritishThermalUnit : ImperialEnergy(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Energy> by BritishThermalUnitBase(Calorie.IT) {

    internal class BritishThermalUnitBase(private val calorieUnit: CalorieUnit, symbolPostfix: String = "") : SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Energy> {
        override val symbol: String = "Btu$symbolPostfix"
        override val system = MeasurementSystem.Imperial
        override val quantity = PhysicalQuantity.Energy
        override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(Rankine.fromSIUnit(Kilo(calorieUnit).fromSIUnit(value)))
        override fun toSIUnit(value: Decimal): Decimal = Kilo(calorieUnit).toSIUnit(Rankine.toSIUnit(Pound.toSIUnit(value)))
    }

    @Serializable
    object Thermal : ImperialEnergy(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Energy> by BritishThermalUnitBase(Calorie, "-th")
}

/**
 * Wraps a [MetricAndImperialEnergy] unit to a [MetricEnergy] unit
 * @param metricAndImperialEnergy the [MetricAndImperialEnergy] to wrap
 */
@Serializable
data class MetricMetricAndImperialEnergyWrapper(val metricAndImperialEnergy: MetricAndImperialEnergy) : MetricEnergy() {
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override val symbol: String = metricAndImperialEnergy.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.toSIUnit(value)
}

/**
 * Converts a [MetricAndImperialEnergy] unit to a [MetricMetricAndImperialEnergyWrapper] unit
 * @param EnergyUnit the type of [MetricAndImperialEnergy] to convert
 */
val <EnergyUnit : MetricAndImperialEnergy> EnergyUnit.metric get() = MetricMetricAndImperialEnergyWrapper(this)

/**
 * Wraps a [MetricAndImperialEnergy] unit to an [ImperialEnergy] unit
 * @param metricAndImperialEnergy the [MetricAndImperialEnergy] to wrap
 */
@Serializable
data class ImperialMetricAndImperialEnergyWrapper(val metricAndImperialEnergy: MetricAndImperialEnergy) : ImperialEnergy() {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override val symbol: String = metricAndImperialEnergy.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.toSIUnit(value)
}

/**
 * Converts a [MetricAndImperialEnergy] unit to an [ImperialMetricAndImperialEnergyWrapper] unit
 * @param EnergyUnit the type of [MetricAndImperialEnergy] to convert
 */
val <EnergyUnit : MetricAndImperialEnergy> EnergyUnit.imperial get() = ImperialMetricAndImperialEnergyWrapper(this)
