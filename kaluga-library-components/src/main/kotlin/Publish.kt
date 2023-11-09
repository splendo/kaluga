/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.plugins.signing.Sign

fun Project.publish(componentType: ComponentType = ComponentType.Default) {
    afterEvaluate {
        publishing {
            publications {
                logger.info("This project module will be published as: $componentType")
                when (componentType) {
                    is ComponentType.Compose,
                    is ComponentType.DataBinding -> {
                        create("release", MavenPublication::class.java) {
                            from(components.getByName("release"))

                            artifactId = project.name
                            groupId = Library.group
                            version = Library.version
                        }
                        create("debug", MavenPublication::class.java) {
                            from(components.getByName("debug"))

                            artifactId = project.name
                            groupId = Library.group
                            version = Library.version
                        }
                    }
                    is ComponentType.Default -> {
                        getByName("kotlinMultiplatform") {
                            (this as MavenPublication).let {
                                artifactId = project.name
                                groupId = Library.group
                                version = Library.version
                            }
                        }
                    }
                }
            }
        }
    }

    tasks.withType(AbstractPublishToMaven::class.java).configureEach {
        dependsOn(tasks.withType(Sign::class.java))
    }
}
