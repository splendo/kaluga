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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> * Inv<Mul<A, B>> -> Inv<Mul<Mul<A, A>, Mul<A, B>>>

fun <
    LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity>,
    RightReciprocalRightQuantity : UndefinedQuantityType,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<RightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        RightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            RightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            >,
        LeftReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            RightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                RightReciprocalRightQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                    LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                    RightReciprocalRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightReciprocalLeftQuantity,
                RightReciprocalRightQuantity,
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
