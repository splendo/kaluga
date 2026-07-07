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
 * Set of all [Reluctance]
 */
val ReluctanceUnits: Set<Reluctance> get() = ElectricCurrentUnits.flatMap { current ->
    MagneticFluxUnits.map { current per it }
}.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Reluctance]
 * SI unit is `Ampere per Weber`
 * @property current the [ElectricCurrent] component
 * @property per the [MagneticFlux] component
 */
@Serializable
data class Reluctance(val current: ElectricCurrent, val per: MagneticFlux) :
    DefinedScientificUnit<PhysicalQuantity.Reluctance>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Reluctance> {
    override val quantity = PhysicalQuantity.Reluctance
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy { "${current.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(current.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = current.toSIUnit(per.fromSIUnit(value))
}

/**
 * Gets a [Reluctance] from an [ElectricCurrent] and a [MagneticFlux]
 * @param flux the [MagneticFlux] component
 * @return the [Reluctance] represented by the units
 */
infix fun ElectricCurrent.per(flux: MagneticFlux) = Reluctance(this, flux)
