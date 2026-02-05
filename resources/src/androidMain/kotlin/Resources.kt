@file:JvmName("ResourcesAndroidKt")
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

package com.splendo.kaluga.resources

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.splendo.kaluga.base.ApplicationHolder

@Suppress("EnumEntryName")
private enum class DefType {
    string,
    plurals,
    color,
    drawable,
    font,
}

@SuppressLint("DiscouragedApi")
private fun <T> Context.getResource(name: String, defType: DefType, get: Context.(Int) -> T?): T? = try {
    resources.getIdentifier(name, defType.name, packageName)
        .takeIf { it != 0 }
        ?.let { id -> get(id) }
} catch (e: Resources.NotFoundException) {
    null
}

/**
 * Default implementation of a [StringLoader].
 * @param context the [Context] from which to load the string resources
 */
actual class DefaultStringLoader(private val context: Context?) : StringLoader {
    actual constructor() : this(if (ApplicationHolder.isInitialized) ApplicationHolder.applicationContext else null)
    actual override fun loadString(identifier: String, defaultValue: String): String = context?.getResource(identifier, DefType.string, Context::getString)
        ?: defaultValue

    actual override fun loadQuantityString(identifier: String, quantity: Int, defaultValue: String): String =
        context?.getResource(identifier, DefType.plurals) { id -> resources.getQuantityString(id, quantity, quantity) }
            ?: defaultValue
}

/**
 * Default implementation of a [KalugaColorLoader].
 * @param context the [Context] from which to load the color resources
 */
actual class DefaultColorLoader(private val context: Context?) : KalugaColorLoader {
    actual constructor() : this(if (ApplicationHolder.isInitialized) ApplicationHolder.applicationContext else null)
    actual override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? = context?.getResource(identifier, DefType.color) { id ->
        KalugaColor.DarkLightColor(
            ContextCompat.getColor(context.themedContext(false), id),
            ContextCompat.getColor(context.themedContext(true), id),
        )
    } ?: defaultValue

    private fun Context.themedContext(withNightMode: Boolean): Context {
        val res: Resources = resources
        val configuration = Configuration(res.configuration)
        configuration.uiMode = if (withNightMode) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO

        return createConfigurationContext(configuration)
    }
}

/**
 * Default implementation of an [ImageLoader].
 * @param context the [Context] from which to load the image resources
 */
actual class DefaultImageLoader(private val context: Context?) : ImageLoader {
    actual constructor() : this(if (ApplicationHolder.isInitialized) ApplicationHolder.applicationContext else null)
    actual override fun loadImage(identifier: String, defaultValue: KalugaImage?): KalugaImage? =
        context?.getResource(identifier, DefType.drawable) { id -> ContextCompat.getDrawable(this, id) }
            ?.let(::KalugaImage)
            ?: defaultValue
}

/**
 * Default implementation of a [FontLoader].
 * @param context the [Context] from which to load the font resources
 */
actual class DefaultFontLoader(private val context: Context?) : FontLoader {
    actual constructor() : this(if (ApplicationHolder.isInitialized) ApplicationHolder.applicationContext else null)
    actual override fun loadFont(identifier: String, defaultValue: KalugaFont?): KalugaFont? =
        context?.getResource(identifier, DefType.font) { id -> ResourcesCompat.getFont(context, id) }
            ?: defaultValue
}
