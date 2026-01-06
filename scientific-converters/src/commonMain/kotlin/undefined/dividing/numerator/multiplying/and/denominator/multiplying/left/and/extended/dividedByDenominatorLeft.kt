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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying.left.and.extended

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
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Div<Mul<A, B>, Mul<Ex<C>, D>> / C! -> Div<Mul<A, B>, Mul<Mul<Ex<C>, D>, Wr<C>>>

fun <
    NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
    NumeratorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    ExtendedNumeratorDenominatorLeftUnit,
    NumeratorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorDenominatorLeftAndDenominatorQuantity,
            >,
        ExtendedNumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndDenominatorQuantity,
                >,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    NumeratorDenominatorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
        NumeratorDenominatorLeftAndDenominatorQuantity,
        DenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndDenominatorQuantity,
                >,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorLeftAndDenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorLeftAndDenominatorQuantity,
                    >,
                NumeratorDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndDenominatorQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftQuantity,
                NumeratorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    UndefinedQuantityType.Extended<
                        NumeratorDenominatorLeftAndDenominatorQuantity,
                        >,
                    NumeratorDenominatorRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorLeftAndDenominatorQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndDenominatorQuantity,
                >,
            NumeratorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDenominatorLeft(
    right: ScientificValue<NumeratorDenominatorLeftAndDenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    numeratorDenominatorUnitXWrappedDenominatorUnit: NumeratorDenominatorUnit.(WrappedDenominatorUnit) -> TargetDenominatorUnit,
    numeratorNumeratorUnitPerTargetDenominatorUnit: NumeratorNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedNumeratorDenominatorLeftUnit : UndefinedExtendedUnit<
            NumeratorDenominatorLeftAndDenominatorQuantity,
            >,
        ExtendedNumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndDenominatorQuantity,
                >,
            > =
    unit.numerator.numeratorNumeratorUnitPerTargetDenominatorUnit(
        unit.denominator.numeratorDenominatorUnitXWrappedDenominatorUnit(
            right.unit.denominatorAsUndefined(),
        ),
    ).byDividing(this, right, factory)
