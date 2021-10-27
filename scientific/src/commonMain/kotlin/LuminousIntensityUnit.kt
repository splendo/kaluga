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
import kotlinx.serialization.Serializable

val LuminousIntensityUnits = setOf(
    Candela,
    Nanocandela,
    Microcandela,
    Millicandela,
    Centicandela,
    Decicandela,
    Decacandela,
    Hectocandela,
    Kilocandela,
    Megacandela,
    Gigacandela
)

@Serializable
sealed class LuminousIntensity : AbstractScientificUnit<MeasurementType.LuminousIntensity>(), MetricAndImperialScientificUnit<MeasurementType.LuminousIntensity>

@Serializable
object Candela : LuminousIntensity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity> {
    override val symbol = "cd"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.LuminousIntensity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanocandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Nano(Candela)
@Serializable
object Microcandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Micro(Candela)
@Serializable
object Millicandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Milli(Candela)
@Serializable
object Centicandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Centi(Candela)
@Serializable
object Decicandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Deci(Candela)
@Serializable
object Decacandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Deca(Candela)
@Serializable
object Hectocandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Hecto(Candela)
@Serializable
object Kilocandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Kilo(Candela)
@Serializable
object Megacandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Mega(Candela)
@Serializable
object Gigacandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Giga(Candela)
