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
import kotlinx.serialization.Serializable

@Serializable
sealed class Momentum : AbstractScientificUnit<MeasurementType.Momentum>() {
    abstract val mass: Weight
    abstract val speed: Speed
    override val type = MeasurementType.Momentum
    override val symbol: String by lazy { "${mass.symbol} * ${speed.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = speed.fromSIUnit(mass.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = mass.toSIUnit(speed.toSIUnit(value))
}

@Serializable
data class MetricMomentum(override val mass: MetricWeight, override val speed: MetricSpeed) : Momentum(), MetricScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialMomentum(override val mass: ImperialWeight, override val speed: ImperialSpeed) : Momentum(), ImperialScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.Imperial
}

@Serializable
data class UKImperialMomentum(override val mass: UKImperialWeight, override val speed: ImperialSpeed) : Momentum(), UKImperialScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.UKImperial
}

@Serializable
data class USCustomaryMomentum(override val mass: USCustomaryWeight, override val speed: ImperialSpeed) : Momentum(), USCustomaryScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricWeight.x(speed: MetricSpeed) = MetricMomentum(this, speed)
infix fun ImperialWeight.x(speed: ImperialSpeed) = ImperialMomentum(this, speed)
infix fun UKImperialWeight.x(speed: ImperialSpeed) = UKImperialMomentum(this, speed)
infix fun USCustomaryWeight.x(speed: ImperialSpeed) = USCustomaryMomentum(this, speed)
