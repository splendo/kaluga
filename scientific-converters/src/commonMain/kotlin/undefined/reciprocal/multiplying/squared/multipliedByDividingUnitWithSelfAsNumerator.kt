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

// Inv<Mul<A, A>> * Div<Mul<A, A>, B> -> Inv<B>

fun <
    LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSelfAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    reciprocalTargetUnit: RightDenominatorUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.reciprocalTargetUnit().byMultiplying(this, right, factory)
