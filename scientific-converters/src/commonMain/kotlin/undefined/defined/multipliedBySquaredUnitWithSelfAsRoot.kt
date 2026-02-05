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

// A! * Mul<Ex<A>, Ex<A>> -> Mul<Mul<Wr<A>, Ex<A>>, Ex<A>>

fun <
    LeftAndRightLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftAndRightLeftAndRightQuantity>,
    ExtendedRightLeftUnit,
    ExtendedRightRightUnit,
    RightUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightLeftAndRightQuantity,
            >,
        ExtendedRightLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightLeftAndRightQuantity,
            >,
        ExtendedRightRightUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftAndRightLeftAndRightQuantity,
        LeftUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightLeftAndRightQuantity,
            >,
        WrappedLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightLeftAndRightQuantity,
            >,
        ExtendedRightLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            >,
        TargetLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightLeftAndRightQuantity,
            >,
        ExtendedRightLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightLeftAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftAndRightLeftAndRightQuantity, LeftUnit>.multipliedBySquaredUnitWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitXExtendedRightLeftUnit: WrappedLeftUnit.(ExtendedRightLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXExtendedRightLeftUnit: TargetLeftUnit.(ExtendedRightLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedRightLeftUnit : UndefinedExtendedUnit<
            LeftAndRightLeftAndRightQuantity,
            >,
        ExtendedRightLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            >,
        ExtendedRightRightUnit : UndefinedExtendedUnit<
            LeftAndRightLeftAndRightQuantity,
            >,
        ExtendedRightRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightLeftAndRightQuantity,
                >,
            > =
    unit.leftAsUndefined().wrappedLeftUnitXExtendedRightLeftUnit(
        right.unit.left,
    ).targetLeftUnitXExtendedRightLeftUnit(
        right.unit.left,
    ).byMultiplying(this, right, factory)
