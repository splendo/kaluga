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

fun org.gradle.api.Project.databindingAndroidComponent(packageName: String) {
    group = Library.group
    version = Library.version
    commonAndroidComponent(ComponentType.DataBinding, packageName)

    ktlint {
        // Should be replaced by using .editorconfig but this seems to be broken
        // Therefore using older version of ktlint for now
        version.set("0.48.2")
        disabledRules.set(listOf("no-wildcard-imports", "filename", "import-ordering"))
    }

    publish(ComponentType.DataBinding)
}