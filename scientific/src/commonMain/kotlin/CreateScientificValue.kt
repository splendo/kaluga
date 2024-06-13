/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit

/**
 * Creates a [DefaultScientificValue] of this number using a given [AbstractScientificUnit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [AbstractScientificUnit] the value should represents
 * @param unit the [Unit] of the [DefaultScientificValue] to be created
 * @return the created [DefaultScientificValue]
 */
operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Number.invoke(unit: Unit) = this.toDecimal()(unit)

/**
 * Creates a [Value] of this number using a given [unit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] the value should represents
 * @param Value the type of [ScientificValue] to return
 * @param unit the [Unit] of the [DefaultScientificValue] to be created
 * @param factory a method for creating a [Value] using a Decimal value and a [Unit]
 * @return the created [Value]
 */
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Number.invoke(unit: Unit, factory: (Decimal, Unit) -> Value) = this.toDecimal()(unit, factory)

/**
 * Creates a [DefaultScientificValue] of this [Decimal] using a given [AbstractScientificUnit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [AbstractScientificUnit] the value should represents
 * @param unit the [Unit] of the [DefaultScientificValue] to be created
 * @return the created [DefaultScientificValue]
 */
operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Decimal.invoke(unit: Unit) = this(unit, ::DefaultScientificValue)

/**
 * Creates a [Value] of this [Decimal] using a given [unit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] the value should represents
 * @param Value the type of [ScientificValue] to return
 * @param unit the [Unit] of the [DefaultScientificValue] to be created
 * @param factory a method for creating a [Value] using a Decimal value and a [Unit]
 * @return the created [Value]
 */
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Decimal.invoke(unit: Unit, factory: (Decimal, Unit) -> Value) = factory(this, unit)

operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    > Number.invoke(unit: Unit) = this.toDecimal()(unit)

operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    Value : UndefinedScientificValue<Quantity, Unit>,
    > Number.invoke(unit: Unit, factory: (Decimal, Unit) -> Value) = this.toDecimal()(unit, factory)

operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    > Decimal.invoke(unit: Unit) = this(unit, ::DefaultUndefinedScientificValue)
