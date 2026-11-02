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
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.test.Test
import kotlin.test.assertEquals

class BytesExtensionTest {

    @Test
    fun `forwards the bytes configuration to the processor`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(BytesPlugin::class.java)
        project.extensions.configure<KotlinMultiplatformExtension> { jvm() }
        project.extensions.configure<BytesExtension> {
            generate.set(setOf(Generate.VIEW, Generate.DATA))
            skipUnsupportedViews.set(true)
        }
        (project as ProjectInternal).evaluate()

        val arguments = project.extensions.getByType<KspExtension>().arguments
        assertEquals(setOf("DATA", "VIEW"), arguments.getValue("generate").split(",").toSet())
        assertEquals("true", arguments["skipUnsupportedViews"])
    }
}
