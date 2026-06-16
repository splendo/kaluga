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

// Div<Mul<A, A>, Mul<B, B>> * Inv<A> -> Div<A, Mul<B, B>>

fun <
    LeftNumeratorLeftAndRightAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
            LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
        LeftNumeratorLeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightQuantity,
                LeftDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
            LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalNumeratorRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftNumeratorLeftAndRightAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitPerLeftDenominatorUnit: LeftNumeratorLeftUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
