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

val LuminousEnergyUnits: Set<LuminousEnergy> = LuminousFluxUnits.flatMap { flux ->
    TimeUnits.map { flux x it }
}.toSet()

@Serializable
class LuminousEnergy(val luminousFlux: LuminousFlux, val time: Time) : MetricAndImperialScientificUnit<MeasurementType.LuminousEnergy> {
    override val symbol: String = "${luminousFlux.symbol}⋅${time.symbol}"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.LuminousEnergy
    override fun fromSIUnit(value: Decimal): Decimal = luminousFlux.fromSIUnit(time.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = time.toSIUnit(luminousFlux.toSIUnit(value))
}

infix fun LuminousFlux.x(time: Time) = LuminousEnergy(this, time)
