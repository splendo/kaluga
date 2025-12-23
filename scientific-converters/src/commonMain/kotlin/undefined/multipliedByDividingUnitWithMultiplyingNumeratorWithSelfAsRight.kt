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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// A * Div<Mul<B, A>, C> -> Div<Mul<Mul<A, B>, A>, C>

fun <
    LeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftAndRightNumeratorRightQuantity>,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            LeftAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        LeftAndRightNumeratorRightQuantity,
        LeftUnit,
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftAndRightNumeratorRightQuantity,
            RightNumeratorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        LeftAndRightNumeratorRightQuantity,
        LeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftAndRightNumeratorRightQuantity,
                RightNumeratorLeftQuantity,
                >,
            LeftAndRightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftAndRightNumeratorRightQuantity,
                    RightNumeratorLeftQuantity,
                    >,
                LeftAndRightNumeratorRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    LeftAndRightNumeratorRightQuantity,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftAndRightNumeratorRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightNumeratorLeftUnit: LeftUnit.(RightNumeratorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXLeftUnit: TargetNumeratorLeftUnit.(LeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightNumeratorLeftUnit(
    right.unit.numerator.left,
).targetNumeratorLeftUnitXLeftUnit(
    unit,
).targetNumeratorUnitPerRightDenominatorUnit(
    right.unit.denominator,
).byMultiplying(this, right, factory)
