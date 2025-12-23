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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, Mul<B, B>> * Div<A, B> -> Div<Mul<Mul<A, A>, A>, Mul<Mul<B, B>, B>>

fun <
    LeftNumeratorLeftAndRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorQuantity>,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            >,
        LeftNumeratorUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
                >,
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
                LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
                >,
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
                    LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
                    >,
                LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
                    LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
                    >,
                LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithNumeratorRootAsNumeratorAndDenominatorRootAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorLeftAndRightAndRightNumeratorQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftNumeratorLeftUnit: LeftNumeratorUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorLeftUnit: LeftDenominatorUnit.(LeftDenominatorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byMultiplying(this, right, factory)
