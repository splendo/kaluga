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
 * Set of all [Voltage]
 */
val VoltageUnits: Set<Voltage> get() = setOf(
    Volt,
    Nanovolt,
    Microvolt,
    Millivolt,
    Centivolt,
    Decivolt,
    Decavolt,
    Hectovolt,
    Kilovolt,
    Megavolt,
    Gigavolt,
    Abvolt,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Voltage]
 * SI unit is [Volt]
 */
@Serializable
sealed class Voltage :
    DefinedScientificUnit<PhysicalQuantity.Voltage>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Voltage>

@Serializable
data object Volt : Voltage(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage> {
    override val symbol = "V"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
data object Nanovolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Nano(Volt)

@Serializable
data object Abvolt : Voltage() {
    private val ABVOLT_IN_VOLT = 100000000.toDecimal()
    override val symbol = "abV"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value * ABVOLT_IN_VOLT
    override fun toSIUnit(value: Decimal): Decimal = value / ABVOLT_IN_VOLT
}

@Serializable
sealed class VoltMultiple :
    Voltage(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt>

@Serializable
data object Microvolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Micro(Volt)

@Serializable
data object Millivolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Milli(Volt)

@Serializable
data object Centivolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Centi(Volt)

@Serializable
data object Decivolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Deci(Volt)

@Serializable
data object Decavolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Deca(Volt)

@Serializable
data object Hectovolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Hecto(Volt)

@Serializable
data object Kilovolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Kilo(Volt)

@Serializable
data object Megavolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Mega(Volt)

@Serializable
data object Gigavolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Giga(Volt)

internal fun SerializersModuleBuilder.setupForVoltage() {
    polymorphic(Voltage::class) {
        registerVoltageClasses()
    }
}

internal fun PolymorphicModuleBuilder<Voltage>.registerVoltageClasses() {
    subclass(Abvolt::class, Abvolt.serializer())
    subclass(Nanovolt::class, Nanovolt.serializer())
    subclass(Volt::class, Volt.serializer())
    subclass(Centivolt::class, Centivolt.serializer())
    subclass(Decavolt::class, Decavolt.serializer())
    subclass(Decivolt::class, Decivolt.serializer())
    subclass(Gigavolt::class, Gigavolt.serializer())
    subclass(Hectovolt::class, Hectovolt.serializer())
    subclass(Kilovolt::class, Kilovolt.serializer())
    subclass(Megavolt::class, Megavolt.serializer())
    subclass(Microvolt::class, Microvolt.serializer())
    subclass(Millivolt::class, Millivolt.serializer())
}
