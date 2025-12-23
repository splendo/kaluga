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

// Inv<Mul<A, B>> * Mul<A, C> -> Div<C, B>

fun <
    LeftReciprocalLeftAndRightLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightLeftQuantity>,
    LeftReciprocalRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightLeftQuantity,
            LeftReciprocalRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightLeftQuantity>,
    RightRightQuantity : UndefinedQuantityType,
    RightRightUnit : AbstractUndefinedScientificUnit<RightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightLeftQuantity,
        RightLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightRightQuantity,
        RightRightUnit,
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightLeftQuantity,
            LeftReciprocalRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightLeftQuantity,
            RightRightQuantity,
            >,
        RightUnit,
        >,
    rightRightUnitPerLeftReciprocalRightUnit: RightRightUnit.(LeftReciprocalRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.right.rightRightUnitPerLeftReciprocalRightUnit(
    unit.inverse.right,
).byMultiplying(this, right, factory)
