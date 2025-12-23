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

// Div<A, Mul<B, C>> * Div<Mul<D, A>, C> -> Div<Mul<Mul<A, D>, A>, Mul<Mul<B, C>, C>>

fun <
    LeftNumeratorAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightNumeratorRightQuantity>,
    LeftDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftQuantity>,
    LeftDenominatorRightAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightNumeratorRightQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            LeftNumeratorAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        LeftDenominatorRightAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        LeftNumeratorAndRightNumeratorRightQuantity,
        LeftNumeratorUnit,
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorAndRightNumeratorRightQuantity,
            RightNumeratorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        LeftNumeratorAndRightNumeratorRightQuantity,
        LeftNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorUnit,
        LeftDenominatorRightAndRightDenominatorQuantity,
        LeftDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorAndRightNumeratorRightQuantity,
                RightNumeratorLeftQuantity,
                >,
            LeftNumeratorAndRightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftQuantity,
                LeftDenominatorRightAndRightDenominatorQuantity,
                >,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorAndRightNumeratorRightQuantity,
                    RightNumeratorLeftQuantity,
                    >,
                LeftNumeratorAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftQuantity,
                    LeftDenominatorRightAndRightDenominatorQuantity,
                    >,
                LeftDenominatorRightAndRightDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightNumeratorRightQuantity,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorAsRightAndDenominatorRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftNumeratorAndRightNumeratorRightQuantity,
                >,
            LeftDenominatorRightAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightNumeratorLeftUnit: LeftNumeratorUnit.(RightNumeratorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXLeftNumeratorUnit: TargetNumeratorLeftUnit.(LeftNumeratorUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorRightUnit: LeftDenominatorUnit.(LeftDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightNumeratorLeftUnit(
    right.unit.numerator.left,
).targetNumeratorLeftUnitXLeftNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorRightUnit(
        unit.denominator.right,
    ),
).byMultiplying(this, right, factory)
