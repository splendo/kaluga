/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASISystem,
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
 * The system in which measurement is used
 */
sealed interface MeasurementUsage {

    /**
     * A measurement that is used in the Metric system
     */
    interface UsedInMetric

    /**
     * A measurement that is used in the UK Imperial system
     */
    interface UsedInUKImperial

    /**
     * A measurement that is used in the US Customary system
     */
    interface UsedInUSCustomary

    /**
     * A measurement that is used in both UK Imperial and US Customary systems
     */
    interface UsedInImperial : UsedInUKImperial, UsedInUSCustomary

    /**
     * A measurement that is used in both Metric and UK Imperial systems
     */
    interface UsedInMetricAndUKImperial : UsedInMetric, UsedInUKImperial

    /**
     * A measurement that is used in Metric, UK Imperial and US Customary systems
     */
    interface UsedInMetricAndImperial : UsedInMetric, UsedInImperial
}

/**
 * The system of measurement
 */
@Serializable
sealed class MeasurementSystem : MeasurementUsage, com.splendo.kaluga.base.utils.Serializable {

    /**
     * The metric system
     */
    @Serializable
    data object Metric : MeasurementSystem(), MeasurementUsage.UsedInMetric

    /**
     * The Imperial system (both UK Imperial and US Customary)
     */
    @Serializable
    data object Imperial : MeasurementSystem(), MeasurementUsage.UsedInImperial

    /**
     * The US Customary system
     */
    @Serializable
    data object USCustomary : MeasurementSystem(), MeasurementUsage.UsedInUSCustomary

    /**
     * The UK Imperial system
     */
    @Serializable
    data object UKImperial : MeasurementSystem(), MeasurementUsage.UsedInUKImperial

    /**
     * System shared between Metric and UK Imperial
     */
    @Serializable
    data object MetricAndUKImperial : MeasurementSystem(), MeasurementUsage.UsedInMetricAndUKImperial

    /**
     * A global system
     */
    @Serializable
    data object MetricAndImperial : MeasurementSystem(), MeasurementUsage.UsedInMetricAndImperial
}

/**
 * A base unit of a [PhysicalQuantity] used in a [MeasurementSystem] that has [MeasurementUsage.UsedInMetric]
 * Base units can be multiplied using [MetricMultipleUnit] to get a derived unit (e.g. Meter to Kilometer)
 * @param System the type of [MeasurementSystem] to use. Must have [MeasurementUsage.UsedInMetric]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 */
interface MetricBaseUnit<System, Quantity : PhysicalQuantity> : SystemScientificUnit<System, Quantity> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val quantity: Quantity
}

/**
 * A multiplication of a [MetricBaseUnit].
 * Example: Kilometer is a multiple of Meter
 * @param System the type of [MeasurementSystem] to use. Must have [MeasurementUsage.UsedInMetric]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [MetricBaseUnit] to multiply with
 */
sealed interface MetricMultipleUnit<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>> :
    SystemScientificUnit<System, Quantity> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric

class Giga<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "G${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000000000.0.toDecimal()
}

class Mega<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "M${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000000.0.toDecimal()
}

class Kilo<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "k${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000.0.toDecimal()
}

class Hecto<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "h${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 100.0.toDecimal()
}

class Deca<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "da${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 10.0.toDecimal()
}

class Deci<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "d${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 10.0.toDecimal()
}

class Centi<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "c${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 100.0.toDecimal()
}

class Milli<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "m${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000.0.toDecimal()
}

class Micro<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "μ${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000000.0.toDecimal()
}

class Nano<System, Quantity : PhysicalQuantity, Unit : MetricBaseUnit<System, Quantity>>(private val unit: Unit) :
    MetricMultipleUnit<System, Quantity, Unit> where System : MeasurementSystem, System : MeasurementUsage.UsedInMetric {
    override val symbol: String = "n${unit.symbol}"
    override val system: System = unit.system
    override val quantity: Quantity = unit.quantity
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000000000.0.toDecimal()
}
