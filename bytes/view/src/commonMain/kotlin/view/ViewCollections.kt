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

import com.splendo.kaluga.bytes.layout.InternalBytesApi
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.decodeUShort
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.UnexpectedTerminator

/** The end of an item frame starting at `s`, as the generated item accessors compute it. */
@InternalBytesApi
fun interface ItemEnd {
    fun end(b: ByteArray, s: Int, lim: Int): Int
}

/** Reads the item frame starting at `s`. */
@InternalBytesApi
fun interface ItemRead<out T> {
    fun read(b: ByteArray, s: Int, lim: Int): T
}

/** Rewrites the item frame starting at `s` in place. */
@InternalBytesApi
fun interface ItemWrite<in T> {
    fun write(b: ByteArray, s: Int, lim: Int, value: T)
}

/** Visits the item frame of [frame] (the key or value index for a map) starting at `s`. */
@InternalBytesApi
fun interface ItemVisit {
    fun visit(frame: Int, s: Int)
}

/**
 * The collections of generated views, following how `BytesFormat` decodes them.
 *
 * A collection is a run of item frames (for a map, key and value frames in turn) starting at `first`. Its `count` is
 * the number of items (or entries) when the layout records one; [UNBOUNDED] means the items run until a terminator
 * byte, or until the limit when there is none.
 *
 * In a frame accumulated MOST_SIGNIFICANT_FIRST (`mirrored`), positions are the ends of what they point at, items run
 * downwards from `first` and `lim` is the lowest position they may reach.
 */
@InternalBytesApi
object ViewCollections {

    /** The count of a collection without a recorded count. */
    const val UNBOUNDED: Int = -1

    /** The count a length prefix at [pos] records, read as `BytesFormat` reads it: a one byte count is unsigned. */
    fun lengthPrefixCount(b: ByteArray, pos: Int, lim: Int, marking: StringEncodingSettings.LengthPrefix, order: ByteOrder): Int = when (marking) {
        is StringEncodingSettings.LengthPrefix.ByteLength -> ViewBytes.u8(b, pos, lim)

        is StringEncodingSettings.LengthPrefix.ShortLength -> ushort(b, pos, lim, order)

        is StringEncodingSettings.LengthPrefix.WithOverflow -> if (isByte(b, pos, marking.sentinel, lim)) {
            ushort(b, pos + 1, lim, order)
        } else {
            ViewBytes.u8(b, pos, lim)
        }
    }

    /** [lengthPrefixCount] of the length prefix ending at [pos] in a mirrored frame. */
    fun lengthPrefixCountDown(b: ByteArray, pos: Int, lim: Int, marking: StringEncodingSettings.LengthPrefix, order: ByteOrder): Int = when (marking) {
        is StringEncodingSettings.LengthPrefix.ByteLength -> ViewBytes.u8(b, pos - 1, pos)

        is StringEncodingSettings.LengthPrefix.ShortLength -> ushort(b, pos - 2, pos, order)

        is StringEncodingSettings.LengthPrefix.WithOverflow -> if (isByte(b, pos - 1, marking.sentinel, pos)) {
            ushort(b, pos - 3, pos - 1, order)
        } else {
            ViewBytes.u8(b, pos - 1, pos)
        }
    }.also { ViewBytes.checkRange(lim, pos - lim, b.size) }

    /** [lengthPrefixSize] of the length prefix ending at [pos] in a mirrored frame. */
    fun lengthPrefixSizeDown(b: ByteArray, pos: Int, marking: StringEncodingSettings.LengthPrefix): Int = when (marking) {
        is StringEncodingSettings.LengthPrefix.ByteLength -> 1
        is StringEncodingSettings.LengthPrefix.ShortLength -> 2
        is StringEncodingSettings.LengthPrefix.WithOverflow -> if (isByte(b, pos - 1, marking.sentinel, pos)) 3 else 1
    }

    /** [writeLengthPrefix] ending at [pos] in a mirrored frame. */
    fun writeLengthPrefixDown(b: ByteArray, pos: Int, count: Int, marking: StringEncodingSettings.LengthPrefix, order: ByteOrder): Int {
        val size = marking.expectedByteSize(count.toUInt())
        ViewBytes.checkRange(pos - size, size, pos)
        marking.copyEncodedSizeInto(b, count.toUInt(), order, pos - size)
        return size
    }

    /** The bytes the length prefix at [pos] takes. */
    fun lengthPrefixSize(b: ByteArray, pos: Int, lim: Int, marking: StringEncodingSettings.LengthPrefix): Int = when (marking) {
        is StringEncodingSettings.LengthPrefix.ByteLength -> 1
        is StringEncodingSettings.LengthPrefix.ShortLength -> 2
        is StringEncodingSettings.LengthPrefix.WithOverflow -> if (isByte(b, pos, marking.sentinel, lim)) 3 else 1
    }

    /**
     * Writes a length prefix for [count] items at [pos] and returns its size.
     * @throws IllegalArgumentException, as `BytesFormat` does, when [count] does not fit.
     */
    fun writeLengthPrefix(b: ByteArray, pos: Int, lim: Int, count: Int, marking: StringEncodingSettings.LengthPrefix, order: ByteOrder): Int {
        val size = marking.expectedByteSize(count.toUInt())
        ViewBytes.checkRange(pos, size, lim)
        // The sentinel goes first, where the decoder looks for it, whatever order the count itself is written in.
        if (marking is StringEncodingSettings.LengthPrefix.WithOverflow && size > 1) {
            b[pos] = marking.sentinel
            StringEncodingSettings.LengthPrefix.ShortLength.copyEncodedSizeInto(b, count.toUInt(), order, pos + 1)
        } else {
            marking.copyEncodedSizeInto(b, count.toUInt(), order, pos)
        }
        return size
    }

    /**
     * Throws [UnexpectedTerminator] as `BytesFormat` does when the item written at [pos] starts with [terminator],
     * which would read as the end of the collection.
     */
    fun checkNotTerminator(b: ByteArray, pos: Int, end: Int, terminator: Byte, index: Int) {
        if (end > pos && b[pos] == terminator) throw UnexpectedTerminator("The element at $index starts with the terminator byte in a terminated List")
    }

    /**
     * The end of the collection whose items start at [first], after its terminator when it has one. Each item is an
     * [item] frame, followed by a [value] frame for a map.
     */
    fun end(b: ByteArray, first: Int, lim: Int, count: Int, terminator: Byte?, item: ItemEnd, value: ItemEnd? = null): Int =
        end(b, first, lim, count, terminator, false, item, value)

    /** [end] in a mirrored frame: the lowest position the collection reaches. */
    fun endDown(b: ByteArray, first: Int, lim: Int, count: Int, terminator: Byte?, item: ItemEnd, value: ItemEnd? = null): Int =
        end(b, first, lim, count, terminator, true, item, value)

    private fun end(b: ByteArray, first: Int, lim: Int, count: Int, terminator: Byte?, mirrored: Boolean, item: ItemEnd, value: ItemEnd?): Int {
        var pos = first
        var index = 0
        while (hasNext(b, pos, lim, count, terminator, index, mirrored)) {
            pos = itemEnd(item, b, pos, lim, mirrored)
            if (value != null) pos = itemEnd(value, b, pos, lim, mirrored)
            index++
        }
        val step = if (mirrored) -1 else 1
        return if (count < 0 && terminator != null) pos + step else pos
    }

    /** Visits every item frame starting at [first], in order, the way [end] walks them: frame 0 is the item or key, 1 the value. */
    fun forEach(b: ByteArray, first: Int, lim: Int, count: Int, terminator: Byte?, item: ItemEnd, value: ItemEnd?, mirrored: Boolean, visit: ItemVisit) {
        var pos = first
        var index = 0
        while (hasNext(b, pos, lim, count, terminator, index, mirrored)) {
            visit.visit(0, pos)
            pos = itemEnd(item, b, pos, lim, mirrored)
            if (value != null) {
                visit.visit(1, pos)
                pos = itemEnd(value, b, pos, lim, mirrored)
            }
            index++
        }
    }

    /** The items starting at [first], read lazily: an item's offset is found the first time it, or one after it, is read. */
    fun <T> list(b: ByteArray, first: Int, lim: Int, count: Int, terminator: Byte?, end: ItemEnd, read: ItemRead<T>, mirrored: Boolean = false): List<T> =
        ScannedList(b, first, lim, count, terminator, end, read, mirrored)

    /** The entries starting at [first], decoded eagerly as `BytesFormat` decodes a map: a later duplicate key replaces an earlier one. */
    fun <K, V> map(
        b: ByteArray,
        first: Int,
        lim: Int,
        count: Int,
        terminator: Byte?,
        keyEnd: ItemEnd,
        key: ItemRead<K>,
        valueEnd: ItemEnd,
        value: ItemRead<V>,
        mirrored: Boolean = false,
    ): Map<K, V> {
        val result = LinkedHashMap<K, V>()
        var pos = first
        var index = 0
        while (hasNext(b, pos, lim, count, terminator, index, mirrored)) {
            val k = key.read(b, pos, lim)
            pos = itemEnd(keyEnd, b, pos, lim, mirrored)
            result[k] = value.read(b, pos, lim)
            pos = itemEnd(valueEnd, b, pos, lim, mirrored)
            index++
        }
        return result
    }

    internal fun hasNext(b: ByteArray, pos: Int, lim: Int, count: Int, terminator: Byte?, index: Int, mirrored: Boolean = false): Boolean = when {
        count >= 0 -> index < count
        mirrored && terminator != null -> !(pos > lim && b[pos - 1] == terminator)
        mirrored -> pos > lim
        terminator != null -> !(pos < lim && b[pos] == terminator)
        else -> pos < lim
    }

    // An item running past the limit cannot be read, as the decoder finds when it reads it.
    internal fun itemEnd(end: ItemEnd, b: ByteArray, pos: Int, lim: Int, mirrored: Boolean = false): Int = end.end(b, pos, lim).also {
        if (!mirrored) {
            ViewBytes.checkRange(pos, it - pos, lim)
        } else if (it < lim || it > pos) {
            throw ByteArrayEndedBeforeSerializationCompleted("Not enough bytes remaining: an item from $pos down to $it passes $lim")
        }
    }

    // Whether the byte at [pos] is [byte]; `false` when it is not within [lim].
    private fun isByte(b: ByteArray, pos: Int, byte: Byte, lim: Int): Boolean = pos in 0 until lim && b[pos] == byte

    private fun ushort(b: ByteArray, pos: Int, lim: Int, order: ByteOrder): Int {
        ViewBytes.checkRange(pos, 2, lim)
        return b.decodeUShort(pos, order).toInt()
    }
}

/**
 * The items of a view collection, each read from its bytes on access. Offsets are found by scanning items in order,
 * once, as far as the highest index read so far; [size] scans them all when the layout records no count.
 */
class ScannedList<out T> internal constructor(
    private val b: ByteArray,
    first: Int,
    private val lim: Int,
    private val count: Int,
    private val terminator: Byte?,
    private val end: ItemEnd,
    private val read: ItemRead<T>,
    private val mirrored: Boolean = false,
) : AbstractList<T>() {

    // Grown as items are scanned, so a long collection costs nothing until it is read that far.
    private var offsets = IntArray(if (count in 1..7) count + 1 else 8).also { it[0] = first }
    private var scanned = 0
    private var complete = false

    override val size: Int get() = if (count >= 0) count else scanAll()

    override fun get(index: Int): T {
        if (index < 0 || (count >= 0 && index >= count)) throw IndexOutOfBoundsException("Index $index is not in 0 until $size")
        while (scanned <= index) {
            if (!scanNext()) throw IndexOutOfBoundsException("Index $index is not in 0 until $scanned")
        }
        return read.read(b, offsets[index], lim)
    }

    private fun scanAll(): Int {
        while (scanNext()) Unit
        return scanned
    }

    // Finds the start of the item after the last one scanned, returning false when there is none.
    private fun scanNext(): Boolean {
        if (complete) return false
        val pos = offsets[scanned]
        if (!ViewCollections.hasNext(b, pos, lim, count, terminator, scanned, mirrored)) {
            complete = true
            return false
        }
        if (scanned + 1 >= offsets.size) offsets = offsets.copyOf(offsets.size * 2)
        offsets[scanned + 1] = ViewCollections.itemEnd(end, b, pos, lim, mirrored)
        scanned++
        return true
    }
}

/**
 * The [count] items of a view collection whose items all take [stride] bytes, starting at [first], read and written
 * in place.
 */
class FixedStrideList<T> internal constructor(
    private val b: ByteArray,
    private val first: Int,
    private val lim: Int,
    private val count: Int,
    private val stride: Int,
    private val read: ItemRead<T>,
    private val write: ItemWrite<T>?,
    private val onChange: ViewParent?,
    private val mirrored: Boolean,
) : AbstractList<T>() {

    // In a mirrored frame item frames run downwards and are addressed by their ends.
    private fun at(index: Int): Int = if (mirrored) first - index * stride else first + index * stride

    override val size: Int get() = count

    override fun get(index: Int): T {
        checkIndex(index)
        return read.read(b, at(index), lim)
    }

    /**
     * Rewrites the item at [index] in place and returns the item it replaces.
     * @throws UnsupportedOperationException when the items cannot be rewritten in place.
     */
    operator fun set(index: Int, element: T): T {
        val writer = write ?: throw UnsupportedOperationException("These items cannot be rewritten in place")
        val previous = get(index)
        writer.write(b, at(index), lim, element)
        onChange?.changed()
        return previous
    }

    private fun checkIndex(index: Int) {
        if (index !in 0 until count) throw IndexOutOfBoundsException("Index $index is not in 0 until $count")
    }

    companion object {
        /**
         * A list of [count] items of [stride] bytes from [first], checked to lie within [lim]; [onChange] is told
         * about every item [set].
         */
        @InternalBytesApi
        fun <T> of(
            b: ByteArray,
            first: Int,
            lim: Int,
            count: Int,
            stride: Int,
            read: ItemRead<T>,
            write: ItemWrite<T>? = null,
            onChange: ViewParent? = null,
            mirrored: Boolean = false,
        ): FixedStrideList<T> {
            if (mirrored) {
                ViewBytes.checkRange(first - count * stride, count * stride, first)
                if (first - count * stride <
                    lim
                ) {
                    throw ByteArrayEndedBeforeSerializationCompleted("Not enough bytes remaining: $count items of $stride bytes before $first pass $lim")
                }
            } else {
                ViewBytes.checkRange(first, count * stride, lim)
            }
            return FixedStrideList(b, first, lim, count, stride, read, write, onChange, mirrored)
        }
    }
}
