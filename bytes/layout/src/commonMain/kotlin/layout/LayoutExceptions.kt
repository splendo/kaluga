/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.layout

import kotlinx.serialization.SerializationException

/**
 * Thrown when a Flag is set that has already been reserved by a sibling node.
 */
class FlagIndexException(message: String) : SerializationException(message)

/**
 * Thrown when ByteOrder is changed in a sub-structure. Changing byte orders is only allowed for predictable types such as numeric types.
 */
class InvalidByteOrderException(message: String) : SerializationException(message)

/**
 * Thrown when byte stuffing is used in an unsupported position or configuration
 * (e.g. a nested field, a non-LSB byte order, or a scheme that cannot protect the delimiter).
 */
class UnsupportedByteStuffing(message: String) : SerializationException(message)

/**
 * Thrown when the byte discriminators of an enum or polymorphic type do not all have the same length.
 */
class InconsistentDiscriminatorLength(message: String) : SerializationException(message)

/**
 * Thrown when the subtypes of a size-polymorphic sealed type cannot be told apart by their byte size.
 */
class InvalidSizePolymorphic(message: String) : SerializationException(message)

/**
 * Thrown when an enum combines annotations that cannot apply to it, such as a checksum, or a prefix on a flag-packed enum.
 */
class InvalidEnumLayout(message: String) : SerializationException(message)

/**
 * Thrown when the numeric annotations on a property describe an encoding that cannot be represented,
 * such as a [NumericSettings.Decimal] with a 16-bit size.
 */
class InvalidNumericSettings(message: String) : SerializationException(message)

/**
 * Thrown when a [Char] is given an encoding without a fixed size, such as UTF-8.
 */
class UnsupportedCharEncoding(message: String) : SerializationException(message)

/**
 * Exception thrown if the [ByteArray] being decoded is smaller than its layout requires.
 */
class ByteArrayEndedBeforeSerializationCompleted(override val message: String) : SerializationException()

/**
 * Exception thrown if the [ByteArray] being decoded is bigger than its layout allows.
 */
class UnexpectedBytesRemaining(override val message: String) : SerializationException()

/**
 * Exception thrown when a terminator-marked collection has an item that starts with the terminator byte,
 * which would be indistinguishable from the end of the collection.
 */
class UnexpectedTerminator(override val message: String) : SerializationException()

/**
 * Exception thrown if the prefix of a structure does not match its configured prefix.
 */
class InvalidPrefix(override val message: String) : SerializationException()

/**
 * Exception thrown if the postfix of a structure does not match its configured postfix.
 */
class InvalidPostfix(override val message: String) : SerializationException()

/**
 * Exception thrown if the decoded checksum does not match the checksum computed over the body.
 */
class InvalidChecksumException(val expected: ULong, val actual: ULong) : SerializationException() {
    override val message: String = "Checksum expected $expected but got $actual"
}
