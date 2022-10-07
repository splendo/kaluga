/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

// if the include is made from the example project shared module we need to go up one more directory
val path_prefix = if (file("../gradle/android_common.gradle.kts").exists()) ".." else "../.."

apply("$path_prefix/gradle/android_common.gradle.kts")

dependencies {
    val ext = (gradle as ExtensionAware).extra
    val androidx_compose_version: String by ext
    val androidx_lifecycle_viewmodel_compose_version: String by ext
    val androidx_activity_compose_version: String by ext

    implementation("androidx.compose.foundation:foundation:$androidx_compose_version")
    implementation("androidx.compose.ui:ui:$androidx_compose_version")
    implementation("androidx.compose.ui:ui-tooling:$androidx_compose_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$androidx_lifecycle_viewmodel_compose_version")
    implementation("androidx.activity:activity-compose:$androidx_activity_compose_version")

}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("androidx.compose.material.ExperimentalMaterialApi")
        }
    }
}

val ext = (gradle as ExtensionAware).extra
val component_type: String by ext
if (!component_type.toLowerCase().contains("app")) {
    apply("$path_prefix/gradle/publish.gradle.kts")
}
