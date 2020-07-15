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

package com.splendo.kaluga.base.text

sealed class StringFormatterException(message: String?) : Exception(message) {
    data class MissingFormatArgumentException(val formatSpecified: String) : StringFormatterException("Format specifier '$formatSpecified'")
    data class MissingFormatWidthException(val specification: String) : StringFormatterException("The specification, $specification, misses a required width.")
    data class UnknownFormatConversionException(val conversion: String) : StringFormatterException("Conversion = '$conversion'")
    data class UnexpectedChar(val currentCharacter: Char) : StringFormatterException("Unexpected Char $currentCharacter")
    data class DuplicateFormatFlagsException(val flags: String) : StringFormatterException("Flags = '$flags'")
    data class UnknownFormatFlagsException(val flags: String) : StringFormatterException("Flags = '$flags'")
    data class NumberFormatException(val exception: kotlin.NumberFormatException) : StringFormatterException(exception.message)
    data class IllegalFormatWidthException(val width: Int) : StringFormatterException("$width")
    data class IllegalFormatPrecisionException(val precision: Int) : StringFormatterException("$precision")
    data class IllegalFormatConversionException(val currentChar: Char, val any: Any) : StringFormatterException("$currentChar != $any")
    data class IllegalFormatFlagsException(val flags: String) : StringFormatterException("An illegal set of flags, $flags, was supplied.")
    data class UnsupportedFormat(val format: String) : StringFormatterException("$format not Supported")
    data class MalformedValue(val value: Any) : StringFormatterException("Malformed Value: $value")
    data class FormatFlagsConversionMismatchException(val flags: String, val currentChar: Char) : StringFormatterException("Conversion = $currentChar, Flags = $flags")
}
