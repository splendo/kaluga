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
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlin.jvm.JvmName

@JvmName("valueTimesDimensionless")
infix operator fun <
    Quantity : PhysicalQuantity.PhysicalQuantityWithDimension,
    Unit : AbstractScientificUnit<Quantity>,
    DimensionlessUnit : Dimensionless,
    > ScientificValue<Quantity, Unit>.times(
    value: ScientificValue<PhysicalQuantity.Dimensionless, DimensionlessUnit>,
) = timesDimensionless(value, ::DefaultScientificValue)

@JvmName("dimensionlessTimesValue")
infix operator fun <
    Quantity : PhysicalQuantity.PhysicalQuantityWithDimension,
    Unit : AbstractScientificUnit<Quantity>,
    DimensionlessUnit : Dimensionless,
    > ScientificValue<PhysicalQuantity.Dimensionless, DimensionlessUnit>.times(
    value: ScientificValue<Quantity, Unit>,
) = value.timesDimensionless(this, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity.PhysicalQuantityWithDimension,
    Unit : ScientificUnit<Quantity>,
    DimensionlessUnit : Dimensionless,
    Value : ScientificValue<Quantity, Unit>,
    > ScientificValue<Quantity, Unit>.timesDimensionless(
    modifier: ScientificValue<PhysicalQuantity.Dimensionless, DimensionlessUnit>,
    factory: (Decimal, Unit) -> Value,
) = unit.byMultiplying(this, modifier, factory)

@JvmName("valueDivDimensionless")
infix operator fun <
    Quantity : PhysicalQuantity.PhysicalQuantityWithDimension,
    Unit : AbstractScientificUnit<Quantity>,
    DimensionlessUnit : Dimensionless,
    > ScientificValue<Quantity, Unit>.div(
    value: ScientificValue<PhysicalQuantity.Dimensionless, DimensionlessUnit>,
) = divDimensionless(value, ::DefaultScientificValue)

@JvmName("dimensionlessDivValue")
infix operator fun <
    Quantity : PhysicalQuantity.PhysicalQuantityWithDimension,
    Unit : AbstractScientificUnit<Quantity>,
    DimensionlessUnit : Dimensionless,
    > ScientificValue<PhysicalQuantity.Dimensionless, DimensionlessUnit>.div(
    value: ScientificValue<Quantity, Unit>,
) = value.divDimensionless(this, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity.PhysicalQuantityWithDimension,
    Unit : ScientificUnit<Quantity>,
    DimensionlessUnit : Dimensionless,
    Value : ScientificValue<Quantity, Unit>,
    > ScientificValue<Quantity, Unit>.divDimensionless(
    modifier: ScientificValue<PhysicalQuantity.Dimensionless, DimensionlessUnit>,
    factory: (Decimal, Unit) -> Value,
) = unit.byDividing(this, modifier, factory)
