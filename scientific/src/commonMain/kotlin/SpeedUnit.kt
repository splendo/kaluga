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

val MetricSpeedUnits: Set<MetricSpeed> = MetricLengthUnits.map { length ->
    TimeUnits.map { MetricSpeed(length, it) }
}.flatten().toSet()

val ImperialSpeedUnits: Set<ImperialSpeed> = ImperialLengthUnits.map { length ->
    TimeUnits.map { ImperialSpeed(length, it) }
}.flatten().toSet()

val SpeedUnits: Set<Speed> = MetricSpeedUnits + ImperialSpeedUnits

@Serializable
sealed class Speed : AbstractScientificUnit<MeasurementType.Speed>() {
    abstract val distance: Length
    abstract val per: Time
    override val type = MeasurementType.Speed
    override val symbol: String by lazy { "${distance.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = distance.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = distance.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricSpeed(override val distance: MetricLength, override val per: Time) : Speed(), MetricScientificUnit<MeasurementType.Speed> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialSpeed(override val distance: ImperialLength, override val per: Time) : Speed(), ImperialScientificUnit<MeasurementType.Speed> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricLength.per(time: Time) = MetricSpeed(this, time)
infix fun ImperialLength.per(time: Time) = ImperialSpeed(this, time)

val MetricSpeedOfLight = 299792458(Meter per Second)
val ImperialSpeedOfLight = MetricSpeedOfLight.convert(Foot per Second)
