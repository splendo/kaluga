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
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// A! / Div<B, Mul<C, Ex<A>>> -> Div<Mul<Mul<Wr<A>, C>, Ex<A>>, B>

fun <
    NumeratorAndDenominatorDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorDenominatorRightQuantity>,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
    ExtendedDenominatorDenominatorRightUnit,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorRightQuantity,
            >,
        ExtendedDenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            DenominatorDenominatorLeftQuantity,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorDenominatorUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
        NumeratorAndDenominatorDenominatorRightQuantity,
        NumeratorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorRightQuantity,
            >,
        WrappedNumeratorUnit,
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorRightQuantity,
                >,
            DenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorRightQuantity,
            >,
        ExtendedDenominatorDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorRightQuantity,
                    >,
                DenominatorDenominatorLeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    UndefinedQuantityType.Extended<
                        NumeratorAndDenominatorDenominatorRightQuantity,
                        >,
                    DenominatorDenominatorLeftQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorRightQuantity,
                    >,
                >,
            DenominatorNumeratorQuantity,
            >,
        TargetUnit,
        >,
    > ScientificValue<NumeratorAndDenominatorDenominatorRightQuantity, NumeratorUnit>.dividedByDividingUnitWithMultiplyingDenominatorWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                DenominatorDenominatorLeftQuantity,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorRightQuantity,
                    >,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXDenominatorDenominatorLeftUnit: WrappedNumeratorUnit.(DenominatorDenominatorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXExtendedDenominatorDenominatorRightUnit: TargetNumeratorLeftUnit.(ExtendedDenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorUnit: TargetNumeratorUnit.(DenominatorNumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedDenominatorDenominatorRightUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorDenominatorRightQuantity,
            >,
        ExtendedDenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorRightQuantity,
                >,
            > =
    unit.numeratorAsUndefined().wrappedNumeratorUnitXDenominatorDenominatorLeftUnit(
        right.unit.denominator.left,
    ).targetNumeratorLeftUnitXExtendedDenominatorDenominatorRightUnit(
        right.unit.denominator.right,
    ).targetNumeratorUnitPerDenominatorNumeratorUnit(
        right.unit.numerator,
    ).byDividing(this, right, factory)
