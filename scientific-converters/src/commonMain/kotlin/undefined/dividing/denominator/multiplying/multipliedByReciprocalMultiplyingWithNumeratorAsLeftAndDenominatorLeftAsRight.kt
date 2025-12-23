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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, Mul<B, C>> * Inv<Mul<A, B>> -> Inv<Mul<Mul<B, C>, B>>

fun <
    LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
    LeftDenominatorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightReciprocalRightQuantity>,
    LeftDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightReciprocalRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightReciprocalLeftQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightReciprocalRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftNumeratorAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftDenominatorLeftAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorAndRightReciprocalLeftQuantity,
            LeftDenominatorLeftAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightReciprocalRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        LeftDenominatorLeftAndRightReciprocalRightQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightReciprocalRightQuantity,
                LeftDenominatorRightQuantity,
                >,
            LeftDenominatorLeftAndRightReciprocalRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightReciprocalRightQuantity,
                    LeftDenominatorRightQuantity,
                    >,
                LeftDenominatorLeftAndRightReciprocalRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightReciprocalLeftQuantity,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightReciprocalRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithNumeratorAsLeftAndDenominatorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorAndRightReciprocalLeftQuantity,
                LeftDenominatorLeftAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftDenominatorUnitXLeftDenominatorLeftUnit: LeftDenominatorUnit.(LeftDenominatorLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.leftDenominatorUnitXLeftDenominatorLeftUnit(
    unit.denominator.left,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
