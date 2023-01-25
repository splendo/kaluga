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
import com.splendo.kaluga.scientific.PhysicalQuantity
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
sealed class LuminousFlux : AbstractScientificUnit<PhysicalQuantity.LuminousFlux>(), MetricAndImperialScientificUnit<PhysicalQuantity.LuminousFlux>

@Serializable
object Lumen : LuminousFlux(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux> {
    override val symbol = "lm"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.LuminousFlux
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class LumenMultiple : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen>

@Serializable
object Nanolumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Nano(Lumen)
@Serializable
object Microlumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Micro(Lumen)
@Serializable
object Millilumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Milli(Lumen)
@Serializable
object Centilumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Centi(Lumen)
@Serializable
object Decilumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Deci(Lumen)
@Serializable
object Decalumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Deca(Lumen)
@Serializable
object Hectolumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Hecto(Lumen)
@Serializable
object Kilolumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Kilo(Lumen)
@Serializable
object Megalumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Mega(Lumen)
@Serializable
object Gigalumen : LumenMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousFlux, Lumen> by Giga(Lumen)
