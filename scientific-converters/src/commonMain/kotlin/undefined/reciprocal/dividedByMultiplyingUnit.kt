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

// Inv<A> / Mul<B, C> -> Inv<Mul<Mul<A, B>, C>>

fun <
    NumeratorReciprocalQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalQuantity>,
    NumeratorUnit : UndefinedReciprocalUnit<
        NumeratorReciprocalQuantity,
        NumeratorReciprocalUnit,
        >,
    DenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorLeftQuantity>,
    DenominatorRightQuantity : UndefinedQuantityType,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalQuantity,
        NumeratorReciprocalUnit,
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalQuantity,
            DenominatorLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalQuantity,
                DenominatorLeftQuantity,
                >,
            DenominatorRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalQuantity,
                    DenominatorLeftQuantity,
                    >,
                DenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        NumeratorReciprocalQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            DenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXDenominatorLeftUnit: NumeratorReciprocalUnit.(DenominatorLeftUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXDenominatorRightUnit: TargetReciprocalLeftUnit.(DenominatorRightUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXDenominatorLeftUnit(
    right.unit.left,
).targetReciprocalLeftUnitXDenominatorRightUnit(
    right.unit.right,
).reciprocalTargetUnit().byDividing(this, right, factory)
