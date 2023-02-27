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

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSBundle
import platform.Foundation.NSFileHandle
import platform.Foundation.NSString
import platform.Foundation.closeFile
import platform.Foundation.fileHandleForReadingAtPath
import platform.Foundation.fileHandleForUpdatingAtPath
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.readDataToEndOfFile
import platform.Foundation.stringWithUTF8String

actual class DefaultKalugaFile actual constructor(
    override val path: KalugaFile.Path,
    override val mode: Set<KalugaFile.Mode>
) : KalugaFile {

    private var file: NSFileHandle? = null

    private fun Set<KalugaFile.Mode>.toFileHandle(path: String) = when {
        canUpdate() -> NSFileHandle.fileHandleForUpdatingAtPath(path)
        canWrite() -> NSFileHandle.fileHandleForWritingAtPath(path)
        canRead() -> NSFileHandle.fileHandleForReadingAtPath(path)
        else -> throw KalugaFile.Exception.UnsupportedMode(this)
    }

    private fun bytesPointer(): Pair<CPointer<ByteVar>, Int> {
        val file = file ?: throw KalugaFile.Exception.CantOpenFile(path.filepath)
        val data = file.readDataToEndOfFile()
        val bytes = data.bytes?.reinterpret<ByteVar>() ?: throw KalugaFile.Exception.CantReadFile(path.filepath)
        return bytes to data.length.toInt()
    }

    override fun open() {
        file = mode.toFileHandle(path.filepath)
            ?: throw KalugaFile.Exception.CantOpenFile(path.filepath)
    }

    override fun readLines(encoding: KalugaFile.Encoding): List<String> {
        val (bytes, _) = bytesPointer()
        return when (encoding) {
            KalugaFile.Encoding.UTF8 -> NSString.stringWithUTF8String(bytes)
                ?.split(KalugaFile.NEW_LINE_REGEX)
        } ?: emptyList()
    }

    override fun readBytes(): ByteArray {
        val (bytes, size) = bytesPointer()
        return ByteArray(size, init = bytes::get)
    }

    override fun close() {
        file?.closeFile()
    }
}

actual class DefaultFilePath actual constructor(
    name: String,
    path: String
) : KalugaFile.Path {
    override val filepath = NSBundle.mainBundle.pathForResource(name, ofType = "")
        ?: throw KalugaFile.Exception.CantOpenFile(name)
}
