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
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable
import kotlin.native.concurrent.ThreadLocal

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
    Abcoulomb
)

@Serializable
sealed class ElectricCharge : AbstractScientificUnit<PhysicalQuantity.ElectricCharge>(), MetricAndImperialScientificUnit<PhysicalQuantity.ElectricCharge>

@Serializable
object Coulomb : ElectricCharge(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge> {
    override val symbol = "C"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = PhysicalQuantity.ElectricCharge
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanocoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Nano(Coulomb)
@Serializable
object Microcoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Micro(Coulomb)
@Serializable
object Millicoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Milli(Coulomb)
@Serializable
object Centicoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Centi(Coulomb)
@Serializable
object Decicoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Deci(Coulomb)
@Serializable
object Decacoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Deca(Coulomb)
@Serializable
object Abcoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Deca(Coulomb) {
    override val symbol: String = "abC"
}
@Serializable
object Hectocoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Hecto(Coulomb)
@Serializable
object Kilocoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Kilo(Coulomb)
@Serializable
object Megacoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Mega(Coulomb)
@Serializable
object Gigacoulomb : ElectricCharge(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCharge, Coulomb> by Giga(Coulomb)

@ThreadLocal
val elementaryCharge = 1.602176634e-19(Coulomb)
