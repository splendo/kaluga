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

import com.splendo.kaluga.bytes.annotations.Length

/**
 * The math of a delta-coded list, see [CollectionSettings.DeltaCoding]: its first item takes the item's `width` and
 * signedness, every further item a signed `step` from the one before it, added modulo 2 to the power of `width`.
 * Items are handled as the Long a number of `width` reads as, so an unsigned 16-bit item is `0..65535`.
 */
@InternalBytesApi
object DeltaCodec {

    /**
     * How many items [bytes] bytes of a delta-coded list hold: none for no bytes, else the first item and a step each.
     * @throws ByteArrayEndedBeforeSerializationCompleted when the bytes are not a first item and whole steps.
     */
    fun count(bytes: Int, width: Length, step: Length): Int {
        if (bytes == 0) return 0
        val steps = bytes - width.bytes
        if (steps < 0 || steps % step.bytes != 0) {
            throw ByteArrayEndedBeforeSerializationCompleted(
                "A delta-coded list of $bytes bytes is not a first item of ${width.bytes} bytes and whole steps of ${step.bytes}",
            )
        }
        return 1 + steps / step.bytes
    }

    /** The bytes a delta-coded list of [count] items takes. */
    fun size(count: Int, width: Length, step: Length): Int = if (count == 0) 0 else width.bytes + (count - 1) * step.bytes

    /** The item [step] after [previous], wrapped around into the range of an item. */
    fun next(previous: Long, step: Long, width: Length, signed: Boolean): Long = wrap(previous + step, width, signed)

    /**
     * What item [index], [value], is written as: the item itself for the first, else the step from [previous].
     * @throws IllegalArgumentException when [value] does not fit an item or the step to it does not fit [step].
     */
    fun encode(index: Int, previous: Long, value: Long, width: Length, signed: Boolean, step: Length): Long {
        require(width.fits(value, signed)) {
            "Item $index of a delta-coded list, $value, does not fit ${if (signed) "a signed" else "an unsigned"} ${width.bits}-bit item"
        }
        if (index == 0) return value
        // The step is the difference modulo the item's range, so an item may wrap around to the next.
        val difference = wrap(value - previous, width, signed = true)
        require(step.fits(difference, signed = true)) {
            "The step to item $index of a delta-coded list, from $previous to $value, is $difference, which does not fit a signed ${step.bits}-bit step"
        }
        return difference
    }

    private fun wrap(value: Long, width: Length, signed: Boolean): Long {
        val unused = Long.SIZE_BITS - width.bits
        return when {
            unused == 0 -> value
            signed -> (value shl unused) shr unused
            else -> value and (-1L ushr unused)
        }
    }

    private val Length.bits: Int get() = bytes * Byte.SIZE_BITS
}
