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

val MetricKinematicViscosityUnits: Set<MetricKinematicViscosity> get() = MetricAreaUnits.flatMap { area ->
    TimeUnits.map { area per it }
}.toSet()

val ImperialKinematicViscosityUnits: Set<ImperialKinematicViscosity> get() = ImperialAreaUnits.flatMap { area ->
    TimeUnits.map { area per it }
}.toSet()

val KinematicViscosityUnits: Set<KinematicViscosity> get() = MetricKinematicViscosityUnits +
    ImperialKinematicViscosityUnits

@Serializable
sealed class KinematicViscosity : AbstractScientificUnit<PhysicalQuantity.KinematicViscosity>() {
    abstract val area: Area
    abstract val time: Time
    override val quantity = PhysicalQuantity.KinematicViscosity
    override val symbol: String by lazy { "${area.symbol}/${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.fromSIUnit(area.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = area.toSIUnit(time.toSIUnit(value))
}

@Serializable
data class MetricKinematicViscosity(override val area: MetricArea, override val time: Time) : KinematicViscosity(), MetricScientificUnit<PhysicalQuantity.KinematicViscosity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialKinematicViscosity(override val area: ImperialArea, override val time: Time) : KinematicViscosity(), ImperialScientificUnit<PhysicalQuantity.KinematicViscosity> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricArea.per(time: Time) = MetricKinematicViscosity(this, time)
infix fun ImperialArea.per(time: Time) = ImperialKinematicViscosity(this, time)
infix fun Area.per(time: Time): KinematicViscosity = when (this) {
    is MetricArea -> this per time
    is ImperialArea -> this per time
}
