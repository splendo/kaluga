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

package com.splendo.kaluga.resources.stylable

import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.KalugaFont
import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.TintedImage
import com.splendo.kaluga.resources.defaultFont
import kotlin.jvm.JvmInline

sealed interface KalugaButtonStyle<StateStyle : ButtonStateStyle> {
    val padding: Padding
    val defaultStyle: StateStyle
    val pressedStyle: StateStyle
    val disabledStyle: StateStyle

    companion object {


        @Deprecated("Use KalugaButtonStyle.TextOnly")
        operator fun invoke(
            font: KalugaFont,
            textSize: Float,
            textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
            defaultStyle: ButtonStateStyle.TextOnly,
            pressedStyle: ButtonStateStyle.TextOnly = defaultStyle,
            disabledStyle: ButtonStateStyle.TextOnly = defaultStyle,
        ) = TextOnly(
            font,
            textSize,
            textAlignment,
            Padding(),
            defaultStyle,
            pressedStyle,
            disabledStyle,
        )
        @Deprecated("Use KalugaButtonStyle.TextOnly")
        /**
         * Constructor
         * @param textStyle the [KalugaTextStyle] of the button text
         * @param textAlignment the [KalugaTextAlignment] of the text of the button
         * @param backgroundColor the [KalugaColor] of the background of the button when not in a special state
         * @param pressedBackgroundColor the [KalugaColor] of the background of the button when pressed
         * @param disabledBackgroundColor the [KalugaColor] of the background of the button when disabled
         * @param shape the [KalugaBackgroundStyle.Shape] of the background of the button
         */
        operator fun invoke(
            textStyle: KalugaTextStyle,
            textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
            backgroundColor: KalugaColor = DefaultColors.clear,
            pressedBackgroundColor: KalugaColor = backgroundColor,
            disabledBackgroundColor: KalugaColor = backgroundColor,
            shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle(),
        ) = TextOnly(
            textStyle.font,
            textStyle.size,
            textAlignment,
            Padding(),
            ButtonStateStyle.textOnly {
                textColor = textStyle.color
                setBackgroundStyle(backgroundColor, shape)
              },
            ButtonStateStyle.textOnly {
                textColor = textStyle.color
                setBackgroundStyle(pressedBackgroundColor, shape)
            },
            ButtonStateStyle.textOnly {
                textColor = textStyle.color
                setBackgroundStyle(disabledBackgroundColor, shape)
            },
        )
    }

    sealed interface WithoutText<StateStyle : ButtonStateStyle.WithoutText> : KalugaButtonStyle<StateStyle>
    data class WithoutContent(
        override val padding: Padding = Padding(),
        override val defaultStyle: ButtonStateStyle.WithoutContent = ButtonStateStyle.withoutContent {  },
        override val pressedStyle: ButtonStateStyle.WithoutContent = defaultStyle,
        override val disabledStyle: ButtonStateStyle.WithoutContent = defaultStyle,
    ) : WithoutText<ButtonStateStyle.WithoutContent>

    sealed interface WithText<StateStyle : ButtonStateStyle.WithText> : KalugaButtonStyle<StateStyle> {
        val font: KalugaFont
        val textSize: Float
        val textAlignment: KalugaTextAlignment

        /**
         * Gets the [KalugaTextStyle] of the button depending on the state
         * @param isEnabled if `true` the button is enabled
         * @param isPressed if `true` the button is pressed
         * @return the [KalugaTextStyle] to apply to the text of the button in the current state
         */
        fun getStateTextStyle(isEnabled: Boolean, isPressed: Boolean) = KalugaTextStyle(font, getStateStyle(isEnabled, isPressed).textColor, textSize, textAlignment)
    }

    data class TextOnly(
        override val font: KalugaFont = defaultFont,
        override val textSize: Float = 12.0f,
        override val textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
        override val padding: Padding = Padding(),
        override val defaultStyle: ButtonStateStyle.TextOnly = ButtonStateStyle.textOnly {  },
        override val pressedStyle: ButtonStateStyle.TextOnly = defaultStyle,
        override val disabledStyle: ButtonStateStyle.TextOnly = defaultStyle,
    ) : WithText<ButtonStateStyle.TextOnly>

    sealed interface WithImage<StateStyle> where StateStyle : ButtonStateStyle, StateStyle : ButtonStateStyle.WithImage {
        val defaultStyle: StateStyle
        val pressedStyle: StateStyle
        val disabledStyle: StateStyle
    }

    data class ImageOnly(
        override val padding: Padding = Padding(),
        override val defaultStyle: ButtonStateStyle.ImageOnly = ButtonStateStyle.imageOnly {  },
        override val pressedStyle: ButtonStateStyle.ImageOnly = defaultStyle,
        override val disabledStyle: ButtonStateStyle.ImageOnly = defaultStyle,
    ) : WithoutText<ButtonStateStyle.ImageOnly>, WithImage<ButtonStateStyle.ImageOnly>

    data class WithImageAndText(
        override val font: KalugaFont = defaultFont,
        override val textSize: Float = 12.0f,
        override val textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
        val imageGravity: ImageGravity = ImageGravity.START,
        val spacing: Float = 0.0f,
        override val padding: Padding = Padding(),
        override val defaultStyle: ButtonStateStyle.WithImageAndText = ButtonStateStyle.withImageAndText {  },
        override val pressedStyle: ButtonStateStyle.WithImageAndText = defaultStyle,
        override val disabledStyle: ButtonStateStyle.WithImageAndText = defaultStyle,
    ) : WithText<ButtonStateStyle.WithImageAndText>, WithImage<ButtonStateStyle.WithImageAndText>

    /**
     * Gets the [StateStyle] of the button depending on the state
     * @param isEnabled if `true` the button is enabled
     * @param isPressed if `true` the button is pressed
     * @return the [StateStyle] to apply to the text of the button in the current state
     */
    fun getStateStyle(isEnabled: Boolean, isPressed: Boolean): StateStyle {
        return if (!isEnabled) {
            disabledStyle
        } else if (isPressed) {
            pressedStyle
        } else {
            defaultStyle
        }
    }
}

sealed interface ButtonImage {

    data object Hidden : ButtonImage
    @JvmInline
    value class Image(val image: KalugaImage) : ButtonImage

    @JvmInline
    value class Tinted(val image: TintedImage) : ButtonImage
}

sealed interface ImageSize {
    data object Intrinsic : ImageSize

    @JvmInline
    value class Sized private constructor(private val size: Pair<Float, Float>) : ImageSize {

        constructor(width: Float, height: Float) : this(width to height)
        val width: Float get() = size.first
        val height: Float get() = size.second
    }
}

enum class ImageGravity {
    START,
    END,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
}

data class Padding(val start: Float = 0.0f, val end: Float = 0.0f, val top: Float = 0.0f, val bottom: Float = 0.0f) {

    constructor(horizontal: Float = 0.0f, vertical: Float = 0.0f) : this(horizontal, horizontal, vertical, vertical)
}
