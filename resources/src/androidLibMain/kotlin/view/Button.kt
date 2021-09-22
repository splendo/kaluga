/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

@file:JvmName("AndroidButton")
package com.splendo.kaluga.resources.view

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import com.splendo.kaluga.resources.Color
import com.splendo.kaluga.resources.stylable.ButtonStyle

fun android.widget.Button.bindButton(button: KalugaButton<*>) {
    text = when (button) {
        is KalugaButton.Plain -> button.text
        is KalugaButton.Styled -> button.text.spannable
    }
    applyButtonStyle(button.style)
    isAllCaps = false
    isEnabled = button.isEnabled
    setOnClickListener { button.action() }
}

sealed class RippleStyle {
    object None : RippleStyle()
    object ForegroundRipple : RippleStyle()
    data class CustomRipple(val color: Color) : RippleStyle()
}

fun android.widget.Button.applyButtonStyle(style: ButtonStyle, rippleStyle: RippleStyle = RippleStyle.ForegroundRipple) {
    typeface = style.font
    isAllCaps = false
    textSize = style.textSize
    setTextColor(
        ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf()
            ),
            intArrayOf(
                style.pressedStyle.textColor,
                style.disabledStyle.textColor,
                style.defaultStyle.textColor
            )
        )
    )
    val stateListDrawable = StateListDrawable().apply {
        addState(
            intArrayOf(android.R.attr.state_pressed),
            style.pressedStyle.backgroundStyle.createDrawable(context)
        )
        addState(
            intArrayOf(-android.R.attr.state_enabled),
            style.disabledStyle.backgroundStyle.createDrawable(context)
        )
        addState(
            StateSet.WILD_CARD,
            style.defaultStyle.backgroundStyle.createDrawable(context)
        )
    }
    background = when (rippleStyle) {
        is RippleStyle.None -> stateListDrawable
        is RippleStyle.ForegroundRipple -> {
            RippleDrawable(
                ColorStateList(arrayOf(intArrayOf()), intArrayOf(style.pressedStyle.textColor)),
                stateListDrawable,
                null
            )
        }
        is RippleStyle.CustomRipple -> {
            RippleDrawable(
                ColorStateList(arrayOf(intArrayOf()), intArrayOf(rippleStyle.color)),
                stateListDrawable,
                null
            )
        }
    }
}
