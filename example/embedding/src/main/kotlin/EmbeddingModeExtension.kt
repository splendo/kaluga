/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.plugin

import org.gradle.api.logging.Logger
import java.util.Properties
import javax.inject.Inject

open class EmbeddingModeExtension @Inject constructor(private val properties: Properties, private val logger: Logger) {

    val embeddingMode = generateEmbeddingMode()

    private fun generateEmbeddingMode(): EmbeddingMode {
        val providedEmbeddingMethod = if (System.getenv().containsKey("EXAMPLE_EMBEDDING_METHOD")) {
            System.getenv()["EXAMPLE_EMBEDDING_METHOD"].also {
                logger.lifecycle("System env EXAMPLE_EMBEDDING_METHOD set to ${System.getenv()["EXAMPLE_EMBEDDING_METHOD"]}, using $it")
            }!!
        } else {
            val exampleEmbeddingMethodLocalProperties = properties["kaluga.exampleEmbeddingMethod"] as? String
            (exampleEmbeddingMethodLocalProperties ?: "composite").also {
                logger.lifecycle("local.properties read (kaluga.exampleEmbeddingMethod=$exampleEmbeddingMethodLocalProperties, using $it)")
            }
        }

        return if (providedEmbeddingMethod == "composite") {
            EmbeddingMode.Composite
        } else {
            val exampleMavenRepo = if (System.getenv().containsKey("EXAMPLE_MAVEN_REPO")) {
                System.getenv()["EXAMPLE_MAVEN_REPO"].also {
                    logger.lifecycle("System env EXAMPLE_MAVEN_REPO set to ${System.getenv()["EXAMPLE_MAVEN_REPO"]}, using $it")
                }!!
            } else {
                val exampleMavenRepoLocalProperties: String? =
                    properties["kaluga.exampleMavenRepo"] as? String
                exampleMavenRepoLocalProperties?.also {
                    logger.lifecycle("local.properties read (kaluga.exampleMavenRepo=$exampleMavenRepoLocalProperties, using $it)")
                }
                    ?: "local".also {
                        logger.info("local.properties not found, using default value ($it)")
                    }
            }
            logger.lifecycle("Using repo: $exampleMavenRepo for resolving dependencies")

            when (exampleMavenRepo) {
                "", "local" -> EmbeddingMode.MavenLocal
                "none" -> EmbeddingMode.None
                else ->
                    EmbeddingMode.MavenRepo(exampleMavenRepo)
            }
        }
    }
}