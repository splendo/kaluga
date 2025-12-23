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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// A / Div<B, Mul<C, A>> -> Div<Mul<Mul<A, C>, A>, B>

fun <
    NumeratorAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorAndDenominatorDenominatorRightQuantity>,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            DenominatorDenominatorLeftQuantity,
            NumeratorAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        NumeratorAndDenominatorDenominatorRightQuantity,
        NumeratorUnit,
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorAndDenominatorDenominatorRightQuantity,
            DenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        NumeratorAndDenominatorDenominatorRightQuantity,
        NumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorAndDenominatorDenominatorRightQuantity,
                DenominatorDenominatorLeftQuantity,
                >,
            NumeratorAndDenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorAndDenominatorDenominatorRightQuantity,
                    DenominatorDenominatorLeftQuantity,
                    >,
                NumeratorAndDenominatorDenominatorRightQuantity,
                >,
            DenominatorNumeratorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    NumeratorAndDenominatorDenominatorRightQuantity,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingDenominatorWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                DenominatorDenominatorLeftQuantity,
                NumeratorAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXDenominatorDenominatorLeftUnit: NumeratorUnit.(DenominatorDenominatorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXNumeratorUnit: TargetNumeratorLeftUnit.(NumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorUnit: TargetNumeratorUnit.(DenominatorNumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXDenominatorDenominatorLeftUnit(
    right.unit.denominator.left,
).targetNumeratorLeftUnitXNumeratorUnit(
    unit,
).targetNumeratorUnitPerDenominatorNumeratorUnit(
    right.unit.numerator,
).byDividing(this, right, factory)
