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
import kotlin.math.roundToLong

class CommonFormatter private constructor(builder: Builder) : Formatter {
    class Builder private constructor() {
        companion object {
            fun build(build: Builder.() -> Unit = {}) : Formatter {
                val builder = Builder()
                build(builder)
                return CommonFormatter(builder)
            }
        }

        val addedFormatters : Array<Formatter> = arrayOf()
        fun append() {}
    }

    companion object {
        val default = Builder.build()
    }

    init {

    }

    override fun format(value: ScientificValue<*, *>): String = value.value.toDouble().let {
        if (it.compareTo(it.roundToLong()) == 0) it.toLong() else value.value
    }.let {
        "$it ${value.unit.symbol.withoutSpaces()}"
    }
}

fun String.withoutSpaces() = filterNot { it.isWhitespace() }