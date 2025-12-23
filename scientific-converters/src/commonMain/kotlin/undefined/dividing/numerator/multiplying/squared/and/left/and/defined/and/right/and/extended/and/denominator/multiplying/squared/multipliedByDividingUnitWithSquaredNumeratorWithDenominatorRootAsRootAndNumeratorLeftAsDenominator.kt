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
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// Div<Mul<Wr<A>, Ex<A>>, Mul<B, B>> * Div<Mul<B, B>, Ex<A>> -> A!

fun <
    LeftNumeratorLeftAndRightAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftNumeratorLeftUnit : DefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorQuantity>,
    WrappedLeftNumeratorLeftUnit : WrappedUndefinedExtendedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
        LeftNumeratorLeftUnit,
        >,
    ExtendedLeftNumeratorRightUnit,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        WrappedLeftNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedLeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
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
            LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    ExtendedRightDenominatorUnit,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedRightDenominatorUnit,
        >,
    LeftNumeratorLeftValue : ScientificValue<LeftNumeratorLeftAndRightAndRightDenominatorQuantity, LeftNumeratorLeftUnit>,
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
            LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithDenominatorRootAsRootAndNumeratorLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                LeftDenominatorLeftAndRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        RightUnit,
        >,
    factory: (Decimal, LeftNumeratorLeftUnit) -> LeftNumeratorLeftValue,
) where
        ExtendedLeftNumeratorRightUnit : UndefinedExtendedUnit<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedLeftNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            >,
        ExtendedRightDenominatorUnit : UndefinedExtendedUnit<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        ExtendedRightDenominatorUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                >,
            > =
    unit.numerator.left.wrapped.byMultiplying(this, right, factory)
