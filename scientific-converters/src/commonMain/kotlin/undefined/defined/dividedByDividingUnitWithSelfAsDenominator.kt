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
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// A! / Div<B, Ex<A>> -> Div<Mul<Wr<A>, Ex<A>>, B>

fun <
    NumeratorAndDenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorDenominatorQuantity>,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    ExtendedDenominatorDenominatorUnit : UndefinedExtendedUnit<
        NumeratorAndDenominatorDenominatorQuantity,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorQuantity,
            >,
        ExtendedDenominatorDenominatorUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
    NumeratorAndDenominatorDenominatorQuantity,
    NumeratorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorQuantity,
            >,
        WrappedNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorQuantity,
            >,
        ExtendedDenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorQuantity,
                >,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorQuantity,
                >,
            >,
        DenominatorNumeratorQuantity,
        >,
    TargetUnit,
    >,
    > ScientificValue<NumeratorAndDenominatorDenominatorQuantity, NumeratorUnit>.dividedByDividingUnitWithSelfAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXExtendedDenominatorDenominatorUnit: WrappedNumeratorUnit.(ExtendedDenominatorDenominatorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorUnit: TargetNumeratorUnit.(DenominatorNumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorAsUndefined().wrappedNumeratorUnitXExtendedDenominatorDenominatorUnit(
    right.unit.denominator,
).targetNumeratorUnitPerDenominatorNumeratorUnit(
    right.unit.numerator,
).byDividing(this, right, factory)
