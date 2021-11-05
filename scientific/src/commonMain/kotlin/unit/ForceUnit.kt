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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.convertValue
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

val MetricForceUnits: Set<MetricForce> get() = setOf (
    Newton,
    Nanonewton,
    Micronewton,
    Millinewton,
    Centinewton,
    Decinewton,
    Decanewton,
    Hectonewton,
    Kilonewton,
    Meganewton,
    Giganewton,
    Dyne,
    Nanodyne,
    Microdyne,
    Millidyne,
    Centidyne,
    Decidyne,
    Decadyne,
    Hectodyne,
    Kilodyne,
    Megadyne,
    Gigadyne,
    KilogramForce,
    TonneForce,
    GramForce,
    MilligramForce
    )

val ImperialForceUnits: Set<ImperialForce> get() = setOf(
    Poundal,
    PoundForce,
    OunceForce,
    GrainForce,
)

val USCustomaryForceUnits: Set<USCustomaryForce> get() = setOf(
    Kip,
    UsTonForce
) + ImperialForceUnits.map { it.usCustomary }

val UKImperialForceUnits: Set<UKImperialForce> get() = setOf(
    ImperialTonForce
) + ImperialForceUnits.map { it.ukImperial }

val ForceUnits: Set<Force> get() = MetricForceUnits + ImperialForceUnits + UKImperialForceUnits.filter { it !is UKImperialImperialForceWrapper }.toSet() + USCustomaryForceUnits.filter { it !is USCustomaryImperialForceWrapper }

@Serializable
sealed class Force : AbstractScientificUnit<MeasurementType.Force>()

@Serializable
sealed class MetricForce : Force(), MetricScientificUnit<MeasurementType.Force>

@Serializable
sealed class ImperialForce : Force(), ImperialScientificUnit<MeasurementType.Force> {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Force
}

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
object Centinewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Centi(Newton)
@Serializable
object Decinewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Deci(Newton)
@Serializable
object Decanewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Deca(Newton)
@Serializable
object Hectonewton : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> by Hecto(Newton)
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
object Nanodyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Nano(Dyne)
@Serializable
object Microdyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Micro(Dyne)
@Serializable
object Millidyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Milli(Dyne)
@Serializable
object Centidyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Centi(Dyne)
@Serializable
object Decidyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Deci(Dyne)
@Serializable
object Decadyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Deca(Dyne)
@Serializable
object Hectodyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Hecto(Dyne)
@Serializable
object Kilodyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Kilo(Dyne)
@Serializable
object Megadyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Mega(Dyne)
@Serializable
object Gigadyne : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> by Giga(Dyne)

@Serializable
object KilogramForce : MetricForce() {
    override val symbol: String = "kgf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = value / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = value * MetricStandardGravityAcceleration.decimalValue
}
@Serializable
object TonneForce : MetricForce() {
    override val symbol: String = "tf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = Tonne.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Tonne.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}
@Serializable
object GramForce : MetricForce() {
    override val symbol: String = "gf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = Gram.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Gram.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}
@Serializable
object MilligramForce : MetricForce() {
    override val symbol: String = "gf"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Force
    override fun fromSIUnit(value: Decimal): Decimal = Milligram.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Milligram.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
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
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

@Serializable
object OunceForce : ImperialForce() {
    override val symbol: String = "ozf"
    override fun fromSIUnit(value: Decimal): Decimal = Ounce.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Ounce.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

@Serializable
object GrainForce : ImperialForce() {
    override val symbol: String = "grf"
    override fun fromSIUnit(value: Decimal): Decimal = Grain.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Grain.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

@Serializable
object Kip : USCustomaryForce() {
    private const val POUNDS_FORCE_IN_KIP = 1000.0
    override val symbol: String ="kip"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / POUNDS_FORCE_IN_KIP.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * POUNDS_FORCE_IN_KIP.toDecimal())
}

@Serializable
object UsTonForce : USCustomaryForce() {
    override val symbol: String = "STf"
    override fun fromSIUnit(value: Decimal): Decimal = UsTon.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = UsTon.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

@Serializable
data class USCustomaryImperialForceWrapper(val imperial: ImperialForce) : USCustomaryForce() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

val <ForceUnit : ImperialForce> ForceUnit.usCustomary get() = USCustomaryImperialForceWrapper(this)

@Serializable
object ImperialTonForce : UKImperialForce() {
    override val symbol: String = "LTf"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTon.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = ImperialTon.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

@Serializable
data class UKImperialImperialForceWrapper(val imperial: ImperialForce) : UKImperialForce() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

val <ForceUnit : ImperialForce> ForceUnit.ukImperial get() = UKImperialImperialForceWrapper(this)
