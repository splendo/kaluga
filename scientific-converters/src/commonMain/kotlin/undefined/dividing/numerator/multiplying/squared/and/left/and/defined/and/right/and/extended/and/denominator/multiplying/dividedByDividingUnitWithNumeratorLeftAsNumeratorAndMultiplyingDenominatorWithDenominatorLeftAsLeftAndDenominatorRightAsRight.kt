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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.left.and.defined.and.right.and.extended.and.denominator.multiplying

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

// Div<Mul<Wr<A>, Ex<A>>, Mul<B, C>> / Div<Ex<A>, Mul<B, C>> -> A!

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorNumeratorLeftUnit : DefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    WrappedNumeratorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<
    NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
    NumeratorNumeratorLeftUnit,
        >,
    ExtendedNumeratorNumeratorRightUnit : UndefinedExtendedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        >,
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
    NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
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
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    ExtendedDenominatorNumeratorUnit : UndefinedExtendedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        ExtendedDenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
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
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorLeftAsNumeratorAndMultiplyingDenominatorWithDenominatorLeftAsLeftAndDenominatorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
                NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorNumeratorLeftUnit) -> NumeratorNumeratorLeftValue,
) = unit.numerator.left.wrapped.byDividing(this, right, factory)
