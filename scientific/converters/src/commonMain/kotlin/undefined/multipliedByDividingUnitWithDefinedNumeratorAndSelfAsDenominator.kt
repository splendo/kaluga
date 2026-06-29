@file:Suppress("ktlint:standard:wrapping")
/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// A * Div<Wr<B>, A> -> B!

fun <
    LeftAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
    RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightNumeratorUnit : DefinedScientificUnit<RightNumeratorQuantity>,
    WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            RightNumeratorQuantity,
            >,
        WrappedRightNumeratorUnit,
        LeftAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    RightNumeratorValue : ScientificValue<RightNumeratorQuantity, RightNumeratorUnit>,
    > UndefinedScientificValue<
    LeftAndRightDenominatorQuantity,
    LeftUnit,
    >.multipliedByDividingUnitWithDefinedNumeratorAndSelfAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                RightNumeratorQuantity,
                >,
            LeftAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    factory: (Decimal, RightNumeratorUnit) -> RightNumeratorValue,
) = right.unit.numerator.wrapped.byMultiplying(this, right, factory)
