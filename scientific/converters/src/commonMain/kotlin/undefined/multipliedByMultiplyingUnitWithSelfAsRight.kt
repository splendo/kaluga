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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// A * Mul<B, A> -> Mul<Mul<A, B>, A>

fun <
    LeftAndRightRightQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftAndRightRightQuantity>,
    RightLeftQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        LeftAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        LeftAndRightRightQuantity,
        LeftUnit,
        RightLeftQuantity,
        RightLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftAndRightRightQuantity,
            RightLeftQuantity,
            >,
        TargetLeftUnit,
        LeftAndRightRightQuantity,
        LeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftAndRightRightQuantity,
                RightLeftQuantity,
                >,
            LeftAndRightRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    LeftAndRightRightQuantity,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftQuantity,
            LeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightLeftUnit: LeftUnit.(RightLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXLeftUnit: TargetLeftUnit.(LeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightLeftUnit(
    right.unit.left,
).targetLeftUnitXLeftUnit(
    unit,
).byMultiplying(this, right, factory)
