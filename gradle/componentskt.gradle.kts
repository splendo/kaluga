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

val androidLibAndroidTestDebugImplementation by configurations

val androidTestHelperDebugImplementation by configurations.creating {
    extendsFrom(configurations[androidLibAndroidTestDebugImplementation.name])
}

val jarAndroidTest = tasks.create<Jar>("jarAndroidTest") {
    dependsOn("assembleDebugAndroidTest")
    from("build/tmp/kotlin-classes/debugAndroidTest")
}

artifacts.add(androidTestHelperDebugImplementation.name, jarAndroidTest)
