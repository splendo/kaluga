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
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

/**
 * Set of all [ElectricCharge]
 */
val ElectricChargeUnits: Set<ElectricCharge> get() = setOf(
    Coulomb,
    Nanocoulomb,
    Microcoulomb,
    Millicoulomb,
    Centicoulomb,
    Decicoulomb,
    Decacoulomb,
    Hectocoulomb,
    Kilocoulomb,
    Megacoulomb,
    Gigacoulomb,
    Abcoulomb,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ElectricCharge]
 * SI unit is [Coulomb]
 */
@Serializable
sealed class ElectricCharge :
    AbstractScientificUnit<PhysicalQuantity.ElectricCharge>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.ElectricCharge>

@Serializable
data object Coulomb : ElectricCharge(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge> {
    override val symbol = "C"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricCharge
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class CoulombMultiple :
    ElectricCharge(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb>

@Serializable
data object Nanocoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Nano(Coulomb)

@Serializable
data object Microcoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Micro(Coulomb)

@Serializable
data object Millicoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Milli(Coulomb)

@Serializable
data object Centicoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Centi(Coulomb)

@Serializable
data object Decicoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Deci(Coulomb)

@Serializable
data object Decacoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Deca(Coulomb)

@Serializable
data object Abcoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Deca(Coulomb) {
    override val symbol: String = "abC"
}

@Serializable
data object Hectocoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Hecto(Coulomb)

@Serializable
data object Kilocoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Kilo(Coulomb)

@Serializable
data object Megacoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Mega(Coulomb)

@Serializable
data object Gigacoulomb : CoulombMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Giga(Coulomb)

/**
 * The [ElectricCharge] carried by a single proton
 */
val elementaryCharge = 1.602176634e-19(Coulomb)
