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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying.squared.and.extended

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

// Div<Mul<A, A>, Mul<Ex<B>, Ex<B>>> * B! -> Div<Mul<A, A>, Ex<B>>

fun <
    LeftNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    ExtendedLeftDenominatorLeftUnit,
    ExtendedLeftDenominatorRightUnit,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftDenominatorLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftDenominatorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            >,
        LeftDenominatorUnit,
        >,
    LeftDenominatorLeftAndRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<LeftDenominatorLeftAndRightAndRightQuantity>,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            >,
        >,
    LeftUnit,
    >.multipliedByDenominatorRoot(
    right: ScientificValue<LeftDenominatorLeftAndRightAndRightQuantity, RightUnit>,
    leftNumeratorUnitPerExtendedLeftDenominatorLeftUnit: LeftNumeratorUnit.(ExtendedLeftDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedLeftDenominatorLeftUnit : UndefinedExtendedUnit<
            LeftDenominatorLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            >,
        ExtendedLeftDenominatorRightUnit : UndefinedExtendedUnit<
            LeftDenominatorLeftAndRightAndRightQuantity,
            >,
        ExtendedLeftDenominatorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftDenominatorLeftAndRightAndRightQuantity,
                >,
            > =
    unit.numerator.leftNumeratorUnitPerExtendedLeftDenominatorLeftUnit(
        unit.denominator.left,
    ).byMultiplying(this, right, factory)
