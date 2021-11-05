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
import com.splendo.kaluga.scientific.MeasurementType
import kotlinx.serialization.Serializable

val LuminousFluxUnits: Set<LuminousFlux> get() = setOf(
    Lumen,
    Nanolumen,
    Microlumen,
    Millilumen,
    Centilumen,
    Decilumen,
    Decalumen,
    Hectolumen,
    Kilolumen,
    Megalumen,
    Gigalumen
)

@Serializable
sealed class LuminousFlux : AbstractScientificUnit<MeasurementType.LuminousFlux>(), MetricAndImperialScientificUnit<MeasurementType.LuminousFlux>

@Serializable
object Lumen : LuminousFlux(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux> {
    override val symbol = "lm"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.LuminousFlux
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanolumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Nano(Lumen)
@Serializable
object Microlumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Micro(Lumen)
@Serializable
object Millilumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Milli(Lumen)
@Serializable
object Centilumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Centi(Lumen)
@Serializable
object Decilumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Deci(Lumen)
@Serializable
object Decalumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Deca(Lumen)
@Serializable
object Hectolumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Hecto(Lumen)
@Serializable
object Kilolumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Kilo(Lumen)
@Serializable
object Megalumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Mega(Lumen)
@Serializable
object Gigalumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Giga(Lumen)
