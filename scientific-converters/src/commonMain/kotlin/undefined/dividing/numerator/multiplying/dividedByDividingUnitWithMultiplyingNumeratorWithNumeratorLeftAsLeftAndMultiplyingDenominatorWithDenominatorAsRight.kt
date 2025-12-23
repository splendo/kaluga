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

// Div<Mul<A, B>, C> / Div<Mul<A, D>, Mul<E, C>> -> Div<Mul<B, E>, D>

fun <
    NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
    NumeratorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorRightQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
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
    DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            DenominatorDenominatorLeftQuantity,
            NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorRightQuantity,
            DenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightQuantity,
                DenominatorDenominatorLeftQuantity,
                >,
            DenominatorNumeratorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithNumeratorLeftAsLeftAndMultiplyingDenominatorWithDenominatorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                DenominatorDenominatorLeftQuantity,
                NumeratorDenominatorAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorRightUnitXDenominatorDenominatorLeftUnit: NumeratorNumeratorRightUnit.(DenominatorDenominatorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorRightUnit: TargetNumeratorUnit.(DenominatorNumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.numeratorNumeratorRightUnitXDenominatorDenominatorLeftUnit(
    right.unit.denominator.left,
).targetNumeratorUnitPerDenominatorNumeratorRightUnit(
    right.unit.numerator.right,
).byDividing(this, right, factory)
