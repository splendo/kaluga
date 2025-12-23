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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// A! / Div<B, Mul<Ex<A>, C>> -> Div<Mul<Mul<Wr<A>, Ex<A>>, C>, B>

fun <
    NumeratorAndDenominatorDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorDenominatorLeftQuantity>,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    ExtendedDenominatorDenominatorLeftUnit : UndefinedExtendedUnit<
        NumeratorAndDenominatorDenominatorLeftQuantity,
        >,
    DenominatorDenominatorRightQuantity : UndefinedQuantityType,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorLeftQuantity,
            >,
        ExtendedDenominatorDenominatorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorLeftQuantity,
                >,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
    NumeratorAndDenominatorDenominatorLeftQuantity,
    NumeratorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorLeftQuantity,
            >,
        WrappedNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorDenominatorLeftQuantity,
            >,
        ExtendedDenominatorDenominatorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorLeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorDenominatorLeftQuantity,
                >,
            >,
        TargetNumeratorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorLeftQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorLeftQuantity,
                    >,
                >,
            DenominatorDenominatorRightQuantity,
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
                    NumeratorAndDenominatorDenominatorLeftQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorLeftQuantity,
                    >,
                >,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorNumeratorQuantity,
        >,
    TargetUnit,
    >,
    > ScientificValue<NumeratorAndDenominatorDenominatorLeftQuantity, NumeratorUnit>.dividedByDividingUnitWithMultiplyingDenominatorWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorDenominatorLeftQuantity,
                    >,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXExtendedDenominatorDenominatorLeftUnit: WrappedNumeratorUnit.(ExtendedDenominatorDenominatorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXDenominatorDenominatorRightUnit: TargetNumeratorLeftUnit.(DenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorUnit: TargetNumeratorUnit.(DenominatorNumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorAsUndefined().wrappedNumeratorUnitXExtendedDenominatorDenominatorLeftUnit(
    right.unit.denominator.left,
).targetNumeratorLeftUnitXDenominatorDenominatorRightUnit(
    right.unit.denominator.right,
).targetNumeratorUnitPerDenominatorNumeratorUnit(
    right.unit.numerator,
).byDividing(this, right, factory)
