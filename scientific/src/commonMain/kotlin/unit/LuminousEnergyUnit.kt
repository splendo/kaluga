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
 * Set of all [LuminousEnergy]
 */
val LuminousEnergyUnits: Set<LuminousEnergy> get() = LuminousFluxUnits.flatMap { flux ->
    TimeUnits.map { flux x it }
}.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.LuminousEnergy]
 * SI unit is `Lumen x Second`
 * @property luminousFlux the [LuminousFlux] component
 * @property time the [Time] component
 */
@Serializable
data class LuminousEnergy(val luminousFlux: LuminousFlux, val time: Time) :
    AbstractScientificUnit<PhysicalQuantity.LuminousEnergy>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.LuminousEnergy> {
        override val symbol: String = "${luminousFlux.symbol}⋅${time.symbol}"
        override val system = MeasurementSystem.MetricAndImperial
        override val quantity = PhysicalQuantity.LuminousEnergy
        override fun fromSIUnit(value: Decimal): Decimal = luminousFlux.fromSIUnit(time.fromSIUnit(value))
        override fun toSIUnit(value: Decimal): Decimal = time.toSIUnit(luminousFlux.toSIUnit(value))
    }

/**
 * Gets a [LuminousEnergy] from a [LuminousFlux] and a [Time]
 * @param time the [Time] component
 * @return the [LuminousEnergy] represented by the units
 */
infix fun LuminousFlux.x(time: Time) = LuminousEnergy(this, time)
