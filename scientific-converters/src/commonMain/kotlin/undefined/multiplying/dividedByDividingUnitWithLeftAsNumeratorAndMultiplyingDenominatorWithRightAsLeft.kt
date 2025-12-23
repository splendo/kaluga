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

// Mul<A, B> / Div<A, Mul<B, C>> -> Mul<Mul<B, B>, C>

fun <
    NumeratorLeftAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorQuantity>,
    NumeratorRightAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorNumeratorQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorRightUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorQuantity>,
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
        NumeratorLeftAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorRightUnit,
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorRightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorDenominatorLeftQuantity,
            NumeratorRightAndDenominatorDenominatorLeftQuantity,
            >,
        TargetLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorDenominatorLeftQuantity,
                NumeratorRightAndDenominatorDenominatorLeftQuantity,
                >,
            DenominatorDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorNumeratorQuantity,
        NumeratorRightAndDenominatorDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithLeftAsNumeratorAndMultiplyingDenominatorWithRightAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorLeftAndDenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorRightUnitXNumeratorRightUnit: NumeratorRightUnit.(NumeratorRightUnit) -> TargetLeftUnit,
    targetLeftUnitXDenominatorDenominatorRightUnit: TargetLeftUnit.(DenominatorDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.numeratorRightUnitXNumeratorRightUnit(
    unit.right,
).targetLeftUnitXDenominatorDenominatorRightUnit(
    right.unit.denominator.right,
).byDividing(this, right, factory)
