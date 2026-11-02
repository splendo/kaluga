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

package com.splendo.kaluga.bytes.ksp

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.splendo.kaluga.bytes.annotations.Generate

/**
 * @property commonSources the `commonMain` source directories, passed by the Gradle plugin as the `commonSource` arg.
 * @property isSingleTarget whether the consuming project has a single (non-metadata) target.
 * @property isMultiplatformPass whether this KSP pass processes more than one platform, i.e. the common metadata pass.
 * @property skipUnsupportedViews whether a layout using a feature VIEW generation does not support yet gets no view
 * and a warning, instead of failing the build; the `skipUnsupportedViews` arg.
 * @property registryPrefix what the names of the generated registry objects start with, unique per module and source
 * set so registries of different ones never clash; the `registryPrefix` arg.
 */
data class Options(
    val defaultGenerate: Set<Generate>,
    val commonSources: List<String> = emptyList(),
    val isSingleTarget: Boolean = false,
    val isMultiplatformPass: Boolean = false,
    val skipUnsupportedViews: Boolean = false,
    val registryPrefix: String = DEFAULT_REGISTRY_PREFIX,
) {
    constructor(environment: SymbolProcessorEnvironment) : this(
        parseGenerate(environment.options["generate"]),
        environment.options["commonSource"].orEmpty().split(":").filter { it.isNotBlank() },
        environment.options["isSingleTarget"] == "true",
        environment.platforms.size > 1,
        environment.options["skipUnsupportedViews"] == "true",
        environment.options["registryPrefix"]?.takeIf { it.isNotBlank() } ?: DEFAULT_REGISTRY_PREFIX,
    )

    /**
     * Whether this pass owns generation for [declaration]. Common declarations are generated once by the metadata pass
     * (or the single leaf pass); a per-target pass skips them so it does not emit a duplicate of every generated type.
     */
    fun includeInGeneration(declaration: KSClassDeclaration): Boolean = when {
        isSingleTarget || isMultiplatformPass -> true
        else -> declaration.containingFile?.filePath?.let(::isOutside) ?: true
    }

    /** Whether the source file at [path] lies outside every common source directory. */
    internal fun isOutside(path: String): Boolean = commonSources.none { path.isWithin(it) }

    companion object {
        const val DEFAULT_REGISTRY_PREFIX = "Generated"

        /**
         * Parses the `generate` KSP arg the Gradle plugin serializes from its `bytes { generate.set(...) }` DSL; without
         * the arg both are generated.
         * @throws IllegalArgumentException when the arg is given but names nothing to generate.
         */
        internal fun parseGenerate(raw: String?): Set<Generate> {
            if (raw == null) return setOf(Generate.DATA, Generate.VIEW)
            val generate = raw.split(",").filter { it.isNotBlank() }.map { name ->
                Generate.entries.firstOrNull { it.name == name.trim() } ?: throw IllegalArgumentException("Unknown generate option '$name'; use DATA and/or VIEW")
            }.toSet()
            require(generate.isNotEmpty()) { "The generate option must name DATA and/or VIEW" }
            return generate
        }
    }
}

// Whether this path is [directory] or a file under it, matching whole path segments only.
private fun String.isWithin(directory: String): Boolean {
    val root = directory.trimEnd('/', '\\')
    return this == root || startsWith("$root/") || startsWith("$root\\")
}
