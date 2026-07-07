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
 * Set of all [EquivalentDoseRate]
 */
val EquivalentDoseRateUnits: Set<EquivalentDoseRate> get() = IonizingRadiationEquivalentDoseUnits.flatMap { equivalentDose ->
    TimeUnits.map { equivalentDose per it }
}.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.EquivalentDoseRate]
 * SI unit is `Sievert per Second`
 * @property equivalentDose the [IonizingRadiationEquivalentDose] component
 * @property per the [Time] component
 */
@Serializable
data class EquivalentDoseRate(val equivalentDose: IonizingRadiationEquivalentDose, val per: Time) :
    DefinedScientificUnit<PhysicalQuantity.EquivalentDoseRate>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.EquivalentDoseRate> {
    override val quantity = PhysicalQuantity.EquivalentDoseRate
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy { "${equivalentDose.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(equivalentDose.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = equivalentDose.toSIUnit(per.fromSIUnit(value))
}

/**
 * Gets an [EquivalentDoseRate] from an [IonizingRadiationEquivalentDose] and a [Time]
 * @param time the [Time] component
 * @return the [EquivalentDoseRate] represented by the units
 */
infix fun IonizingRadiationEquivalentDose.per(time: Time) = EquivalentDoseRate(this, time)
