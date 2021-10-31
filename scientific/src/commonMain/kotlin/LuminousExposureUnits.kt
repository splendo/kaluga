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
import com.splendo.kaluga.scientific.converter.illuminance.times
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricLuminousExposureUnits = MetricIlluminanceUnits.flatMap { illuminance ->
    TimeUnits.map { illuminance x it }
}.toSet()

val ImperialLuminousExposureUnits = ImperialIlluminanceUnits.flatMap { illuminance ->
    TimeUnits.map { illuminance x it }
}.toSet()

val LuminousExposureUnits: Set<LuminousExposure> = MetricLuminousExposureUnits + ImperialLuminousExposureUnits

@Serializable
abstract class LuminousExposure : ScientificUnit<MeasurementType.LuminousExposure> {
    abstract val illuminance: Illuminance
    abstract val time: Time
    override val symbol: String get() = "${illuminance.symbol}⋅${time.symbol}"
    override val type = MeasurementType.LuminousExposure
    override fun fromSIUnit(value: Decimal): Decimal = illuminance.fromSIUnit(time.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = time.toSIUnit(illuminance.toSIUnit(value))
}

@Serializable
class MetricLuminousExposure(override val illuminance: MetricIlluminance, override val time: Time) : LuminousExposure(), MetricScientificUnit<MeasurementType.LuminousExposure> {
    override val system = MeasurementSystem.Metric
}

@Serializable
class ImperialLuminousExposure(override val illuminance: ImperialIlluminance, override val time: Time) : LuminousExposure(), ImperialScientificUnit<MeasurementType.LuminousExposure> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricIlluminance.x(time: Time) = MetricLuminousExposure(this, time)
infix fun ImperialIlluminance.x(time: Time) = ImperialLuminousExposure(this, time)

@JvmName("illuminanceFromLuminousExposureAndTime")
fun <
    LuminousExposureUnit : LuminousExposure,
    TimeUnit : Time,
    IlluminanceUnit : Illuminance
    > IlluminanceUnit.illuminance(
    luminousExposure: ScientificValue<MeasurementType.LuminousExposure, LuminousExposureUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(luminousExposure, time)

@JvmName("luminousExposureFromIlluminanceAndTime")
fun <
    LuminousExposureUnit : LuminousExposure,
    TimeUnit : Time,
    IlluminanceUnit : Illuminance
    > LuminousExposureUnit.luminousExposure(
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(illuminance, time)

@JvmName("timeFromLuminousExposureAndIlluminance")
fun <
    LuminousExposureUnit : LuminousExposure,
    TimeUnit : Time,
    IlluminanceUnit : Illuminance
    > TimeUnit.time(
    luminousExposure: ScientificValue<MeasurementType.LuminousExposure, LuminousExposureUnit>,
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>
) = byDividing(luminousExposure, illuminance)

@JvmName("metricLuminousExposureDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.illuminance.illuminance(this, time)
@JvmName("imperialLuminousExposureDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.illuminance.illuminance(this, time)
@JvmName("luminousExposureDivTime")
infix operator fun <LuminousExposureUnit : LuminousExposure, TimeUnit : Time> ScientificValue<MeasurementType.LuminousExposure, LuminousExposureUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.illuminance.illuminance(this, time)

@JvmName("metricLuminousExposureDivMetricIlluminance")
infix operator fun <IlluminanceUnit : MetricIlluminance> ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = unit.time.time(this, illuminance)
@JvmName("imperialLuminousExposureDivImperialIlluminance")
infix operator fun <IlluminanceUnit : ImperialIlluminance> ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = unit.time.time(this, illuminance)
@JvmName("luminousExposureDivIlluminance")
infix operator fun <IlluminanceUnit : Illuminance> ScientificValue<MeasurementType.LuminousExposure, LuminousExposure>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = unit.time.time(this, illuminance)

@JvmName("MetricIlluminanceTimesTime")
infix operator fun <IlluminanceUnit : MetricIlluminance, TimeUnit : Time> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).luminousExposure(this, time)
@JvmName("ImperialIlluminanceTimesTime")
infix operator fun <IlluminanceUnit : ImperialIlluminance, TimeUnit : Time> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).luminousExposure(this, time)
@JvmName("IlluminanceTimesTime")
infix operator fun <IlluminanceUnit : Illuminance, TimeUnit : Time> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Lux x time.unit).luminousExposure(this, time)

@JvmName("timeTimesMetricIlluminance")
infix operator fun <IlluminanceUnit : MetricIlluminance, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = illuminance * this
@JvmName("timeTimesImperialIlluminance")
infix operator fun <IlluminanceUnit : ImperialIlluminance, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = illuminance * this
@JvmName("timeTimesIlluminance")
infix operator fun <IlluminanceUnit : Illuminance, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = illuminance * this

@JvmName("illuminanceFromLuminousExposureAndArea")
fun <
    LuminousExposureUnit : LuminousExposure,
    AreaUnit : Area
    > LuminousEnergy.luminousEnergy(
    luminousExposure: ScientificValue<MeasurementType.LuminousExposure, LuminousExposureUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byMultiplying(luminousExposure, area)

@JvmName("luminousExposureFromLuminousEnergyAndArea")
fun <
    LuminousExposureUnit : LuminousExposure,
    AreaUnit : Area
    > LuminousExposureUnit.luminousExposure(
    luminousEnergy: ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byDividing(luminousEnergy, area)

@JvmName("areaFromLuminousEnergyAndExposure")
fun <
    LuminousExposureUnit : LuminousExposure,
    AreaUnit : Area
    > AreaUnit.area(
    luminousEnergy: ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>,
    luminousExposure: ScientificValue<MeasurementType.LuminousExposure, LuminousExposureUnit>,
) = byDividing(luminousEnergy, luminousExposure)

@JvmName("luminousEnergyDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.luminousFlux) / 1(area.unit)).unit x unit.time).luminousExposure(this, area)
@JvmName("luminousEnergyDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.luminousFlux) / 1(area.unit)).unit x unit.time).luminousExposure(this, area)
@JvmName("luminousEnergyDivArea")
infix operator fun <AreaUnit : Area> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.luminousFlux) / 1(area.unit)).unit x unit.time).luminousExposure(this, area)

@JvmName("luminousEnergyDivMetricExposure")
infix operator fun ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>) = (1(unit.luminousFlux) / 1(luminousExposure.unit.illuminance)).unit.area(this, luminousExposure)
@JvmName("luminousEnergyDivImperialExposure")
infix operator fun ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>) = (1(unit.luminousFlux) / 1(luminousExposure.unit.illuminance)).unit.area(this, luminousExposure)
@JvmName("luminousEnergyDivExposure")
infix operator fun <ExposureUnit : LuminousExposure> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, ExposureUnit>) = (1(unit.luminousFlux) / 1(luminousExposure.unit.illuminance)).unit.area(this, luminousExposure)

@JvmName("metricExposureTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.illuminance) * 1(area.unit)).unit x unit.time).luminousEnergy(this, area)
@JvmName("imperialExposureTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.illuminance) * 1(area.unit)).unit x unit.time).luminousEnergy(this, area)
@JvmName("exposureTimesArea")
infix operator fun <ExposureUnit : LuminousExposure, AreaUnit : Area> ScientificValue<MeasurementType.LuminousExposure, ExposureUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.illuminance) * 1(area.unit)).unit x unit.time).luminousEnergy(this, area)

@JvmName("metricAreaTimesMetricExposure")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>) = luminousExposure * this
@JvmName("imperialAreaTimesImperialExposure")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>) = luminousExposure * this
@JvmName("areaTimesExposure")
infix operator fun <ExposureUnit : LuminousExposure, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, ExposureUnit>) = luminousExposure * this
