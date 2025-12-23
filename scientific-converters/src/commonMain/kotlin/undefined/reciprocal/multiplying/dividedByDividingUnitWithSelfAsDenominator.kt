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

// Inv<Mul<A, B>> / Div<C, Mul<A, B>> -> Inv<C>

fun <
    NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity>,
    NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            DenominatorNumeratorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSelfAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
                NumeratorReciprocalRightAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    reciprocalTargetUnit: DenominatorNumeratorUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.reciprocalTargetUnit().byDividing(this, right, factory)
