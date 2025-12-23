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

// Mul<A, B> / Div<Mul<A, A>, Mul<B, C>> -> Div<Mul<Mul<B, B>, C>, A>

fun <
    NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorRightAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
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
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightQuantity : UndefinedQuantityType,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorRightUnit,
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorDenominatorLeftQuantity,
            NumeratorRightAndDenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorDenominatorLeftQuantity,
                NumeratorRightAndDenominatorDenominatorLeftQuantity,
                >,
            DenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorRightAndDenominatorDenominatorLeftQuantity,
                    NumeratorRightAndDenominatorDenominatorLeftQuantity,
                    >,
                DenominatorDenominatorRightQuantity,
                >,
            NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithLeftAsRootAndMultiplyingDenominatorWithRightAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorRightUnitXNumeratorRightUnit: NumeratorRightUnit.(NumeratorRightUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXDenominatorDenominatorRightUnit: TargetNumeratorLeftUnit.(DenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorLeftUnit: TargetNumeratorUnit.(NumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.numeratorRightUnitXNumeratorRightUnit(
    unit.right,
).targetNumeratorLeftUnitXDenominatorDenominatorRightUnit(
    right.unit.denominator.right,
).targetNumeratorUnitPerNumeratorLeftUnit(
    unit.left,
).byDividing(this, right, factory)
