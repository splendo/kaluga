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

// Mul<A, B> / Div<Mul<A, A>, B> -> Div<Mul<B, B>, A>

fun <
    NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        NumeratorRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorDenominatorQuantity,
            NumeratorRightAndDenominatorDenominatorQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorDenominatorQuantity,
                NumeratorRightAndDenominatorDenominatorQuantity,
                >,
            NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorRightAndDenominatorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithLeftAsRootAndRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorRightUnitXNumeratorRightUnit: NumeratorRightUnit.(NumeratorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorLeftUnit: TargetNumeratorUnit.(NumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.numeratorRightUnitXNumeratorRightUnit(
    unit.right,
).targetNumeratorUnitPerNumeratorLeftUnit(
    unit.left,
).byDividing(this, right, factory)
