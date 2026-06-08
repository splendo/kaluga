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

// A * Div<Mul<A, A>, B> -> Div<Mul<Mul<A, A>, A>, B>

fun <
    LeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftAndRightNumeratorLeftAndRightQuantity,
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftAndRightNumeratorLeftAndRightQuantity,
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftAndRightNumeratorLeftAndRightQuantity,
        LeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftAndRightNumeratorLeftAndRightQuantity,
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    LeftAndRightNumeratorLeftAndRightQuantity,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftAndRightNumeratorLeftAndRightQuantity,
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftUnit: RightNumeratorUnit.(LeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftUnit(
    unit,
).targetNumeratorUnitPerRightDenominatorUnit(
    right.unit.denominator,
).byMultiplying(this, right, factory)
