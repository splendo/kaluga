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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<B, A> / Div<Mul<A, A>, B> -> Div<Mul<B, B>, A>

fun <
    NumeratorLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorDenominatorQuantity>,
    NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorDenominatorQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorRightUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorLeftAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorDenominatorQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndDenominatorDenominatorQuantity,
        NumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorDenominatorQuantity,
            NumeratorLeftAndDenominatorDenominatorQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorDenominatorQuantity,
                NumeratorLeftAndDenominatorDenominatorQuantity,
                >,
            NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorDenominatorQuantity,
        NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithRightAsRootAndLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorLeftAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorLeftUnitXNumeratorLeftUnit: NumeratorLeftUnit.(NumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorRightUnit: TargetNumeratorUnit.(NumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.numeratorLeftUnitXNumeratorLeftUnit(
    unit.left,
).targetNumeratorUnitPerNumeratorRightUnit(
    unit.right,
).byDividing(this, right, factory)
