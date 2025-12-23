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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.left.and.extended

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// Mul<Ex<A>, B> * A! -> Mul<Mul<Ex<A>, B>, Wr<A>>

fun <
    ExtendedLeftLeftUnit,
    LeftRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightQuantity,
            >,
        ExtendedLeftLeftUnit,
        LeftRightQuantity,
        LeftRightUnit,
        >,
    LeftLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<LeftLeftAndRightQuantity>,
    WrappedRightUnit : WrappedUndefinedExtendedUnit<
        LeftLeftAndRightQuantity,
        RightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightQuantity,
                >,
            LeftRightQuantity,
            >,
        LeftUnit,
        UndefinedQuantityType.Extended<
            LeftLeftAndRightQuantity,
            >,
        WrappedRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftLeftAndRightQuantity,
                    >,
                LeftRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightQuantity,
            >,
        LeftRightQuantity,
        >,
    LeftUnit,
    >.multipliedByLeft(
    right: ScientificValue<LeftLeftAndRightQuantity, RightUnit>,
    rightAsUndefined: RightUnit.() -> WrappedRightUnit,
    leftUnitXWrappedRightUnit: LeftUnit.(WrappedRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedLeftLeftUnit : UndefinedExtendedUnit<
            LeftLeftAndRightQuantity,
            >,
        ExtendedLeftLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightQuantity,
                >,
            > =
    unit.leftUnitXWrappedRightUnit(
        right.unit.rightAsUndefined(),
    ).byMultiplying(this, right, factory)
