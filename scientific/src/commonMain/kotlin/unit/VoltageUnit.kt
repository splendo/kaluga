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
    Abvolt
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Voltage]
 * SI unit is [Volt]
 */
@Serializable
sealed class Voltage : AbstractScientificUnit<PhysicalQuantity.Voltage>(), MetricAndImperialScientificUnit<PhysicalQuantity.Voltage>

@Serializable
object Volt : Voltage(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage> {
    override val symbol = "V"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanovolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Nano(Volt)

@Serializable
object Abvolt : Voltage() {
    private const val ABVOLT_IN_VOLT = 100000000.0
    override val symbol = "abV"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value * ABVOLT_IN_VOLT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / ABVOLT_IN_VOLT.toDecimal()
}

@Serializable
sealed class VoltMultiple : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt>

@Serializable
object Microvolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Micro(Volt)

@Serializable
object Millivolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Milli(Volt)

@Serializable
object Centivolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Centi(Volt)

@Serializable
object Decivolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Deci(Volt)

@Serializable
object Decavolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Deca(Volt)

@Serializable
object Hectovolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Hecto(Volt)

@Serializable
object Kilovolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Kilo(Volt)

@Serializable
object Megavolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Mega(Volt)

@Serializable
object Gigavolt : VoltMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Voltage, Volt> by Giga(Volt)
