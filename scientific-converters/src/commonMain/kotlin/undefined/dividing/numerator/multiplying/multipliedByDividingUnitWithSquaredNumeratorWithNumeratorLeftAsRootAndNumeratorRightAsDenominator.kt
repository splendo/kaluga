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

// Div<Mul<A, B>, C> * Div<Mul<A, A>, B> -> Div<Mul<Mul<A, A>, A>, C>

fun <
    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity>,
    LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightDenominatorQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightAndRightDenominatorQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
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
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftNumeratorRightAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithNumeratorLeftAsRootAndNumeratorRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftNumeratorRightAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftNumeratorLeftUnit: RightNumeratorUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorUnit: TargetNumeratorUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
