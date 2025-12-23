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

// Div<Mul<A, B>, Mul<C, D>> * Div<Mul<C, C>, D> -> Div<Mul<Mul<A, B>, C>, Mul<D, D>>

fun <
    LeftNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftQuantity>,
    LeftNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity>,
    LeftDenominatorRightAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftDenominatorRightAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorRightAndRightDenominatorQuantity,
        LeftDenominatorRightUnit,
        LeftDenominatorRightAndRightDenominatorQuantity,
        LeftDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftQuantity,
                LeftNumeratorRightQuantity,
                >,
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightDenominatorQuantity,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftQuantity,
                    LeftNumeratorRightQuantity,
                    >,
                LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightAndRightDenominatorQuantity,
                LeftDenominatorRightAndRightDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithDenominatorLeftAsRootAndDenominatorRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftDenominatorLeftUnit: LeftNumeratorUnit.(LeftDenominatorLeftUnit) -> TargetNumeratorUnit,
    leftDenominatorRightUnitXLeftDenominatorRightUnit: LeftDenominatorRightUnit.(LeftDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftDenominatorLeftUnit(
    unit.denominator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.right.leftDenominatorRightUnitXLeftDenominatorRightUnit(
        unit.denominator.right,
    ),
).byMultiplying(this, right, factory)
