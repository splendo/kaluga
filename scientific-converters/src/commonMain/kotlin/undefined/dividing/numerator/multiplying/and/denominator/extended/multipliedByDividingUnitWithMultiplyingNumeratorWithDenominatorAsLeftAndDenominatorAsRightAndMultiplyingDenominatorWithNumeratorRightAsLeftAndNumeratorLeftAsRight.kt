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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.extended

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

// Div<Mul<A, B>, Ex<C>> * Div<Mul<Wr<C>, Ex<C>>, Mul<B, A>> -> C!

fun <
    LeftNumeratorLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorRightQuantity>,
    LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightDenominatorLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    ExtendedLeftDenominatorUnit,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedLeftDenominatorUnit,
        >,
    LeftDenominatorAndRightNumeratorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightNumeratorLeftUnit : DefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftAndRightQuantity>,
    WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        >,
    ExtendedRightNumeratorRightUnit,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        WrappedRightNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorLeftAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
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
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightDenominatorLeftQuantity,
            LeftNumeratorLeftAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    RightNumeratorLeftValue : ScientificValue<LeftDenominatorAndRightNumeratorLeftAndRightQuantity, RightNumeratorLeftUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftQuantity,
            >,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorAsLeftAndDenominatorAsRightAndMultiplyingDenominatorWithNumeratorRightAsLeftAndNumeratorLeftAsRight(
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
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightDenominatorLeftQuantity,
                LeftNumeratorLeftAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    factory: (Decimal, RightNumeratorLeftUnit) -> RightNumeratorLeftValue,
) where
        ExtendedLeftDenominatorUnit : UndefinedExtendedUnit<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedLeftDenominatorUnit : AbstractUndefinedScientificUnit<
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
    right.unit.numerator.left.wrapped.byMultiplying(this, right, factory)
