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

internal enum class RegularFormatCharacter(val char: Char) {
    // Byte, Short, Integer, Long
    DECIMAL_INTEGER('d'),
    OCTAL_INTEGER('o'),
    HEXADECIMAL_INTEGER('x'),
    HEXADECIMAL_INTEGER_UPPER('X'),

    // Float, Double
    SCIENTIFIC('e'),
    SCIENTIFIC_UPPER('E'),
    GENERAL('g'),
    GENERAL_UPPER('G'),
    DECIMAL_FLOAT('f'),
    HEXADECIMAL_FLOAT('a'),
    HEXADECIMAL_FLOAT_UPPER('A'),

    // Character, Byte, Short, Integer
    CHARACTER('c'),
    CHARACTER_UPPER('C'),

    // Calendar, long
    DATE_TIME('t'),
    DATE_TIME_UPPER('T'),

    // if (arg.TYPE != boolean) return boolean
    // if (arg != null) return true; else return false;
    BOOLEAN('b'),
    BOOLEAN_UPPER('B'),

    STRING('s'),
    STRING_IOS('@'),
    STRING_UPPER('S'),

    // arg.hashCode()
    HASHCODE('h'),
    HASHCODE_UPPER('H'),
    LINE_SEPARATOR('n'),
    PERCENT_SIGN('%'), ;

    companion object {
        internal fun parse(c: Char): RegularFormatCharacter =
            RegularFormatCharacter.values().find { it.char == c } ?: throw throw StringFormatterException.UnknownFormatConversionException(c.toString())
    }

    // Returns true if and only if the Conversion is applicable to all objects.
    fun isGeneral(): Boolean = when (this) {
        BOOLEAN, BOOLEAN_UPPER, STRING, STRING_UPPER, STRING_IOS, HASHCODE, HASHCODE_UPPER -> true
        else -> false
    }

    // Returns true if and only if the Conversion is applicable to character.
    fun isCharacter(): Boolean = when (this) {
        CHARACTER, CHARACTER_UPPER -> true
        else -> false
    }

    // Returns true if and only if the Conversion is an integer type.
    fun isInteger(): Boolean = when (this) {
        DECIMAL_INTEGER, OCTAL_INTEGER, HEXADECIMAL_INTEGER, HEXADECIMAL_INTEGER_UPPER -> true
        else -> false
    }

    // Returns true if and only if the Conversion is a floating-point type.
    fun isFloat(): Boolean = when (this) {
        SCIENTIFIC, SCIENTIFIC_UPPER, GENERAL, GENERAL_UPPER, DECIMAL_FLOAT, HEXADECIMAL_FLOAT, HEXADECIMAL_FLOAT_UPPER -> true
        else -> false
    }

    // Returns true if and only if the Conversion does not require an argument
    fun isText(): Boolean = when (this) {
        LINE_SEPARATOR, PERCENT_SIGN -> true
        else -> false
    }
}
