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

// A! * Mul<Ex<A>, B> -> Mul<Mul<Wr<A>, Ex<A>>, B>

fun <
    LeftAndRightLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftAndRightLeftQuantity>,
    ExtendedRightLeftUnit,
    RightRightQuantity : UndefinedQuantityType,
    RightRightUnit : AbstractUndefinedScientificUnit<RightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightLeftQuantity,
            >,
        ExtendedRightLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftAndRightLeftQuantity,
        LeftUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightLeftQuantity,
            >,
        WrappedLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightLeftQuantity,
            >,
        ExtendedRightLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightLeftQuantity,
                >,
            >,
        TargetLeftUnit,
        RightRightQuantity,
        RightRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightLeftQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightLeftQuantity,
                    >,
                >,
            RightRightQuantity,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftAndRightLeftQuantity, LeftUnit>.multipliedByMultiplyingUnitWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftQuantity,
                >,
            RightRightQuantity,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitXExtendedRightLeftUnit: WrappedLeftUnit.(ExtendedRightLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXRightRightUnit: TargetLeftUnit.(RightRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedRightLeftUnit : UndefinedExtendedUnit<
            LeftAndRightLeftQuantity,
            >,
        ExtendedRightLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftQuantity,
                >,
            > =
    unit.leftAsUndefined().wrappedLeftUnitXExtendedRightLeftUnit(
        right.unit.left,
    ).targetLeftUnitXRightRightUnit(
        right.unit.right,
    ).byMultiplying(this, right, factory)
