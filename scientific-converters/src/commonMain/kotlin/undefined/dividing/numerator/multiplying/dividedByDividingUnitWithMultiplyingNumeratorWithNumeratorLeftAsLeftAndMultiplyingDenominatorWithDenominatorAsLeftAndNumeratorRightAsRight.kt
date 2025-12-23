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

// Div<Mul<A, B>, C> / Div<Mul<A, D>, Mul<C, B>> -> Div<Mul<B, B>, D>

fun <
    NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
    NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightQuantity : UndefinedQuantityType,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorRightUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
                >,
            DenominatorNumeratorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
            >,
        NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithNumeratorLeftAsLeftAndMultiplyingDenominatorWithDenominatorAsLeftAndNumeratorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorDenominatorLeftQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorRightUnitXNumeratorNumeratorRightUnit: NumeratorNumeratorRightUnit.(NumeratorNumeratorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorRightUnit: TargetNumeratorUnit.(DenominatorNumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.numeratorNumeratorRightUnitXNumeratorNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerDenominatorNumeratorRightUnit(
    right.unit.numerator.right,
).byDividing(this, right, factory)
