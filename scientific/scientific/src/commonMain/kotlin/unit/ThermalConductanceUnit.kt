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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MetricAndUKImperialThermalConductance]
 */
val MetricAndUKImperialThermalConductanceUnits: Set<MetricAndUKImperialThermalConductance> get() = MetricAndImperialPowerUnits.flatMap { power ->
    MetricAndUkImperialTemperatureUnits.map { power per it }
}.toSet()

/**
 * Set of all [MetricThermalConductance]
 */
val MetricThermalConductanceUnits: Set<MetricThermalConductance> get() = MetricPowerUnits.flatMap { power ->
    MetricAndUkImperialTemperatureUnits.map { power per it }
}.toSet()

/**
 * Set of all [UKImperialThermalConductance]
 */
val UKImperialThermalConductanceUnits: Set<UKImperialThermalConductance> get() = ImperialPowerUnits.flatMap { power ->
    MetricAndUkImperialTemperatureUnits.map { power per it }
}.toSet()

/**
 * Set of all [USCustomaryThermalConductance]
 */
val USCustomaryThermalConductanceUnits: Set<USCustomaryThermalConductance> get() = ImperialPowerUnits.flatMap { power ->
    USCustomaryTemperatureUnits.map { power per it }
}.toSet()

/**
 * Set of all [ThermalConductance]
 */
val ThermalConductanceUnits: Set<ThermalConductance> get() = MetricAndUKImperialThermalConductanceUnits +
    MetricThermalConductanceUnits.filter { it.power !is MetricMetricAndImperialPowerWrapper }.toSet() +
    UKImperialThermalConductanceUnits.filter { it.power !is ImperialMetricAndImperialPowerWrapper }.toSet() +
    USCustomaryThermalConductanceUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ThermalConductance]
 * SI unit is `Watt per Kelvin`
 */
@Serializable
sealed class ThermalConductance : DefinedScientificUnit<PhysicalQuantity.ThermalConductance>() {

    /**
     * The [Power] component
     */
    abstract val power: Power

    /**
     * The [Temperature] component
     */
    abstract val per: Temperature
    override val quantity = PhysicalQuantity.ThermalConductance
    override val symbol: String by lazy { "${power.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.deltaToSIUnitDelta(power.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = power.toSIUnit(per.deltaFromSIUnitDelta(value))
}

/**
 * A [ThermalConductance] for [MeasurementSystem.MetricAndUKImperial]
 * @param power the [MetricAndImperialPower] component
 * @param per the [MetricAndUKImperialTemperature] component
 */
@Serializable
data class MetricAndUKImperialThermalConductance(override val power: MetricAndImperialPower, override val per: MetricAndUKImperialTemperature) :
    ThermalConductance(),
    MetricAndUKImperialScientificUnit<PhysicalQuantity.ThermalConductance> {
    override val system = MeasurementSystem.MetricAndUKImperial

    /**
     * The [MetricThermalConductance] equivalent to this [MetricAndUKImperialThermalConductance]
     */
    val metric get() = power.metric per per

    /**
     * The [UKImperialThermalConductance] equivalent to this [MetricAndUKImperialThermalConductance]
     */
    val ukImperial get() = power.imperial per per
}

/**
 * A [ThermalConductance] for [MeasurementSystem.Metric]
 * @param power the [MetricPower] component
 * @param per the [MetricAndUKImperialTemperature] component
 */
@Serializable
data class MetricThermalConductance(override val power: MetricPower, override val per: MetricAndUKImperialTemperature) :
    ThermalConductance(),
    MetricScientificUnit<PhysicalQuantity.ThermalConductance> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [ThermalConductance] for [MeasurementSystem.UKImperial]
 * @param power the [ImperialPower] component
 * @param per the [MetricAndUKImperialTemperature] component
 */
@Serializable
data class UKImperialThermalConductance(override val power: ImperialPower, override val per: MetricAndUKImperialTemperature) :
    ThermalConductance(),
    UKImperialScientificUnit<PhysicalQuantity.ThermalConductance> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [ThermalConductance] for [MeasurementSystem.USCustomary]
 * @param power the [ImperialPower] component
 * @param per the [USCustomaryTemperature] component
 */
@Serializable
data class USCustomaryThermalConductance(override val power: ImperialPower, override val per: USCustomaryTemperature) :
    ThermalConductance(),
    USCustomaryScientificUnit<PhysicalQuantity.ThermalConductance> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricAndUKImperialThermalConductance] from a [MetricAndImperialPower] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [MetricAndUKImperialThermalConductance] represented by the units
 */
infix fun MetricAndImperialPower.per(temperature: MetricAndUKImperialTemperature) = MetricAndUKImperialThermalConductance(this, temperature)

/**
 * Gets a [MetricThermalConductance] from a [MetricPower] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [MetricThermalConductance] represented by the units
 */
infix fun MetricPower.per(temperature: MetricAndUKImperialTemperature) = MetricThermalConductance(this, temperature)

/**
 * Gets a [UKImperialThermalConductance] from an [ImperialPower] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [UKImperialThermalConductance] represented by the units
 */
infix fun ImperialPower.per(temperature: MetricAndUKImperialTemperature) = UKImperialThermalConductance(this, temperature)

/**
 * Gets a [USCustomaryThermalConductance] from a [MetricAndImperialPower] and a [USCustomaryTemperature]
 * @param temperature the [USCustomaryTemperature] component
 * @return the [USCustomaryThermalConductance] represented by the units
 */
infix fun MetricAndImperialPower.per(temperature: USCustomaryTemperature) = USCustomaryThermalConductance(this.imperial, temperature)

/**
 * Gets a [USCustomaryThermalConductance] from an [ImperialPower] and a [USCustomaryTemperature]
 * @param temperature the [USCustomaryTemperature] component
 * @return the [USCustomaryThermalConductance] represented by the units
 */
infix fun ImperialPower.per(temperature: USCustomaryTemperature) = USCustomaryThermalConductance(this, temperature)

internal fun SerializersModuleBuilder.setupForThermalConductance() {
    polymorphic(ThermalConductance::class) {
        registerThermalConductanceClasses()
    }
}

internal fun PolymorphicModuleBuilder<ThermalConductance>.registerThermalConductanceClasses() {
    subclass(MetricAndUKImperialThermalConductance::class, MetricAndUKImperialThermalConductance.serializer())
    subclass(MetricThermalConductance::class, MetricThermalConductance.serializer())
    subclass(UKImperialThermalConductance::class, UKImperialThermalConductance.serializer())
    subclass(USCustomaryThermalConductance::class, USCustomaryThermalConductance.serializer())
}
