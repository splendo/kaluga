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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<B, A>, Mul<D, C>> * Div<B, Mul<C, C>> -> Div<Mul<Mul<B, A>, B>, Mul<Mul<D, C>, Mul<C, C>>>

fun <
    LeftNumeratorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorQuantity>,
    LeftNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftQuantity>,
    LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftNumeratorLeftAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftNumeratorLeftAndRightNumeratorQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorQuantity,
                LeftNumeratorRightQuantity,
                >,
            LeftNumeratorLeftAndRightNumeratorQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftQuantity,
                LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightNumeratorQuantity,
                    LeftNumeratorRightQuantity,
                    >,
                LeftNumeratorLeftAndRightNumeratorQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftQuantity,
                    LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                    LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorQuantity,
            LeftNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithNumeratorLeftAsNumeratorAndSquaredDenominatorWithDenominatorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorLeftAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftNumeratorLeftUnit: LeftNumeratorUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXRightDenominatorUnit: LeftDenominatorUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXRightDenominatorUnit(
        right.unit.denominator,
    ),
).byMultiplying(this, right, factory)
