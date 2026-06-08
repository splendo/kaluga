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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, Mul<B, B>> * Div<B, Mul<A, A>> -> Inv<Mul<B, A>>

fun <
    LeftNumeratorAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftAndRightQuantity>,
    LeftDenominatorLeftAndRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
        LeftDenominatorLeftUnit,
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
            LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
                LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithDenominatorRootAsNumeratorAndSquaredDenominatorWithNumeratorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftDenominatorLeftAndRightAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftDenominatorLeftUnitXLeftNumeratorUnit: LeftDenominatorLeftUnit.(LeftNumeratorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.left.leftDenominatorLeftUnitXLeftNumeratorUnit(
    unit.numerator,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
