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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<B> * Inv<Mul<A, A>> -> Inv<Mul<Mul<B, A>, A>>

fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalQuantity>,
    LeftUnit : UndefinedReciprocalUnit<
        LeftReciprocalQuantity,
        LeftReciprocalUnit,
        >,
    RightReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<RightReciprocalLeftAndRightQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<RightReciprocalLeftAndRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        RightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        RightReciprocalLeftAndRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            RightReciprocalLeftAndRightQuantity,
            RightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalQuantity,
        LeftReciprocalUnit,
        RightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalQuantity,
            RightReciprocalLeftAndRightQuantity,
            >,
        TargetReciprocalLeftUnit,
        RightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalQuantity,
                RightReciprocalLeftAndRightQuantity,
                >,
            RightReciprocalLeftAndRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalQuantity,
                    RightReciprocalLeftAndRightQuantity,
                    >,
                RightReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        LeftReciprocalQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalSquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                RightReciprocalLeftAndRightQuantity,
                RightReciprocalLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXRightReciprocalLeftUnit: LeftReciprocalUnit.(RightReciprocalLeftUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXRightReciprocalLeftUnit: TargetReciprocalLeftUnit.(RightReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitXRightReciprocalLeftUnit(
    right.unit.inverse.left,
).targetReciprocalLeftUnitXRightReciprocalLeftUnit(
    right.unit.inverse.left,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
