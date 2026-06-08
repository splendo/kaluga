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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, B> * Mul<A, A> -> Mul<Mul<A, B>, Mul<A, A>>

fun <
    LeftLeftAndRightLeftAndRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightLeftAndRightQuantity>,
    LeftRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightLeftAndRightQuantity,
        LeftLeftUnit,
        LeftRightQuantity,
        LeftRightUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightLeftAndRightQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightLeftAndRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightLeftAndRightQuantity,
        RightLeftUnit,
        LeftLeftAndRightLeftAndRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightLeftAndRightQuantity,
            LeftRightQuantity,
            >,
        LeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightLeftAndRightQuantity,
            LeftLeftAndRightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightLeftAndRightQuantity,
                LeftRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightLeftAndRightQuantity,
                LeftLeftAndRightLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightLeftAndRightQuantity,
        LeftRightQuantity,
        >,
    LeftUnit,
    >.multipliedBySquaredUnitWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightLeftAndRightQuantity,
            LeftLeftAndRightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightUnit: LeftUnit.(RightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightUnit(
    right.unit,
).byMultiplying(this, right, factory)
