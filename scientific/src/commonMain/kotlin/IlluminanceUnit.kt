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
sealed class Illuminance : AbstractScientificUnit<MeasurementType.Illuminance>()

@Serializable
sealed class MetricIlluminance : Illuminance(), MetricScientificUnit<MeasurementType.Illuminance>
@Serializable
sealed class ImperialIlluminance : Illuminance(), ImperialScientificUnit<MeasurementType.Illuminance>

@Serializable
object Lux : MetricIlluminance(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Illuminance> {
    override val symbol = "lx"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Illuminance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Nano(Lux)
@Serializable
object MicroLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Micro(Lux)
@Serializable
object MilliLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Milli(Lux)
@Serializable
object CentiLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Centi(Lux)
@Serializable
object DeciLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Deci(Lux)
@Serializable
object DecaLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Deca(Lux)
@Serializable
object HectoLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Hecto(Lux)
@Serializable
object KiloLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Kilo(Lux)
@Serializable
object MegaLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Mega(Lux)
@Serializable
object GigaLux : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Lux> by Giga(Lux)

@Serializable
object Phot : MetricIlluminance(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Illuminance> {
    override val symbol = "ph"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Illuminance
    override fun fromSIUnit(value: Decimal): Decimal = SquareCentimeter.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareCentimeter.fromSIUnit(value)
}

@Serializable
object NanoPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Nano(Phot)
@Serializable
object MicroPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Micro(Phot)
@Serializable
object MilliPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Milli(Phot)
@Serializable
object CentiPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Centi(Phot)
@Serializable
object DeciPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Deci(Phot)
@Serializable
object DecaPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Deca(Phot)
@Serializable
object HectoPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Hecto(Phot)
@Serializable
object KiloPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Kilo(Phot)
@Serializable
object MegaPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Mega(Phot)
@Serializable
object GigaPhot : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> by Giga(Phot)

@Serializable
object FootCandle : ImperialIlluminance() {
    override val symbol: String = "fc"
    override val system = MeasurementSystem.Imperial
    override val type=  MeasurementType.Illuminance
    override fun fromSIUnit(value: Decimal): Decimal = SquareFoot.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareFoot.fromSIUnit(value)
}

fun <
    FluxUnit : LuminousFlux,
    AreaUnit : Area,
    IlluminanceUnit : Illuminance
    >
    IlluminanceUnit.illuminance(
    flux: ScientificValue<MeasurementType.LuminousFlux, FluxUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
): ScientificValue<MeasurementType.Illuminance, IlluminanceUnit> = byDividing(flux, area)

@JvmName("lumenDivSquareCentimeter")
infix fun <FluxUnit : LuminousFlux> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(squareCentimeter: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Phot.illuminance(this, squareCentimeter)
@JvmName("lumenDivMetricArea")
infix fun <FluxUnit : LuminousFlux, AreaUnit : MetricArea> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Lux.illuminance(this, area)
@JvmName("lumenDivImperialArea")
infix fun <FluxUnit : LuminousFlux, AreaUnit : ImperialArea> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootCandle.illuminance(this, area)
