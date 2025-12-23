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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, A>, Mul<B, C>> * Div<B, Mul<A, A>> -> Inv<C>

fun <
    LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
    LeftDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftDenominatorLeftAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithDenominatorLeftAsNumeratorAndSquaredDenominatorWithNumeratorRootAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftDenominatorLeftAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    reciprocalTargetUnit: LeftDenominatorRightUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.right.reciprocalTargetUnit().byMultiplying(this, right, factory)
