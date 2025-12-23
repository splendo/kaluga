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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.left.and.defined.and.right.and.extended.and.denominator.multiplying.squared

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

// Div<Mul<Wr<A>, Ex<A>>, Mul<B, B>> / Div<Ex<A>, Mul<B, B>> -> A!

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorNumeratorLeftUnit : DefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    WrappedNumeratorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorLeftUnit,
        >,
    ExtendedNumeratorNumeratorRightUnit,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        WrappedNumeratorNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
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
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    ExtendedDenominatorNumeratorUnit,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedDenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    NumeratorNumeratorLeftValue : ScientificValue<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity, NumeratorNumeratorLeftUnit>,
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
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorLeftAsNumeratorAndSquaredDenominatorWithDenominatorRootAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorNumeratorLeftUnit) -> NumeratorNumeratorLeftValue,
) where
        ExtendedNumeratorNumeratorRightUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedNumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<
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
    unit.numerator.left.wrapped.byDividing(this, right, factory)
