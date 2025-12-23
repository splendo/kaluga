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

package com.splendo.kaluga.scientific.converter.undefined.dimensionless

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// One / A! -> Inv<Wr<A>>

fun <
    NumeratorUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<DenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
    DenominatorQuantity,
    DenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Extended<
            DenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Extended<
            DenominatorQuantity,
            >,
        >,
    TargetUnit,
    >,
    > ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.dividedByDefinedUnit(
    right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    reciprocalTargetUnit: WrappedDenominatorUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominatorAsUndefined().reciprocalTargetUnit().byDividing(this, right, factory)
