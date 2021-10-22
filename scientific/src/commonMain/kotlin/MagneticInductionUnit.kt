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
import kotlin.jvm.JvmName

@Serializable
sealed class MagneticInduction : AbstractScientificUnit<MeasurementType.MagneticInduction>(), MetricAndImperialScientificUnit<MeasurementType.MagneticInduction>

@Serializable
object Tesla : MagneticInduction(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction> {
    override val symbol = "T"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.MagneticInduction
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Nano(Tesla)
@Serializable
object MicroTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Micro(Tesla)
@Serializable
object MilliTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Milli(Tesla)
@Serializable
object CentiTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Centi(Tesla)
@Serializable
object DeciTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Deci(Tesla)
@Serializable
object DecaTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Deca(Tesla)
@Serializable
object HectoTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Hecto(Tesla)
@Serializable
object KiloTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Kilo(Tesla)
@Serializable
object MegaTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Mega(Tesla)
@Serializable
object GigaTesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticInduction, Tesla> by Giga(Tesla)

@JvmName("inductionFromFluxAndArea")
fun <
    FluxUnit : MagneticFlux,
    AreaUnit : Area,
    InductionUnit : MagneticInduction
    >
    InductionUnit.induction(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.MagneticInduction, InductionUnit> = byDividing(flux, area)

@JvmName("fluxDivArea")
infix operator fun <FluxUnit : MagneticFlux, AreaUnit : Area> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Tesla.induction(this, area)
