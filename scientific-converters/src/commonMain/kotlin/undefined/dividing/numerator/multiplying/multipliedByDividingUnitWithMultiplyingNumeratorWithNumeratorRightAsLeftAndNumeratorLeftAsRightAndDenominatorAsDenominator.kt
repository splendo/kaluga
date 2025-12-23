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

// Div<Mul<A, B>, C> * Div<Mul<B, A>, C> -> Div<Mul<Mul<A, B>, Mul<B, A>>, Mul<C, C>>

fun <
    LeftNumeratorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorRightQuantity>,
    LeftNumeratorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        LeftDenominatorAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightDenominatorQuantity,
        LeftDenominatorUnit,
        LeftDenominatorAndRightDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                LeftNumeratorRightAndRightNumeratorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightDenominatorQuantity,
            LeftDenominatorAndRightDenominatorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightNumeratorRightQuantity,
                    LeftNumeratorRightAndRightNumeratorLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorRightAndRightNumeratorLeftQuantity,
                    LeftNumeratorLeftAndRightNumeratorRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorQuantity,
                LeftDenominatorAndRightDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            >,
        LeftDenominatorAndRightDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorRightAsLeftAndNumeratorLeftAsRightAndDenominatorAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                >,
            LeftDenominatorAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightNumeratorUnit: LeftNumeratorUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorUnit: LeftDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightNumeratorUnit(
    right.unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
