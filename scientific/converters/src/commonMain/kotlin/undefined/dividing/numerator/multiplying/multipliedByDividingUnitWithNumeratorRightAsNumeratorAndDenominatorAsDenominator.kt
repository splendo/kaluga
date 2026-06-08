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

// Div<Mul<A, B>, C> * Div<B, C> -> Div<Mul<Mul<A, B>, B>, Mul<C, C>>

fun <
    LeftNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftQuantity>,
    LeftNumeratorRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorQuantity>,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        LeftNumeratorRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        LeftDenominatorAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorQuantity,
            >,
        LeftNumeratorUnit,
        LeftNumeratorRightAndRightNumeratorQuantity,
        LeftNumeratorRightUnit,
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
                LeftNumeratorLeftQuantity,
                LeftNumeratorRightAndRightNumeratorQuantity,
                >,
            LeftNumeratorRightAndRightNumeratorQuantity,
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
                    LeftNumeratorLeftQuantity,
                    LeftNumeratorRightAndRightNumeratorQuantity,
                    >,
                LeftNumeratorRightAndRightNumeratorQuantity,
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
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorQuantity,
            >,
        LeftDenominatorAndRightDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithNumeratorRightAsNumeratorAndDenominatorAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorRightAndRightNumeratorQuantity,
            LeftDenominatorAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftNumeratorRightUnit: LeftNumeratorUnit.(LeftNumeratorRightUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorUnit: LeftDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
