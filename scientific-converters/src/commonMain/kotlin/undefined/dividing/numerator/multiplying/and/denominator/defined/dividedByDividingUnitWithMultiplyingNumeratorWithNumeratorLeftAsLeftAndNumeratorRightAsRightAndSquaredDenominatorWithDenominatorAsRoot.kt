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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.defined

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

// Div<Mul<A, B>, Wr<C>> / Div<Mul<A, B>, Mul<Ex<C>, Ex<C>>> -> C!

fun <
    NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
    NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorDenominatorUnit : DefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity>,
    WrappedNumeratorDenominatorUnit : WrappedUndefinedExtendedUnit<
    NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
    NumeratorDenominatorUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        WrappedNumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    ExtendedDenominatorDenominatorLeftUnit : UndefinedExtendedUnit<
        NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
        >,
    ExtendedDenominatorDenominatorRightUnit : UndefinedExtendedUnit<
        NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
        >,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        ExtendedDenominatorDenominatorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        ExtendedDenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorDenominatorUnit,
        >,
    NumeratorDenominatorValue : ScientificValue<NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity, NumeratorDenominatorUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithNumeratorLeftAsLeftAndNumeratorRightAsRightAndSquaredDenominatorWithDenominatorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity,
                NumeratorNumeratorRightAndDenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorDenominatorUnit) -> NumeratorDenominatorValue,
) = unit.denominator.wrapped.byDividing(this, right, factory)
