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

import android.content.res.ColorStateList
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.TextView
import com.splendo.kaluga.resources.stylable.KalugaTextStyle

/**
 * Makes a [TextView] look like the specification of a [KalugaLabel]
 * @param label the [KalugaLabel] that specifies the look of the [TextView]
 */
fun TextView.bindLabel(label: KalugaLabel) {
    text = when (label) {
        is KalugaLabel.Plain -> label.text
        is KalugaLabel.Styled -> {
            if (label.text.spannable.getSpans(0, label.text.spannable.length, URLSpan::class.java).isNotEmpty()) {
                label.text.linkStyle?.let {
                    setLinkTextColor(it.color)
                }
                movementMethod = LinkMovementMethod.getInstance()
            }
            label.text.spannable
        }
    }
    applyTextStyle(label.style)
}

/**
 * Makes a [TextView] look as specified by a [KalugaTextStyle]
 * @param textStyle the [KalugaTextStyle] that specifies the look of the [TextView]
 */
fun TextView.applyTextStyle(textStyle: KalugaTextStyle) {
    typeface = textStyle.font
    textSize = textStyle.size
    setTextColor(ColorStateList(arrayOf(intArrayOf()), intArrayOf(textStyle.color)))
    isAllCaps = false
    gravity = textStyle.alignment.alignment(context).gravity
}
