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
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName
import kotlin.math.PI

val MetricLuminanceUnits = setOf(
    Nit,
    Nanonit,
    Micronit,
    Millinit,
    Centinit,
    Decinit,
    Decanit,
    Hectonit,
    Kilonit,
    Meganit,
    Giganit,
    Stilb,
    Apostilb,
    Lambert,
    Skot,
    Bril
)

val ImperialLuminanceUnits = setOf(
    FootLambert
)

val LuminanceUnits: Set<Luminance> = MetricLuminanceUnits + ImperialLuminanceUnits

@Serializable
sealed class Luminance : AbstractScientificUnit<MeasurementType.Luminance>()

@Serializable
sealed class MetricLuminance : Luminance(), MetricScientificUnit<MeasurementType.Luminance>
@Serializable
sealed class ImperialLuminance : Luminance(), ImperialScientificUnit<MeasurementType.Luminance>

@Serializable
object Nit : MetricLuminance(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Luminance> {
    override val symbol: String = "nt"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanonit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Nano(Nit)
@Serializable
object Micronit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Micro(Nit)
@Serializable
object Millinit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Milli(Nit)
@Serializable
object Centinit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Centi(Nit)
@Serializable
object Decinit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Deci(Nit)
@Serializable
object Decanit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Deca(Nit)
@Serializable
object Hectonit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Hecto(Nit)
@Serializable
object Kilonit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Kilo(Nit)
@Serializable
object Meganit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Mega(Nit)
@Serializable
object Giganit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Luminance, Nit> by Giga(Nit)

@Serializable
object Stilb : MetricLuminance() {
    override val symbol: String = "sb"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = SquareCentimeter.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareCentimeter.fromSIUnit(value)
}

@Serializable
object Apostilb : MetricLuminance() {
    private const val APOSTILB_IN_NIT = PI
    override val symbol: String = "asb"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = value * APOSTILB_IN_NIT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / APOSTILB_IN_NIT.toDecimal()
}

@Serializable
object Lambert : MetricLuminance() {
    override val symbol: String = "L"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(Stilb.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Stilb.toSIUnit(Apostilb.toSIUnit(value))
}

@Serializable
object Skot : MetricLuminance() {
    private const val SKOT_IN_APOSTILB = 1000.0
    override val symbol: String = "sk"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(value) * SKOT_IN_APOSTILB.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Apostilb.toSIUnit(value / SKOT_IN_APOSTILB.toDecimal())
}

@Serializable
object Bril : MetricLuminance() {
    private const val BRIL_IN_APOSTILB = 10000000.0
    override val symbol: String = "Bril"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(value) * BRIL_IN_APOSTILB.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Apostilb.toSIUnit(value / BRIL_IN_APOSTILB.toDecimal())
}

@Serializable
object FootLambert : ImperialLuminance() {
    override val symbol: String = "fL"
    override val type = MeasurementType.Luminance
    override val system = MeasurementSystem.Imperial
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(SquareFoot.toSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = SquareFoot.fromSIUnit(Apostilb.toSIUnit(value))
}

@JvmName("luminanceFromLuminousIntensityAndArea")
fun <
    LuminousIntensityUnit : LuminousIntensity,
    AreaUnit : Area,
    LuminanceUnit : Luminance
    > LuminanceUnit.luminance(
    luminousIntensity: ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byDividing(luminousIntensity, area)

@JvmName("luminousIntensityFromLuminanceAndArea")
fun <
    LuminousIntensityUnit : LuminousIntensity,
    AreaUnit : Area,
    LuminanceUnit : Luminance
    > LuminousIntensityUnit.luminousIntensity(
    luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byMultiplying(luminance, area)

@JvmName("areaFromLuminousIntensityAndLuminance")
fun <
    LuminousIntensityUnit : LuminousIntensity,
    AreaUnit : Area,
    LuminanceUnit : Luminance
    > AreaUnit.area(
    luminousIntensity: ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>,
    luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>
) = byDividing(luminousIntensity, luminance)

@JvmName("luminousIntensityDivSquareCentimeter")
infix operator fun <LuminousIntensityUnit : LuminousIntensity> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Stilb.luminance(this, area)
@JvmName("luminousIntensityDivMetricArea")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, AreaUnit : MetricArea> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Nit.luminance(this, area)
@JvmName("luminousIntensityDivIMperialArea")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, AreaUnit : ImperialArea> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootLambert.luminance(this, area)
@JvmName("luminousIntensityDivArea")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, AreaUnit : Area> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Nit.luminance(this, area)

@JvmName("luminanceTimesArea")
infix operator fun <LuminanceUnit : Luminance, AreaUnit : Area> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Candela.luminousIntensity(this, area)
@JvmName("areaTimesLuminance")
infix operator fun <LuminanceUnit : Luminance, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = luminance * this

@JvmName("luminousIntensityDivStilb")
infix operator fun <LuminousIntensityUnit : LuminousIntensity> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, Stilb>) = SquareCentimeter.area(this, luminance)
@JvmName("luminousIntensityDivLambert")
infix operator fun <LuminousIntensityUnit : LuminousIntensity> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, Lambert>) = SquareCentimeter.area(this, luminance)
@JvmName("luminousIntensityDivMetricLuminance")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, LuminanceUnit : MetricLuminance> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = SquareMeter.area(this, luminance)
@JvmName("luminousIntensityDivImperialLuminance")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, LuminanceUnit : ImperialLuminance> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, ImperialLuminance>) = SquareFoot.area(this, luminance)
@JvmName("luminousIntensityDivLuminance")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, LuminanceUnit : Luminance> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = SquareMeter.area(this, luminance)

@JvmName("luminanceFromIlluminanceAndSolidAngle")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance
    >
    LuminanceUnit.luminance(
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>,
    solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>
) = byDividing(illuminance, solidAngle)

@JvmName("solidAngleFromIlluminanceAndLuminance")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance
    >
    SolidAngleUnit.solidAngle(
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>,
    luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>
) = byDividing(illuminance, luminance)

@JvmName("illuminanceFromLuminanceAndSolidAngle")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance
    >
    IlluminanceUnit.illuminance(
    luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>,
    solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>
) = byMultiplying(luminance, solidAngle)

@JvmName("photDivSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, Phot>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Stilb.luminance(this, solidAngle)
@JvmName("photMultipleDivSolidAngle")
infix operator fun <PhotUnit, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, PhotUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) where PhotUnit : Illuminance, PhotUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> = Stilb.luminance(this, solidAngle)
@JvmName("metricIlluminanceDivSolidAngle")
infix operator fun <IlluminanceUnit : MetricIlluminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Nit.luminance(this, solidAngle)
@JvmName("imperialIlluminanceDivSolidAngle")
infix operator fun <IlluminanceUnit : ImperialIlluminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = FootLambert.luminance(this, solidAngle)
@JvmName("illuminanceDivSolidAngle")
infix operator fun <IlluminanceUnit : Illuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Nit.luminance(this, solidAngle)

@JvmName("illuminanceDivLuminancee")
infix operator fun <IlluminanceUnit : Illuminance, LuminanceUnit : Luminance> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = Steradian.solidAngle(this, luminance)

@JvmName("stilbTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, Stilb>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Phot.illuminance(this, solidAngle)
@JvmName("lambertTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, Lambert>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Phot.illuminance(this, solidAngle)
@JvmName("metricLuminanceTimesSolidAngle")
infix operator fun <LuminanceUnit : MetricLuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Lux.illuminance(this, solidAngle)
@JvmName("imperialLuminanceTimesSolidAngle")
infix operator fun <LuminanceUnit : ImperialLuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = FootCandle.illuminance(this, solidAngle)
@JvmName("luminanceTimesSolidAngle")
infix operator fun <LuminanceUnit : Luminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Lux.illuminance(this, solidAngle)

@JvmName("solidAngleTimesStilb")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>.times(luminance: ScientificValue<MeasurementType.Luminance, Stilb>) = luminance * this
@JvmName("solidAngleTimesLambertTimes")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>.times(luminance: ScientificValue<MeasurementType.Luminance, Lambert>) = luminance * this
@JvmName("solidAngleTimesMetricLuminance")
infix operator fun <LuminanceUnit : MetricLuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>.times(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = luminance * this
@JvmName("solidAngleTimesImperialLuminance")
infix operator fun <LuminanceUnit : ImperialLuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>.times(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = luminance * this
@JvmName("solidAngleTimesLuminance")
infix operator fun <LuminanceUnit : Luminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>.times(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = luminance * this
