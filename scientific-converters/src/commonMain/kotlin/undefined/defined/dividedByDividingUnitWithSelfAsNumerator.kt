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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit

// A! / Div<Ex<A>, B> -> B

fun <
    NumeratorAndDenominatorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
    ExtendedDenominatorNumeratorUnit,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorNumeratorQuantity,
            >,
        ExtendedDenominatorNumeratorUnit,
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    DenominatorDenominatorValue : UndefinedScientificValue<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    > ScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorUnit>.dividedByDividingUnitWithSelfAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorQuantity,
                >,
            DenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, DenominatorDenominatorUnit) -> DenominatorDenominatorValue,
) where
        ExtendedDenominatorNumeratorUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorNumeratorQuantity,
            >,
        ExtendedDenominatorNumeratorUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorQuantity,
                >,
            > =
    right.unit.denominator.byDividing(this, right, factory)
