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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<A> / Mul<B, A> -> Inv<Mul<Mul<A, B>, A>>

fun <
    NumeratorReciprocalAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalAndDenominatorRightQuantity>,
    NumeratorUnit : UndefinedReciprocalUnit<
        NumeratorReciprocalAndDenominatorRightQuantity,
        NumeratorReciprocalUnit,
        >,
    DenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorReciprocalAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalAndDenominatorRightQuantity,
        NumeratorReciprocalUnit,
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalAndDenominatorRightQuantity,
            DenominatorLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        NumeratorReciprocalAndDenominatorRightQuantity,
        NumeratorReciprocalUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalAndDenominatorRightQuantity,
                DenominatorLeftQuantity,
                >,
            NumeratorReciprocalAndDenominatorRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalAndDenominatorRightQuantity,
                    DenominatorLeftQuantity,
                    >,
                NumeratorReciprocalAndDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        NumeratorReciprocalAndDenominatorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            DenominatorLeftQuantity,
            NumeratorReciprocalAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXDenominatorLeftUnit: NumeratorReciprocalUnit.(DenominatorLeftUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXNumeratorReciprocalUnit: TargetReciprocalLeftUnit.(NumeratorReciprocalUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXDenominatorLeftUnit(
    right.unit.left,
).targetReciprocalLeftUnitXNumeratorReciprocalUnit(
    unit.inverse,
).reciprocalTargetUnit().byDividing(this, right, factory)
