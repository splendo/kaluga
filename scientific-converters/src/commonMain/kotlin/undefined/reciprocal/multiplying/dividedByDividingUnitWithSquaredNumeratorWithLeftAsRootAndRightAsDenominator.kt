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

// Inv<Mul<A, B>> / Div<Mul<A, A>, B> -> Inv<Mul<Mul<A, A>, A>>

fun <
    NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithLeftAsRootAndRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorReciprocalRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    denominatorNumeratorUnitXNumeratorReciprocalLeftUnit: DenominatorNumeratorUnit.(NumeratorReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.denominatorNumeratorUnitXNumeratorReciprocalLeftUnit(
    unit.inverse.left,
).reciprocalTargetUnit().byDividing(this, right, factory)
