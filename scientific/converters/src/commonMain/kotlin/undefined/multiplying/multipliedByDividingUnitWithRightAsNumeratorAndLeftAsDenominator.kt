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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, B> * Div<B, A> -> Mul<B, B>

fun <
    LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
    LeftRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightDenominatorQuantity,
        LeftLeftUnit,
        LeftRightAndRightNumeratorQuantity,
        LeftRightUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorQuantity>,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        LeftRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        LeftLeftAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        LeftRightAndRightNumeratorQuantity,
        LeftRightUnit,
        LeftRightAndRightNumeratorQuantity,
        LeftRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightNumeratorQuantity,
            LeftRightAndRightNumeratorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightDenominatorQuantity,
        LeftRightAndRightNumeratorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithRightAsNumeratorAndLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftRightAndRightNumeratorQuantity,
            LeftLeftAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftRightUnitXLeftRightUnit: LeftRightUnit.(LeftRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.leftRightUnitXLeftRightUnit(
    unit.right,
).byMultiplying(this, right, factory)
