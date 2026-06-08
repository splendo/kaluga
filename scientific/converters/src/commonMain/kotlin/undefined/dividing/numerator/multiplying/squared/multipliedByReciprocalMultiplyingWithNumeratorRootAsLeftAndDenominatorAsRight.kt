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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, A>, B> * Inv<Mul<A, B>> -> Div<A, Mul<B, B>>

fun <
    LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightReciprocalRightQuantity,
        LeftDenominatorUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftDenominatorAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            LeftDenominatorAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightReciprocalRightQuantity,
        LeftDenominatorUnit,
        LeftDenominatorAndRightReciprocalRightQuantity,
        LeftDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
        LeftNumeratorLeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightReciprocalRightQuantity,
            LeftDenominatorAndRightReciprocalRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightReciprocalRightQuantity,
                LeftDenominatorAndRightReciprocalRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            >,
        LeftDenominatorAndRightReciprocalRightQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithNumeratorRootAsLeftAndDenominatorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
                LeftDenominatorAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftDenominatorUnitXLeftDenominatorUnit: LeftDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    leftNumeratorLeftUnitPerTargetDenominatorUnit: LeftNumeratorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
