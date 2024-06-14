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

import co.touchlab.stately.concurrency.AtomicInt
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value

/**
 * ByteArrayWriter can hold (by append new bytes) [Byte]s in buffer
 *
 * New data can be appended using [append] method
 * Current buffer can be read using [toByteArray] method
 */
sealed class ByteArrayWriter {

    protected val currentByteIndex = AtomicInt(0)

    /** @return Written data size */
    val size get() = currentByteIndex.value

    /** Appends given [byteArray] into buffer */
    abstract fun append(byteArray: ByteArray)
    /** Converts current buffer into [ByteArray] */
    abstract fun toByteArray(): ByteArray

    /** Fixed size [ByteArrayWriter], can hold up to [bufferSize] bytes */
    class Fixed(val bufferSize: Int) : ByteArrayWriter() {

        private val buffer = ByteArray(bufferSize)

        override fun append(byteArray: ByteArray) {
            if (byteArray.isEmpty()) return

            byteArray.copyInto(
                buffer,
                destinationOffset = currentByteIndex.value,
                startIndex = 0,
                endIndex = byteArray.size
            )
            currentByteIndex.addAndGet(byteArray.size)
        }
        override fun toByteArray() = buffer.copyOf(currentByteIndex.value)
    }

    /** Dynamic size [ByteArrayWriter], will be resized or shrunk automatically */
    class Dynamic(initialBufferSize: Int = 32) : ByteArrayWriter() {

        private val buffer = AtomicReference(ByteArray(initialBufferSize))

        override fun append(byteArray: ByteArray) {
            if (byteArray.isEmpty()) return
            ensureCapacity(byteArray.size)

            val newBuffer = ByteArray(buffer.value.size)
            buffer.value.copyInto(newBuffer)
            byteArray.copyInto(
                newBuffer,
                destinationOffset = currentByteIndex.value,
                startIndex = 0,
                endIndex = byteArray.size
            )
            buffer.value = newBuffer
            currentByteIndex.addAndGet(byteArray.size)
        }

        override fun toByteArray() = buffer.value.copyOf(currentByteIndex.value)

        /**
         * Removes first [size] bytes from the buffer
         * The rest bytes will be shifted to the beginning of the buffer
         * */
        fun removeFirst(size: Int): ByteArray {
            if (size == 0) return ByteArray(0)
            val copy = ByteArray(size)
            buffer.value.copyInto(copy, startIndex = 0, endIndex = size)
            val newSize = (currentByteIndex.value - size).takeHighestOneBit() shl 1
            val newBuffer = ByteArray(newSize)
            buffer.value.copyInto(newBuffer, startIndex = size, endIndex = currentByteIndex.value)
            buffer.value = newBuffer
            currentByteIndex.addAndGet(-size)
            return copy
        }

        private fun ensureCapacity(appendSize: Int) {
            if (currentByteIndex.value + appendSize <= buffer.value.size) return
            // Double buffer each time
            val newSize = (currentByteIndex.value + appendSize).takeHighestOneBit() shl 1
            val newBuffer = ByteArray(newSize)
            buffer.value.copyInto(newBuffer)
            buffer.value = newBuffer
        }
    }
}
