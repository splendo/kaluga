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

import com.splendo.kaluga.logging.debug
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.NSString
import platform.Foundation.closeFile
import platform.Foundation.fileHandleForReadingAtPath
import platform.Foundation.fileHandleForUpdatingAtPath
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.readDataToEndOfFile
import platform.Foundation.stringWithUTF8String

actual class DefaultFilePath actual constructor(
    name: String,
    path: String
) : KalugaFile.Path {
    override val filepath = NSBundle.mainBundle.pathForResource(name, ofType = "") ?: ""
}

actual class DefaultKalugaFile actual constructor(
    override val path: KalugaFile.Path,
    override val mode: Set<KalugaFile.Mode>
) : KalugaFile {

    private companion object {
        const val TAG = "DefaultKalugaFile"
    }

    private var file: NSFileHandle? = null
    private var data: NSData? = null
    private var byteIterator: ByteIterator? = null
    private var lineIterator: Iterator<String>? = null

    override fun open() {

        file = when {
            mode.canUpdate() -> NSFileHandle.fileHandleForUpdatingAtPath(path.filepath)
            mode.canWrite() -> NSFileHandle.fileHandleForWritingAtPath(path.filepath)
            mode.canRead() -> NSFileHandle.fileHandleForReadingAtPath(path.filepath)
            else -> throw KalugaFile.Exception.UnsupportedMode(mode)
        }

        if (file == null) {
            throw KalugaFile.Exception.CantOpenFile(path)
        }
    }

    override fun nextLine(): String? {
        readIfNeeded()
        val iterator = lineIterator ?: return null
        if (!iterator.hasNext()) return null
        return iterator.next()
    }

    override fun nextByte(): Byte? {
        readIfNeeded()
        val iterator = byteIterator ?: return null
        if (!iterator.hasNext()) return null
        return iterator.next()
    }

    override fun close() {
        if (file != null) {
            file?.closeFile()
        } else {
            // already closed or never opened
            debug(TAG, "Already closed: $path")
        }
    }

    private fun readIfNeeded() {
        if (data == null) {
            val file = file ?: throw KalugaFile.Exception.CantOpenFile(path)
            data = file.readDataToEndOfFile()
            if (data != null) {
                val bytes: CPointer<ByteVar> = data!!.bytes!!.reinterpret()
                byteIterator = ByteArray(data!!.length.toInt(), init = bytes::get).iterator()
                val string = NSString.stringWithUTF8String(bytes) ?: ""
                lineIterator = string.splitToSequence(Regex("\\r\\n|\\n")).iterator()
            }
        }
    }
}
