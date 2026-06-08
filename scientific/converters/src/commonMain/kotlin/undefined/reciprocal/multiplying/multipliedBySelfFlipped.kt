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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> * Inv<Mul<B, A>> -> Inv<Mul<Mul<A, B>, Mul<B, A>>>

fun <
    LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
    LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightReciprocalRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightReciprocalLeftQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalRightQuantity,
            LeftReciprocalRightAndRightReciprocalLeftQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftReciprocalLeftAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightReciprocalLeftQuantity,
            LeftReciprocalLeftAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalRightQuantity,
            LeftReciprocalRightAndRightReciprocalLeftQuantity,
            >,
        LeftReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightReciprocalLeftQuantity,
            LeftReciprocalLeftAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightReciprocalRightQuantity,
                LeftReciprocalRightAndRightReciprocalLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightReciprocalLeftQuantity,
                LeftReciprocalLeftAndRightReciprocalRightQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightReciprocalRightQuantity,
                    LeftReciprocalRightAndRightReciprocalLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalRightAndRightReciprocalLeftQuantity,
                    LeftReciprocalLeftAndRightReciprocalRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalRightQuantity,
            LeftReciprocalRightAndRightReciprocalLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedBySelfFlipped(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightReciprocalLeftQuantity,
                LeftReciprocalLeftAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXRightReciprocalUnit: LeftReciprocalUnit.(RightReciprocalUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitXRightReciprocalUnit(
    right.unit.inverse,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
