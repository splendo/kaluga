/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.resources

interface KalugaFile {

    companion object {
        val NEW_LINE_REGEX = Regex("\\r\\n|\\n|\\r")
        const val PATH_SEPARATOR = "/"
    }

    interface Path {
        val filepath: String
    }

    sealed class Exception(override val message: String?) : RuntimeException(message) {
        data class CantOpenFile(val filepath: String) : Exception(
            message = "File couldn't be opened at: ($filepath)"
        )
        data class CantReadFile(val filepath: String) : Exception(
            message = "File couldn't be read at: ($filepath)"
        )
        data class UnsupportedMode(val mode: Set<Mode>) : Exception(
            message = "Given mode ($mode) is not supported!"
        )
    }

    enum class Mode {
        Read, Write, Binary;

        companion object {
            val ReadOnly = setOf(Read)
            val ReadBinary = setOf(Read, Binary)
        }
    }

    enum class Encoding {
        UTF8
    }

    val path: Path
    val mode: Set<Mode>

    /** Opens file in [mode] mode */
    fun open()
    /** Returns next line in the file */
    fun readLines(encoding: Encoding): List<String>
    /** Returns next byte in the file */
    fun readBytes(): ByteArray
    /** Closes file */
    fun close()
}

fun Set<KalugaFile.Mode>.canRead() = contains(KalugaFile.Mode.Read)
fun Set<KalugaFile.Mode>.canWrite() = contains(KalugaFile.Mode.Write)
fun Set<KalugaFile.Mode>.canUpdate() = canRead() && canWrite()
fun Set<KalugaFile.Mode>.isBinary() = contains(KalugaFile.Mode.Binary)

expect class DefaultFilePath(
    name: String,
    path: String
) : KalugaFile.Path

expect class DefaultKalugaFile(
    path: KalugaFile.Path,
    mode: Set<KalugaFile.Mode>
) : KalugaFile

private fun <T> sequence(next: () -> T?) = Sequence {
    iterator {
        while (true) {
            next()?.let { yield(it) } ?: break
        }
    }
}

/** Returns sequence of the lines from the file */
fun KalugaFile.lineSequence(
    encoding: KalugaFile.Encoding = KalugaFile.Encoding.UTF8
) = readLines(encoding).asSequence()

/** Returns sequence of the bytes from the file */
fun KalugaFile.byteSequence() = readBytes().asSequence()

private fun <T, R> KalugaFile.useElements(sequence: () -> Sequence<T>, block: (Sequence<T>) -> R): R {
    open()
    return block(sequence()).also { close() }
}

/** Use lines with automatic open file on start and close file at the end */
fun <T> KalugaFile.useLines(block: (Sequence<String>) -> T) = useElements(
    sequence = this::lineSequence,
    block = block
)

/** Use bytes with automatic open file on start and close file at the end */
fun <T> KalugaFile.useBytes(block: (Sequence<Byte>) -> T) = useElements(
    sequence = this::byteSequence,
    block = block
)
