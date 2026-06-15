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

// Inv<Mul<A, B>> * Mul<B, B> -> Div<B, A>

fun <
    LeftReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftQuantity>,
    LeftReciprocalRightAndRightLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightLeftAndRightQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightLeftAndRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightLeftAndRightQuantity,
        RightLeftUnit,
        LeftReciprocalRightAndRightLeftAndRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftReciprocalRightAndRightLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftReciprocalRightAndRightLeftAndRightQuantity,
            LeftReciprocalLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedBySquaredUnitWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightLeftAndRightQuantity,
            LeftReciprocalRightAndRightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalRightUnitPerLeftReciprocalLeftUnit: LeftReciprocalRightUnit.(LeftReciprocalLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.right.leftReciprocalRightUnitPerLeftReciprocalLeftUnit(
    unit.inverse.left,
).byMultiplying(this, right, factory)
