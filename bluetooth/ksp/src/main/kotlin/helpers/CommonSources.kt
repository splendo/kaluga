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

package com.splendo.kaluga.bluetooth.ksp.helpers

/** The common source directories in the `commonSource` option the Gradle plugin passes; none when it is absent. */
internal fun commonSourceDirectories(option: String?): List<String> = option.orEmpty().split(":").filter { it.isNotBlank() }

/** Whether this path is [directory] itself or lies inside it, so a sibling such as `kotlinx` next to `kotlin` does not count. */
internal fun String.isWithin(directory: String): Boolean {
    val root = directory.trimEnd('/', '\\')
    return this == root || startsWith("$root/") || startsWith("$root\\")
}
