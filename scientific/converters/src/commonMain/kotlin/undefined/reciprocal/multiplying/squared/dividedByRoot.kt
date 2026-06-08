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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> / A -> Inv<Mul<Mul<A, A>, A>>

fun <
    NumeratorReciprocalLeftAndRightAndDenominatorQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorQuantity>,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorQuantity>,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        NumeratorReciprocalUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                    NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                    >,
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByRoot(
    right: UndefinedScientificValue<
        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXNumeratorReciprocalLeftUnit: NumeratorReciprocalUnit.(NumeratorReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXNumeratorReciprocalLeftUnit(
    unit.inverse.left,
).reciprocalTargetUnit().byDividing(this, right, factory)
