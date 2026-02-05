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

package com.splendo.kaluga.scientific.converter.undefined.defined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// A! * Mul<B, Ex<A>> -> Mul<Mul<Wr<A>, B>, Ex<A>>

fun <
    LeftAndRightRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftAndRightRightQuantity>,
    RightLeftQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftQuantity>,
    ExtendedRightRightUnit,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightRightQuantity,
            >,
        ExtendedRightRightUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftAndRightRightQuantity,
        LeftUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightRightQuantity,
            >,
        WrappedLeftUnit,
        RightLeftQuantity,
        RightLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightRightQuantity,
                >,
            RightLeftQuantity,
            >,
        TargetLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightRightQuantity,
            >,
        ExtendedRightRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightRightQuantity,
                    >,
                RightLeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftAndRightRightQuantity, LeftUnit>.multipliedByMultiplyingUnitWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftAndRightRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitXRightLeftUnit: WrappedLeftUnit.(RightLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXExtendedRightRightUnit: TargetLeftUnit.(ExtendedRightRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedRightRightUnit : UndefinedExtendedUnit<
            LeftAndRightRightQuantity,
            >,
        ExtendedRightRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightRightQuantity,
                >,
            > =
    unit.leftAsUndefined().wrappedLeftUnitXRightLeftUnit(
        right.unit.left,
    ).targetLeftUnitXExtendedRightRightUnit(
        right.unit.right,
    ).byMultiplying(this, right, factory)
