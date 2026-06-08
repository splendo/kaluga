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

// Inv<Mul<A, B>> * Div<C, Mul<A, B>> -> Div<C, Mul<Mul<A, B>, Mul<A, B>>>

fun <
    LeftReciprocalLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftQuantity>,
    LeftReciprocalRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightDenominatorRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<RightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        LeftReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                LeftReciprocalRightAndRightDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                LeftReciprocalRightAndRightDenominatorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                    LeftReciprocalRightAndRightDenominatorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                    LeftReciprocalRightAndRightDenominatorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSelfAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftQuantity,
                LeftReciprocalRightAndRightDenominatorRightQuantity,
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
