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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, C> * Div<Mul<B, B>, A> -> Div<Mul<Mul<B, B>, B>, C>

fun <
    LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftNumeratorLeftAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                    >,
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithNumeratorRightAsRootAndNumeratorLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftNumeratorLeftAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftNumeratorRightUnit: RightNumeratorUnit.(LeftNumeratorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorUnit: TargetNumeratorUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
