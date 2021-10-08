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

sealed interface MeasurementSystem {
    interface Metric : MeasurementSystem
    interface USCustomary : MeasurementSystem
    interface UKImperial : MeasurementSystem
    interface Imperial : USCustomary, UKImperial
    interface Global : Imperial, Metric
}

interface BaseMetricUnit<T : MeasurementType, S : MeasurementSystem.Metric> : ScientificUnit<S, T>

class Kilo<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "k${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 1000.0.toDecimal()
        override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 1000.0.toDecimal()
}

class Hecto<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "h${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 100.0.toDecimal()
}

class Deca<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "da${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) / 10.0.toDecimal()
        override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) * 10.0.toDecimal()
}

class Deci<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "d${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 10.0.toDecimal()
}

class Centi<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "c${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 100.0.toDecimal()
}

class Milli<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "c${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000.0.toDecimal()
}

class Micro<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "μ${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000000.0.toDecimal()
}

class Nano<T : MeasurementType, S : MeasurementSystem.Metric, U : BaseMetricUnit<T, S>>(private val unit : U) : ScientificUnit<S, T> {
    override val symbol: String = "μ${unit.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(value) * 1000000000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(value) / 1000000000.0.toDecimal()
}

