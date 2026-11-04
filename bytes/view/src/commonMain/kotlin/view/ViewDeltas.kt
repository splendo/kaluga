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

package com.splendo.kaluga.bytes.view

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.layout.DeltaCodec
import com.splendo.kaluga.bytes.layout.InternalBytesApi
import com.splendo.kaluga.bytes.layout.NumericCodec

/**
 * The delta-coded lists of generated views, following how `BytesFormat` encodes a `@DeltaCoded` list: its first item
 * of `width` bytes at `first`, then a signed step of `step` bytes per further item, up to `lim`.
 */
@InternalBytesApi
object ViewDeltas {

    /**
     * Where the delta-coded list at [first] ends, which is [lim].
     * @throws com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted as `BytesFormat` does when
     * the bytes up to [lim] are not a first item and whole steps.
     */
    fun end(first: Int, lim: Int, width: Length, step: Length): Int {
        ViewBytes.checkRange(first, 0, lim)
        DeltaCodec.count(lim - first, width, step)
        return lim
    }

    /** The bytes a delta-coded list of [count] items takes. */
    fun sizeOf(count: Int, width: Length, step: Length): Int = DeltaCodec.size(count, width, step)

    /**
     * Writes item [index], [value], of a delta-coded list at [pos]: the item itself for the first, else the step to
     * it from [previous]. Returns the bytes written.
     * @throws IllegalArgumentException as `BytesFormat` does when the item or the step to it does not fit.
     */
    fun write(b: ByteArray, pos: Int, lim: Int, index: Int, previous: Long, value: Long, width: Length, signed: Boolean, step: Length, order: ByteOrder): Int {
        val written = DeltaCodec.encode(index, previous, value, width, signed, step)
        val length = if (index == 0) width else step
        ViewBytes.writeNatural(b, pos, written, length, index > 0 || signed, order, lim)
        return length.bytes
    }

    /** The `Short` items of the delta-coded list from [first] to [lim]. */
    fun shorts(b: ByteArray, first: Int, lim: Int, width: Length, signed: Boolean, step: Length, order: ByteOrder): DeltaCodedShortList =
        DeltaCodedShortList(b, first, count(first, lim, width, step), width, signed, step, order)

    /** The `Int` items of the delta-coded list from [first] to [lim]. */
    fun ints(b: ByteArray, first: Int, lim: Int, width: Length, signed: Boolean, step: Length, order: ByteOrder): DeltaCodedIntList =
        DeltaCodedIntList(b, first, count(first, lim, width, step), width, signed, step, order)

    private fun count(first: Int, lim: Int, width: Length, step: Length): Int {
        ViewBytes.checkRange(first, 0, lim)
        return DeltaCodec.count(lim - first, width, step)
    }
}

/**
 * The items of a delta-coded view list, read from its bytes: the first item, then a step to every next one. Its
 * [size] follows from the bytes alone, but an item is only known from the one before it, so [get] adds up every step
 * before its index and takes time in proportion to it. Read the items in order instead, with an [iterator], or with
 * the `forEachShort`, `forEachInt` and `copyInto` of [DeltaCodedShortList] and [DeltaCodedIntList], which box none of them.
 */
sealed class DeltaCodedList<T>(
    @PublishedApi internal val b: ByteArray,
    @PublishedApi internal val first: Int,
    final override val size: Int,
    @PublishedApi internal val width: Length,
    @PublishedApi internal val signed: Boolean,
    @PublishedApi internal val step: Length,
    @PublishedApi internal val order: ByteOrder,
) : AbstractList<T>() {

    /** Item [index] as the Long an item reads as, from [previous], the item before it, which the first item ignores. */
    @PublishedApi
    internal fun next(index: Int, previous: Long): Long = if (index == 0) {
        NumericCodec.readNatural(b, first, width, signed, order)
    } else {
        val difference = NumericCodec.readNatural(b, first + width.bytes + (index - 1) * step.bytes, step, true, order)
        DeltaCodec.next(previous, difference, width, signed)
    }

    /** [value] as the type of an item. */
    protected abstract fun item(value: Long): T

    /** Adds up the steps up to [index]. */
    override fun get(index: Int): T {
        if (index !in 0 until size) throw IndexOutOfBoundsException("Index $index is not in 0 until $size")
        var value = 0L
        for (at in 0..index) value = next(at, value)
        return item(value)
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var index = 0
        private var value = 0L

        override fun hasNext(): Boolean = index < size

        override fun next(): T {
            if (index >= size) throw NoSuchElementException("A delta-coded list of $size items has no item $index")
            value = this@DeltaCodedList.next(index++, value)
            return item(value)
        }
    }
}

/** A [DeltaCodedList] of `Short` items. */
class DeltaCodedShortList internal constructor(b: ByteArray, first: Int, size: Int, width: Length, signed: Boolean, step: Length, order: ByteOrder) :
    DeltaCodedList<Short>(b, first, size, width, signed, step, order) {

    override fun item(value: Long): Short = value.toShort()

    /** Calls [action] with every item in order, reading each step once and boxing none. */
    inline fun forEachShort(action: (Short) -> Unit) {
        var value = 0L
        for (index in 0 until size) {
            value = next(index, value)
            action(value.toShort())
        }
    }

    /** Copies every item into [destination] from [destinationOffset] on, and returns [destination]. */
    fun copyInto(destination: ShortArray, destinationOffset: Int = 0): ShortArray {
        if (destinationOffset < 0 || destinationOffset > destination.size - size) {
            throw IndexOutOfBoundsException("$size items from $destinationOffset do not fit an array of ${destination.size}")
        }
        var index = destinationOffset
        forEachShort { destination[index++] = it }
        return destination
    }

    /** The items as a new array. */
    fun toShortArray(): ShortArray = copyInto(ShortArray(size))
}

/** A [DeltaCodedList] of `Int` items. */
class DeltaCodedIntList internal constructor(b: ByteArray, first: Int, size: Int, width: Length, signed: Boolean, step: Length, order: ByteOrder) :
    DeltaCodedList<Int>(b, first, size, width, signed, step, order) {

    override fun item(value: Long): Int = value.toInt()

    /** Calls [action] with every item in order, reading each step once and boxing none. */
    inline fun forEachInt(action: (Int) -> Unit) {
        var value = 0L
        for (index in 0 until size) {
            value = next(index, value)
            action(value.toInt())
        }
    }

    /** Copies every item into [destination] from [destinationOffset] on, and returns [destination]. */
    fun copyInto(destination: IntArray, destinationOffset: Int = 0): IntArray {
        if (destinationOffset < 0 || destinationOffset > destination.size - size) {
            throw IndexOutOfBoundsException("$size items from $destinationOffset do not fit an array of ${destination.size}")
        }
        var index = destinationOffset
        forEachInt { destination[index++] = it }
        return destination
    }

    /** The items as a new array. */
    fun toIntArray(): IntArray = copyInto(IntArray(size))
}
