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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.right.and.extended

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

// Div<Mul<A, Ex<B>>, C> * B! -> Div<Mul<Mul<A, Ex<B>>, Wr<B>>, C>

fun <
    LeftNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftQuantity>,
    ExtendedLeftNumeratorRightUnit : UndefinedExtendedUnit<
        LeftNumeratorRightAndRightQuantity,
        >,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftNumeratorRightAndRightQuantity,
            >,
        ExtendedLeftNumeratorRightUnit,
        >,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftNumeratorRightAndRightQuantity,
                >,
            >,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    LeftNumeratorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<LeftNumeratorRightAndRightQuantity>,
    WrappedRightUnit : WrappedUndefinedExtendedUnit<
    LeftNumeratorRightAndRightQuantity,
    RightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftNumeratorRightAndRightQuantity,
                >,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftNumeratorRightAndRightQuantity,
            >,
        WrappedRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftQuantity,
                UndefinedQuantityType.Extended<
                    LeftNumeratorRightAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                LeftNumeratorRightAndRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftQuantity,
                UndefinedQuantityType.Extended<
                    LeftNumeratorRightAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                LeftNumeratorRightAndRightQuantity,
                >,
            >,
        LeftDenominatorQuantity,
        >,
    TargetUnit,
    >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            UndefinedQuantityType.Extended<
                LeftNumeratorRightAndRightQuantity,
                >,
            >,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByNumeratorRight(
    right: ScientificValue<LeftNumeratorRightAndRightQuantity, RightUnit>,
    rightAsUndefined: RightUnit.() -> WrappedRightUnit,
    leftNumeratorUnitXWrappedRightUnit: LeftNumeratorUnit.(WrappedRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorUnit: TargetNumeratorUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXWrappedRightUnit(
    right.unit.rightAsUndefined(),
).targetNumeratorUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
