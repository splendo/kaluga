/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.formatter

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.ScientificUnit

/**
 * Interface for formatting a [ScientificValue] to a String
 */
interface ScientificValueFormatter {

    /**
     * Formats a [ScientificValue] into a String
     * @param value the [ScientificValue] to format
     * @return the formatted text
     */
    fun format(value: ScientificValue<*, *>): String
}

/**
 * Gets the String representation of a [ScientificValue] using a [ScientificValueFormatter]
 * @param formatter the [ScientificValueFormatter] to use for formatting
 * @return the String representation of the [ScientificValue]
 */
fun ScientificValue<*, *>.toString(formatter: ScientificValueFormatter = CommonScientificValueFormatter.default): String = formatter.format(this)

internal class FormatterScientificValue<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>>(
    override val decimalValue: Decimal,
    override val unit: Unit,
) : ScientificValue<Quantity, Unit> {
    override val value: Number = decimalValue.toDouble()
}
