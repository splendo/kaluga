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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> * Mul<A, B> -> Div<B, A>

fun <
    LeftReciprocalLeftAndRightAndRightLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightLeftQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightLeftQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightAndRightLeftQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightLeftQuantity,
            LeftReciprocalLeftAndRightAndRightLeftQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightLeftQuantity>,
    RightRightQuantity : UndefinedQuantityType,
    RightRightUnit : AbstractUndefinedScientificUnit<RightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightLeftQuantity,
        RightLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightRightQuantity,
        RightRightUnit,
        LeftReciprocalLeftAndRightAndRightLeftQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightRightQuantity,
            LeftReciprocalLeftAndRightAndRightLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightLeftQuantity,
            LeftReciprocalLeftAndRightAndRightLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightLeftQuantity,
            RightRightQuantity,
            >,
        RightUnit,
        >,
    rightRightUnitPerLeftReciprocalLeftUnit: RightRightUnit.(LeftReciprocalLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.right.rightRightUnitPerLeftReciprocalLeftUnit(
    unit.inverse.left,
).byMultiplying(this, right, factory)
