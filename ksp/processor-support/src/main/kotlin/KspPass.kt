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

package com.splendo.kaluga.ksp.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSDeclaration

/**
 * Which declarations a pass of a Kaluga KSP processor generates code for, read from the options the Kaluga code
 * generation plugins pass. In a multiplatform project KSP runs a pass for the common metadata and one per target, and
 * every target pass sees the common sources too, whose code the metadata pass (or the single target's pass) owns.
 *
 * @property commonSources the `commonMain` source directories; the `commonSource` option.
 * @property isSingleTarget whether the project has a single (non-metadata) target; the `isSingleTarget` option.
 * @property isMultiplatformPass whether this pass processes more than one platform, i.e. the common metadata pass.
 */
data class KspPass(val commonSources: List<String> = emptyList(), val isSingleTarget: Boolean = false, val isMultiplatformPass: Boolean = false) {

    constructor(environment: SymbolProcessorEnvironment) : this(
        commonSourceDirectories(environment.options[COMMON_SOURCE_OPTION]),
        environment.options[SINGLE_TARGET_OPTION] == "true",
        environment.platforms.size > 1,
    )

    /**
     * Whether this pass generates the code of [declaration]. A per-target pass skips the common declarations, so it
     * does not emit a duplicate of every type generated for them.
     */
    fun owns(declaration: KSDeclaration): Boolean = when {
        isSingleTarget || isMultiplatformPass -> true
        else -> declaration.containingFile?.filePath?.let(::isOutsideCommonSources) ?: true
    }

    /** Whether the source file at [path] lies outside every common source directory. */
    fun isOutsideCommonSources(path: String): Boolean = commonSources.none { path.isWithin(it) }

    companion object {
        const val COMMON_SOURCE_OPTION = "commonSource"
        const val SINGLE_TARGET_OPTION = "isSingleTarget"

        /** The directories in a `commonSource` [option]; none when it is absent. */
        fun commonSourceDirectories(option: String?): List<String> = option.orEmpty().split(":").filter { it.isNotBlank() }
    }
}

/** Whether this path is [directory] itself or lies inside it, so a sibling such as `kotlinx` next to `kotlin` does not count. */
fun String.isWithin(directory: String): Boolean {
    val root = directory.trimEnd('/', '\\')
    return this == root || startsWith("$root/") || startsWith("$root\\")
}
