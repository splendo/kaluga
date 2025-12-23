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

package com.splendo.kaluga.scientific.converter.undefined.dividing

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, B> * Div<Mul<A, A>, Mul<B, B>> -> Div<Mul<Mul<A, A>, A>, Mul<Mul<B, B>, B>>

fun <
    LeftNumeratorAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightNumeratorLeftAndRightQuantity>,
    LeftDenominatorAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftAndRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorUnit,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
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
            LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
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
            LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
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
                LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
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
                    LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
                    >,
                LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
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
        LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithNumeratorAsRootAndSquaredDenominatorWithDenominatorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftNumeratorUnit: RightNumeratorUnit.(LeftNumeratorUnit) -> TargetNumeratorUnit,
    rightDenominatorUnitXLeftDenominatorUnit: RightDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    right.unit.denominator.rightDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
