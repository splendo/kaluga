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

// Inv<A> * Inv<Mul<A, B>> -> Inv<Mul<Mul<A, A>, B>>

fun <
    LeftReciprocalAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightReciprocalLeftQuantity>,
    LeftUnit : UndefinedReciprocalUnit<
        LeftReciprocalAndRightReciprocalLeftQuantity,
        LeftReciprocalUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightReciprocalLeftQuantity>,
    RightReciprocalRightQuantity : UndefinedQuantityType,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<RightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        RightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalAndRightReciprocalLeftQuantity,
            RightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalAndRightReciprocalLeftQuantity,
        LeftReciprocalUnit,
        LeftReciprocalAndRightReciprocalLeftQuantity,
        LeftReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalAndRightReciprocalLeftQuantity,
            LeftReciprocalAndRightReciprocalLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        RightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalAndRightReciprocalLeftQuantity,
                LeftReciprocalAndRightReciprocalLeftQuantity,
                >,
            RightReciprocalRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalAndRightReciprocalLeftQuantity,
                    LeftReciprocalAndRightReciprocalLeftQuantity,
                    >,
                RightReciprocalRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        LeftReciprocalAndRightReciprocalLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalAndRightReciprocalLeftQuantity,
                RightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXLeftReciprocalUnit: LeftReciprocalUnit.(LeftReciprocalUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXRightReciprocalRightUnit: TargetReciprocalLeftUnit.(RightReciprocalRightUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitXLeftReciprocalUnit(
    unit.inverse,
).targetReciprocalLeftUnitXRightReciprocalRightUnit(
    right.unit.inverse.right,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
