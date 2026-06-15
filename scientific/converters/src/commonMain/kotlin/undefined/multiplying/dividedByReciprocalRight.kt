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

// Mul<A, B> / Inv<B> -> Mul<Mul<B, A>, B>

fun <
    NumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftQuantity>,
    NumeratorRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorReciprocalQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorReciprocalQuantity,
        NumeratorRightUnit,
        >,
    DenominatorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorReciprocalQuantity>,
    DenominatorUnit : UndefinedReciprocalUnit<
        NumeratorRightAndDenominatorReciprocalQuantity,
        DenominatorReciprocalUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorReciprocalQuantity,
        NumeratorRightUnit,
        NumeratorLeftQuantity,
        NumeratorLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorReciprocalQuantity,
            NumeratorLeftQuantity,
            >,
        TargetLeftUnit,
        NumeratorRightAndDenominatorReciprocalQuantity,
        NumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorRightAndDenominatorReciprocalQuantity,
                NumeratorLeftQuantity,
                >,
            NumeratorRightAndDenominatorReciprocalQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftQuantity,
        NumeratorRightAndDenominatorReciprocalQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            NumeratorRightAndDenominatorReciprocalQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorRightUnitXNumeratorLeftUnit: NumeratorRightUnit.(NumeratorLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXNumeratorRightUnit: TargetLeftUnit.(NumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.numeratorRightUnitXNumeratorLeftUnit(
    unit.left,
).targetLeftUnitXNumeratorRightUnit(
    unit.right,
).byDividing(this, right, factory)
