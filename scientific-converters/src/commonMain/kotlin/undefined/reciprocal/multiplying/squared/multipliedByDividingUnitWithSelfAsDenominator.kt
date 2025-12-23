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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> * Div<B, Mul<A, A>> -> Div<B, Mul<Mul<A, A>, Mul<A, A>>>

fun <
    LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<RightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                    LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                    LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSelfAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXLeftReciprocalUnit: LeftReciprocalUnit.(LeftReciprocalUnit) -> TargetDenominatorUnit,
    rightNumeratorUnitPerTargetDenominatorUnit: RightNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitPerTargetDenominatorUnit(
    unit.inverse.leftReciprocalUnitXLeftReciprocalUnit(
        unit.inverse,
    ),
).byMultiplying(this, right, factory)
