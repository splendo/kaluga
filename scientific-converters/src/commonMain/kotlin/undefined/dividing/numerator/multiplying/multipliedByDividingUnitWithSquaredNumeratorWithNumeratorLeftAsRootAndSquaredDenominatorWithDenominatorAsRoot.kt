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

// Div<Mul<B, A>, C> * Div<Mul<B, B>, Mul<C, C>> -> Div<Mul<Mul<B, A>, Mul<B, B>>, Mul<Mul<C, C>, C>>

fun <
    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity>,
    LeftNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftAndRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                >,
            LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                    LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                    >,
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithNumeratorLeftAsRootAndSquaredDenominatorWithDenominatorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightNumeratorUnit: LeftNumeratorUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
    rightDenominatorUnitXLeftDenominatorUnit: RightDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightNumeratorUnit(
    right.unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    right.unit.denominator.rightDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
