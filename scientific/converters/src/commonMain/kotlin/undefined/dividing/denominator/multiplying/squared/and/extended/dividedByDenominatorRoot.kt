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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying.squared.and.extended

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
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Div<A, Mul<Ex<B>, Ex<B>>> / B! -> Div<A, Mul<Mul<Ex<B>, Ex<B>>, Wr<B>>>

fun <
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorQuantity>,
    ExtendedNumeratorDenominatorLeftUnit,
    ExtendedNumeratorDenominatorRightUnit,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorDenominatorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        NumeratorDenominatorUnit,
        >,
    NumeratorDenominatorLeftAndRightAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
        DenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        NumeratorDenominatorUnit,
        UndefinedQuantityType.Extended<
            NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    UndefinedQuantityType.Extended<
                        NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                        >,
                    UndefinedQuantityType.Extended<
                        NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                        >,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorQuantity,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        >,
    NumeratorUnit,
    >.dividedByDenominatorRoot(
    right: ScientificValue<NumeratorDenominatorLeftAndRightAndDenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    numeratorDenominatorUnitXWrappedDenominatorUnit: NumeratorDenominatorUnit.(WrappedDenominatorUnit) -> TargetDenominatorUnit,
    numeratorNumeratorUnitPerTargetDenominatorUnit: NumeratorNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedNumeratorDenominatorLeftUnit : UndefinedExtendedUnit<
            NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        ExtendedNumeratorDenominatorRightUnit : UndefinedExtendedUnit<
            NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorDenominatorLeftAndRightAndDenominatorQuantity,
                >,
            > =
    unit.numerator.numeratorNumeratorUnitPerTargetDenominatorUnit(
        unit.denominator.numeratorDenominatorUnitXWrappedDenominatorUnit(
            right.unit.denominatorAsUndefined(),
        ),
    ).byDividing(this, right, factory)
