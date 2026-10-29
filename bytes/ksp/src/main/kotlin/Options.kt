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
import com.splendo.kaluga.bytes.annotations.Generate
import com.splendo.kaluga.ksp.processor.KspPass

/**
 * @property pass which declarations this KSP pass generates code for.
 * @property skipUnsupportedViews whether a layout using a feature VIEW generation does not support yet gets no view
 * and a warning, instead of failing the build; the `skipUnsupportedViews` arg.
 * @property registryPrefix what the names of the generated registry objects start with, unique per module and source
 * set so registries of different ones never clash; the `registryPrefix` arg.
 */
data class Options(
    val defaultGenerate: Set<Generate>,
    val pass: KspPass = KspPass(),
    val skipUnsupportedViews: Boolean = false,
    val registryPrefix: String = DEFAULT_REGISTRY_PREFIX,
) {
    constructor(environment: SymbolProcessorEnvironment) : this(
        parseGenerate(environment.options["generate"]),
        KspPass(environment),
        environment.options["skipUnsupportedViews"] == "true",
        environment.options["registryPrefix"]?.takeIf { it.isNotBlank() } ?: DEFAULT_REGISTRY_PREFIX,
    )

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
