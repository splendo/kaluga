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
object Newton : MetricForce(), BaseMetricUnit<MeasurementType.Force, MeasurementSystem.Metric> {
    override val symbol: String = "N"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanonewton : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Nano(Newton)
@Serializable
object Micronewton : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Micro(Newton)
@Serializable
object Millinewton : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Milli(Newton)
@Serializable
object Kilonewton : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Kilo(Newton)
@Serializable
object Meganewton : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Mega(Newton)
@Serializable
object Giganewton : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Giga(Newton)

@Serializable
object Dyne : MetricForce(), BaseMetricUnit<MeasurementType.Force, MeasurementSystem.Metric> {
    override val symbol: String = "dyn"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = value * 100000.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 100000.toDecimal()
}
@Serializable
object Millidyne : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Milli(Dyne)
@Serializable
object Kilodyne : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Kilo(Dyne)
@Serializable
object Megadyne : MetricForce(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Force> by Mega(Dyne)
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
    const val POUNDS_FORCE_IN_KIP = 1000.0
    override val symbol: String ="kip"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / POUNDS_FORCE_IN_KIP.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * POUNDS_FORCE_IN_KIP.toDecimal())
}

@Serializable
data class USCustomaryImperialForceWrapper(val imperial: ImperialVolume) : USCustomaryForce() {
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

fun <
    MassUnit : ScientificUnit<MeasurementType.Weight>,
    AccelerationUnit : ScientificUnit<MeasurementType.Acceleration>,
    ForceUnit : ScientificUnit<MeasurementType.Force>
    > ForceUnit.force(
    mass: ScientificValue<MeasurementType.Weight, MassUnit>,
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) : ScientificValue<MeasurementType.Force, ForceUnit> = byMultiplying(mass, acceleration)

fun <
    MassUnit : ScientificUnit<MeasurementType.Weight>,
    AccelerationUnit : ScientificUnit<MeasurementType.Acceleration>,
    ForceUnit : ScientificUnit<MeasurementType.Force>
    > MassUnit.mass(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) : ScientificValue<MeasurementType.Weight, MassUnit> = byDividing(force, acceleration)

fun <
    MassUnit : ScientificUnit<MeasurementType.Weight>,
    AccelerationUnit : ScientificUnit<MeasurementType.Acceleration>,
    ForceUnit : ScientificUnit<MeasurementType.Force>
    > AccelerationUnit.acceleration(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    mass: ScientificValue<MeasurementType.Weight, MassUnit>
) : ScientificValue<MeasurementType.Acceleration, AccelerationUnit> = byDividing(force, mass)

@JvmName("kilogramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Kilogram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Newton.force(this, acceleration)
@JvmName("hectogramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Hectogram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Newton.force(this, acceleration)
@JvmName("decagramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Decagram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Newton.force(this, acceleration)
@JvmName("gramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Gram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("decigramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Decigram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("centigramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Centigram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("milligramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Milligram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("microgramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Microgram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("nanogramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Nanogram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("poundTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Pound>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.force(this, acceleration)
@JvmName("ounceTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Ounce>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = OunceForce.force(this, acceleration)
@JvmName("grainTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Grain>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = GrainForce.force(this, acceleration)
@JvmName("usTonTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, UsTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTonForce.force(this, acceleration)
@JvmName("imperialTonTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, ImperialTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTonForce.force(this, acceleration)

@JvmName("accelerationTimesKilogram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = Newton.force(mass, this)
@JvmName("accelerationTimesHectogram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Hectogram>) = Newton.force(mass, this)
@JvmName("accelerationTimesDecagram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Decagram>) = Newton.force(mass, this)
@JvmName("accelerationTimesGram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Gram>) = Dyne.force(mass, this)
@JvmName("accelerationTimesDecigram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Decigram>) = Dyne.force(mass, this)
@JvmName("accelerationTimesCentigram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Centigram>) = Dyne.force(mass, this)
@JvmName("accelerationTimesMilligram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Milligram>) = Dyne.force(mass, this)
@JvmName("accelerationTimesMicrogram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Microgram>) = Dyne.force(mass, this)
@JvmName("accelerationTimesNanogram")
operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Nanogram>) = Dyne.force(mass, this)
@JvmName("accelerationTimesPound")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Pound>) = PoundForce.force(mass, this)
@JvmName("accelerationTimesOunce")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Ounce>) = OunceForce.force(mass, this)
@JvmName("accelerationTimesGrain")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Grain>) = GrainForce.force(mass, this)
@JvmName("accelerationTimesUSTon")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, UsTon>) = UsTonForce.force(mass, this)
@JvmName("accelerationTimesImperialTon")
operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) = ImperialTonForce.force(mass, this)

@JvmName("newtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Newton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("nanonewtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Nanonewton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("micronewtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Micronewton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("millinewtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Millinewton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("kilonewtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Kilonewton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("meganewtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Meganewton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("giganewtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Giganewton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("dyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("megadyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Megadyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("kilodyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Kilodyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("millidyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Millidyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("poundalDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("poundForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("ounceForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Ounce.mass(this, acceleration)
@JvmName("grainForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Grain.mass(this, acceleration)
@JvmName("kipDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("usTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTon.mass(this, acceleration)
@JvmName("imperialTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTon.mass(this, acceleration)

@JvmName("newtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Newton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("nanonewtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Nanonewton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("micronewtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Micronewton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("millinewtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Millinewton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("kilonewtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Kilonewton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("meganewtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Meganewton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("giganewtonDivKilogram")
operator fun ScientificValue<MeasurementType.Force, Giganewton>.div(mass: ScientificValue<MeasurementType.Weight, Kilogram>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("dyneDivGram")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("megadyneDivGram")
operator fun ScientificValue<MeasurementType.Force, Megadyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("kilodyneDivGram")
operator fun ScientificValue<MeasurementType.Force, Kilodyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("millidyneDivGram")
operator fun ScientificValue<MeasurementType.Force, Millidyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("poundalDivPound")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("poundForceDivPound")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("ounceForceDivOunce")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(mass: ScientificValue<MeasurementType.Weight, Ounce>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("grainForceDivGrain")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(mass: ScientificValue<MeasurementType.Weight, Grain>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("kipDivPound")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("usTonForceDivUsTon")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(mass: ScientificValue<MeasurementType.Weight, UsTon>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("imperialTonForceDivImperialTon")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) = (Foot per Second per Second).acceleration(this, mass)
