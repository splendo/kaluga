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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.left.and.extended.and.right.and.defined

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

// Div<Mul<Ex<A>, Wr<A>>, B> / Div<Ex<A>, B> -> A!

fun <
    ExtendedNumeratorNumeratorLeftUnit,
    NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorNumeratorRightUnit : DefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    WrappedNumeratorNumeratorRightUnit : WrappedUndefinedExtendedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        WrappedNumeratorNumeratorRightUnit,
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
    ExtendedDenominatorNumeratorUnit,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedDenominatorNumeratorUnit,
        NumeratorDenominatorAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    NumeratorNumeratorRightValue : ScientificValue<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightUnit>,
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
    >.dividedByDividingUnitWithNumeratorLeftAsNumeratorAndDenominatorAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            NumeratorDenominatorAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorNumeratorRightUnit) -> NumeratorNumeratorRightValue,
) where
        ExtendedNumeratorNumeratorLeftUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            >,
        ExtendedDenominatorNumeratorUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedDenominatorNumeratorUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            > =
    unit.numerator.right.wrapped.byDividing(this, right, factory)
