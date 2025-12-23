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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// A / B! -> Div<A, Wr<B>>

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<DenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
    DenominatorQuantity,
    DenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorQuantity,
        NumeratorUnit,
        UndefinedQuantityType.Extended<
            DenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorQuantity,
        UndefinedQuantityType.Extended<
            DenominatorQuantity,
            >,
        >,
    TargetUnit,
    >,
    > UndefinedScientificValue<
    NumeratorQuantity,
    NumeratorUnit,
    >.dividedByDefinedUnit(
    right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    numeratorUnitPerWrappedDenominatorUnit: NumeratorUnit.(WrappedDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitPerWrappedDenominatorUnit(
    right.unit.denominatorAsUndefined(),
).byDividing(this, right, factory)
