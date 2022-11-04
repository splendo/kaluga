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

package com.splendo.kaluga.base.text

enum class Flag(val char: Char) {
    LEFT_JUSTIFY('-'),
    UPPERCASE('^'),
    ALTERNATE('#'),

    // numerics
    PLUS('+'),
    LEADING_SPACE(' '),
    ZERO_PAD('0'),
    GROUP(','),
    PARENTHESES('('),

    // indexing
    PREVIOUS('<');

    companion object {
        fun parse(stringToMatch: String): Set<Flag> {
            val f = mutableSetOf<Flag>()
            for (c in stringToMatch) {
                val v = parse(c)
                if (f.contains(v)) throw StringFormatterException.DuplicateFormatFlagsException(v.toString())
                f.add(v)
            }
            return f
        }

        // parse those flags which may be provided by users
        private fun parse(c: Char): Flag {
            return values().find { it.char == c } ?: throw StringFormatterException.UnknownFormatFlagsException(c.toString())
        }
    }
}

fun Set<Flag>.toString(): String {
    val sb = StringBuilder()
    this.forEach {
        sb.append(it.char)
    }
    return sb.toString()
}
