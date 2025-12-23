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

// Inv<Mul<B, A>> * Div<Mul<B, B>, C> -> Div<B, Mul<A, C>>

fun <
    LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity>,
    LeftReciprocalRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightQuantity,
            RightDenominatorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightQuantity,
                RightDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalRightUnitXRightDenominatorUnit: LeftReciprocalRightUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
    leftReciprocalLeftUnitPerTargetDenominatorUnit: LeftReciprocalLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.left.leftReciprocalLeftUnitPerTargetDenominatorUnit(
    unit.inverse.right.leftReciprocalRightUnitXRightDenominatorUnit(
        right.unit.denominator,
    ),
).byMultiplying(this, right, factory)
