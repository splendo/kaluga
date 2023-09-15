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

import org.gradle.kotlin.dsl.dependencies

fun org.gradle.api.Project.composeAndroidComponent(packageName: String) {
    group = Library.group
    version = Library.version
    commonAndroidComponent(ComponentType.Compose, packageName)
    dependencies {
        implementationDependency(Dependencies.AndroidX.Compose.Foundation)
        implementationDependency(Dependencies.AndroidX.Compose.UI)
        implementationDependency(Dependencies.AndroidX.Compose.UITooling)
        implementationDependency(Dependencies.AndroidX.Lifecycle.ViewModelCompose)
        implementationDependency(Dependencies.AndroidX.Activity.Compose)
    }

    kotlinAndroid {

        sourceSets.all {
            languageSettings {
                optIn("androidx.compose.material.ExperimentalMaterialApi")
            }
        }
    }

    androidLibrary {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    publish(ComponentType.Compose)
}
