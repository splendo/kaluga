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

val MetricLengthUnits = setOf(
    Meter,
    Nanometer,
    Micrometer,
    Millimeter,
    Centimeter,
    Decimeter,
    Decameter,
    Hectometer,
    Kilometer,
    Megameter,
    Gigameter,
    NauticalMile
)

val ImperialLengthUnits = setOf(
    Inch,
    Foot,
    Yard,
    Mile
)

val LengthUnits: Set<Length> = MetricLengthUnits + ImperialLengthUnits

@Serializable
sealed class Length : AbstractScientificUnit<MeasurementType.Length>()

@Serializable
sealed class MetricLength : Length(), MetricScientificUnit<MeasurementType.Length>

@Serializable
sealed class ImperialLength(override val symbol: String) : Length(), ImperialScientificUnit<MeasurementType.Length> {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Length
}

// Metric Length
@Serializable
object Meter : MetricLength(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Length> {
    override val symbol: String = "m"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Length
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Nano(Meter)

@Serializable
object Micrometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Micro(Meter)

@Serializable
object Millimeter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Milli(Meter)

@Serializable
object Centimeter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Centi(Meter)

@Serializable
object Decimeter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Deci(Meter)

@Serializable
object Decameter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Deca(Meter)

@Serializable
object Hectometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Hecto(Meter)

@Serializable
object Kilometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Kilo(Meter)

@Serializable
object Megameter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Mega(Meter)

@Serializable
object Gigameter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Giga(Meter)

@Serializable
object NauticalMile : MetricLength() {
    private const val METER_IN_NAUTICAL_MILE = 1852
    override val symbol: String = "nmi"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Length
    override fun toSIUnit(value: Decimal): Decimal = value * METER_IN_NAUTICAL_MILE.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / METER_IN_NAUTICAL_MILE.toDecimal()
}

// Imperial Length
@Serializable
object Inch : ImperialLength("in") {
    private const val INCHES_IN_FOOT = 12
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(value / INCHES_IN_FOOT.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(value) * INCHES_IN_FOOT.toDecimal()
}

@Serializable
object Foot : ImperialLength("ft") {
    private const val METER_IN_FEET = 0.3048
    override fun toSIUnit(value: Decimal): Decimal = value * METER_IN_FEET.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / METER_IN_FEET.toDecimal()
}

@Serializable
object Yard : ImperialLength("yd") {
    private const val FOOT_IN_YARD = 3
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(value * FOOT_IN_YARD.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(value) / FOOT_IN_YARD.toDecimal()
}

@Serializable
object Mile : ImperialLength("mi") {
    private const val YARDS_IN_MILE = 1760
    override fun toSIUnit(value: Decimal): Decimal = Yard.toSIUnit(value * YARDS_IN_MILE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Yard.fromSIUnit(value) / YARDS_IN_MILE.toDecimal()
}

@JvmName("distanceFromSpeedAndTime")
fun <
    LengthUnit : Length,
    TimeUnit : Time,
    SpeedUnit : Speed
    > LengthUnit.distance(
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(speed, time)

@JvmName("widthFromAreaAndLength")
fun <
    LengthUnit : Length,
    WidthUnit : Length,
    AreaUnit : Area
    > WidthUnit.width(
    area: ScientificValue<MeasurementType.Area, AreaUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byDividing(area, length)

@JvmName("heightFromVolumeAndArea")
fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume
    > HeightUnit.height(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.Length, HeightUnit> = byDividing(volume, area)

@JvmName("heightFromVolumeLengthAndWidth")
fun <
    HeightUnit : Length,
    LengthUnit : Length,
    WidthUnit : Length,
    VolumeUnit : Volume
    > HeightUnit.height(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>,
) = height(volume, SquareMeter.area(length, width))

@JvmName("lengthFromEnergyAndForce")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length
    > LengthUnit.distance(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    force: ScientificValue<MeasurementType.Force, ForceUnit>
) = byDividing(energy, force)

@JvmName("metricSpeedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, MetricSpeed>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) : ScientificValue<MeasurementType.Length, MetricLength> = unit.distance.distance(this, time)
@JvmName("timeTimesMetricSpeed")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) : ScientificValue<MeasurementType.Length, MetricLength> = speed * this
@JvmName("imperialSpeedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) : ScientificValue<MeasurementType.Length, ImperialLength> = unit.distance.distance(this, time)
@JvmName("timeTimesImperialSpeed")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) : ScientificValue<MeasurementType.Length, ImperialLength> = speed * this
@JvmName("speedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, Speed>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) : ScientificValue<MeasurementType.Length, Length> = unit.distance.distance(this, time)
@JvmName("timeTimesSpeed")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(speed: ScientificValue<MeasurementType.Speed, Speed>) : ScientificValue<MeasurementType.Length, Length> = speed * this

@JvmName("squareMeterDivMeter")
operator fun ScientificValue<MeasurementType.Area, SquareMeter>.div(length: ScientificValue<MeasurementType.Length, Meter>) = Meter.width(this, length)
@JvmName("squareNanoeterDivNanometer")
operator fun ScientificValue<MeasurementType.Area, SquareNanometer>.div(length: ScientificValue<MeasurementType.Length, Nanometer>) = Nanometer.width(this, length)
@JvmName("squareMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementType.Area, SquareMicrometer>.div(length: ScientificValue<MeasurementType.Length, Micrometer>) = Micrometer.width(this, length)
@JvmName("squareMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementType.Area, SquareMillimeter>.div(length: ScientificValue<MeasurementType.Length, Millimeter>) = Millimeter.width(this, length)
@JvmName("squareCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.div(length: ScientificValue<MeasurementType.Length, Centimeter>) = Centimeter.width(this, length)
@JvmName("squareDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementType.Area, SquareDecimeter>.div(length: ScientificValue<MeasurementType.Length, Decimeter>) = Decimeter.width(this, length)
@JvmName("squareDecameterDivDecameter")
operator fun ScientificValue<MeasurementType.Area, SquareDecameter>.div(length: ScientificValue<MeasurementType.Length, Decameter>) = Decameter.width(this, length)
@JvmName("squareHectometerDivHectometer")
operator fun ScientificValue<MeasurementType.Area, SquareHectometer>.div(length: ScientificValue<MeasurementType.Length, Hectometer>) = Hectometer.width(this, length)
@JvmName("squareKilometerDivKilometer")
operator fun ScientificValue<MeasurementType.Area, SquareKilometer>.div(length: ScientificValue<MeasurementType.Length, Kilometer>) = Kilometer.width(this, length)
@JvmName("squareMegameterDivMegameter")
operator fun ScientificValue<MeasurementType.Area, SquareMegameter>.div(length: ScientificValue<MeasurementType.Length, Megameter>) = Megameter.width(this, length)
@JvmName("squareGigameterDivGigameter")
operator fun ScientificValue<MeasurementType.Area, SquareGigameter>.div(length: ScientificValue<MeasurementType.Length, Gigameter>) = Gigameter.width(this, length)
@JvmName("metricAreaDivMetricLength")
operator fun <AreaUnit : MetricArea, LengthUnit : MetricLength> ScientificValue<MeasurementType.Area, AreaUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Meter.width(this, length)
@JvmName("squareInchDivInch")
operator fun ScientificValue<MeasurementType.Area, SquareInch>.div(length: ScientificValue<MeasurementType.Length, Inch>) = Inch.width(this, length)
@JvmName("squareFootDivFoot")
operator fun ScientificValue<MeasurementType.Area, SquareFoot>.div(length: ScientificValue<MeasurementType.Length, Foot>) = Foot.width(this, length)
@JvmName("squareYardDivYard")
operator fun ScientificValue<MeasurementType.Area, SquareYard>.div(length: ScientificValue<MeasurementType.Length, Yard>) = Yard.width(this, length)
@JvmName("squareMileDivMile")
operator fun ScientificValue<MeasurementType.Area, SquareMile>.div(length: ScientificValue<MeasurementType.Length, Mile>) = Mile.width(this, length)
@JvmName("imperialAreaDivImperialLength")
operator fun <AreaUnit : ImperialArea, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Area, AreaUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Foot.width(this, length)
@JvmName("areaDivLength")
operator fun <AreaUnit : Area, LengthUnit : Length> ScientificValue<MeasurementType.Area, AreaUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Meter.width(this, length)

@JvmName("cubicMeterDivSquareMeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMeter>.div(area: ScientificValue<MeasurementType.Area, SquareMeter>) = Meter.height(this, area)
@JvmName("cubicNanometerDivSquareNanometer")
operator fun ScientificValue<MeasurementType.Volume, CubicNanometer>.div(area: ScientificValue<MeasurementType.Area, SquareNanometer>) = Nanometer.height(this, area)
@JvmName("cubicMicrometerDivSquareMicrometer")
operator fun ScientificValue<MeasurementType.Volume, CubicMicrometer>.div(area: ScientificValue<MeasurementType.Area, SquareMicrometer>) = Micrometer.height(this, area)
@JvmName("cubicMillimeterDivSquareMillimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMillimeter>.div(area: ScientificValue<MeasurementType.Area, SquareMillimeter>) = Millimeter.height(this, area)
@JvmName("cubicCentimeterDivSquareCentimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Centimeter.height(this, area)
@JvmName("cubicDecimeterDivSquareDecimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecimeter>.div(area: ScientificValue<MeasurementType.Area, SquareDecimeter>) = Decimeter.height(this, area)
@JvmName("cubicDecameterDivSquareDecameter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecameter>.div(area: ScientificValue<MeasurementType.Area, SquareDecameter>) = Decameter.height(this, area)
@JvmName("cubicHectometerDivSquareHectometer")
operator fun ScientificValue<MeasurementType.Volume, CubicHectometer>.div(area: ScientificValue<MeasurementType.Area, SquareHectometer>) = Hectometer.height(this, area)
@JvmName("cubicKilometerDivSquareKilometer")
operator fun ScientificValue<MeasurementType.Volume, CubicKilometer>.div(area: ScientificValue<MeasurementType.Area, SquareKilometer>) = Kilometer.height(this, area)
@JvmName("metricVolumeDivMetricArea")
operator fun <VolumeUnit : MetricVolume, AreaUnit : MetricArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Meter.height(this, area)
@JvmName("cubicInchDivSquareInch")
operator fun ScientificValue<MeasurementType.Volume, CubicInch>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = Inch.height( this, area)
@JvmName("cubicFootDivSquareFoot")
operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = Foot.height(this, area)
@JvmName("cubicYardDivSquareYard")
operator fun ScientificValue<MeasurementType.Volume, CubicYard>.div(area: ScientificValue<MeasurementType.Area, SquareYard>) = Yard.height(this, area)
@JvmName("cubicMileDivSquareMile")
operator fun ScientificValue<MeasurementType.Volume, CubicMile>.div(area: ScientificValue<MeasurementType.Area, SquareMile>) = Mile.height(this, area)
@JvmName("acreFootDivAcre")
operator fun ScientificValue<MeasurementType.Volume, AcreFoot>.div(area: ScientificValue<MeasurementType.Area, Acre>) = Foot.height(this, area)
@JvmName("imperialVolumeDivImperialArea")
operator fun <VolumeUnit : ImperialVolume, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Foot.height(this, area)
@JvmName("ukImperialVolumeDivImperialArea")
operator fun <VolumeUnit : UKImperialVolume, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Foot.height(this, area)
@JvmName("usCustomaryVolumeDivImperialArea")
operator fun <VolumeUnit : USCustomaryVolume, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Foot.height(this, area)
@JvmName("volumeDivArea")
operator fun <VolumeUnit : Volume, AreaUnit : Area> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Meter.height(this, area)

@JvmName("ergDivDyne")
operator fun ScientificValue<MeasurementType.Energy, Erg>.div(force: ScientificValue<MeasurementType.Force, Dyne>) = Centimeter.distance(this, force)
@JvmName("ergMultipleDivDyne")
operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(force: ScientificValue<MeasurementType.Force, Dyne>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Centimeter.distance(this, force)
@JvmName("ergDivDyneMultiple")
operator fun <DyneUnit> ScientificValue<MeasurementType.Energy, Erg>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Centimeter.distance(this, force)
@JvmName("ergMultipleDivDyneMultiple")
operator fun <ErgUnit, DyneUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>, DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Centimeter.distance(this, force)
@JvmName("metricEnergyDivMetricForce")
operator fun <EnergyUnit : MetricEnergy, ForceUnit : MetricForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Meter.distance(this, force)
@JvmName("footPoundalDivPoundal")
operator fun ScientificValue<MeasurementType.Energy, FootPoundal>.div(force: ScientificValue<MeasurementType.Force, Poundal>) = Foot.distance(this, force)
@JvmName("footPoundForceDivPoundForce")
operator fun ScientificValue<MeasurementType.Energy, FootPoundForce>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = Foot.distance(this, force)
@JvmName("inchPoundForceDivPoundForce")
operator fun ScientificValue<MeasurementType.Energy, InchPoundForce>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = Inch.distance(this, force)
@JvmName("inchOunceForceDivOunceForce")
operator fun ScientificValue<MeasurementType.Energy, InchOunceForce>.div(force: ScientificValue<MeasurementType.Force, OunceForce>) = Inch.distance(this, force)
@JvmName("imperialEnergyDivImperialForce")
operator fun <EnergyUnit : ImperialEnergy, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("imperialEnergyDivUKImperialForce")
operator fun <EnergyUnit : ImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("imperialEnergyDivUSCustomaryForce")
operator fun <EnergyUnit : ImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivImperialForce")
operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivUKImperialForce")
operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivUSCustomaryForce")
operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("energyDivForce")
operator fun <EnergyUnit : Energy, ForceUnit : Force> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Meter.distance(this, force)
