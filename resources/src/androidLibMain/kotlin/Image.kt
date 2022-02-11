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

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat

actual data class Image(val drawable: Drawable)

actual fun Image.tinted(color: KalugaColor): Image? {
    return drawable.constantState?.newDrawable()?.mutate()?.let {
        val wrapped = DrawableCompat.wrap(it)
        DrawableCompat.setTint(wrapped, color)
        Image(wrapped)
    }
}
