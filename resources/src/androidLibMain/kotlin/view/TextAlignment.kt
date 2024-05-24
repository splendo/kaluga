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

package com.splendo.kaluga.resources.view

import android.content.Context
import android.os.Build
import android.text.Layout
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.splendo.kaluga.resources.stylable.KalugaTextAlignment

/**
 * Gets the [Layout.Alignment] associated with a [KalugaTextAlignment]
 * @param context the [Context] used to determine the reading direction
 * @return the [Layout.Alignment] associated with this [KalugaTextAlignment]
 */
fun KalugaTextAlignment.alignment(context: Context): Layout.Alignment {

    return when (this) {
        KalugaTextAlignment.LEFT -> if (context.isLayoutLeftToRight()) {
            Layout.Alignment.ALIGN_NORMAL
        } else {
            Layout.Alignment.ALIGN_OPPOSITE
        }
        KalugaTextAlignment.RIGHT -> if (context.isLayoutLeftToRight()) {
            Layout.Alignment.ALIGN_OPPOSITE
        } else {
            Layout.Alignment.ALIGN_NORMAL
        }
        KalugaTextAlignment.CENTER -> Layout.Alignment.ALIGN_CENTER
        KalugaTextAlignment.END -> Layout.Alignment.ALIGN_OPPOSITE
        KalugaTextAlignment.START -> Layout.Alignment.ALIGN_NORMAL
    }
}

/**
 * Gets the text alignment of a [Layout.Alignment]
 * Can be either [View.TEXT_ALIGNMENT_TEXT_START], [View.TEXT_ALIGNMENT_TEXT_END], or [View.TEXT_ALIGNMENT_CENTER]
 */
val Layout.Alignment.viewAlignment: Int get() = when (this) {
    Layout.Alignment.ALIGN_NORMAL -> View.TEXT_ALIGNMENT_TEXT_START
    Layout.Alignment.ALIGN_OPPOSITE -> View.TEXT_ALIGNMENT_TEXT_END
    Layout.Alignment.ALIGN_CENTER -> View.TEXT_ALIGNMENT_CENTER
}

/**
 * Gets the gravity of a [Layout.Alignment]
 * Can be either [Gravity.START], [Gravity.END], or [Gravity.CENTER_HORIZONTAL]
 */
val Layout.Alignment.gravity: Int get() = when (this) {
    Layout.Alignment.ALIGN_NORMAL -> Gravity.START
    Layout.Alignment.ALIGN_OPPOSITE -> Gravity.END
    Layout.Alignment.ALIGN_CENTER -> Gravity.CENTER_HORIZONTAL
}

internal fun Context.isLayoutLeftToRight(): Boolean {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales.get(0)
    } else {
        @Suppress("DEPRECATION")
        resources.configuration.locale
    }

    return TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_LTR
}
