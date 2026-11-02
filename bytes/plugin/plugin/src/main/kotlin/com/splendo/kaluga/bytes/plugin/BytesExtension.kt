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

package com.splendo.kaluga.bytes.plugin

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.GradleException
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty

/**
 * The code the `com.splendo.kaluga.bytes.plugin` generates for `@BytesLayout` interfaces that do not declare their
 * own `generate` override. Mirrors `com.splendo.kaluga.bytes.annotations.Generate`; kept separate because this
 * module cannot depend on the published `com.splendo.kaluga.bytes:annotations` artifact it configures for consumers.
 */
enum class Generate {
    DATA,
    VIEW,
}

/** Serializes to the `generate` KSP arg [Options][com.splendo.kaluga.bytes.ksp] parses on the processor side. */
internal fun Set<Generate>.toKspArg(): String = joinToString(",") { it.name }

/**
 * Configures the `com.splendo.kaluga.bytes.plugin` code generation through the `bytes { }` DSL.
 */
open class BytesExtension(private val kspExtension: KspExtension, objects: ObjectFactory) {

    /**
     * The module-wide default for which implementations to generate from a `@BytesLayout` interface. Defaults to
     * both [Generate.DATA] and [Generate.VIEW]. An interface's own `@BytesLayout(generate = [...])` overrides this
     * default for that interface only.
     */
    val generate = objects.setProperty<Generate>().apply {
        add(Generate.DATA)
        add(Generate.VIEW)
    }

    /**
     * Whether a layout using a feature VIEW generation does not support yet gets no view and a build warning, instead
     * of failing the build. Defaults to `false`.
     */
    val skipUnsupportedViews = objects.property<Boolean>().convention(false)

    /**
     * Forwards this configuration to KSP as processor options. Invoked by [BytesPlugin].
     * @throws GradleException when [generate] is empty.
     */
    internal fun afterEvaluate() {
        val generate = generate.get()
        if (generate.isEmpty()) throw GradleException("bytes { generate } must include Generate.DATA and/or Generate.VIEW")
        kspExtension.arg("generate", generate.toKspArg())
        kspExtension.arg("skipUnsupportedViews", skipUnsupportedViews.get().toString())
    }
}
