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

// Inv<Mul<A, B>> / Div<Mul<B, B>, A> -> Inv<Mul<Mul<B, B>, B>>

fun <
    NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
    NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorReciprocalLeftAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithRightAsRootAndLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorReciprocalLeftAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    denominatorNumeratorUnitXNumeratorReciprocalRightUnit: DenominatorNumeratorUnit.(NumeratorReciprocalRightUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.denominatorNumeratorUnitXNumeratorReciprocalRightUnit(
    unit.inverse.right,
).reciprocalTargetUnit().byDividing(this, right, factory)
