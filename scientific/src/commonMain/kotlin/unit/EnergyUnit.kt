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
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MetricNamedEnergyUnit]
 */
val NamedMetricEnergyUnits: Set<MetricNamedEnergyUnit> get() = setOf(
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
    Gigaelectronvolt,
)

/**
 * Set of all [MetricEnergy]
 */
val MetricEnergyUnits: Set<MetricEnergy> get() = NamedMetricEnergyUnits + MetricAndImperialEnergyUnits.map { it.metric }.toSet()

/**
 * Set of all [MetricAndImperialNamedEnergyUnit]
 */
val NamedMetricAndImperialEnergyUnits: Set<MetricAndImperialNamedEnergyUnit> get() = setOf(
    Calorie,
    Calorie.IT,
    Millicalorie,
    Millicalorie.IT,
    Kilocalorie,
    Kilocalorie.IT,
    Megacalorie,
    Megacalorie.IT,
)

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
) + NamedMetricAndImperialEnergyUnits

/**
 * Set of all [ImperialNamedEnergyUnit]
 */
val NamedImperialEnergyUnits: Set<ImperialNamedEnergyUnit> get() = setOf(
    FootPoundal,
    FootPoundForce,
    InchPoundForce,
    InchOunceForce,
    BritishThermalUnit,
    BritishThermalUnit.Thermal,
)

/**
 * Set of all [ImperialEnergy]
 */
val ImperialEnergyUnits: Set<ImperialEnergy> get() = setOf(
    HorsepowerHour,
) + NamedImperialEnergyUnits + MetricAndImperialEnergyUnits.map { it.imperial }.toSet()

/**
 * Set of all [Energy]
 */
val EnergyUnits: Set<Energy> get() = MetricAndImperialEnergyUnits +
    MetricEnergyUnits.filter { it !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    ImperialEnergyUnits.filter { it !is ImperialMetricAndImperialEnergyWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Energy]
 * SI unit is [Joule]
 */
@Serializable
sealed class Energy : DefinedScientificUnit<PhysicalQuantity.Energy>()

/**
 * An [Energy] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricEnergy :
    Energy(),
    MetricScientificUnit<PhysicalQuantity.Energy>

/**
 * An [Energy] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialEnergy :
    Energy(),
    ImperialScientificUnit<PhysicalQuantity.Energy>

/**
 * An [Energy] for [MeasurementSystem.MetricAndImperial]
 */
@Serializable
sealed class MetricAndImperialEnergy :
    Energy(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Energy>

/**
 * A [SystemScientificUnit] for [PhysicalQuantity.Energy] that is named, i.e. its name is not derived from the unit of another physical quantity
 */
sealed interface NamedEnergyUnit<System : MeasurementSystem> : SystemScientificUnit<System, PhysicalQuantity.Energy>

/**
 * A [MetricAndImperialEnergy] [NamedEnergyUnit].
 */
@Serializable
sealed class MetricAndImperialNamedEnergyUnit :
    MetricAndImperialEnergy(),
    NamedEnergyUnit<MeasurementSystem.MetricAndImperial>

/**
 * A [MetricEnergy] [NamedEnergyUnit].
 */
@Serializable
sealed class MetricNamedEnergyUnit :
    MetricEnergy(),
    NamedEnergyUnit<MeasurementSystem.Metric>

/**
 * An [ImperialEnergy] [NamedEnergyUnit].
 */
@Serializable
sealed class ImperialNamedEnergyUnit :
    ImperialEnergy(),
    NamedEnergyUnit<MeasurementSystem.Imperial>

@Serializable
data object Joule : MetricNamedEnergyUnit(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    override val symbol: String = "J"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class JouleMultiple :
    MetricNamedEnergyUnit(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule>

@Serializable
data object Nanojoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Nano(Joule)

@Serializable
data object Microjoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Micro(Joule)

@Serializable
data object Millijoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Milli(Joule)

@Serializable
data object Centijoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Centi(Joule)

@Serializable
data object Decijoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Deci(Joule)

@Serializable
data object Decajoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Deca(Joule)

@Serializable
data object Hectojoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Hecto(Joule)

@Serializable
data object Kilojoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Kilo(Joule)

@Serializable
data object Megajoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Mega(Joule)

@Serializable
data object Gigajoule : JouleMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Joule> by Giga(Joule)

@Serializable
data object Erg : MetricNamedEnergyUnit(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    private val ERG_IN_JOULE = 10000000.toDecimal()
    override val symbol: String = "erg"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value * ERG_IN_JOULE
    override fun toSIUnit(value: Decimal): Decimal = value / ERG_IN_JOULE
}

@Serializable
sealed class ErgMultiple :
    MetricNamedEnergyUnit(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg>

@Serializable
data object Nanoerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Nano(Erg)

@Serializable
data object Microerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Micro(Erg)

@Serializable
data object Millierg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Milli(Erg)

@Serializable
data object Centierg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Centi(Erg)

@Serializable
data object Decierg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Deci(Erg)

@Serializable
data object Decaerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Deca(Erg)

@Serializable
data object Hectoerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Hecto(Erg)

@Serializable
data object Kiloerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Kilo(Erg)

@Serializable
data object Megaerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Mega(Erg)

@Serializable
data object Gigaerg : ErgMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Erg> by Giga(Erg)

@Serializable
data object WattHour : MetricAndImperialEnergy(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy> {
    override val symbol: String = "Wh"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Hour.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Hour.toSIUnit(value)
}

@Serializable
sealed class WattHourMultiple :
    MetricAndImperialEnergy(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour>

@Serializable
data object NanowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Nano(WattHour)

@Serializable
data object MicrowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Micro(WattHour)

@Serializable
data object MilliwattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Milli(WattHour)

@Serializable
data object CentiwattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Centi(WattHour)

@Serializable
data object DeciwattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Deci(WattHour)

@Serializable
data object DecawattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Deca(WattHour)

@Serializable
data object HectowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Hecto(WattHour)

@Serializable
data object KilowattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Kilo(WattHour)

@Serializable
data object MegawattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Mega(WattHour)

@Serializable
data object GigawattHour : WattHourMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, WattHour> by Giga(WattHour)

@Serializable
data object Electronvolt : MetricNamedEnergyUnit(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy> {
    override val symbol: String = "eV"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value.div(elementaryCharge.decimalValue, 29)
    override fun toSIUnit(value: Decimal): Decimal = value.times(elementaryCharge.decimalValue, 29)
}

@Serializable
sealed class ElectronvoltMultiple :
    MetricNamedEnergyUnit(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt>

@Serializable
data object Nanoelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Nano(Electronvolt)

@Serializable
data object Microelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Micro(Electronvolt)

@Serializable
data object Millielectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Milli(Electronvolt)

@Serializable
data object Centielectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Centi(Electronvolt)

@Serializable
data object Decielectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Deci(Electronvolt)

@Serializable
data object Decaelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Deca(Electronvolt)

@Serializable
data object Hectoelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Hecto(Electronvolt)

@Serializable
data object Kiloelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Kilo(Electronvolt)

@Serializable
data object Megaelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Mega(Electronvolt)

@Serializable
data object Gigaelectronvolt : ElectronvoltMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Energy, Electronvolt> by Giga(Electronvolt)

interface CalorieUnit : MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy>

@Serializable
data object Calorie : MetricAndImperialNamedEnergyUnit(), CalorieUnit by CalorieBase(4.184.toDecimal()) {

    internal class CalorieBase(val jouleInCalorie: Decimal, symbolPostfix: String = "") : CalorieUnit {
        override val symbol: String = "cal$symbolPostfix"
        override val system = MeasurementSystem.MetricAndImperial
        override val quantity = PhysicalQuantity.Energy
        override fun fromSIUnit(value: Decimal): Decimal = value / jouleInCalorie
        override fun toSIUnit(value: Decimal): Decimal = value * jouleInCalorie
    }

    @Serializable
    data object IT : MetricAndImperialNamedEnergyUnit(), CalorieUnit by CalorieBase(4.1868.toDecimal(), "-IT")
}

@Serializable
sealed class CalorieMultiple :
    MetricAndImperialNamedEnergyUnit(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> {

    @Serializable
    sealed class IT :
        MetricAndImperialNamedEnergyUnit(),
        MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT>
}

@Serializable
data object Millicalorie : CalorieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Milli(Calorie) {

    @Serializable
    data object IT : CalorieMultiple.IT(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Milli(Calorie.IT)
}

@Serializable
data object Kilocalorie : CalorieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Kilo(Calorie) {

    @Serializable
    data object IT : CalorieMultiple.IT(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Kilo(Calorie.IT)
}

@Serializable
data object Megacalorie : CalorieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie> by Mega(Calorie) {

    @Serializable
    data object IT : CalorieMultiple.IT(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Energy, Calorie.IT> by Mega(Calorie.IT)
}

@Serializable
data object FootPoundal : ImperialNamedEnergyUnit() {
    override val symbol: String = "ftpdl"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(Poundal.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Poundal.toSIUnit(Foot.toSIUnit(value))
}

@Serializable
data object FootPoundForce : ImperialNamedEnergyUnit() {
    override val symbol: String = "ftlbf"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(PoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(Foot.toSIUnit(value))
}

@Serializable
data object InchPoundForce : ImperialNamedEnergyUnit() {
    override val symbol: String = "inlbf"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Inch.fromSIUnit(PoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(Inch.toSIUnit(value))
}

@Serializable
data object InchOunceForce : ImperialNamedEnergyUnit() {
    override val symbol: String = "inozf"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = Inch.fromSIUnit(OunceForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = OunceForce.toSIUnit(Inch.toSIUnit(value))
}

@Serializable
data object HorsepowerHour : ImperialEnergy() {
    override val symbol: String = "hph"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Energy
    override fun fromSIUnit(value: Decimal): Decimal = WattHour.fromSIUnit(Horsepower.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Horsepower.toSIUnit(WattHour.toSIUnit(value))
}

@Serializable
data object BritishThermalUnit : ImperialNamedEnergyUnit(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Energy> by BritishThermalUnitBase(Calorie.IT) {

    internal class BritishThermalUnitBase(private val calorieUnit: CalorieUnit, symbolPostfix: String = "") :
        SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Energy> {
        override val symbol: String = "Btu$symbolPostfix"
        override val system = MeasurementSystem.Imperial
        override val quantity = PhysicalQuantity.Energy
        override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(Rankine.fromSIUnit(Kilo(calorieUnit).fromSIUnit(value)))
        override fun toSIUnit(value: Decimal): Decimal = Kilo(calorieUnit).toSIUnit(Rankine.toSIUnit(Pound.toSIUnit(value)))
    }

    @Serializable
    data object Thermal : ImperialNamedEnergyUnit(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Energy> by BritishThermalUnitBase(Calorie, "-th")
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

internal fun SerializersModuleBuilder.setupForEnergy() {
    polymorphic(Energy::class) {
        registerEnergyClasses()
    }
    polymorphic(MetricAndImperialEnergy::class) {
        registerMetricAndImperialEnergy()
    }
    polymorphic(MetricEnergy::class) {
        registerMetricEnergyClasses()
    }
    polymorphic(ImperialEnergy::class) {
        registerImperialEnergyClasses()
    }
}

internal fun PolymorphicModuleBuilder<Energy>.registerEnergyClasses() {
    registerMetricAndImperialEnergy()
    registerMetricEnergyClasses()
    registerImperialEnergyClasses()
}

internal fun PolymorphicModuleBuilder<MetricAndImperialEnergy>.registerMetricAndImperialEnergy() {
    subclass(Calorie::class, Calorie.serializer())
    subclass(Calorie.IT::class, Calorie.IT.serializer())
    subclass(Kilocalorie::class, Kilocalorie.serializer())
    subclass(Megacalorie::class, Megacalorie.serializer())
    subclass(Millicalorie::class, Millicalorie.serializer())
    subclass(Kilocalorie.IT::class, Kilocalorie.IT.serializer())
    subclass(Megacalorie.IT::class, Megacalorie.IT.serializer())
    subclass(Millicalorie.IT::class, Millicalorie.IT.serializer())
    subclass(WattHour::class, WattHour.serializer())
    subclass(CentiwattHour::class, CentiwattHour.serializer())
    subclass(DecawattHour::class, DecawattHour.serializer())
    subclass(DeciwattHour::class, DeciwattHour.serializer())
    subclass(GigawattHour::class, GigawattHour.serializer())
    subclass(HectowattHour::class, HectowattHour.serializer())
    subclass(KilowattHour::class, KilowattHour.serializer())
    subclass(MegawattHour::class, MegawattHour.serializer())
    subclass(MicrowattHour::class, MicrowattHour.serializer())
    subclass(MilliwattHour::class, MilliwattHour.serializer())
    subclass(NanowattHour::class, NanowattHour.serializer())
}

internal fun PolymorphicModuleBuilder<MetricEnergy>.registerMetricEnergyClasses() {
    subclass(MetricMetricAndImperialEnergyWrapper::class, MetricMetricAndImperialEnergyWrapper.serializer())
    subclass(Electronvolt::class, Electronvolt.serializer())
    subclass(Centielectronvolt::class, Centielectronvolt.serializer())
    subclass(Decaelectronvolt::class, Decaelectronvolt.serializer())
    subclass(Decielectronvolt::class, Decielectronvolt.serializer())
    subclass(Gigaelectronvolt::class, Gigaelectronvolt.serializer())
    subclass(Hectoelectronvolt::class, Hectoelectronvolt.serializer())
    subclass(Kiloelectronvolt::class, Kiloelectronvolt.serializer())
    subclass(Megaelectronvolt::class, Megaelectronvolt.serializer())
    subclass(Microelectronvolt::class, Microelectronvolt.serializer())
    subclass(Millielectronvolt::class, Millielectronvolt.serializer())
    subclass(Nanoelectronvolt::class, Nanoelectronvolt.serializer())
    subclass(Erg::class, Erg.serializer())
    subclass(Centierg::class, Centierg.serializer())
    subclass(Decaerg::class, Decaerg.serializer())
    subclass(Decierg::class, Decierg.serializer())
    subclass(Gigaerg::class, Gigaerg.serializer())
    subclass(Hectoerg::class, Hectoerg.serializer())
    subclass(Kiloerg::class, Kiloerg.serializer())
    subclass(Megaerg::class, Megaerg.serializer())
    subclass(Microerg::class, Microerg.serializer())
    subclass(Millierg::class, Millierg.serializer())
    subclass(Nanoerg::class, Nanoerg.serializer())
    subclass(Joule::class, Joule.serializer())
    subclass(Centijoule::class, Centijoule.serializer())
    subclass(Decajoule::class, Decajoule.serializer())
    subclass(Decijoule::class, Decijoule.serializer())
    subclass(Gigajoule::class, Gigajoule.serializer())
    subclass(Hectojoule::class, Hectojoule.serializer())
    subclass(Kilojoule::class, Kilojoule.serializer())
    subclass(Megajoule::class, Megajoule.serializer())
    subclass(Microjoule::class, Microjoule.serializer())
    subclass(Millijoule::class, Millijoule.serializer())
    subclass(Nanojoule::class, Nanojoule.serializer())
}

internal fun PolymorphicModuleBuilder<ImperialEnergy>.registerImperialEnergyClasses() {
    subclass(HorsepowerHour::class, HorsepowerHour.serializer())
    subclass(ImperialMetricAndImperialEnergyWrapper::class, ImperialMetricAndImperialEnergyWrapper.serializer())
    subclass(BritishThermalUnit::class, BritishThermalUnit.serializer())
    subclass(BritishThermalUnit.Thermal::class, BritishThermalUnit.Thermal.serializer())
    subclass(FootPoundForce::class, FootPoundForce.serializer())
    subclass(FootPoundal::class, FootPoundal.serializer())
    subclass(InchOunceForce::class, InchOunceForce.serializer())
    subclass(InchPoundForce::class, InchPoundForce.serializer())
}
