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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.extended

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

// Div<A, Ex<B>> / Div<A, Mul<Wr<B>, Ex<B>>> -> B!

fun <
    NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
    ExtendedNumeratorDenominatorUnit : UndefinedExtendedUnit<
        NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorNumeratorQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        ExtendedNumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
    NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorDenominatorLeftUnit : DefinedScientificUnit<NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity>,
    WrappedDenominatorDenominatorLeftUnit : WrappedUndefinedExtendedUnit<
    NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
    DenominatorDenominatorLeftUnit,
        >,
    ExtendedDenominatorDenominatorRightUnit : UndefinedExtendedUnit<
        NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
        >,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        WrappedDenominatorDenominatorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        ExtendedDenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorNumeratorQuantity,
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
    DenominatorDenominatorLeftValue : ScientificValue<NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity, DenominatorDenominatorLeftUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorNumeratorQuantity,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorAsNumeratorAndMultiplyingDenominatorWithDenominatorAsLeftAndDenominatorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorAndDenominatorNumeratorQuantity,
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
    factory: (Decimal, DenominatorDenominatorLeftUnit) -> DenominatorDenominatorLeftValue,
) = right.unit.denominator.left.wrapped.byDividing(this, right, factory)
