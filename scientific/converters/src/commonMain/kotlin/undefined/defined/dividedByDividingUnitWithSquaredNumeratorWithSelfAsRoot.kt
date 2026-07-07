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

package com.splendo.kaluga.scientific.converter.undefined.defined

import com.splendo.kaluga.base.decimal.Decimal
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

// A! / Div<Mul<Ex<A>, Ex<A>>, B> -> Div<B, Ex<A>>

fun <
    NumeratorAndDenominatorNumeratorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorNumeratorLeftAndRightQuantity>,
    ExtendedDenominatorNumeratorLeftUnit,
    ExtendedDenominatorNumeratorRightUnit,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        ExtendedDenominatorNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        ExtendedDenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        DenominatorNumeratorUnit,
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        ExtendedDenominatorNumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorDenominatorQuantity,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<NumeratorAndDenominatorNumeratorLeftAndRightQuantity, NumeratorUnit>.dividedByDividingUnitWithSquaredNumeratorWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                >,
            DenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    denominatorDenominatorUnitPerExtendedDenominatorNumeratorLeftUnit: DenominatorDenominatorUnit.(ExtendedDenominatorNumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedDenominatorNumeratorLeftUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        ExtendedDenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        ExtendedDenominatorNumeratorRightUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        ExtendedDenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            > =
    right.unit.denominator.denominatorDenominatorUnitPerExtendedDenominatorNumeratorLeftUnit(
        right.unit.numerator.left,
    ).byDividing(this, right, factory)
