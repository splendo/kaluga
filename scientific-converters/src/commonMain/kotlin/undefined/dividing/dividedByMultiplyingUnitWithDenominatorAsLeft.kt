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

package com.splendo.kaluga.scientific.converter.undefined.dividing

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, B> / Mul<B, C> -> Div<A, Mul<Mul<B, B>, C>>

fun <
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorQuantity>,
    NumeratorDenominatorAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorLeftQuantity>,
    DenominatorRightQuantity : UndefinedQuantityType,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        NumeratorDenominatorAndDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorLeftQuantity,
            NumeratorDenominatorAndDenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorLeftQuantity,
                NumeratorDenominatorAndDenominatorLeftQuantity,
                >,
            DenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorAndDenominatorLeftQuantity,
                    NumeratorDenominatorAndDenominatorLeftQuantity,
                    >,
                DenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorQuantity,
        NumeratorDenominatorAndDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXNumeratorDenominatorUnit: NumeratorDenominatorUnit.(NumeratorDenominatorUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXDenominatorRightUnit: TargetDenominatorLeftUnit.(DenominatorRightUnit) -> TargetDenominatorUnit,
    numeratorNumeratorUnitPerTargetDenominatorUnit: NumeratorNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorUnit(
        unit.denominator,
    ).targetDenominatorLeftUnitXDenominatorRightUnit(
        right.unit.right,
    ),
).byDividing(this, right, factory)
