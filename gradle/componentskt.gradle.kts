/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

// in preparation for a full port to kts, new functionality can go here if it's in kts

import kotlin.text.*

// if ((gradle as ExtensionAware).extra["connect_check_expansion"] == true) {
//
//     val mymodules = project.parent?.subprojects?.filter {
//         it.name.startsWith("${project.name}-") || it.name.endsWith("-${project.name}")
//     }
//
//     mymodules?.forEach() { module ->
//         afterEvaluate {
//             logger.info("[connect_check_expansion] :${project.name}:connectedDebugAndroidTest dependsOn:${module.name}:connectedDebugAndroidTest")
//             tasks.getByPath("connectedDebugAndroidTest")
//                 .dependsOn(":${module.name}:connectedDebugAndroidTest")
//         }
//     }
// }
