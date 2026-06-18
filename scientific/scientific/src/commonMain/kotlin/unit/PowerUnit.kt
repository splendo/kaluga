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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.base.decimal.div
import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MetricAndImperialPower]
 */
val MetricAndImperialPowerUnits: Set<MetricAndImperialPower> get() = setOf(
    Watt,
    Nanowatt,
    Microwatt,
    Milliwatt,
    Centiwatt,
    Deciwatt,
    Decawatt,
    Hectowatt,
    Kilowatt,
    Megawatt,
    Gigawatt,
) + NamedMetricAndImperialEnergyUnits.flatMap { energyUnit ->
    TimeUnits.map { energyUnit per it }
}

/**
 * Set of all [MetricPower]
 */
val MetricPowerUnits: Set<MetricPower> get() = MetricAndImperialPowerUnits.map { it.metric }.toSet() +
    NamedMetricEnergyUnits.flatMap { energyUnit ->
        TimeUnits.map { energyUnit per it }
    } +
    setOf(
        MetricHorsepower,
    )

/**
 * Set of all [ImperialPower]
 */
val ImperialPowerUnits: Set<ImperialPower> get() = MetricAndImperialPowerUnits.map { it.imperial }.toSet() +
    NamedImperialEnergyUnits.flatMap { energyUnit ->
        TimeUnits.map { energyUnit per it }
    } +
    setOf(
        Horsepower,
    )

/**
 * Set of all [Power]
 */
val PowerUnits: Set<Power> get() = MetricAndImperialPowerUnits +
    MetricPowerUnits.filter { it !is MetricMetricAndImperialPowerWrapper }.toSet() +
    ImperialPowerUnits.filter { it !is ImperialMetricAndImperialPowerWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Power]
 * SI unit is [Watt]
 */
@Serializable
sealed class Power : DefinedScientificUnit<PhysicalQuantity.Power>()

/**
 * A [Power] for [MeasurementSystem.MetricAndImperial]
 */
@Serializable
sealed class MetricAndImperialPower :
    Power(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Power>

/**
 * A [Power] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricPower :
    Power(),
    MetricScientificUnit<PhysicalQuantity.Power>

/**
 * A [Power] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialPower :
    Power(),
    ImperialScientificUnit<PhysicalQuantity.Power>

/**
 * A [SystemScientificUnit] for [PhysicalQuantity.Power] that is constructed from a [NamedEnergyUnit] and a [Time]
 */
sealed interface CombinedPower<System : MeasurementSystem> : SystemScientificUnit<System, PhysicalQuantity.Power> {
    val energy: NamedEnergyUnit<System>
    val per: Time

    override val symbol: String get() = "${energy.symbol}/${per.symbol}"
    override val quantity get() = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MetricAndImperialPower] [CombinedPower]
 */
@Serializable
data class MetricAndImperialCombinedPower(override val energy: MetricAndImperialNamedEnergyUnit, override val per: Time) :
    MetricAndImperialPower(),
    CombinedPower<MeasurementSystem.MetricAndImperial> {
    override val system: MeasurementSystem.MetricAndImperial = MeasurementSystem.MetricAndImperial
}

/**
 * A [MetricPower] [CombinedPower]
 */
@Serializable
data class MetricCombinedPower(override val energy: MetricNamedEnergyUnit, override val per: Time) :
    MetricPower(),
    CombinedPower<MeasurementSystem.Metric> {
    override val system: MeasurementSystem.Metric = MeasurementSystem.Metric
}

/**
 * An [ImperialPower] [CombinedPower]
 */
@Serializable
data class ImperialCombinedPower(override val energy: ImperialNamedEnergyUnit, override val per: Time) :
    ImperialPower(),
    CombinedPower<MeasurementSystem.Imperial> {
    override val system: MeasurementSystem.Imperial = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAndImperialCombinedPower] from a [MetricAndImperialNamedEnergyUnit] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAndImperialCombinedPower] represented by the units
 */
infix fun MetricAndImperialNamedEnergyUnit.per(time: Time) = MetricAndImperialCombinedPower(this, time)

/**
 * Gets a [MetricCombinedPower] from a [MetricNamedEnergyUnit] and a [Time]
 * @param time the [Time] component
 * @return the [MetricCombinedPower] represented by the units
 */
infix fun MetricNamedEnergyUnit.per(time: Time) = MetricCombinedPower(this, time)

/**
 * Gets an [ImperialCombinedPower] from a [ImperialNamedEnergyUnit] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAndImperialPower] represented by the units
 */
infix fun ImperialNamedEnergyUnit.per(time: Time) = ImperialCombinedPower(this, time)

@Serializable
data object Watt : MetricAndImperialPower(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power> {
    override val symbol: String = "W"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class WattMultiple :
    MetricAndImperialPower(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt>

@Serializable
data object Nanowatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Nano(Watt)

@Serializable
data object Microwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Micro(Watt)

@Serializable
data object Milliwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Milli(Watt)

@Serializable
data object Centiwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Centi(Watt)

@Serializable
data object Deciwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Deci(Watt)

@Serializable
data object Decawatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Deca(Watt)

@Serializable
data object Hectowatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Hecto(Watt)

@Serializable
data object Kilowatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Kilo(Watt)

@Serializable
data object Megawatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Mega(Watt)

@Serializable
data object Gigawatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Giga(Watt)

@Serializable
data object MetricHorsepower : MetricPower() {
    private val KILOGRAM_FORCE_METER_SECOND_TO_WATT = 75.toDecimal()
    override val symbol: String = "PS"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = KilogramForce.fromSIUnit(value) / KILOGRAM_FORCE_METER_SECOND_TO_WATT
    override fun toSIUnit(value: Decimal): Decimal = KilogramForce.toSIUnit(value * KILOGRAM_FORCE_METER_SECOND_TO_WATT)
}

@Serializable
data object Horsepower : ImperialPower() {
    private val FOOTPOUND_PER_MINUTE = 33000.toDecimal()
    override val symbol: String = "hp"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = FootPoundForce.fromSIUnit(Minute.toSIUnit(value)) / FOOTPOUND_PER_MINUTE
    override fun toSIUnit(value: Decimal): Decimal = Minute.fromSIUnit(FootPoundForce.toSIUnit(value * FOOTPOUND_PER_MINUTE))
}

/**
 * Wraps a [MetricAndImperialPower] unit to a [MetricPower] unit
 * @param metricAndImperialPower the [MetricAndImperialPower] to wrap
 */
@Serializable
data class MetricMetricAndImperialPowerWrapper(val metricAndImperialPower: MetricAndImperialPower) : MetricPower() {
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Power
    override val symbol: String = metricAndImperialPower.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialPower.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialPower.toSIUnit(value)
}

/**
 * Converts a [MetricAndImperialPower] unit to a [MetricMetricAndImperialPowerWrapper] unit
 * @param PowerUnit the type of [MetricAndImperialPower] to convert
 */
val <PowerUnit : MetricAndImperialPower> PowerUnit.metric get() = MetricMetricAndImperialPowerWrapper(this)

/**
 * Wraps a [MetricAndImperialPower] unit to an [ImperialPower] unit
 * @param metricAndImperialPower the [MetricAndImperialPower] to wrap
 */
@Serializable
data class ImperialMetricAndImperialPowerWrapper(val metricAndImperialPower: MetricAndImperialPower) : ImperialPower() {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override val symbol: String = metricAndImperialPower.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialPower.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialPower.toSIUnit(value)
}

/**
 * Converts a [MetricAndImperialPower] unit to an [ImperialMetricAndImperialPowerWrapper] unit
 * @param PowerUnit the type of [MetricAndImperialPower] to convert
 */
val <PowerUnit : MetricAndImperialPower> PowerUnit.imperial get() = ImperialMetricAndImperialPowerWrapper(this)

internal fun SerializersModuleBuilder.setupForPower() {
    polymorphic(Power::class) {
        registerPowerClasses()
    }
    polymorphic(MetricAndImperialPower::class) {
        registerMetricAndImperialPowerClasses()
    }
    polymorphic(MetricPower::class) {
        registerMetricPowerClasses()
    }
    polymorphic(ImperialPower::class) {
        registerImperialPowerClasses()
    }
}

internal fun PolymorphicModuleBuilder<Power>.registerPowerClasses() {
    registerMetricAndImperialPowerClasses()
    registerMetricPowerClasses()
    registerImperialPowerClasses()
}

internal fun PolymorphicModuleBuilder<MetricAndImperialPower>.registerMetricAndImperialPowerClasses() {
    subclass(MetricAndImperialCombinedPower::class, MetricAndImperialCombinedPower.serializer())
    subclass(Watt::class, Watt.serializer())
    subclass(Centiwatt::class, Centiwatt.serializer())
    subclass(Decawatt::class, Decawatt.serializer())
    subclass(Deciwatt::class, Deciwatt.serializer())
    subclass(Gigawatt::class, Gigawatt.serializer())
    subclass(Hectowatt::class, Hectowatt.serializer())
    subclass(Kilowatt::class, Kilowatt.serializer())
    subclass(Megawatt::class, Megawatt.serializer())
    subclass(Microwatt::class, Microwatt.serializer())
    subclass(Milliwatt::class, Milliwatt.serializer())
    subclass(Nanowatt::class, Nanowatt.serializer())
}

internal fun PolymorphicModuleBuilder<MetricPower>.registerMetricPowerClasses() {
    subclass(MetricCombinedPower::class, MetricCombinedPower.serializer())
    subclass(MetricHorsepower::class, MetricHorsepower.serializer())
    subclass(MetricMetricAndImperialPowerWrapper::class, MetricMetricAndImperialPowerWrapper.serializer())
}

internal fun PolymorphicModuleBuilder<ImperialPower>.registerImperialPowerClasses() {
    subclass(Horsepower::class, Horsepower.serializer())
    subclass(ImperialCombinedPower::class, ImperialCombinedPower.serializer())
    subclass(ImperialMetricAndImperialPowerWrapper::class, ImperialMetricAndImperialPowerWrapper.serializer())
}
