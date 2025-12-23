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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Mul<A, A> / Div<Mul<A, A>, Wr<B>> -> B!

fun <
    NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorRightUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorDenominatorUnit : DefinedScientificUnit<DenominatorDenominatorQuantity>,
    WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Extended<
            DenominatorDenominatorQuantity,
            >,
        WrappedDenominatorDenominatorUnit,
        >,
    DenominatorDenominatorValue : ScientificValue<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSelfAsNumeratorAndDefinedDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                DenominatorDenominatorQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, DenominatorDenominatorUnit) -> DenominatorDenominatorValue,
) = right.unit.denominator.wrapped.byDividing(this, right, factory)
