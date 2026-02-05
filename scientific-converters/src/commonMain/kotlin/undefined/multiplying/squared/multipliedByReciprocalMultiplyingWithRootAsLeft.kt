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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<A, A> * Inv<Mul<A, B>> -> Div<A, B>

fun <
    LeftLeftAndRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightReciprocalLeftQuantity>,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightReciprocalLeftQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightReciprocalLeftQuantity,
        LeftLeftUnit,
        LeftLeftAndRightAndRightReciprocalLeftQuantity,
        LeftRightUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightReciprocalLeftQuantity>,
    RightReciprocalRightQuantity : UndefinedQuantityType,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<RightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        RightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightReciprocalLeftQuantity,
            RightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftLeftAndRightAndRightReciprocalLeftQuantity,
        LeftLeftUnit,
        RightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftLeftAndRightAndRightReciprocalLeftQuantity,
            RightReciprocalRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightAndRightReciprocalLeftQuantity,
        LeftLeftAndRightAndRightReciprocalLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightReciprocalLeftQuantity,
                RightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftLeftUnitPerRightReciprocalRightUnit: LeftLeftUnit.(RightReciprocalRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.leftLeftUnitPerRightReciprocalRightUnit(
    right.unit.inverse.right,
).byMultiplying(this, right, factory)
