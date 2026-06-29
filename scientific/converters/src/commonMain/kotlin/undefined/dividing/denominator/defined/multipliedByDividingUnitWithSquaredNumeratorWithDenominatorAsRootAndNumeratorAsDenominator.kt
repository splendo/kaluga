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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.defined

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Div<A, Wr<B>> * Div<Mul<Ex<B>, Ex<B>>, A> -> B!

fun <
    LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
    LeftDenominatorAndRightNumeratorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftDenominatorUnit : DefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftAndRightQuantity>,
    WrappedLeftDenominatorUnit : WrappedUndefinedExtendedUnit<
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightDenominatorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        WrappedLeftDenominatorUnit,
        >,
    ExtendedRightNumeratorLeftUnit,
    ExtendedRightNumeratorRightUnit,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        RightNumeratorUnit,
        LeftNumeratorAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    LeftDenominatorValue : ScientificValue<LeftDenominatorAndRightNumeratorLeftAndRightQuantity, LeftDenominatorUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightDenominatorQuantity,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithDenominatorAsRootAndNumeratorAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                    >,
                >,
            LeftNumeratorAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    factory: (Decimal, LeftDenominatorUnit) -> LeftDenominatorValue,
) where
        ExtendedRightNumeratorLeftUnit : UndefinedExtendedUnit<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        ExtendedRightNumeratorRightUnit : UndefinedExtendedUnit<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                >,
            > =
    unit.denominator.wrapped.byMultiplying(this, right, factory)
