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

package com.splendo.kaluga.resources

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.splendo.kaluga.base.ApplicationHolder.Companion.application
import com.splendo.kaluga.base.ApplicationHolder.Companion.applicationContext
import kotlinx.coroutines.CompletableDeferred

actual class DefaultStringLoader(private val context: Context?): StringLoader {
    actual constructor() : this(if (application != null) applicationContext else null)
    override fun loadString(identifier: String, defaultValue: String): String {
        if (context == null)
            return defaultValue
        val id = context.resources.getIdentifier(identifier, "string", context.packageName)
        return try {
            context.getString(id)
        } catch (e: Resources.NotFoundException) {
            defaultValue
        }
    }
    override fun loadQuantityString(identifier: String, quantity: Int, defaultValue: String): String {
        if (context == null)
            return defaultValue
        val id = context.resources.getIdentifier(identifier, "plurals", context.packageName)
        return try {
            context.resources.getQuantityString(id, quantity, quantity)
        } catch (e: Resources.NotFoundException) {
            defaultValue
        }
    }
}

actual class DefaultColorLoader(private val context: Context?): ColorLoader {
    actual constructor() : this(if (application != null) applicationContext else null)
    override fun loadColor(identifier: String, defaultValue: Color?): Color? {
        if (context == null)
            return defaultValue
        val id = context.resources.getIdentifier(identifier, "color", context.packageName)
        return try {
            ContextCompat.getColor(context, id)
        } catch (e: Resources.NotFoundException) {
            defaultValue
        }
    }
}

actual class DefaultImageLoader(private val context: Context?): ImageLoader {
    actual constructor() : this(if (application != null) applicationContext else null)
    override fun loadImage(identifier: String, defaultValue: Image?): Image? {
        if (context == null)
            return defaultValue
        val id = context.resources.getIdentifier(identifier, "drawable", context.packageName)
        return try {
            ContextCompat.getDrawable(context, id)?.let { Image(it) }
        } catch (e: Resources.NotFoundException) {
            defaultValue
        }
    }
}

actual class DefaultFontLoader(private val context: Context?, private val handler: Handler?): FontLoader {
    actual constructor() : this(if (application != null) applicationContext else null, null)
    override suspend fun loadFont(identifier: String, defaultValue: Font?): Font? {
        if (context == null)
            return defaultValue
        val id = context.resources.getIdentifier(identifier, "font", context.packageName)
        return try {
            val deferredFont = CompletableDeferred<Typeface?>()
            val callback = object : ResourcesCompat.FontCallback() {
                override fun onFontRetrievalFailed(reason: Int) {
                    deferredFont.complete(defaultValue)
                }

                override fun onFontRetrieved(typeface: Typeface) {
                    deferredFont.complete(typeface)
                }
            }
            ResourcesCompat.getFont(context, id, callback, handler)
            deferredFont.await()
        } catch (e: Resources.NotFoundException) {
            defaultValue
        }
    }
}
