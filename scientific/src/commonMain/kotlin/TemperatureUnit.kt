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
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable

@Serializable
sealed class Temperature<System : MeasurementSystem> :
    AbstractScientificUnit<System, MeasurementType.Temperature>()

@Serializable
sealed class MetricTemperature(override val symbol: String) :
    Temperature<MeasurementSystem.Metric>()

@Serializable
sealed class USCustomaryTemperature(override val symbol: String) :
    Temperature<MeasurementSystem.USCustomary>()

@Serializable
object Celsius : MetricTemperature("°C") {
    override fun toSIUnit(value: Decimal): Decimal = value + Kelvin.KELVIN_FREEZING.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value - Kelvin.KELVIN_FREEZING.toDecimal()
}

@Serializable
object Kelvin : MetricTemperature("K") {
    const val KELVIN_FREEZING = 273.15
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Fahrenheit : USCustomaryTemperature("°F") {
    const val FAHRENHEIT_FREEZING = 32.00
    override fun toSIUnit(value: Decimal): Decimal =
        (value - FAHRENHEIT_FREEZING.toDecimal()) * 5.0.toDecimal() / 9.0.toDecimal() + Kelvin.KELVIN_FREEZING.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        (value - Kelvin.KELVIN_FREEZING.toDecimal()) * 9.0.toDecimal() / 5.0.toDecimal() + FAHRENHEIT_FREEZING.toDecimal()
}

@Serializable
object Rankine : USCustomaryTemperature("°R") {
    override fun toSIUnit(value: Decimal): Decimal = value * 5.0.toDecimal() / 9.0.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * 9.0.toDecimal() / 5.0.toDecimal()
}
