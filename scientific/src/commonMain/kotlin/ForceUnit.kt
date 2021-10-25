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

@Serializable
sealed class Force : AbstractScientificUnit<MeasurementType.Force>()

@Serializable
sealed class MetricForce : Force(), MetricScientificUnit<MeasurementType.Force>

@Serializable
sealed class USCustomaryForce : Force(), USCustomaryScientificUnit<MeasurementType.Force> {
    override val system = MeasurementSystem.USCustomary
    override val type = MeasurementType.Force
}

@Serializable
sealed class UKImperialForce : Force(), UKImperialScientificUnit<MeasurementType.Force> {
    override val system = MeasurementSystem.UKImperial
    override val type = MeasurementType.Force
}

@Serializable
sealed class ImperialForce : Force(), ImperialScientificUnit<MeasurementType.Force> {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Force
}

@Serializable
object Newton : MetricForce(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Force> {
    override val symbol: String = "N"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanonewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Nano(Newton)
@Serializable
object Micronewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Micro(Newton)
@Serializable
object Millinewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Milli(Newton)
@Serializable
object Kilonewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Kilo(Newton)
@Serializable
object Meganewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Mega(Newton)
@Serializable
object Giganewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Giga(Newton)

@Serializable
object Dyne : MetricForce(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Force> {
    override val symbol: String = "dyn"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = value * 100000.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 100000.toDecimal()
}
@Serializable
object Millidyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Milli(Dyne)
@Serializable
object Kilodyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Kilo(Dyne)
@Serializable
object Megadyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Mega(Dyne)
@Serializable
object KilogramForce : MetricForce() {
    override val symbol: String = "kgf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = value / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = value * MetricStandardGravityAcceleration.value
}
@Serializable
object TonneForce : MetricForce() {
    override val symbol: String = "tf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = Tonne.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = Tonne.toSIUnit(value) * MetricStandardGravityAcceleration.value
}
@Serializable
object GramForce : MetricForce() {
    override val symbol: String = "gf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = Gram.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = Gram.toSIUnit(value) * MetricStandardGravityAcceleration.value
}
@Serializable
object MilligramForce : MetricForce() {
    override val symbol: String = "gf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = Milligram.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = Milligram.toSIUnit(value) * MetricStandardGravityAcceleration.value
}

// Imperial
@Serializable
object Poundal : ImperialForce() {
    override val symbol: String = "pdl"
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * 1(Meter per Second per Second).convertValue(Foot per Second per Second)
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value) * 1(Foot per Second per Second).convertValue(Meter per Second per Second)
}

@Serializable
object PoundForce : ImperialForce() {
    override val symbol: String = "lbf"
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value) * MetricStandardGravityAcceleration.value
}

@Serializable
object OunceForce : ImperialForce() {
    override val symbol: String = "ozf"
    override fun fromSIUnit(value: Decimal): Decimal = Ounce.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = Ounce.toSIUnit(value) * MetricStandardGravityAcceleration.value
}

@Serializable
object GrainForce : ImperialForce() {
    override val symbol: String = "grf"
    override fun fromSIUnit(value: Decimal): Decimal = Grain.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = Grain.toSIUnit(value) * MetricStandardGravityAcceleration.value
}

@Serializable
object Kip : USCustomaryForce() {
    private const val POUNDS_FORCE_IN_KIP = 1000.0
    override val symbol: String ="kip"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / POUNDS_FORCE_IN_KIP.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * POUNDS_FORCE_IN_KIP.toDecimal())
}

@Serializable
data class USCustomaryImperialForceWrapper(val imperial: ImperialForce) : USCustomaryForce() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

@Serializable
object UsTonForce : USCustomaryForce() {
    override val symbol: String = "STf"
    override fun fromSIUnit(value: Decimal): Decimal = UsTon.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = UsTon.toSIUnit(value) * MetricStandardGravityAcceleration.value
}

@Serializable
data class UKImperialImperialForceWrapper(val imperial: ImperialForce) : UKImperialForce() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

@Serializable
object ImperialTonForce : UKImperialForce() {
    override val symbol: String = "LTf"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTon.fromSIUnit(value) / MetricStandardGravityAcceleration.value
    override fun toSIUnit(value: Decimal): Decimal = ImperialTon.toSIUnit(value) * MetricStandardGravityAcceleration.value
}

@JvmName("forceFromMassAndAcceleration")
fun <
    MassUnit : ScientificUnit<MeasurementType.Weight>,
    AccelerationUnit : ScientificUnit<MeasurementType.Acceleration>,
    ForceUnit : ScientificUnit<MeasurementType.Force>
    > ForceUnit.force(
    mass: ScientificValue<MeasurementType.Weight, MassUnit>,
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) : ScientificValue<MeasurementType.Force, ForceUnit> = byMultiplying(mass, acceleration)

@JvmName("forceFromMomentumAndTime")
fun <
    ForceUnit : Force,
    TimeUnit : Time,
    MomentumUnit : Momentum
    > ForceUnit.force(
    momentum: ScientificValue<MeasurementType.Momentum, MomentumUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(momentum, time)

@JvmName("forceFromPressureAndArea")
fun <
    ForceUnit : ScientificUnit<MeasurementType.Force>,
    AreaUnit : ScientificUnit<MeasurementType.Area>,
    PressureUnit : ScientificUnit<MeasurementType.Pressure>
    > ForceUnit.force(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.Force, ForceUnit> = byMultiplying(pressure, area)

@JvmName("kilogramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Kilogram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Newton.force(this, acceleration)
@JvmName("accelerationTimesKilogram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = mass * this
@JvmName("gramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Gram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("accelerationTimesGram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Gram>) = mass * this
@JvmName("gramMultipleTimesAcceleration")
operator fun <GramUnit> ScientificValue<MeasurementType.Weight, GramUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) where GramUnit : MetricWeight, GramUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> = Newton.force(this, acceleration)
@JvmName("accelerationTimesGramMultiple")
operator fun <GramUnit> ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, GramUnit>) where GramUnit : MetricWeight, GramUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> = mass * this
@JvmName("metricWeightTimesMetricAcceleration")
operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Newton.force(this, acceleration)
@JvmName("metricAccelerationTimesMetricWeight")
operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("poundTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Pound>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.force(this, acceleration)
@JvmName("accelerationTimesPound")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Pound>) = PoundForce.force(mass, this)
@JvmName("ounceTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Ounce>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = OunceForce.force(this, acceleration)
@JvmName("accelerationTimesOunce")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Ounce>) = OunceForce.force(mass, this)
@JvmName("grainTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Grain>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = GrainForce.force(this, acceleration)
@JvmName("accelerationTimesGrain")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Grain>) = GrainForce.force(mass, this)
@JvmName("usTonTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, UsTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTonForce.force(this, acceleration)
@JvmName("accelerationTimesUSTon")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, UsTon>) = UsTonForce.force(mass, this)
@JvmName("imperialTonTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, ImperialTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTonForce.force(this, acceleration)
@JvmName("accelerationTimesImperialTon")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) = ImperialTonForce.force(mass, this)
@JvmName("imperialWeightTimesImperialAcceleration")
operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.force(this, acceleration)
@JvmName("imperialAccelerationTimesImperialWeight")
operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("ukImperialWeightTimesImperialAcceleration")
operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UKImperialImperialForceWrapper(PoundForce).force(this, acceleration)
@JvmName("imperialAccelerationTimesUKImperialWeight")
operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("usCustomaryWeightTimesImperialAcceleration")
operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = USCustomaryImperialForceWrapper(PoundForce).force(this, acceleration)
@JvmName("imperialAccelerationTimesUSCustomaryWeight")
operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("weightTimesAcceleration")
operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>) = Newton.force(this, acceleration)
@JvmName("accelerationTimesWeight")
operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Acceleration, AccelerationUnit>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this

@JvmName("metricMomentumDivTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Newton.force(this, time)
@JvmName("imperialMomentumDivTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = PoundForce.force(this, time)
@JvmName("ukImperialMomentumDivTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = UKImperialImperialForceWrapper(PoundForce).force(this, time)
@JvmName("usCustomaryMomentumDivTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = USCustomaryImperialForceWrapper(PoundForce).force(this, time)
@JvmName("momentumDivTime")
operator fun <MomentumUnit : Momentum, TimeUnit : Time> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Newton.force(this, time)

@JvmName("baryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, Barye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("metricAreaTimesBarye")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, Barye>) = pressure * this
@JvmName("baryeMultipleTimesMetricArea")
operator fun <Area : MetricArea, B : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye>> ScientificValue<MeasurementType.Pressure, B>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("metricAreaTimesBaryeMultiple")
operator fun <Area : MetricArea, B : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye>> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, B>) = pressure * this
@JvmName("centibaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, CentiBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("metricPressureTimesMetricArea")
operator fun <Pressure : MetricPressure, Area : MetricArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(area: ScientificValue<MeasurementType.Area, Area>) = Newton.force(this, area)
@JvmName("metricAreaTimesMetricPressure")
operator fun <Pressure : MetricPressure, Area : MetricArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = pressure * this
@JvmName("poundSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, PoundSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("imperialAreaTimesPoundSquareInch")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) = pressure * this
@JvmName("poundSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, PoundSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("imperialAreaTimesPoundSquareFeet")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFeet>) = pressure * this
@JvmName("ounceSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, OunceSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = OunceForce.force(this, area)
@JvmName("imperialAreaTimesOunceSquareInch")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, OunceSquareInch>) = pressure * this
@JvmName("kilopoundSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("imperialAreaTimesKilopoundSquareInch")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>) = pressure * this
@JvmName("inchMercuryTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, InchOfMercury>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("imperialAreaTimesInchMercury")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, InchOfMercury>) = pressure * this
@JvmName("inchWaterTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, InchOfWater>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("imperialAreaTimesInchWater")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, InchOfWater>) = pressure * this
@JvmName("feetOfWaterTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, FootOfWater>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("imperialAreaTimesFeetOfWater")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, FootOfWater>) = pressure * this
@JvmName("kipSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = Kip.force(this, area)
@JvmName("imperialAreaTimesKipSquareInch")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, KipSquareInch>) = pressure * this
@JvmName("kipSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = Kip.force(this, area)
@JvmName("imperialAreaTimesKipSquareFeet")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, KipSquareFeet>) = pressure * this
@JvmName("usTonSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = UsTonForce.force(this, area)
@JvmName("imperialAreaTimesUsTonSquareInch")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareInch>) = pressure * this
@JvmName("usTonSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = UsTonForce.force(this, area)
@JvmName("imperialAreaTimesUsTonSquareFeet")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareFeet>) = pressure * this
@JvmName("imperialTonSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonForce.force(this, area)
@JvmName("imperialAreaTimesImperialTonSquareInch")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>) = pressure * this
@JvmName("imperialTonSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonForce.force(this, area)
@JvmName("imperialAreaTimesImperialTonSquareFeet")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareFeet>) = pressure * this
