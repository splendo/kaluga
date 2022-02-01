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

import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlin.math.roundToLong

private typealias CustomFormatHandler = (Number) -> String
sealed class CommonFormatter(builder: Builder) : Formatter {
    class Builder {
        companion object {
            fun build(build: Builder.() -> Unit = {}) : Formatter {
                val builder = Builder()
                build(builder)
                return Buildable(builder)
            }
        }

        internal val customFormatters = mutableMapOf< ScientificUnit<*>, CustomFormatHandler>()
        fun ifUnitIs(unit: ScientificUnit<*>, format: CustomFormatHandler) {
            customFormatters[unit] = format
        }
    }

    private val customFormatters = builder.customFormatters

    override fun format(value: ScientificValue<*, *>) : String {
       val customFormatter = customFormatters[value.unit]
        return customFormatter?.let { it(value.value.pretty()) } ?: defaultFormat(value)
    }

    private fun defaultFormat(value: ScientificValue<*, *>) : String =
        "${value.value.pretty()} ${value.unit.symbol.withoutSpaces()}"

    /**
     *   The custom formatter with customisation applied using builder
     *   Is private as formatter of this type can be only created using builder
     */
    private class Buildable(builder: Builder) : CommonFormatter(builder)

    /**
     *   Default formatter with no customisation applied
     */
    object Default : CommonFormatter(Builder())
}

private fun Number.pretty() = toDouble().let {
    if (it.compareTo(it.roundToLong()) == 0) it.toLong() else this
}

private fun String.withoutSpaces() = filterNot(Char::isWhitespace)