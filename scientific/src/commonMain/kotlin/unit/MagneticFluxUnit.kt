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

val MagneticFluxUnits: Set<MagneticFlux> get() = setOf(
    Weber,
    Nanoweber,
    Microweber,
    Milliweber,
    Centiweber,
    Deciweber,
    Decaweber,
    Hectoweber,
    Kiloweber,
    Megaweber,
    Gigaweber,
    Maxwell
)

@Serializable
sealed class MagneticFlux : AbstractScientificUnit<MeasurementType.MagneticFlux>(), MetricAndImperialScientificUnit<MeasurementType.MagneticFlux>

@Serializable
object Weber : MagneticFlux(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux> {
    override val symbol = "Wb"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.MagneticFlux
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanoweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Nano(Weber)
@Serializable
object Microweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Micro(Weber)
@Serializable
object Milliweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Milli(Weber)
@Serializable
object Centiweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Centi(Weber)
@Serializable
object Deciweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Deci(Weber)
@Serializable
object Decaweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Deca(Weber)
@Serializable
object Hectoweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Hecto(Weber)
@Serializable
object Kiloweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Kilo(Weber)
@Serializable
object Megaweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Mega(Weber)
@Serializable
object Gigaweber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Giga(Weber)
@Serializable
object Maxwell : MagneticFlux() {
    override val symbol = "Mx"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.MagneticFlux
    override fun fromSIUnit(value: Decimal): Decimal = Abvolt.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Abvolt.toSIUnit(value)
}
