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
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.One

infix operator fun <
    Unit : Dimensionless,
    > ScientificValue<PhysicalQuantity.Dimensionless, Unit>.times(decimal: Decimal) =
    convertToOneByMultiplying(decimal, ::DefaultScientificValue)

fun <
    Unit : Dimensionless,
    Value : ScientificValue<PhysicalQuantity.Dimensionless, One>,
    > ScientificValue<PhysicalQuantity.Dimensionless, Unit>.convertToOneByMultiplying(
    decimal: Decimal,
    factory: (Decimal, One) -> Value,
) = decimal.invoke(One).let { it.unit.byMultiplying(this, it, factory) }

infix operator fun <
    Unit : Dimensionless,
    > ScientificValue<PhysicalQuantity.Dimensionless, Unit>.div(decimal: Decimal) =
    convertToOneByDividing(decimal, ::DefaultScientificValue)

fun <
    Unit : Dimensionless,
    Value : ScientificValue<PhysicalQuantity.Dimensionless, One>,
    > ScientificValue<PhysicalQuantity.Dimensionless, Unit>.convertToOneByDividing(
    decimal: Decimal,
    factory: (Decimal, One) -> Value,
) = decimal.invoke(One).let { it.unit.byDividing(this, it, factory) }
