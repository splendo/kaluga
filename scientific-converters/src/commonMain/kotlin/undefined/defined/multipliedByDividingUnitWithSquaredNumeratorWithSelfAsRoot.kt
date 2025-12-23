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

// A! * Div<Mul<Ex<A>, Ex<A>>, B> -> Div<Mul<Mul<Wr<A>, Ex<A>>, Ex<A>>, B>

fun <
    LeftAndRightNumeratorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftAndRightNumeratorLeftAndRightQuantity>,
    ExtendedRightNumeratorLeftUnit,
    ExtendedRightNumeratorRightUnit,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftAndRightNumeratorLeftAndRightQuantity,
        LeftUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        WrappedLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
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
                        LeftAndRightNumeratorLeftAndRightQuantity,
                        >,
                    UndefinedQuantityType.Extended<
                        LeftAndRightNumeratorLeftAndRightQuantity,
                        >,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                >,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftAndRightNumeratorLeftAndRightQuantity, LeftUnit>.multipliedByDividingUnitWithSquaredNumeratorWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitXExtendedRightNumeratorLeftUnit: WrappedLeftUnit.(ExtendedRightNumeratorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXExtendedRightNumeratorLeftUnit: TargetNumeratorLeftUnit.(ExtendedRightNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedRightNumeratorLeftUnit : UndefinedExtendedUnit<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        ExtendedRightNumeratorRightUnit : UndefinedExtendedUnit<
            LeftAndRightNumeratorLeftAndRightQuantity,
            >,
        ExtendedRightNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightNumeratorLeftAndRightQuantity,
                >,
            > =
    unit.leftAsUndefined().wrappedLeftUnitXExtendedRightNumeratorLeftUnit(
        right.unit.numerator.left,
    ).targetNumeratorLeftUnitXExtendedRightNumeratorLeftUnit(
        right.unit.numerator.left,
    ).targetNumeratorUnitPerRightDenominatorUnit(
        right.unit.denominator,
    ).byMultiplying(this, right, factory)
