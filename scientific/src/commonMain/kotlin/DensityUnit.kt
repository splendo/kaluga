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
sealed class Density : AbstractScientificUnit<MeasurementType.Density>() {
    abstract val weight: Weight
    abstract val per: Volume
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.Density
    override fun fromSIUnit(value: Decimal): Decimal = weight.fromSIUnit(value) * per.convert(1.0.toDecimal(), CubicMeter)
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(value) / per.convert(1.0.toDecimal(), CubicMeter)
}

@Serializable
data class MetricDensity(override val weight: MetricWeight, override val per: MetricVolume) : Density(), MetricScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialDensity(override val weight: ImperialWeight, override val per: ImperialVolume) : Density(), ImperialScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.Imperial
}
@Serializable
data class USCustomaryDensity(override val weight: USCustomaryWeight, override val per: USCustomaryVolume) : Density(), USCustomaryScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialDensity(override val weight: UKImperialWeight, override val per: UKImperialVolume) : Density(), UKImperialScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(volume: MetricVolume) = MetricDensity(this, volume)
infix fun ImperialWeight.per(volume: ImperialVolume) = ImperialDensity(this, volume)
infix fun USCustomaryWeight.per(volume: USCustomaryVolume) = USCustomaryDensity(this, volume)
infix fun UKImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this, volume)
