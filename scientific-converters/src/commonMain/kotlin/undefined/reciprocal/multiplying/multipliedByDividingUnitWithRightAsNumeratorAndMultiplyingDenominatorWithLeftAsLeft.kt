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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> * Div<B, Mul<A, C>> -> Inv<Mul<Mul<A, A>, C>>

fun <
    LeftReciprocalLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftQuantity>,
    LeftReciprocalRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightNumeratorQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightNumeratorQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftQuantity>,
    RightDenominatorRightQuantity : UndefinedQuantityType,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<RightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftReciprocalRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightDenominatorLeftQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                >,
            RightDenominatorRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                    LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                    >,
                RightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightNumeratorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithRightAsNumeratorAndMultiplyingDenominatorWithLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftReciprocalRightAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalLeftUnitXLeftReciprocalLeftUnit: LeftReciprocalLeftUnit.(LeftReciprocalLeftUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXRightDenominatorRightUnit: TargetReciprocalLeftUnit.(RightDenominatorRightUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.left.leftReciprocalLeftUnitXLeftReciprocalLeftUnit(
    unit.inverse.left,
).targetReciprocalLeftUnitXRightDenominatorRightUnit(
    right.unit.denominator.right,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
