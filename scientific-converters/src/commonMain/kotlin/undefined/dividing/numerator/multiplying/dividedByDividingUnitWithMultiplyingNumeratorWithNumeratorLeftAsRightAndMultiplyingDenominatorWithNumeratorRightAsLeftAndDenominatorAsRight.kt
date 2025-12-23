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

// Div<Mul<A, B>, C> / Div<Mul<D, A>, Mul<B, C>> -> Div<Mul<B, B>, D>

fun <
    NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity>,
    NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorRightQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorNumeratorLeftQuantity,
            NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorRightUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
                >,
            DenominatorNumeratorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
            >,
        NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithNumeratorLeftAsRightAndMultiplyingDenominatorWithNumeratorRightAsLeftAndDenominatorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorNumeratorLeftAndDenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorLeftQuantity,
                NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorRightUnitXNumeratorNumeratorRightUnit: NumeratorNumeratorRightUnit.(NumeratorNumeratorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorLeftUnit: TargetNumeratorUnit.(DenominatorNumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.numeratorNumeratorRightUnitXNumeratorNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerDenominatorNumeratorLeftUnit(
    right.unit.numerator.left,
).byDividing(this, right, factory)
