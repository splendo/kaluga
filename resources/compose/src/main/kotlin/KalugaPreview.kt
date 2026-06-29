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

package com.splendo.kaluga.resources.compose

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.splendo.kaluga.base.ApplicationHolder

/**
 * Sets up an infrastructure for previews.
 * @param resourcePackageOverride if provided, overrides a lookup package for resources
 * @param content a preview content
 */
@Composable
fun KalugaPreview(resourcePackageOverride: String? = null, content: @Composable () -> Unit) {
    ApplicationHolder.applicationContext = PreviewContextWrapper(
        base = LocalContext.current.applicationContext,
        packageNameOverride = resourcePackageOverride,
    )

    content()
}

private class PreviewContextWrapper private constructor(base: Context, private val packageNameOverride: String?, private val resourcesOverride: Resources?) :
    ContextWrapper(base) {
    constructor(base: Context, packageNameOverride: String?) : this(base, packageNameOverride, null)
    override fun getPackageName(): String = packageNameOverride ?: super.getPackageName()
    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        @Suppress("DEPRECATION")
        return super.createConfigurationContext(overrideConfiguration)
            ?: PreviewContextWrapper(this, packageNameOverride, Resources(resources.assets, resources.displayMetrics, overrideConfiguration))
    }
    override fun getResources(): Resources = resourcesOverride ?: super.getResources()
}
