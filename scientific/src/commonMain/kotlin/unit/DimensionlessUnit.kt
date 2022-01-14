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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

@Serializable
sealed class Dimensionless : ScientificUnit<PhysicalQuantity.Dimensionless>,
    MetricAndImperialScientificUnit<PhysicalQuantity.Dimensionless>

object Percent : Dimensionless(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Dimensionless> {
    override val symbol: String = "%"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Dimensionless
    override fun fromSIUnit(value: Decimal): Decimal = value * 100.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 100.0.toDecimal()
}

object Permill : Dimensionless(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Dimensionless> {
    override val symbol: String = "‰"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Dimensionless
    override fun fromSIUnit(value: Decimal): Decimal = value * 1000.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / 1000.0.toDecimal()
}

