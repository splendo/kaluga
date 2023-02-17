/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.convertValue
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricForce]
 */
val MetricForceUnits: Set<MetricForce> get() = setOf(
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

/**
 * Set of all [ImperialForce]
 */
val ImperialForceUnits: Set<ImperialForce> get() = setOf(
    Poundal,
    PoundForce,
    OunceForce,
    GrainForce,
)

/**
 * Set of all [USCustomaryForce]
 */
val USCustomaryForceUnits: Set<USCustomaryForce> get() = setOf(
    Kip,
    UsTonForce
) + ImperialForceUnits.map { it.usCustomary }

/**
 * Set of all [UKImperialForce]
 */
val UKImperialForceUnits: Set<UKImperialForce> get() = setOf(
    ImperialTonForce
) + ImperialForceUnits.map { it.ukImperial }

/**
 * Set of all [Force]
 */
val ForceUnits: Set<Force> get() = MetricForceUnits + ImperialForceUnits + UKImperialForceUnits.filter { it !is UKImperialImperialForceWrapper }.toSet() + USCustomaryForceUnits.filter { it !is USCustomaryImperialForceWrapper }

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Force]
 * SI unit is [Newton]
 */
@Serializable
sealed class Force : AbstractScientificUnit<PhysicalQuantity.Force>()

/**
 * A [Force] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricForce : Force(), MetricScientificUnit<PhysicalQuantity.Force>

/**
 * A [Force] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialForce : Force(), ImperialScientificUnit<PhysicalQuantity.Force> {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Force
}

/**
 * A [Force] for [MeasurementSystem.USCustomary]
 */
@Serializable
sealed class USCustomaryForce : Force(), USCustomaryScientificUnit<PhysicalQuantity.Force> {
    override val system = MeasurementSystem.USCustomary
    override val quantity = PhysicalQuantity.Force
}

/**
 * A [Force] for [MeasurementSystem.UKImperial]
 */
@Serializable
sealed class UKImperialForce : Force(), UKImperialScientificUnit<PhysicalQuantity.Force> {
    override val system = MeasurementSystem.UKImperial
    override val quantity = PhysicalQuantity.Force
}

@Serializable
object Newton : MetricForce(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Force> {
    override val symbol: String = "N"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Force
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class NewtonMultiple : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton>

@Serializable
object Nanonewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Nano(Newton)
@Serializable
object Micronewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Micro(Newton)
@Serializable
object Millinewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Milli(Newton)
@Serializable
object Centinewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Centi(Newton)
@Serializable
object Decinewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Deci(Newton)
@Serializable
object Decanewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Deca(Newton)
@Serializable
object Hectonewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Hecto(Newton)
@Serializable
object Kilonewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Kilo(Newton)
@Serializable
object Meganewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Mega(Newton)
@Serializable
object Giganewton : NewtonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Newton> by Giga(Newton)

@Serializable
object Dyne : MetricForce(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Force> {
    override val symbol: String = "dyn"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Force
    override fun fromSIUnit(value: Decimal): Decimal = value * 100000.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 100000.toDecimal()
}

@Serializable
sealed class DyneMultiple : MetricForce(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne>

@Serializable
object Nanodyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Nano(Dyne)
@Serializable
object Microdyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Micro(Dyne)
@Serializable
object Millidyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Milli(Dyne)
@Serializable
object Centidyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Centi(Dyne)
@Serializable
object Decidyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Deci(Dyne)
@Serializable
object Decadyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Deca(Dyne)
@Serializable
object Hectodyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Hecto(Dyne)
@Serializable
object Kilodyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Kilo(Dyne)
@Serializable
object Megadyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Mega(Dyne)
@Serializable
object Gigadyne : DyneMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> by Giga(Dyne)

@Serializable
object KilogramForce : MetricForce() {
    override val symbol: String = "kgf"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Force
    override fun fromSIUnit(value: Decimal): Decimal = value / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = value * MetricStandardGravityAcceleration.decimalValue
}
@Serializable
object TonneForce : MetricForce() {
    override val symbol: String = "tf"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Force
    override fun fromSIUnit(value: Decimal): Decimal = Tonne.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Tonne.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}
@Serializable
object GramForce : MetricForce() {
    override val symbol: String = "gf"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Force
    override fun fromSIUnit(value: Decimal): Decimal = Gram.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = Gram.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}
@Serializable
object MilligramForce : MetricForce() {
    override val symbol: String = "gf"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Force
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
    override val symbol: String = "kip"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / POUNDS_FORCE_IN_KIP.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * POUNDS_FORCE_IN_KIP.toDecimal())
}

@Serializable
object UsTonForce : USCustomaryForce() {
    override val symbol: String = "STf"
    override fun fromSIUnit(value: Decimal): Decimal = UsTon.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = UsTon.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

/**
 * Wraps an [ImperialForce] unit to an [USCustomaryForce] unit
 * @param imperial the [ImperialForce] to wrap
 */
@Serializable
data class USCustomaryImperialForceWrapper(val imperial: ImperialForce) : USCustomaryForce() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

/**
 * Converts an [ImperialForce] unit to an [USCustomaryImperialForceWrapper] unit
 * @param ForceUnit the type of [ImperialForce] to convert
 */
val <ForceUnit : ImperialForce> ForceUnit.usCustomary get() = USCustomaryImperialForceWrapper(this)

@Serializable
object ImperialTonForce : UKImperialForce() {
    override val symbol: String = "LTf"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTon.fromSIUnit(value) / MetricStandardGravityAcceleration.decimalValue
    override fun toSIUnit(value: Decimal): Decimal = ImperialTon.toSIUnit(value) * MetricStandardGravityAcceleration.decimalValue
}

/**
 * Wraps an [ImperialForce] unit to an [UKImperialForce] unit
 * @param imperial the [ImperialForce] to wrap
 */
@Serializable
data class UKImperialImperialForceWrapper(val imperial: ImperialForce) : UKImperialForce() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

/**
 * Converts an [ImperialForce] unit to an [UKImperialImperialForceWrapper] unit
 * @param ForceUnit the type of [ImperialForce] to convert
 */
val <ForceUnit : ImperialForce> ForceUnit.ukImperial get() = UKImperialImperialForceWrapper(this)
