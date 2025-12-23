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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.right.and.extended

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
import kotlin.jvm.JvmName

// Mul<A, Ex<B>> / B! -> A

fun <
    NumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftQuantity>,
    ExtendedNumeratorRightUnit,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftQuantity,
        NumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorRightUnit,
        >,
    NumeratorRightAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
    NumeratorLeftValue : UndefinedScientificValue<
        NumeratorLeftQuantity,
        NumeratorLeftUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftQuantity,
        UndefinedQuantityType.Extended<
            NumeratorRightAndDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByRight(
    right: ScientificValue<NumeratorRightAndDenominatorQuantity, DenominatorUnit>,
    factory: (Decimal, NumeratorLeftUnit) -> NumeratorLeftValue,
) where
        ExtendedNumeratorRightUnit : UndefinedExtendedUnit<
            NumeratorRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorRightAndDenominatorQuantity,
                >,
            > =
    unit.left.byDividing(this, right, factory)
