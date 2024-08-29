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
import kotlinx.serialization.Serializable
import kotlin.math.PI

/**
 * Set of all [MetricLuminance]
 */
val MetricLuminanceUnits: Set<MetricLuminance> get() = setOf(
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
    Bril,
)

/**
 * Set of all [ImperialLuminance]
 */
val ImperialLuminanceUnits: Set<ImperialLuminance> get() = setOf(
    FootLambert,
)

/**
 * Set of all [Luminance]
 */
val LuminanceUnits: Set<Luminance> get() = MetricLuminanceUnits + ImperialLuminanceUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Luminance]
 * SI unit is [Nit]
 */
@Serializable
sealed class Luminance : AbstractScientificUnit<PhysicalQuantity.Luminance>()

/**
 * A [Luminance] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricLuminance :
    Luminance(),
    MetricScientificUnit<PhysicalQuantity.Luminance>

/**
 * A [Luminance] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialLuminance :
    Luminance(),
    ImperialScientificUnit<PhysicalQuantity.Luminance>

@Serializable
data object Nit : MetricLuminance(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance> {
    override val symbol: String = "nt"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class NitMultiple :
    MetricLuminance(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit>

@Serializable
data object Nanonit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Nano(Nit)

@Serializable
data object Micronit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Micro(Nit)

@Serializable
data object Millinit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Milli(Nit)

@Serializable
data object Centinit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Centi(Nit)

@Serializable
data object Decinit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Deci(Nit)

@Serializable
data object Decanit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Deca(Nit)

@Serializable
data object Hectonit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Hecto(Nit)

@Serializable
data object Kilonit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Kilo(Nit)

@Serializable
data object Meganit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Mega(Nit)

@Serializable
data object Giganit : NitMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Giga(Nit)

@Serializable
data object Stilb : MetricLuminance() {
    override val symbol: String = "sb"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = SquareCentimeter.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareCentimeter.fromSIUnit(value)
}

@Serializable
data object Apostilb : MetricLuminance() {
    private const val APOSTILB_IN_NIT = PI
    override val symbol: String = "asb"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = value * APOSTILB_IN_NIT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / APOSTILB_IN_NIT.toDecimal()
}

@Serializable
data object Lambert : MetricLuminance() {
    override val symbol: String = "L"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(Stilb.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Stilb.toSIUnit(Apostilb.toSIUnit(value))
}

@Serializable
data object Skot : MetricLuminance() {
    private const val SKOT_IN_APOSTILB = 1000.0
    override val symbol: String = "sk"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(value) * SKOT_IN_APOSTILB.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Apostilb.toSIUnit(value / SKOT_IN_APOSTILB.toDecimal())
}

@Serializable
data object Bril : MetricLuminance() {
    private const val BRIL_IN_APOSTILB = 10000000.0
    override val symbol: String = "Bril"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(value) * BRIL_IN_APOSTILB.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Apostilb.toSIUnit(value / BRIL_IN_APOSTILB.toDecimal())
}

@Serializable
data object FootLambert : ImperialLuminance() {
    override val symbol: String = "fL"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Imperial
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(SquareFoot.toSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = SquareFoot.fromSIUnit(Apostilb.toSIUnit(value))
}
