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

// Inv<Mul<A, B>> / A -> Inv<Mul<Mul<A, B>, A>>

fun <
    NumeratorReciprocalLeftAndDenominatorQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorQuantity>,
    NumeratorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorQuantity>,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        NumeratorReciprocalLeftAndDenominatorQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorQuantity,
                NumeratorReciprocalRightQuantity,
                >,
            NumeratorReciprocalLeftAndDenominatorQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorQuantity,
                    NumeratorReciprocalRightQuantity,
                    >,
                NumeratorReciprocalLeftAndDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByLeft(
    right: UndefinedScientificValue<
        NumeratorReciprocalLeftAndDenominatorQuantity,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXNumeratorReciprocalLeftUnit: NumeratorReciprocalUnit.(NumeratorReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXNumeratorReciprocalLeftUnit(
    unit.inverse.left,
).reciprocalTargetUnit().byDividing(this, right, factory)
