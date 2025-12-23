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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, A>, Mul<B, B>> * Inv<Mul<A, B>> -> Div<A, Mul<Mul<B, B>, B>>

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
    LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            >,
        LeftDenominatorUnit,
        LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
        LeftNumeratorLeftUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
                LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
                >,
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
                    LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
                    >,
                LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
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
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithNumeratorRootAsLeftAndDenominatorRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightReciprocalLeftQuantity,
                LeftDenominatorLeftAndRightAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftDenominatorUnitXLeftDenominatorLeftUnit: LeftDenominatorUnit.(LeftDenominatorLeftUnit) -> TargetDenominatorUnit,
    leftNumeratorLeftUnitPerTargetDenominatorUnit: LeftNumeratorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byMultiplying(this, right, factory)
