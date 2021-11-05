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

sealed interface MeasurementUsage {
    interface UsedInMetric
    interface UsedInUKImperial
    interface UsedInUSCustomary
    interface UsedInImperial : UsedInUKImperial, UsedInUSCustomary
    interface UsedInMetricAndUKImperial : UsedInMetric, UsedInUKImperial
    interface UsedInMetricAndImperial : UsedInMetric, UsedInImperial
}

@Serializable
sealed class MeasurementSystem : MeasurementUsage {

    @Serializable
    object Metric : MeasurementSystem(), MeasurementUsage.UsedInMetric
    @Serializable
    object Imperial : MeasurementSystem(), MeasurementUsage.UsedInImperial
    @Serializable
    object USCustomary : MeasurementSystem(), MeasurementUsage.UsedInUSCustomary
    @Serializable
    object UKImperial : MeasurementSystem(), MeasurementUsage.UsedInUKImperial
    @Serializable
    object MetricAndUKImperial : MeasurementSystem(), MeasurementUsage.UsedInMetricAndUKImperial
    @Serializable
    object MetricAndImperial : MeasurementSystem(), MeasurementUsage.UsedInMetricAndImperial
}

interface MetricBaseUnit<S, T : PhysicalQuantity> : SystemScientificUnit<S, T> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val type: T
}

interface MetricMultipleUnit<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>> : SystemScientificUnit<S, T> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric

class Giga<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "G${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000000000.0.toDecimal()
}

class Mega<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "M${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000000.0.toDecimal()
}

class Kilo<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "k${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000.0.toDecimal()
}

class Hecto<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "h${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 100.0.toDecimal()
}

class Deca<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "da${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 10.0.toDecimal()
}

class Deci<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "d${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 10.0.toDecimal()
}

class Centi<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "c${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 100.0.toDecimal()
}

class Milli<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "c${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000.0.toDecimal()
}

class Micro<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "μ${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000000.0.toDecimal()
}

class Nano<S, T : PhysicalQuantity, U : MetricBaseUnit<S, T>>(private val unit: U) : MetricMultipleUnit<S, T, U> where S : MeasurementSystem, S : MeasurementUsage.UsedInMetric {
    override val symbol: String = "μ${unit.symbol}"
    override val system: S = unit.system
    override val type: T = unit.type
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000000000.0.toDecimal()
}
