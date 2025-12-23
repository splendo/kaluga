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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.left.and.defined

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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// Mul<Wr<A>, B> * Inv<B> -> A!

fun <
    LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftLeftUnit : DefinedScientificUnit<LeftLeftQuantity>,
    WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<
    LeftLeftQuantity,
    LeftLeftUnit,
        >,
    LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftLeftQuantity,
            >,
        WrappedLeftLeftUnit,
        LeftRightAndRightReciprocalQuantity,
        LeftRightUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftRightAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    LeftLeftValue : ScientificValue<LeftLeftQuantity, LeftLeftUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Extended<
            LeftLeftQuantity,
            >,
        LeftRightAndRightReciprocalQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftRightAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    factory: (Decimal, LeftLeftUnit) -> LeftLeftValue,
) = unit.left.wrapped.byMultiplying(this, right, factory)
