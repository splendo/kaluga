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

package com.splendo.kaluga.plugin

import com.splendo.kaluga.plugin.extensions.KalugaMultiplatformSubprojectExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import kotlin.reflect.KClass

class MultiplatformLibraryComponentsPlugin : BaseLibraryComponentsPlugin<KalugaMultiplatformSubprojectExtension>() {

    override val subExtensionClass: KClass<KalugaMultiplatformSubprojectExtension> = KalugaMultiplatformSubprojectExtension::class

    override fun PluginManager.addSubprojectExtensionPlugins() {
        apply(KotlinMultiplatformPluginWrapper::class)
    }
}
