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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, C> / Div<B, Mul<C, A>> -> Mul<A, A>

fun <
    NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity>,
    NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
            NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorRightAsNumeratorAndMultiplyingDenominatorWithDenominatorAsLeftAndNumeratorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
                NumeratorNumeratorLeftAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorLeftUnitXNumeratorNumeratorLeftUnit: NumeratorNumeratorLeftUnit.(NumeratorNumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.numeratorNumeratorLeftUnitXNumeratorNumeratorLeftUnit(
    unit.numerator.left,
).byDividing(this, right, factory)
