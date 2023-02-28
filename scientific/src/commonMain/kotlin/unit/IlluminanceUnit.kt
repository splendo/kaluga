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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricIlluminance]
 */
val MetricIlluminanceUnits: Set<MetricIlluminance> get() = setOf(
    Lux,
    Nanolux,
    Microlux,
    Millilux,
    Centilux,
    Decilux,
    Decalux,
    Hectolux,
    Kilolux,
    Megalux,
    Gigalux,
    Phot,
    Nanophot,
    Microphot,
    Milliphot,
    Centiphot,
    Deciphot,
    Decaphot,
    Hectophot,
    Kilophot,
    Megaphot,
    Gigaphot
)

/**
 * Set of all [ImperialIlluminance]
 */
val ImperialIlluminanceUnits: Set<ImperialIlluminance> get() = setOf(
    FootCandle
)

/**
 * Set of all [Illuminance]
 */
val IlluminanceUnits: Set<Illuminance> get() = MetricIlluminanceUnits + ImperialIlluminanceUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Illuminance]
 * SI unit is [Lux]
 */
@Serializable
sealed class Illuminance : AbstractScientificUnit<PhysicalQuantity.Illuminance>()

/**
 * An [Illuminance] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricIlluminance : Illuminance(), MetricScientificUnit<PhysicalQuantity.Illuminance>

/**
 * An [Illuminance] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialIlluminance : Illuminance(), ImperialScientificUnit<PhysicalQuantity.Illuminance>

@Serializable
object Lux : MetricIlluminance(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance> {
    override val symbol = "lx"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Illuminance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class LuxMultiple : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux>

@Serializable
object Nanolux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Nano(Lux)
@Serializable
object Microlux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Micro(Lux)
@Serializable
object Millilux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Milli(Lux)
@Serializable
object Centilux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Centi(Lux)
@Serializable
object Decilux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Deci(Lux)
@Serializable
object Decalux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Deca(Lux)
@Serializable
object Hectolux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Hecto(Lux)
@Serializable
object Kilolux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Kilo(Lux)
@Serializable
object Megalux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Mega(Lux)
@Serializable
object Gigalux : LuxMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Lux> by Giga(Lux)

@Serializable
object Phot : MetricIlluminance(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance> {
    override val symbol = "ph"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Illuminance
    override fun fromSIUnit(value: Decimal): Decimal = SquareCentimeter.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareCentimeter.fromSIUnit(value)
}

@Serializable
sealed class PhotMultiple : MetricIlluminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot>

@Serializable
object Nanophot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Nano(Phot)
@Serializable
object Microphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Micro(Phot)
@Serializable
object Milliphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Milli(Phot)
@Serializable
object Centiphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Centi(Phot)
@Serializable
object Deciphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Deci(Phot)
@Serializable
object Decaphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Deca(Phot)
@Serializable
object Hectophot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Hecto(Phot)
@Serializable
object Kilophot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Kilo(Phot)
@Serializable
object Megaphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Mega(Phot)
@Serializable
object Gigaphot : PhotMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Illuminance, Phot> by Giga(Phot)

@Serializable
object FootCandle : ImperialIlluminance() {
    override val symbol: String = "fc"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Illuminance
    override fun fromSIUnit(value: Decimal): Decimal = SquareFoot.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareFoot.fromSIUnit(value)
}
