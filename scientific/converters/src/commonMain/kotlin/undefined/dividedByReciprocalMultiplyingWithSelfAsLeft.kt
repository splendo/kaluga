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

// A / Inv<Mul<A, B>> -> Mul<Mul<A, A>, B>

fun <
    NumeratorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightQuantity : UndefinedQuantityType,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorAndDenominatorReciprocalLeftQuantity,
            DenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        NumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorUnit,
        NumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorAndDenominatorReciprocalLeftQuantity,
            NumeratorAndDenominatorReciprocalLeftQuantity,
            >,
        TargetLeftUnit,
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorAndDenominatorReciprocalLeftQuantity,
                NumeratorAndDenominatorReciprocalLeftQuantity,
                >,
            DenominatorReciprocalRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    NumeratorAndDenominatorReciprocalLeftQuantity,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorAndDenominatorReciprocalLeftQuantity,
                DenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXNumeratorUnit: NumeratorUnit.(NumeratorUnit) -> TargetLeftUnit,
    targetLeftUnitXDenominatorReciprocalRightUnit: TargetLeftUnit.(DenominatorReciprocalRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXNumeratorUnit(
    unit,
).targetLeftUnitXDenominatorReciprocalRightUnit(
    right.unit.inverse.right,
).byDividing(this, right, factory)
