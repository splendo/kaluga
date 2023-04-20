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

package com.splendo.kaluga.scientific.converter.dimensionless

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlin.jvm.JvmName

@JvmName("dimensionlessTimesDimensionlessDefault")
infix operator fun <
    LeftUnit : AbstractScientificUnit<PhysicalQuantity.Dimensionless>,
    RightUnit : AbstractScientificUnit<PhysicalQuantity.Dimensionless>,
    > ScientificValue<PhysicalQuantity.Dimensionless, LeftUnit>.times(
    modifier: ScientificValue<PhysicalQuantity.Dimensionless, RightUnit>
) = modify(modifier, ::DefaultScientificValue)

@JvmName("dimensionlessDivDimensionlessDefault")
infix operator fun <
    LeftUnit : AbstractScientificUnit<PhysicalQuantity.Dimensionless>,
    RightUnit : AbstractScientificUnit<PhysicalQuantity.Dimensionless>,
    > ScientificValue<PhysicalQuantity.Dimensionless, LeftUnit>.div(
    modifier: ScientificValue<PhysicalQuantity.Dimensionless, RightUnit>
) = unit.byDividing(this, modifier, ::DefaultScientificValue)

@JvmName("dimensionlessTimesValue")
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    Modifier : AbstractScientificUnit<PhysicalQuantity.Dimensionless>,
    > ScientificValue<PhysicalQuantity.Dimensionless, Modifier>.times(
    value: ScientificValue<Quantity, Unit>
) = value.modify(this, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Modifier : ScientificUnit<PhysicalQuantity.Dimensionless>,
    Value : ScientificValue<Quantity, Unit>
    > ScientificValue<Quantity, Unit>.modify(
    modifier: ScientificValue<PhysicalQuantity.Dimensionless, Modifier>,
    factory: (Decimal, Unit) -> Value
) = unit.byMultiplying(this, modifier, factory)
