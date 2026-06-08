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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> * Div<Mul<A, B>, C> -> Div<Mul<Mul<A, A>, Mul<A, B>>, C>

fun <
    LeftLeftAndRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightNumeratorLeftQuantity>,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightNumeratorLeftQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightNumeratorLeftQuantity,
        LeftLeftUnit,
        LeftLeftAndRightAndRightNumeratorLeftQuantity,
        LeftRightUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightNumeratorLeftQuantity>,
    RightNumeratorRightQuantity : UndefinedQuantityType,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<RightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightNumeratorLeftQuantity,
            LeftLeftAndRightAndRightNumeratorLeftQuantity,
            >,
        LeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightNumeratorLeftQuantity,
                LeftLeftAndRightAndRightNumeratorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftLeftAndRightAndRightNumeratorLeftQuantity,
                    LeftLeftAndRightAndRightNumeratorLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftLeftAndRightAndRightNumeratorLeftQuantity,
                    RightNumeratorRightQuantity,
                    >,
                >,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightAndRightNumeratorLeftQuantity,
        LeftLeftAndRightAndRightNumeratorLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightNumeratorUnit: LeftUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightNumeratorUnit(
    right.unit.numerator,
).targetNumeratorUnitPerRightDenominatorUnit(
    right.unit.denominator,
).byMultiplying(this, right, factory)
