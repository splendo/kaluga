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

internal object Conversion {
    // Byte, Short, Integer, Long (and associated primitives due to autoboxing)
    const val DECIMAL_INTEGER = 'd'
    const val OCTAL_INTEGER = 'o'
    const val HEXADECIMAL_INTEGER = 'x'
    const val HEXADECIMAL_INTEGER_UPPER = 'X'

    // Float, Double (and associated primitives due to autoboxing)
    const val SCIENTIFIC = 'e'
    const val SCIENTIFIC_UPPER = 'E'
    const val GENERAL = 'g'
    const val GENERAL_UPPER = 'G'
    const val DECIMAL_FLOAT = 'f'
    const val HEXADECIMAL_FLOAT = 'a'
    const val HEXADECIMAL_FLOAT_UPPER = 'A'

    // Character, Byte, Short, Integer (and associated primitives due to autoboxing)
    const val CHARACTER = 'c'
    const val CHARACTER_UPPER = 'C'

    // Calendar, long
    const val DATE_TIME = 't'
    const val DATE_TIME_UPPER = 'T'

    // if (arg.TYPE != boolean) return boolean
    // if (arg != null) return true; else return false;
    const val BOOLEAN = 'b'
    const val BOOLEAN_UPPER = 'B'

    // if (arg instanceof Formattable) arg.formatTo()
    // else arg.toString();
    const val STRING = 's'
    const val STRING_UPPER = 'S'

    // arg.hashCode()
    const val HASHCODE = 'h'
    const val HASHCODE_UPPER = 'H'
    const val LINE_SEPARATOR = 'n'
    const val PERCENT_SIGN = '%'
    fun isValid(c: Char): Boolean {
        return (isGeneral(c) || isInteger(c) || isFloat(c) || isText(c) || c == 't' || isCharacter(c))
    }

    // Returns true iff the Conversion is applicable to all objects.
    fun isGeneral(c: Char): Boolean {
        return when (c) {
            BOOLEAN, BOOLEAN_UPPER, STRING, STRING_UPPER, HASHCODE, HASHCODE_UPPER -> true
            else -> false
        }
    }

    // Returns true iff the Conversion is applicable to character.
    fun isCharacter(c: Char): Boolean {
        return when (c) {
            CHARACTER, CHARACTER_UPPER -> true
            else -> false
        }
    }

    // Returns true iff the Conversion is an integer type.
    fun isInteger(c: Char): Boolean {
        return when (c) {
            DECIMAL_INTEGER, OCTAL_INTEGER, HEXADECIMAL_INTEGER, HEXADECIMAL_INTEGER_UPPER -> true
            else -> false
        }
    }

    // Returns true iff the Conversion is a floating-point type.
    fun isFloat(c: Char): Boolean {
        return when (c) {
            SCIENTIFIC, SCIENTIFIC_UPPER, GENERAL, GENERAL_UPPER, DECIMAL_FLOAT, HEXADECIMAL_FLOAT, HEXADECIMAL_FLOAT_UPPER -> true
            else -> false
        }
    }

    // Returns true iff the Conversion does not require an argument
    fun isText(c: Char): Boolean {
        return when (c) {
            LINE_SEPARATOR, PERCENT_SIGN -> true
            else -> false
        }
    }
}
