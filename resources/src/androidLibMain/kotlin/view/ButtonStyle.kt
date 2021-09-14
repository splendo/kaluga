package com.splendo.kaluga.resources.view

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.widget.Button
import com.splendo.kaluga.resources.Color
import com.splendo.kaluga.resources.stylable.ButtonStyle

sealed class RippleStyle {
    object None : RippleStyle()
    object ForegroundRipple : RippleStyle()
    data class CustomRipple(val color: Color) : RippleStyle()
}

fun Button.applyButtonStyle(style: ButtonStyle, rippleStyle: RippleStyle = RippleStyle.ForegroundRipple) {
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
