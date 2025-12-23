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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> / Div<Mul<C, A>, B> -> Inv<Mul<Mul<A, C>, A>>

fun <
    NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity>,
    NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorNumeratorLeftQuantity,
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            DenominatorNumeratorLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                DenominatorNumeratorLeftQuantity,
                >,
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                    DenominatorNumeratorLeftQuantity,
                    >,
                NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithLeftAsRightAndRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                >,
            NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalLeftUnitXDenominatorNumeratorLeftUnit: NumeratorReciprocalLeftUnit.(DenominatorNumeratorLeftUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXNumeratorReciprocalLeftUnit: TargetReciprocalLeftUnit.(NumeratorReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.left.numeratorReciprocalLeftUnitXDenominatorNumeratorLeftUnit(
    right.unit.numerator.left,
).targetReciprocalLeftUnitXNumeratorReciprocalLeftUnit(
    unit.inverse.left,
).reciprocalTargetUnit().byDividing(this, right, factory)
