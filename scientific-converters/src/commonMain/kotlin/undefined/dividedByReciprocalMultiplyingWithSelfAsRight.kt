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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// A / Inv<Mul<B, A>> -> Mul<Mul<A, B>, A>

fun <
    NumeratorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        DenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorAndDenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorReciprocalLeftQuantity,
            NumeratorAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        NumeratorAndDenominatorReciprocalRightQuantity,
        NumeratorUnit,
        DenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorAndDenominatorReciprocalRightQuantity,
            DenominatorReciprocalLeftQuantity,
            >,
        TargetLeftUnit,
        NumeratorAndDenominatorReciprocalRightQuantity,
        NumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorAndDenominatorReciprocalRightQuantity,
                DenominatorReciprocalLeftQuantity,
                >,
            NumeratorAndDenominatorReciprocalRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    NumeratorAndDenominatorReciprocalRightQuantity,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                DenominatorReciprocalLeftQuantity,
                NumeratorAndDenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXDenominatorReciprocalLeftUnit: NumeratorUnit.(DenominatorReciprocalLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXNumeratorUnit: TargetLeftUnit.(NumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXDenominatorReciprocalLeftUnit(
    right.unit.inverse.left,
).targetLeftUnitXNumeratorUnit(
    unit,
).byDividing(this, right, factory)
