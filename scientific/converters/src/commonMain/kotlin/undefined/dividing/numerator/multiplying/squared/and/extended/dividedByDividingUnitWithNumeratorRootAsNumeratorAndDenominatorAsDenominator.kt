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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.extended

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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Div<Mul<Ex<A>, Ex<A>>, B> / Div<Wr<A>, B> -> A!

fun <
    ExtendedNumeratorNumeratorLeftUnit,
    ExtendedNumeratorNumeratorRightUnit,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorNumeratorUnit : DefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    WrappedDenominatorNumeratorUnit : WrappedUndefinedExtendedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        WrappedDenominatorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    DenominatorNumeratorValue : ScientificValue<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            >,
        NumeratorDenominatorAndDenominatorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorRootAsNumeratorAndDenominatorAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            NumeratorDenominatorAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, DenominatorNumeratorUnit) -> DenominatorNumeratorValue,
) where
        ExtendedNumeratorNumeratorLeftUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            >,
        ExtendedNumeratorNumeratorRightUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            > =
    right.unit.numerator.wrapped.byDividing(this, right, factory)
