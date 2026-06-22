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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared.and.extended

import com.splendo.kaluga.base.decimal.Decimal
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

// Mul<Ex<A>, Ex<A>> * A! -> Mul<Mul<Ex<A>, Ex<A>>, Wr<A>>

fun <
    ExtendedLeftLeftUnit,
    ExtendedLeftRightUnit,
    LeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftLeftUnit,
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftRightUnit,
        >,
    LeftLeftAndRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<LeftLeftAndRightAndRightQuantity>,
    WrappedRightUnit : WrappedUndefinedExtendedUnit<
        LeftLeftAndRightAndRightQuantity,
        RightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightQuantity,
                >,
            >,
        LeftUnit,
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightQuantity,
            >,
        WrappedRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftLeftAndRightAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftLeftAndRightAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightQuantity,
            >,
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByRoot(
    right: ScientificValue<LeftLeftAndRightAndRightQuantity, RightUnit>,
    rightAsUndefined: RightUnit.() -> WrappedRightUnit,
    leftUnitXWrappedRightUnit: LeftUnit.(WrappedRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedLeftLeftUnit : UndefinedExtendedUnit<
            LeftLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightQuantity,
                >,
            >,
        ExtendedLeftRightUnit : UndefinedExtendedUnit<
            LeftLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightQuantity,
                >,
            > =
    unit.leftUnitXWrappedRightUnit(
        right.unit.rightAsUndefined(),
    ).byMultiplying(this, right, factory)
