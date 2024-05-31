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

package extensions

import com.android.build.gradle.LibraryExtension
import helpers.jvmTarget
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import javax.inject.Inject

class KalugaAndroidSubprojectExtension @Inject constructor(
    versionCatalog: VersionCatalog,
    objects: ObjectFactory,
) : BaseKalugaSubprojectExtension(versionCatalog, objects) {

    var isDatabindingEnabled: Boolean = false
    val Project.hasCompose: Boolean get() = extensions.findByType(ComposeCompilerGradlePluginExtension::class) != null
    override fun Project.configureSubproject() {
        extensions.configure(KotlinAndroidProjectExtension::class) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
            }
            sourceSets.all {
                languageSettings {
                    if (hasCompose) {
                        optIn("androidx.compose.material.ExperimentalMaterialApi")
                    }
                }
            }
        }
        dependencies {
            androidMainDependencies.forEach {
                add("implementation", it)
            }

            if (hasCompose) {
                add("implementation", "androidx-compose-foundation".asDependency())
                add("implementation", "androidx-compose-ui".asDependency())
                add("implementation", "androidx-compose-ui-tooling".asDependency())
                add("implementation", "androidx-lifecycle-viewmodel-compose".asDependency())
                add("implementation", "androidx-activity-compose".asDependency())
            }

            androidTestDependencies.forEach {
                add("testImplementation", it)
            }
            androidInstrumentedTestDependencies.forEach {
                add("androidTestImplementation", it)
            }
        }
    }

    override fun LibraryExtension.configure() {
        buildFeatures {
            dataBinding = isDatabindingEnabled
        }
    }
    override fun PublicationContainer.configure(project: Project) {
        create("release", MavenPublication::class) {
            from(project.components.getByName("release"))

            artifactId = project.name
            groupId = baseGroup
            version = this@KalugaAndroidSubprojectExtension.version
        }
        create("debug", MavenPublication::class) {
            from(project.components.getByName("debug"))

            artifactId = project.name
            groupId = baseGroup
            version = this@KalugaAndroidSubprojectExtension.version
        }
    }
}