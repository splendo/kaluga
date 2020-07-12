/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.resources.strings

internal class Flags(private var flags: Int) {

    fun valueOf(): Int {
        return flags
    }

    operator fun contains(f: Flags): Boolean {
        return flags and f.valueOf() == f.valueOf()
    }

    fun dup(): Flags {
        return Flags(flags)
    }

    internal fun add(f: Flags): Flags {
        flags = flags or f.valueOf()
        return this
    }

    fun remove(f: Flags): Flags {
        flags = flags and f.valueOf().inv()
        return this
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (contains(LEFT_JUSTIFY)) sb.append('-')
        if (contains(UPPERCASE)) sb.append('^')
        if (contains(ALTERNATE)) sb.append('#')
        if (contains(PLUS)) sb.append('+')
        if (contains(LEADING_SPACE)) sb.append(' ')
        if (contains(ZERO_PAD)) sb.append('0')
        if (contains(GROUP)) sb.append(',')
        if (contains(PARENTHESES)) sb.append('(')
        if (contains(PREVIOUS)) sb.append('<')
        return sb.toString()
    }

    companion object {
        val NONE = Flags(0) // ''

        // duplicate declarations from Formattable.java
        val LEFT_JUSTIFY = Flags(1 shl 0) // '-'
        val UPPERCASE = Flags(1 shl 1) // '^'
        val ALTERNATE = Flags(1 shl 2) // '#'

        // numerics
        val PLUS = Flags(1 shl 3) // '+'
        val LEADING_SPACE = Flags(1 shl 4) // ' '
        val ZERO_PAD = Flags(1 shl 5) // '0'
        val GROUP = Flags(1 shl 6) // ','
        val PARENTHESES = Flags(1 shl 7) // '('

        // indexing
        val PREVIOUS = Flags(1 shl 8) // '<'
        fun parse(stringToMatch: String): Flags {
            val f = Flags(0)
            for (c in stringToMatch) {
                val v = parse(c)
                if (f.contains(v)) throw StringFormatterException.DuplicateFormatFlagsException(v.toString())
                f.add(v)
            }
            return f
        }

        // parse those flags which may be provided by users
        private fun parse(c: Char): Flags {
            return when (c) {
                '-' -> LEFT_JUSTIFY
                '#' -> ALTERNATE
                '+' -> PLUS
                ' ' -> LEADING_SPACE
                '0' -> ZERO_PAD
                ',' -> GROUP
                '(' -> PARENTHESES
                '<' -> PREVIOUS
                else -> throw StringFormatterException.UnknownFormatFlagsException(c.toString())
            }
        }

        // Returns a string representation of the current {@code Flags}.
        fun toString(f: Flags): String {
            return f.toString()
        }
    }
}
