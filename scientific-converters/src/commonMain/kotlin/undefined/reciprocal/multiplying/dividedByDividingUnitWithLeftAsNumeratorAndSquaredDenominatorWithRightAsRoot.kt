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

// Inv<Mul<A, B>> / Div<A, Mul<B, B>> -> Div<B, Mul<A, A>>

fun <
    NumeratorReciprocalLeftAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorQuantity>,
    NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
                NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithLeftAsNumeratorAndSquaredDenominatorWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalLeftUnitXNumeratorReciprocalLeftUnit: NumeratorReciprocalLeftUnit.(NumeratorReciprocalLeftUnit) -> TargetDenominatorUnit,
    numeratorReciprocalRightUnitPerTargetDenominatorUnit: NumeratorReciprocalRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.right.numeratorReciprocalRightUnitPerTargetDenominatorUnit(
    unit.inverse.left.numeratorReciprocalLeftUnitXNumeratorReciprocalLeftUnit(
        unit.inverse.left,
    ),
).byDividing(this, right, factory)
