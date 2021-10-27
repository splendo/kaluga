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

val MetricAndUkImperialTemperatureUnits = setOf(Kelvin, Celsius)
val USCustomaryTemperatureUnits = setOf(Rankine, Fahrenheit)
val TemperatureUnits: Set<Temperature> = MetricAndUkImperialTemperatureUnits + USCustomaryTemperatureUnits

@Serializable
sealed class Temperature : AbstractScientificUnit<MeasurementType.Temperature>() {
    override val type = MeasurementType.Temperature
}

@Serializable
sealed class MetricAndUKImperialTemperature(override val symbol: String) : Temperature(), MetricAndUKImperialScientificUnit<MeasurementType.Temperature>, MeasurementUsage.UsedInUKImperial {
    override val system = MeasurementSystem.MetricAndUKImperial
}

@Serializable
sealed class USCustomaryTemperature(override val symbol: String) : Temperature(), USCustomaryScientificUnit<MeasurementType.Temperature> {
    override val system = MeasurementSystem.USCustomary
}

@Serializable
object Celsius : MetricAndUKImperialTemperature("°C") {
    override fun toSIUnit(value: Decimal): Decimal = value + Kelvin.KELVIN_FREEZING.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value - Kelvin.KELVIN_FREEZING.toDecimal()
}

@Serializable
object Kelvin : MetricAndUKImperialTemperature("K") {
    internal const val KELVIN_FREEZING = 273.15
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Fahrenheit : USCustomaryTemperature("°F") {
    override fun toSIUnit(value: Decimal): Decimal = Rankine.toSIUnit(value + Rankine.RANKINE_FREEZING.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Rankine.fromSIUnit(value) - Rankine.RANKINE_FREEZING.toDecimal()
}

@Serializable
object Rankine : USCustomaryTemperature("°R") {
    internal const val RANKINE_FREEZING = 459.67
    private const val FAHRENHEIT_INCREASE_PER_CELSIUS_INCREASE = 5.0 / 9.0
    override fun toSIUnit(value: Decimal): Decimal = value * FAHRENHEIT_INCREASE_PER_CELSIUS_INCREASE.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / FAHRENHEIT_INCREASE_PER_CELSIUS_INCREASE.toDecimal()
}
