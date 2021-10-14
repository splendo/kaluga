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

@Serializable
sealed class Pressure : AbstractScientificUnit<MeasurementType.Pressure>()

@Serializable
sealed class MetricPressure : Pressure(), MetricScientificUnit<MeasurementType.Pressure>

@Serializable
sealed class USCustomaryPressure : Pressure(), USCustomaryScientificUnit<MeasurementType.Pressure>

@Serializable
sealed class UKImperialPressure  : Pressure(), UKImperialScientificUnit<MeasurementType.Pressure>

@Serializable
sealed class ImperialPressure  : Pressure(), CommonImperialScientificUnit<MeasurementType.Pressure>

@Serializable
object Pascal : MetricPressure(), BaseMetricUnit<MeasurementType.Pressure, MeasurementSystem.Metric> {
    override val symbol: String = "P"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}
@Serializable
object NanoPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Nano(Pascal)
@Serializable
object MicroPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Micro(Pascal)
@Serializable
object MilliPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Milli(Pascal)
@Serializable
object CentiPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Centi(Pascal)
@Serializable
object DeciPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deci(Pascal)
@Serializable
object DecaPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deca(Pascal)
@Serializable
object HectoPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Hecto(Pascal)
@Serializable
object KiloPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Kilo(Pascal)
@Serializable
object MegaPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Mega(Pascal)
@Serializable
object GigaPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Giga(Pascal)
@Serializable
object Bar : MetricPressure(), BaseMetricUnit<MeasurementType.Pressure, MeasurementSystem.Metric> {
    const val BAR_PER_PASCAL = 0.00001
    override val symbol: String = "bar"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BAR_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BAR_PER_PASCAL.toDecimal()
}
@Serializable
object MilliBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Milli(Bar)
@Serializable
object CentiBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Centi(Bar)
@Serializable
object DeciBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deci(Bar)