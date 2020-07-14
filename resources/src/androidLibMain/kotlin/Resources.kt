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
import com.splendo.kaluga.base.ApplicationHolder.Companion.applicationContext
import kotlinx.coroutines.CompletableDeferred

actual class StringLoader(private val context: Context) {
    actual constructor() : this(applicationContext)
    actual fun loadString(identifier: String): String {
        val id = context.resources.getIdentifier(identifier, "string", context.packageName)
        return try {
            context.getString(id)
        } catch (e: Resources.NotFoundException) {
            identifier
        }
    }
}

actual class ColorLoader(private val context: Context) {
    actual constructor() : this(applicationContext)
    actual fun loadColor(identifier: String): Color? {
        val id = context.resources.getIdentifier(identifier, "color", context.packageName)
        return try {
            ContextCompat.getColor(context, id)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }
}

actual class ImageLoader(private val context: Context) {
    actual constructor() : this(applicationContext)
    actual fun loadImage(identifier: String): Image? {
        val id = context.resources.getIdentifier(identifier, "drawable", context.packageName)
        return try {
            ContextCompat.getDrawable(context, id)?.let { Image(it) }
        } catch (e: Resources.NotFoundException) {
            null
        }
    }
}

actual class FontLoader(private val context: Context, private val handler: Handler?) {
    actual constructor() : this(applicationContext, null)
    actual suspend fun loadFont(identifier: String): Font? {
        val id = context.resources.getIdentifier(identifier, "font", context.packageName)
        return try {
            val deferredFont = CompletableDeferred<Typeface?>()
            val callback = object : ResourcesCompat.FontCallback() {
                override fun onFontRetrievalFailed(reason: Int) {
                    deferredFont.complete(null)
                }

                override fun onFontRetrieved(typeface: Typeface) {
                    deferredFont.complete(typeface)
                }
            }
            ResourcesCompat.getFont(context, id, callback, handler)
            deferredFont.await()
        } catch (e: Resources.NotFoundException) {
            null
        }
    }
}
