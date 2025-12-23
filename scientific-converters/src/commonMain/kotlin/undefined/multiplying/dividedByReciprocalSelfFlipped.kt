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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<A, B> / Inv<Mul<B, A>> -> Mul<Mul<A, B>, Mul<B, A>>

fun <
    NumeratorLeftAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorReciprocalRightQuantity>,
    NumeratorRightAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorReciprocalLeftQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorReciprocalRightQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorReciprocalLeftQuantity,
        NumeratorRightUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorLeftAndDenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorReciprocalLeftQuantity,
            NumeratorLeftAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorReciprocalRightQuantity,
            NumeratorRightAndDenominatorReciprocalLeftQuantity,
            >,
        NumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorReciprocalLeftQuantity,
            NumeratorLeftAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorReciprocalRightQuantity,
                NumeratorRightAndDenominatorReciprocalLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorReciprocalLeftQuantity,
                NumeratorLeftAndDenominatorReciprocalRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorReciprocalRightQuantity,
        NumeratorRightAndDenominatorReciprocalLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSelfFlipped(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorReciprocalLeftQuantity,
                NumeratorLeftAndDenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXDenominatorReciprocalUnit: NumeratorUnit.(DenominatorReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXDenominatorReciprocalUnit(
    right.unit.inverse,
).byDividing(this, right, factory)
