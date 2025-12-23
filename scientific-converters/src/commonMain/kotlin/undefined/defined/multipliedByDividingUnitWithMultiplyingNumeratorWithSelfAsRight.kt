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

// A! * Div<Mul<B, Ex<A>>, C> -> Div<Mul<Mul<Wr<A>, B>, Ex<A>>, C>

fun <
    LeftAndRightNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftAndRightNumeratorRightQuantity>,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    ExtendedRightNumeratorRightUnit,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorRightQuantity,
                >,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftAndRightNumeratorRightQuantity,
        LeftUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorRightQuantity,
            >,
        WrappedLeftUnit,
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorRightQuantity,
                >,
            RightNumeratorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorRightQuantity,
                    >,
                RightNumeratorLeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    UndefinedQuantityType.Extended<
                        LeftAndRightNumeratorRightQuantity,
                        >,
                    RightNumeratorLeftQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorRightQuantity,
                    >,
                >,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftAndRightNumeratorRightQuantity, LeftUnit>.multipliedByDividingUnitWithMultiplyingNumeratorWithSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorRightQuantity,
                    >,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitXRightNumeratorLeftUnit: WrappedLeftUnit.(RightNumeratorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXExtendedRightNumeratorRightUnit: TargetNumeratorLeftUnit.(ExtendedRightNumeratorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedRightNumeratorRightUnit : UndefinedExtendedUnit<
            LeftAndRightNumeratorRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorRightQuantity,
                >,
            > =
    unit.leftAsUndefined().wrappedLeftUnitXRightNumeratorLeftUnit(
        right.unit.numerator.left,
    ).targetNumeratorLeftUnitXExtendedRightNumeratorRightUnit(
        right.unit.numerator.right,
    ).targetNumeratorUnitPerRightDenominatorUnit(
        right.unit.denominator,
    ).byMultiplying(this, right, factory)
