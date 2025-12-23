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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<A, A> / Inv<A> -> Mul<Mul<A, A>, A>

fun <
    NumeratorLeftAndRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorReciprocalQuantity>,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorReciprocalQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
        NumeratorRightUnit,
        >,
    DenominatorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorReciprocalQuantity>,
    DenominatorUnit : UndefinedReciprocalUnit<
        NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
        DenominatorReciprocalUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
            NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
            >,
        NumeratorUnit,
        NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
        NumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
                NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
                >,
            NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
        NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            NumeratorLeftAndRightAndDenominatorReciprocalQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXNumeratorLeftUnit: NumeratorUnit.(NumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXNumeratorLeftUnit(
    unit.left,
).byDividing(this, right, factory)
