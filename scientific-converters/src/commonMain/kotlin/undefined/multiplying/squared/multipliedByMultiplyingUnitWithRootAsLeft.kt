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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> * Mul<A, B> -> Mul<Mul<A, A>, Mul<A, B>>

fun <
    LeftLeftAndRightAndRightLeftQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightLeftQuantity>,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightLeftQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightLeftQuantity,
        LeftLeftUnit,
        LeftLeftAndRightAndRightLeftQuantity,
        LeftRightUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightLeftQuantity>,
    RightRightQuantity : UndefinedQuantityType,
    RightRightUnit : AbstractUndefinedScientificUnit<RightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightLeftQuantity,
        RightLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightLeftQuantity,
            LeftLeftAndRightAndRightLeftQuantity,
            >,
        LeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightLeftQuantity,
            RightRightQuantity,
            >,
        RightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightLeftQuantity,
                LeftLeftAndRightAndRightLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightLeftQuantity,
                RightRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightAndRightLeftQuantity,
        LeftLeftAndRightAndRightLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightLeftQuantity,
            RightRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightUnit: LeftUnit.(RightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightUnit(
    right.unit,
).byMultiplying(this, right, factory)
