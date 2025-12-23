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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared.and.extended

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

// Mul<Ex<A>, Ex<A>> / A! -> Ex<A>

fun <
    ExtendedNumeratorLeftUnit,
    ExtendedNumeratorRightUnit,
    NumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorRightUnit,
        >,
    NumeratorLeftAndRightAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorLeftAndRightAndDenominatorQuantity>,
    ExtendedNumeratorLeftValue : UndefinedScientificValue<
        UndefinedQuantityType.Extended<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorLeftUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Extended<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        UndefinedQuantityType.Extended<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByRoot(
    right: ScientificValue<NumeratorLeftAndRightAndDenominatorQuantity, DenominatorUnit>,
    factory: (Decimal, ExtendedNumeratorLeftUnit) -> ExtendedNumeratorLeftValue,
) where
        ExtendedNumeratorLeftUnit : UndefinedExtendedUnit<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorLeftAndRightAndDenominatorQuantity,
                >,
            >,
        ExtendedNumeratorRightUnit : UndefinedExtendedUnit<
            NumeratorLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorLeftAndRightAndDenominatorQuantity,
                >,
            > =
    unit.left.byDividing(this, right, factory)
