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

// Div<Mul<A, B>, C> / Mul<D, C> -> Div<Mul<A, B>, Mul<Mul<C, D>, C>>

fun <
    NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
    NumeratorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorRightQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorRightQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorDenominatorAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorRightQuantity,
        NumeratorDenominatorUnit,
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorRightQuantity,
            DenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        NumeratorDenominatorAndDenominatorRightQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorRightQuantity,
                DenominatorLeftQuantity,
                >,
            NumeratorDenominatorAndDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftQuantity,
                NumeratorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorAndDenominatorRightQuantity,
                    DenominatorLeftQuantity,
                    >,
                NumeratorDenominatorAndDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorDenominatorAndDenominatorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            DenominatorLeftQuantity,
            NumeratorDenominatorAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXDenominatorLeftUnit: NumeratorDenominatorUnit.(DenominatorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXNumeratorDenominatorUnit: TargetDenominatorLeftUnit.(NumeratorDenominatorUnit) -> TargetDenominatorUnit,
    numeratorNumeratorUnitPerTargetDenominatorUnit: NumeratorNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXDenominatorLeftUnit(
        right.unit.left,
    ).targetDenominatorLeftUnitXNumeratorDenominatorUnit(
        unit.denominator,
    ),
).byDividing(this, right, factory)
