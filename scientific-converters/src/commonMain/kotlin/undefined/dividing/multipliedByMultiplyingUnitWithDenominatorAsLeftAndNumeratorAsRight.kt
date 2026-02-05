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

package com.splendo.kaluga.scientific.converter.undefined.dividing

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, B> * Mul<B, A> -> Mul<A, A>

fun <
    LeftNumeratorAndRightRightQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightRightQuantity>,
    LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightRightQuantity,
        LeftNumeratorUnit,
        LeftDenominatorAndRightLeftQuantity,
        LeftDenominatorUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightLeftQuantity,
        RightLeftUnit,
        LeftNumeratorAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        LeftNumeratorAndRightRightQuantity,
        LeftNumeratorUnit,
        LeftNumeratorAndRightRightQuantity,
        LeftNumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorAndRightRightQuantity,
            LeftNumeratorAndRightRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightRightQuantity,
        LeftDenominatorAndRightLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithDenominatorAsLeftAndNumeratorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightLeftQuantity,
            LeftNumeratorAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftNumeratorUnit: LeftNumeratorUnit.(LeftNumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftNumeratorUnit(
    unit.numerator,
).byMultiplying(this, right, factory)
