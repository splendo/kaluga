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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

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

val ImperialEnergyUnits: Set<ImperialEnergy> get() = setOf(
    FootPoundal,
    FootPoundForce,
    InchPoundForce,
    InchOunceForce,
    HorsepowerHour,
    BritishThermalUnit,
    BritishThermalUnit.Thermal
) + MetricAndImperialEnergyUnits.map { it.imperial }.toSet()

val EnergyUnits: Set<Energy> get() = MetricAndImperialEnergyUnits + MetricEnergyUnits.filter { it !is MetricMetricAndImperialEnergyWrapper }.toSet() + ImperialEnergyUnits.filter { it !is ImperialMetricAndImperialEnergyWrapper }.toSet()

@Serializable
sealed class Energy : AbstractScientificUnit<PhysicalQuantity.Energy>()

@Serializable
sealed class MetricEnergy : Energy(), MetricScientificUnit<PhysicalQuantity.Energy>
@Serializable
sealed class ImperialEnergy : Energy(), ImperialScientificUnit<PhysicalQuantity.Energy>
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
object Nanojoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Nano(Joule)
@Serializable
object Microjoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Micro(Joule)
@Serializable
object Millijoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Milli(Joule)
@Serializable
object Centijoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Centi(Joule)
@Serializable
object Decijoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Deci(Joule)
@Serializable
object Decajoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Deca(Joule)
@Serializable
object Hectojoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Hecto(Joule)
@Serializable
object Kilojoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Kilo(Joule)
@Serializable
object Megajoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Mega(Joule)
@Serializable
object Gigajoule : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Giga(Joule)

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
object Nanoerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Nano(Erg)
@Serializable
object Microerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Micro(Erg)
@Serializable
object Millierg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Milli(Erg)
@Serializable
object Centierg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Centi(Erg)
@Serializable
object Decierg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Deci(Erg)
@Serializable
object Decaerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Deca(Erg)
@Serializable
object Hectoerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Hecto(Erg)
@Serializable
object Kiloerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Kilo(Erg)
@Serializable
object Megaerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Mega(Erg)
@Serializable
object Gigaerg : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Giga(Erg)

@Serializable
object WattHour : MetricAndImperialEnergy(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy> {
    override val symbol: String = "Wh"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Hour.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Hour.toSIUnit(value)
}
@Serializable
object NanowattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Nano(WattHour)
@Serializable
object MicrowattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Micro(WattHour)
@Serializable
object MilliwattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Milli(WattHour)
@Serializable
object CentiwattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Centi(WattHour)
@Serializable
object DeciwattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Deci(WattHour)
@Serializable
object DecawattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Deca(WattHour)
@Serializable
object HectowattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Hecto(WattHour)
@Serializable
object KilowattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Kilo(WattHour)
@Serializable
object MegawattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Mega(WattHour)
@Serializable
object GigawattHour : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Giga(WattHour)

@Serializable
object Electronvolt : MetricEnergy(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    override val symbol: String = "eV"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value.div(elementaryCharge.decimalValue, 29)
    override fun toSIUnit(value: Decimal): Decimal = value.times(elementaryCharge.decimalValue, 29)
}

@Serializable
object Nanoelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Nano(Electronvolt)
@Serializable
object Microelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Micro(Electronvolt)
@Serializable
object Millielectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Milli(Electronvolt)
@Serializable
object Centielectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Centi(Electronvolt)
@Serializable
object Decielectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Deci(Electronvolt)
@Serializable
object Decaelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Deca(Electronvolt)
@Serializable
object Hectoelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Hecto(Electronvolt)
@Serializable
object Kiloelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Kilo(Electronvolt)
@Serializable
object Megaelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Mega(Electronvolt)
@Serializable
object Gigaelectronvolt : MetricEnergy(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Giga(Electronvolt)

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
object Millicalorie : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Milli(Calorie) {
    @Serializable
    object IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Milli(Calorie.IT)
}
@Serializable
object Kilocalorie : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Kilo(Calorie) {
    @Serializable
    object IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Kilo(Calorie.IT)
}
@Serializable
object Megacalorie : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Mega(Calorie) {
    @Serializable
    object IT : MetricAndImperialEnergy(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Mega(Calorie.IT)
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

@Serializable
data class MetricMetricAndImperialEnergyWrapper(val metricAndImperialEnergy: MetricAndImperialEnergy) : MetricEnergy() {
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override val symbol: String = metricAndImperialEnergy.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.toSIUnit(value)
}

val <EnergyUnit : MetricAndImperialEnergy> EnergyUnit.metric get() = MetricMetricAndImperialEnergyWrapper(this)

@Serializable
data class ImperialMetricAndImperialEnergyWrapper(val metricAndImperialEnergy: MetricAndImperialEnergy) : ImperialEnergy() {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override val symbol: String = metricAndImperialEnergy.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialEnergy.toSIUnit(value)
}

val <EnergyUnit : MetricAndImperialEnergy> EnergyUnit.imperial get() = ImperialMetricAndImperialEnergyWrapper(this)
