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

@file:JvmName("AndroidButton")

package com.splendo.kaluga.resources.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.View
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.colorFrom
import com.splendo.kaluga.resources.dpToPixel
import com.splendo.kaluga.resources.drawable
import com.splendo.kaluga.resources.isInDarkMode
import com.splendo.kaluga.resources.stylable.ButtonImage
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.ImageGravity
import com.splendo.kaluga.resources.stylable.ImageSize
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle
import com.splendo.kaluga.resources.stylable.Padding

/**
 * Makes a [android.widget.Button] look and behave according to a [KalugaButton]
 * @param button the [KalugaButton] that specifies the look and behaviour of the [android.widget.Button]
 * @param rippleStyle a [RippleStyle] that is applied when the button is pressed
 */
fun android.widget.Button.bindButton(button: KalugaButton, rippleStyle: RippleStyle = RippleStyle.ForegroundRipple) {
    text = when (button) {
        is KalugaButton.NoText -> ""
        is KalugaButton.Plain -> button.text
        is KalugaButton.Styled -> button.text.spannable
    }
    applyButtonStyle(button.style, rippleStyle)
    isAllCaps = false
    isEnabled = button.isEnabled
    setOnClickListener { button.action() }
}

/**
 * Style of Ripple effect to use when a [android.widget.Button] is configured with a [KalugaButtonStyle]
 */
sealed class RippleStyle {

    /**
     * A [RippleStyle] where no Ripple effect is applied
     */
    data object None : RippleStyle()

    /**
     * A [RippleStyle] where the ripple has the color of the [com.splendo.kaluga.resources.stylable.TextButtonStateStyle.textColor] of the [KalugaButtonStyle.pressedStyle]
     */
    data object ForegroundRipple : RippleStyle()

    /**
     * A [RippleStyle] where the ripple has a custom [KalugaColor]
     * @property color the [KalugaColor] of the Ripple effect
     */
    data class CustomRipple(val color: KalugaColor) : RippleStyle()
}

/**
 * Makes a [android.widget.Button] look as specified by a [KalugaButtonStyle]
 * @param style the [KalugaButtonStyle] that specifies the look of the [android.widget.Button]
 * @param rippleStyle the [RippleStyle] to apply when the button is pressed
 */
fun android.widget.Button.applyButtonStyle(style: KalugaButtonStyle<*>, rippleStyle: RippleStyle = RippleStyle.ForegroundRipple) {
    if (style is KalugaButtonStyle.WithText<*>) {
        applyButtonStyleWithText(style)
    }

    if (style is KalugaButtonStyle.WithImage<*>) {
        applyButtonStyleWithImage(style)
    }

    setPadding(style.padding)
    applyBackgroundStyle(style, rippleStyle)
}

private fun android.widget.Button.applyButtonStyleWithText(style: KalugaButtonStyle.WithText<*>) {
    typeface = style.font
    isAllCaps = false
    textSize = style.textSize
    textAlignment = style.textAlignment.alignment(context).viewAlignment
    setTextColor(
        ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(),
            ),
            intArrayOf(
                style.pressedStyle.textColor,
                style.disabledStyle.textColor,
                style.defaultStyle.textColor,
            ),
        ),
    )
}

private fun android.widget.Button.applyButtonStyleWithImage(style: KalugaButtonStyle.WithImage<*>) {

    val stateListDrawable = StateListDrawable().apply {
        addState(
            intArrayOf(android.R.attr.state_pressed),
            (style.pressedStyle as ButtonStateStyle.WithImage).drawable(context),
        )
        addState(
            intArrayOf(-android.R.attr.state_enabled),
            (style.disabledStyle as ButtonStateStyle.WithImage).drawable(context),
        )
        addState(
            StateSet.WILD_CARD,
            (style.defaultStyle as ButtonStateStyle.WithImage).drawable(context),
        )
    }

    when (style) {
        is KalugaButtonStyle.WithImageAndText -> {
            when (style.imageGravity) {
                ImageGravity.START -> setCompoundDrawablesRelativeWithIntrinsicBounds(stateListDrawable, null, null, null)
                ImageGravity.END -> setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, stateListDrawable, null)
                ImageGravity.LEFT -> setCompoundDrawablesWithIntrinsicBounds(stateListDrawable, null, null, null)
                ImageGravity.RIGHT -> setCompoundDrawablesWithIntrinsicBounds(null, null, stateListDrawable, null)
                ImageGravity.TOP -> setCompoundDrawablesWithIntrinsicBounds(null, stateListDrawable, null, null)
                ImageGravity.BOTTOM -> setCompoundDrawablesWithIntrinsicBounds(null, null, null, stateListDrawable)
            }
            compoundDrawablePadding = style.spacing.dpToPixel(context).toInt()
        }
        is KalugaButtonStyle.ImageOnly -> {
            setCompoundDrawablesRelative(stateListDrawable, null, null, null)
        }
    }
}

private fun android.widget.Button.applyBackgroundStyle(style: KalugaButtonStyle<*>, rippleStyle: RippleStyle) {
    val stateListDrawable = StateListDrawable().apply {
        addState(
            intArrayOf(android.R.attr.state_pressed),
            style.pressedStyle.backgroundStyle.createDrawable(context),
        )
        addState(
            intArrayOf(-android.R.attr.state_enabled),
            style.disabledStyle.backgroundStyle.createDrawable(context),
        )
        addState(
            StateSet.WILD_CARD,
            style.defaultStyle.backgroundStyle.createDrawable(context),
        )
    }
    background = when (rippleStyle) {
        is RippleStyle.None -> stateListDrawable
        is RippleStyle.ForegroundRipple -> {
            val foregroundColor = when (style) {
                is KalugaButtonStyle.WithText<*> -> style.pressedStyle.textColor
                is KalugaButtonStyle.WithImage<*> -> {
                    val withImage = style.pressedStyle as ButtonStateStyle.WithImage
                    when (val image = withImage.image) {
                        is ButtonImage.Tinted -> image.image.tint
                        is ButtonImage.Image,
                        is ButtonImage.Hidden -> {
                            defaultRippleForeground
                        }
                    }
                }
                is KalugaButtonStyle.WithoutText<*> -> defaultRippleForeground
            }
            RippleDrawable(
                ColorStateList(arrayOf(intArrayOf()), intArrayOf(foregroundColor)),
                stateListDrawable,
                null,
            )
        }
        is RippleStyle.CustomRipple -> {
            RippleDrawable(
                ColorStateList(arrayOf(intArrayOf()), intArrayOf(rippleStyle.color)),
                stateListDrawable,
                null,
            )
        }
    }
}

private fun View.setPadding(padding: Padding): Unit = with(padding) {
    val left = if (context.isLayoutLeftToRight()) {
        start
    } else {
        end
    }
    val right = if (context.isLayoutLeftToRight()) {
        end
    } else {
        start
    }
    setPadding(left.dpToPixel(context).toInt(), top.dpToPixel(context).toInt(), right.dpToPixel(context).toInt(), bottom.dpToPixel(context).toInt())
}

private fun ButtonStateStyle.WithImage.drawable(context: Context): Drawable = (when (val image = image) {
    is ButtonImage.Image -> image.image.drawable
    is ButtonImage.Tinted -> image.image.drawable
    is ButtonImage.Hidden -> null
} ?: ShapeDrawable()).apply {
    when (val size = size) {
        is ImageSize.Sized ->
            setBounds(0, 0, size.width.dpToPixel(context).toInt(), size.height.dpToPixel(context).toInt())
        is ImageSize.Intrinsic -> {}
    }
}

private val defaultRippleForeground: KalugaColor get() = if (isInDarkMode) {
    colorFrom(0.0, 0.0, 0.0, 0.25)
} else {
    colorFrom(1.0, 1.0, 1.0, 0.25)
}
