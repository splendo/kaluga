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
object NanoLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Nano(Lumen)
@Serializable
object MicroLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Micro(Lumen)
@Serializable
object MilliLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Milli(Lumen)
@Serializable
object CentiLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Centi(Lumen)
@Serializable
object DeciLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Deci(Lumen)
@Serializable
object DecaLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Deca(Lumen)
@Serializable
object HectoLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Hecto(Lumen)
@Serializable
object KiloLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Kilo(Lumen)
@Serializable
object MegaLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Mega(Lumen)
@Serializable
object GigaLumen : LuminousFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousFlux, Lumen> by Giga(Lumen)

@JvmName("luminousFluxFromIntensityAndSolidAngle")
fun <
    IntensityUnit : LuminousIntensity,
    SolidAngleUnit : SolidAngle,
    FluxUnit : LuminousFlux
    >
    FluxUnit.flux(
    intensity: ScientificValue<MeasurementType.LuminousIntensity, IntensityUnit>,
    solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>
) : ScientificValue<MeasurementType.LuminousFlux, FluxUnit> = byMultiplying(intensity, solidAngle)

@JvmName("luminousFluxFromIlluminanceAndArea")
fun <
    FluxUnit : LuminousFlux,
    AreaUnit : Area,
    IlluminanceUnit : Illuminance
    >
    FluxUnit.flux(
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
): ScientificValue<MeasurementType.LuminousFlux, FluxUnit> = byMultiplying(illuminance, area)

@JvmName("luminousIntensityTimesSolidAngle")
infix operator fun <IntensityUnit : LuminousIntensity, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.LuminousIntensity, IntensityUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Lumen.flux(this, solidAngle)
@JvmName("solidAngleTimesLuminousIntensity")
infix operator fun <IntensityUnit : LuminousIntensity, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>.times(intensity: ScientificValue<MeasurementType.LuminousIntensity, IntensityUnit>) = intensity * this
@JvmName("illuminanceTimesArea")
infix operator fun <IlluminanceUnit : Illuminance, AreaUnit : Area> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Lumen.flux(this, area)
@JvmName("areaTimesIlluminance")
infix operator fun <IlluminanceUnit : Illuminance, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = illuminance * this
