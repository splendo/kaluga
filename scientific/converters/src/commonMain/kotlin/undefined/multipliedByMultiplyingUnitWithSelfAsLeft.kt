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

// A * Mul<A, B> -> Mul<Mul<A, A>, B>

fun <
    LeftAndRightLeftQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftAndRightLeftQuantity>,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftAndRightLeftQuantity>,
    RightRightQuantity : UndefinedQuantityType,
    RightRightUnit : AbstractUndefinedScientificUnit<RightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftAndRightLeftQuantity,
        RightLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        LeftAndRightLeftQuantity,
        LeftUnit,
        LeftAndRightLeftQuantity,
        LeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftAndRightLeftQuantity,
            LeftAndRightLeftQuantity,
            >,
        TargetLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftAndRightLeftQuantity,
                LeftAndRightLeftQuantity,
                >,
            RightRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    LeftAndRightLeftQuantity,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftAndRightLeftQuantity,
            RightRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXLeftUnit: LeftUnit.(LeftUnit) -> TargetLeftUnit,
    targetLeftUnitXRightRightUnit: TargetLeftUnit.(RightRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXLeftUnit(
    unit,
).targetLeftUnitXRightRightUnit(
    right.unit.right,
).byMultiplying(this, right, factory)
