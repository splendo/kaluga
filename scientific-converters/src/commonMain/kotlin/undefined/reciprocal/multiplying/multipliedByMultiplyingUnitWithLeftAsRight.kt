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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> * Mul<C, A> -> Div<C, B>

fun <
    LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
    LeftReciprocalRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightLeftQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        LeftReciprocalLeftAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightLeftQuantity,
            LeftReciprocalRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftQuantity,
            LeftReciprocalLeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    rightLeftUnitPerLeftReciprocalRightUnit: RightLeftUnit.(LeftReciprocalRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.left.rightLeftUnitPerLeftReciprocalRightUnit(
    unit.inverse.right,
).byMultiplying(this, right, factory)
