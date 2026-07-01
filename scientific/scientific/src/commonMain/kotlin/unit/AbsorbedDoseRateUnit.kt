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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [AbsorbedDoseRate]
 */
val AbsorbedDoseRateUnits: Set<AbsorbedDoseRate> get() = IonizingRadiationAbsorbedDoseUnits.flatMap { absorbedDose ->
    TimeUnits.map { absorbedDose per it }
}.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.AbsorbedDoseRate]
 * SI unit is `Gray per Second`
 * @property absorbedDose the [IonizingRadiationAbsorbedDose] component
 * @property per the [Time] component
 */
@Serializable
data class AbsorbedDoseRate(val absorbedDose: IonizingRadiationAbsorbedDose, val per: Time) :
    DefinedScientificUnit<PhysicalQuantity.AbsorbedDoseRate>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.AbsorbedDoseRate> {
    override val quantity = PhysicalQuantity.AbsorbedDoseRate
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy { "${absorbedDose.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(absorbedDose.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = absorbedDose.toSIUnit(per.fromSIUnit(value))
}

/**
 * Gets an [AbsorbedDoseRate] from an [IonizingRadiationAbsorbedDose] and a [Time]
 * @param time the [Time] component
 * @return the [AbsorbedDoseRate] represented by the units
 */
infix fun IonizingRadiationAbsorbedDose.per(time: Time) = AbsorbedDoseRate(this, time)
