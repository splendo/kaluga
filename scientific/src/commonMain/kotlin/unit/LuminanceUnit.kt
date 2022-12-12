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
    Bril
)

val ImperialLuminanceUnits: Set<ImperialLuminance> get() = setOf(
    FootLambert
)

val LuminanceUnits: Set<Luminance> get() = MetricLuminanceUnits + ImperialLuminanceUnits

@Serializable
sealed class Luminance : AbstractScientificUnit<PhysicalQuantity.Luminance>()

@Serializable
sealed class MetricLuminance : Luminance(), MetricScientificUnit<PhysicalQuantity.Luminance>
@Serializable
sealed class ImperialLuminance : Luminance(), ImperialScientificUnit<PhysicalQuantity.Luminance>

@Serializable
object Nit : MetricLuminance(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance> {
    override val symbol: String = "nt"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanonit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Nano(Nit)
@Serializable
object Micronit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Micro(Nit)
@Serializable
object Millinit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Milli(Nit)
@Serializable
object Centinit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Centi(Nit)
@Serializable
object Decinit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Deci(Nit)
@Serializable
object Decanit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Deca(Nit)
@Serializable
object Hectonit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Hecto(Nit)
@Serializable
object Kilonit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Kilo(Nit)
@Serializable
object Meganit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Mega(Nit)
@Serializable
object Giganit : MetricLuminance(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Luminance, Nit> by Giga(Nit)

@Serializable
object Stilb : MetricLuminance() {
    override val symbol: String = "sb"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = SquareCentimeter.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = SquareCentimeter.fromSIUnit(value)
}

@Serializable
object Apostilb : MetricLuminance() {
    private const val APOSTILB_IN_NIT = PI
    override val symbol: String = "asb"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = value * APOSTILB_IN_NIT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / APOSTILB_IN_NIT.toDecimal()
}

@Serializable
object Lambert : MetricLuminance() {
    override val symbol: String = "L"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(Stilb.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Stilb.toSIUnit(Apostilb.toSIUnit(value))
}

@Serializable
object Skot : MetricLuminance() {
    private const val SKOT_IN_APOSTILB = 1000.0
    override val symbol: String = "sk"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(value) * SKOT_IN_APOSTILB.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Apostilb.toSIUnit(value / SKOT_IN_APOSTILB.toDecimal())
}

@Serializable
object Bril : MetricLuminance() {
    private const val BRIL_IN_APOSTILB = 10000000.0
    override val symbol: String = "Bril"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Metric
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(value) * BRIL_IN_APOSTILB.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Apostilb.toSIUnit(value / BRIL_IN_APOSTILB.toDecimal())
}

@Serializable
object FootLambert : ImperialLuminance() {
    override val symbol: String = "fL"
    override val quantity = PhysicalQuantity.Luminance
    override val system = MeasurementSystem.Imperial
    override fun fromSIUnit(value: Decimal): Decimal = Apostilb.fromSIUnit(SquareFoot.toSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = SquareFoot.fromSIUnit(Apostilb.toSIUnit(value))
}
