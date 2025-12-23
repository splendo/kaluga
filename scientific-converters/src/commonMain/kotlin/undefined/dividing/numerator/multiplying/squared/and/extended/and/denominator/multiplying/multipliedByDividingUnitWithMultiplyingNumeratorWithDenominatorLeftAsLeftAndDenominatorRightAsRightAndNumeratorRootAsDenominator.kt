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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.extended.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
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

// Div<Mul<Ex<A>, Ex<A>>, Mul<B, C>> * Div<Mul<B, C>, Wr<A>> -> A!

fun <
    ExtendedLeftNumeratorLeftUnit,
    ExtendedLeftNumeratorRightUnit,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedLeftNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedLeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
    LeftDenominatorRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightNumeratorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftQuantity,
            LeftDenominatorRightAndRightNumeratorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorRightAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    LeftNumeratorLeftAndRightAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightDenominatorUnit : DefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorQuantity>,
    WrappedRightDenominatorUnit : WrappedUndefinedExtendedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftQuantity,
            LeftDenominatorRightAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        WrappedRightDenominatorUnit,
        >,
    RightDenominatorValue : ScientificValue<LeftNumeratorLeftAndRightAndRightDenominatorQuantity, RightDenominatorUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftQuantity,
            LeftDenominatorRightAndRightNumeratorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorLeftAsLeftAndDenominatorRightAsRightAndNumeratorRootAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightNumeratorLeftQuantity,
                LeftDenominatorRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        RightUnit,
        >,
    factory: (Decimal, RightDenominatorUnit) -> RightDenominatorValue,
) where
        ExtendedLeftNumeratorLeftUnit : UndefinedExtendedUnit<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedLeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        ExtendedLeftNumeratorRightUnit : UndefinedExtendedUnit<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedLeftNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            > =
    right.unit.denominator.wrapped.byMultiplying(this, right, factory)
