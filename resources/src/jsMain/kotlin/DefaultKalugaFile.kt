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

actual class DefaultKalugaFile actual constructor(
    override val path: KalugaFile.Path,
    override val mode: Set<KalugaFile.Mode>
) : KalugaFile {

    override fun open() {
        TODO("Not yet implemented")
    }

    override fun nextLine(): String? {
        TODO("Not yet implemented")
    }

    override fun nextByte(): Byte? {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}

actual class DefaultFilePath actual constructor(
    name: String,
    path: String
) : KalugaFile.Path {
    override val filepath = "$path/$name"
}
