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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, B> * Div<Mul<A, A>, Mul<B, B>> -> Div<Mul<Mul<A, A>, Mul<A, A>>, Mul<Mul<B, B>, B>>

fun <
    LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftAndRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
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
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
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
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
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
                LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
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
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
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
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithNumeratorRootAsRootAndSquaredDenominatorWithDenominatorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftNumeratorUnit: LeftNumeratorUnit.(LeftNumeratorUnit) -> TargetNumeratorUnit,
    rightDenominatorUnitXLeftDenominatorUnit: RightDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    right.unit.denominator.rightDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
