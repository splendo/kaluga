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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> / Mul<B, B> -> Inv<Mul<Mul<A, B>, Mul<B, B>>>

fun <
    NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
    NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftQuantity,
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
        DenominatorLeftUnit,
        NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
        DenominatorRightUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftQuantity,
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            >,
        DenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftQuantity,
                NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftQuantity,
                    NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
                    NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftQuantity,
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedBySquaredUnitWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorLeftAndRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXDenominatorUnit: NumeratorReciprocalUnit.(DenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXDenominatorUnit(
    right.unit,
).reciprocalTargetUnit().byDividing(this, right, factory)
