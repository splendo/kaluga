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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.extended.and.denominator.multiplying.squared

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

// Div<Mul<Ex<A>, Ex<A>>, Mul<B, B>> / A! -> Div<Ex<A>, Mul<B, B>>

fun <
    ExtendedNumeratorNumeratorLeftUnit,
    ExtendedNumeratorNumeratorRightUnit,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    NumeratorNumeratorLeftAndRightAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorQuantity>,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByNumeratorRoot(
    right: ScientificValue<NumeratorNumeratorLeftAndRightAndDenominatorQuantity, DenominatorUnit>,
    extendedNumeratorNumeratorLeftUnitPerNumeratorDenominatorUnit: ExtendedNumeratorNumeratorLeftUnit.(NumeratorDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedNumeratorNumeratorLeftUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        ExtendedNumeratorNumeratorRightUnit : UndefinedExtendedUnit<
            NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorNumeratorLeftAndRightAndDenominatorQuantity,
                >,
            > =
    unit.numerator.left.extendedNumeratorNumeratorLeftUnitPerNumeratorDenominatorUnit(
        unit.denominator,
    ).byDividing(this, right, factory)
