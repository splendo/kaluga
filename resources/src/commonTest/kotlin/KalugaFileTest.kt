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

import kotlin.test.Test
import kotlin.test.assertEquals

class KalugaFileTest {

    companion object {
        private const val PATH_SEPARATOR = "/"
        private val COMMON_TEST_RESOURCES_PATH = listOf(
            ".", "src", "commonTest", "resources"
        )
        private fun List<String>.toPath() = joinToString(PATH_SEPARATOR)
    }

    @Test
    fun testKalugaFile() {
        val file = DefaultKalugaFile(
            path = DefaultFilePath(name = "test.txt", path = COMMON_TEST_RESOURCES_PATH.toPath()),
            mode = KalugaFile.Mode.ReadOnly
        )
        val expected = "hello, kaluga world!"
        val actual = file.useLines { it.joinToString(separator = " ") }
        assertEquals(expected, actual)
    }
}
