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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.right.and.extended

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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, Ex<B>>> * B! -> Inv<A>

fun <
    LeftReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftQuantity>,
    ExtendedLeftReciprocalRightUnit,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            LeftReciprocalRightAndRightQuantity,
            >,
        ExtendedLeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftReciprocalRightAndRightQuantity,
                >,
            >,
        LeftReciprocalUnit,
        >,
    LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<LeftReciprocalRightAndRightQuantity>,
    TargetUnit : UndefinedReciprocalUnit<
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftReciprocalLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftReciprocalRightAndRightQuantity,
                >,
            >,
        >,
    LeftUnit,
    >.multipliedByRight(
    right: ScientificValue<LeftReciprocalRightAndRightQuantity, RightUnit>,
    reciprocalTargetUnit: LeftReciprocalLeftUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedLeftReciprocalRightUnit : UndefinedExtendedUnit<
            LeftReciprocalRightAndRightQuantity,
            >,
        ExtendedLeftReciprocalRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftReciprocalRightAndRightQuantity,
                >,
            > =
    unit.inverse.left.reciprocalTargetUnit().byMultiplying(this, right, factory)
