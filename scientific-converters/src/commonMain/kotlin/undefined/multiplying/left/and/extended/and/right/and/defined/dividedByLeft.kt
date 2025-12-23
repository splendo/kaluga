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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.left.and.extended.and.right.and.defined

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
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// Mul<Ex<A>, Wr<B>> / A! -> B!

fun <
    ExtendedNumeratorLeftUnit,
    NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorRightUnit : DefinedScientificUnit<NumeratorRightQuantity>,
    WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<
        NumeratorRightQuantity,
        NumeratorRightUnit,
        >,
    NumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorLeftAndDenominatorQuantity,
            >,
        ExtendedNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorRightQuantity,
            >,
        WrappedNumeratorRightUnit,
        >,
    NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
    NumeratorRightValue : ScientificValue<NumeratorRightQuantity, NumeratorRightUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Extended<
            NumeratorLeftAndDenominatorQuantity,
            >,
        UndefinedQuantityType.Extended<
            NumeratorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByLeft(
    right: ScientificValue<NumeratorLeftAndDenominatorQuantity, DenominatorUnit>,
    factory: (Decimal, NumeratorRightUnit) -> NumeratorRightValue,
) where
        ExtendedNumeratorLeftUnit : UndefinedExtendedUnit<
            NumeratorLeftAndDenominatorQuantity,
            >,
        ExtendedNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorLeftAndDenominatorQuantity,
                >,
            > =
    unit.right.wrapped.byDividing(this, right, factory)
