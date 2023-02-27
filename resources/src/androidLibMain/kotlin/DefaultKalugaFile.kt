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

import java.io.File

actual class DefaultKalugaFile actual constructor(
    override val path: KalugaFile.Path,
    override val mode: Set<KalugaFile.Mode>
) : KalugaFile {

    private val file = File(path.filepath)

    private fun KalugaFile.Encoding.toCharset() = when (this) {
        KalugaFile.Encoding.UTF8 -> Charsets.UTF_8
    }

    override fun open() = Unit
    override fun readLines(encoding: KalugaFile.Encoding) = file.reader(encoding.toCharset()).readLines()
    override fun readBytes() = file.readBytes()
    override fun close() = Unit
}

actual class DefaultFilePath actual constructor(
    name: String,
    path: String
) : KalugaFile.Path {
    override val filepath = "$path/$name"
}
