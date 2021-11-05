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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val MetricDynamicViscosityUnits: Set<MetricDynamicViscosity> get() = MetricPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val ImperialDynamicViscosityUnits: Set<ImperialDynamicViscosity> get() = ImperialPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val UKImperialDynamicViscosityUnits: Set<UKImperialDynamicViscosity> get() = UKImperialPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val USCustomaryDynamicViscosityUnits: Set<USCustomaryDynamicViscosity> get() = USCustomaryPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val DynamicViscosityUnits: Set<DynamicViscosity> get() = MetricDynamicViscosityUnits +
    ImperialDynamicViscosityUnits +
    UKImperialDynamicViscosityUnits.filter { it.pressure !is UKImperialPressureWrapper }.toSet() +
    USCustomaryDynamicViscosityUnits.filter { it.pressure !is USCustomaryImperialPressureWrapper }.toSet()

@Serializable
sealed class DynamicViscosity : AbstractScientificUnit<PhysicalQuantity.DynamicViscosity>() {
    abstract val pressure: Pressure
    abstract val time: Time
    override val type = PhysicalQuantity.DynamicViscosity
    override val symbol: String by lazy { "${pressure.symbol}⋅${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.fromSIUnit(pressure.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = pressure.toSIUnit(time.toSIUnit(value))
}

@Serializable
data class MetricDynamicViscosity(override val pressure: MetricPressure, override val time: Time) : DynamicViscosity(), MetricScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialDynamicViscosity(override val pressure: ImperialPressure, override val time: Time) : DynamicViscosity(), ImperialScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = pressure.ukImperial x time
    val usCustomary get() = pressure.usCustomary x time
}
@Serializable
data class UKImperialDynamicViscosity(override val pressure: UKImperialPressure, override val time: Time) : DynamicViscosity(), UKImperialScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryDynamicViscosity(override val pressure: USCustomaryPressure, override val time: Time) : DynamicViscosity(), USCustomaryScientificUnit<PhysicalQuantity.DynamicViscosity> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricPressure.x(time: Time) = MetricDynamicViscosity(this, time)
infix fun ImperialPressure.x(time: Time) = ImperialDynamicViscosity(this, time)
infix fun UKImperialPressure.x(time: Time) = UKImperialDynamicViscosity(this, time)
infix fun USCustomaryPressure.x(time: Time) = USCustomaryDynamicViscosity(this, time)
