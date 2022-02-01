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

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.DimensionlessScientificValue
import com.splendo.kaluga.scientific.unit.ScientificUnit

infix operator fun <
    LeftUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
    RightUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
    > DimensionlessScientificValue<LeftUnit>.times(
    modifier: DimensionlessScientificValue<RightUnit>
) = unit.byMultiplying(this, modifier, ::DimensionlessScientificValue)

infix operator fun <
    LeftUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
    RightUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
    > DimensionlessScientificValue<LeftUnit>.div(
    modifier: DimensionlessScientificValue<RightUnit>
) = unit.byDividing(this, modifier, ::DimensionlessScientificValue)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Modifier : ScientificUnit<PhysicalQuantity.Dimensionless>
    > DimensionlessScientificValue<Modifier>.times(
    unit: ScientificValue<Quantity, Unit>
) = unit.unit.byMultiplying(unit, this, ::DefaultScientificValue)
