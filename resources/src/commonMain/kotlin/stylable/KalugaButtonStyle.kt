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
import com.splendo.kaluga.resources.defaultBoldFont
import kotlin.jvm.JvmInline

sealed interface KalugaButtonStyle<StateStyle : ButtonStateStyle> {
    val padding: Padding
    val defaultStyle: StateStyle
    val pressedStyle: StateStyle
    val disabledStyle: StateStyle

    companion object {

        @Deprecated(
            "Use KalugaButtonStyle.TextOnly",
            replaceWith = ReplaceWith(
                "KalugaButtonStyle.textOnly {\n" +
                "            this.font = font\n" +
                "            this.textSize = textSize\n" +
                "            this.textAlignment = textAlignment\n" +
                "            this.defaultStyle = defaultStyle\n" +
                "            this.pressedStyle = pressedStyle\n" +
                "            this.disabledStyle = disabledStyle\n" +
                "        }"
            )
        )
        operator fun invoke(
            font: KalugaFont,
            textSize: Float,
            textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
            defaultStyle: ButtonStateStyle.TextOnly,
            pressedStyle: ButtonStateStyle.TextOnly = defaultStyle,
            disabledStyle: ButtonStateStyle.TextOnly = defaultStyle,
        ) = textOnly {
            this.font = font
            this.textSize = textSize
            this.textAlignment = textAlignment
            this.defaultStyle = defaultStyle
            this.pressedStyle = pressedStyle
            this.disabledStyle = disabledStyle
        }

        @Deprecated("Use KalugaButtonStyle.TextOnly",
            replaceWith = ReplaceWith(
                "KalugaButtonStyle.textOnly { \n" +
                "            setTextStyle(textStyle)\n" +
                "            this.textAlignment = textAlignment\n" +
                "            defaultStyle {\n" +
                "                setBackgroundStyle(backgroundColor, shape)\n" +
                "            }\n" +
                "            pressedStyle { \n" +
                "                setBackgroundStyle(pressedBackgroundColor, shape)\n" +
                "            }\n" +
                "            disabledStyle { \n" +
                "                setBackgroundStyle(disabledBackgroundColor, shape)\n" +
                "            }\n" +
                "        }"
            )
        )
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
        ) = textOnly {
            setTextStyle(textStyle)
            this.textAlignment = textAlignment
            defaultStyle {
                setBackgroundStyle(backgroundColor, shape)
            }
            pressedStyle {
                setBackgroundStyle(pressedBackgroundColor, shape)
            }
            disabledStyle {
                setBackgroundStyle(disabledBackgroundColor, shape)
            }
        }

        fun withoutContent(dsl: KalugaButtonStyleDSL.WithoutContent.() -> Unit): WithoutContent {
            val builder = KalugaButtonStyleDSL.WithoutContent().apply(dsl)
            return WithoutContent(
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle
            )
        }

        fun textOnly(dsl: KalugaButtonStyleDSL.TextOnly.() -> Unit): TextOnly {
            val builder = KalugaButtonStyleDSL.TextOnly().apply(dsl)
            return TextOnly(
                builder.font,
                builder.textSize,
                builder.textAlignment,
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle
            )
        }

        fun imageOnly(dsl: KalugaButtonStyleDSL.ImageOnly.() -> Unit): ImageOnly {
            val builder = KalugaButtonStyleDSL.ImageOnly().apply(dsl)
            return ImageOnly(
                builder.imageSize,
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle
            )
        }


        fun withImageAndText(dsl: KalugaButtonStyleDSL.WithImageAndText.() -> Unit): WithImageAndText {
            val builder = KalugaButtonStyleDSL.WithImageAndText().apply(dsl)
            return WithImageAndText(
                builder.font,
                builder.textSize,
                builder.textAlignment,
                builder.imageSize,
                builder.imageGravity,
                builder.spacing,
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle
            )
        }
    }

    sealed interface WithoutText<StateStyle : ButtonStateStyle.WithoutText> : KalugaButtonStyle<StateStyle>
    data class WithoutContent internal constructor(
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.WithoutContent,
        override val pressedStyle: ButtonStateStyle.WithoutContent,
        override val disabledStyle: ButtonStateStyle.WithoutContent,
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

    data class TextOnly internal constructor(
        override val font: KalugaFont,
        override val textSize: Float,
        override val textAlignment: KalugaTextAlignment,
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.TextOnly,
        override val pressedStyle: ButtonStateStyle.TextOnly,
        override val disabledStyle: ButtonStateStyle.TextOnly,
    ) : WithText<ButtonStateStyle.TextOnly>

    sealed interface WithImage<StateStyle> where StateStyle : ButtonStateStyle, StateStyle : ButtonStateStyle.WithImage {
        val imageSize: ImageSize
        val defaultStyle: StateStyle
        val pressedStyle: StateStyle
        val disabledStyle: StateStyle
    }

    data class ImageOnly internal constructor(
        override val imageSize: ImageSize,
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.ImageOnly,
        override val pressedStyle: ButtonStateStyle.ImageOnly,
        override val disabledStyle: ButtonStateStyle.ImageOnly,
    ) : WithoutText<ButtonStateStyle.ImageOnly>, WithImage<ButtonStateStyle.ImageOnly>

    data class WithImageAndText internal constructor(
        override val font: KalugaFont,
        override val textSize: Float,
        override val textAlignment: KalugaTextAlignment,
        override val imageSize: ImageSize,
        val imageGravity: ImageGravity,
        val spacing: Float,
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.WithImageAndText,
        override val pressedStyle: ButtonStateStyle.WithImageAndText,
        override val disabledStyle: ButtonStateStyle.WithImageAndText,
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

sealed interface KalugaButtonStyleDSL<StateStyle : ButtonStateStyle, StateStyleDSL : ButtonStateStyleDSL> {

    var padding: Padding
    var defaultStyle: StateStyle
    var pressedStyle: StateStyle?
    var disabledStyle: StateStyle?
    fun defaultStyle(dsl: StateStyleDSL.() -> Unit)
    fun pressedStyle(dsl: StateStyleDSL.() -> Unit)
    fun disabledStyle(dsl: StateStyleDSL.() -> Unit)

    class WithoutContent internal constructor() : KalugaButtonStyleDSL<ButtonStateStyle.WithoutContent, ButtonStateStyleDSL.WithoutContent> {
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.WithoutContent = ButtonStateStyle.withoutContent {  }
        override var pressedStyle: ButtonStateStyle.WithoutContent? = null
        override var disabledStyle: ButtonStateStyle.WithoutContent? = null
        override fun defaultStyle(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        override fun pressedStyle(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        override fun disabledStyle(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }

        private fun copyAndUpdateStateStyle(
            stateStyle: ButtonStateStyle.WithoutContent,
            dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit
        ) = ButtonStateStyle.withoutContent {
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
        }
    }

    sealed interface WithText<StateStyle : ButtonStateStyle.WithText, StateStyleDSL : ButtonStateStyleDSL.WithText> : KalugaButtonStyleDSL<StateStyle, StateStyleDSL> {
        var font: KalugaFont
        var textSize: Float
        var textAlignment: KalugaTextAlignment
        fun setTextStyle(textStyle: KalugaTextStyle)
    }

    class TextOnly internal constructor() : WithText<ButtonStateStyle.TextOnly, ButtonStateStyleDSL.TextOnly> {
        override var font: KalugaFont = defaultBoldFont
        override var textSize: Float = 12.0f
        override var textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.TextOnly = ButtonStateStyle.textOnly {  }
        override var pressedStyle: ButtonStateStyle.TextOnly? = null
        override var disabledStyle: ButtonStateStyle.TextOnly? = null
        override fun defaultStyle(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        override fun pressedStyle(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        override fun disabledStyle(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }

        override fun setTextStyle(textStyle: KalugaTextStyle) {
            font = textStyle.font
            textSize = textStyle.size
            textAlignment = textStyle.alignment
            defaultStyle = defaultStyle.copy(textColor = textStyle.color)
            pressedStyle = pressedStyle?.copy(textColor = textStyle.color)
            disabledStyle = disabledStyle?.copy(textColor = textStyle.color)
        }

        private fun copyAndUpdateStateStyle(
            stateStyle: ButtonStateStyle.TextOnly,
            dsl: ButtonStateStyleDSL.TextOnly.() -> Unit
        ) = ButtonStateStyle.textOnly {
            textColor = stateStyle.textColor
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
        }
    }

    sealed interface WithImage<StateStyle : ButtonStateStyle.WithImage, StateStyleDSL : ButtonStateStyleDSL.WithImage> : KalugaButtonStyleDSL<StateStyle, StateStyleDSL> {
        var imageSize: ImageSize
    }

    class ImageOnly internal constructor() : WithImage<ButtonStateStyle.ImageOnly, ButtonStateStyleDSL.ImageOnly> {
        override var imageSize: ImageSize = ImageSize.Intrinsic
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.ImageOnly = ButtonStateStyle.imageOnly {  }
        override var pressedStyle: ButtonStateStyle.ImageOnly? = null
        override var disabledStyle: ButtonStateStyle.ImageOnly? = null
        override fun defaultStyle(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        override fun pressedStyle(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        override fun disabledStyle(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }
        private fun copyAndUpdateStateStyle(
            stateStyle: ButtonStateStyle.ImageOnly,
            dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit
        ) = ButtonStateStyle.imageOnly {
            image = stateStyle.image
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
        }
    }

    class WithImageAndText internal constructor() : WithText<ButtonStateStyle.WithImageAndText, ButtonStateStyleDSL.WithImageAndText>, WithImage<ButtonStateStyle.WithImageAndText, ButtonStateStyleDSL.WithImageAndText> {
        override var font: KalugaFont = defaultBoldFont
        override var textSize: Float = 12.0f
        override var textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER
        override var imageSize: ImageSize = ImageSize.Intrinsic
        var imageGravity: ImageGravity = ImageGravity.START
        var spacing: Float = 0.0f
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.WithImageAndText = ButtonStateStyle.withImageAndText {  }
        override var pressedStyle: ButtonStateStyle.WithImageAndText? = null
        override var disabledStyle: ButtonStateStyle.WithImageAndText? = null
        override fun defaultStyle(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        override fun pressedStyle(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        override fun disabledStyle(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }

        override fun setTextStyle(textStyle: KalugaTextStyle) {
            font = textStyle.font
            textSize = textStyle.size
            textAlignment = textStyle.alignment
            defaultStyle = defaultStyle.copy(textColor = textStyle.color)
            pressedStyle = pressedStyle?.copy(textColor = textStyle.color)
            disabledStyle = disabledStyle?.copy(textColor = textStyle.color)
        }

        private fun copyAndUpdateStateStyle(
            stateStyle: ButtonStateStyle.WithImageAndText,
            dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit
        ) = ButtonStateStyle.withImageAndText {
            textColor = stateStyle.textColor
            image = stateStyle.image
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
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

    companion object {
        val defaultButtonPadding = Padding(horizontal = 12.0f, vertical = 8.0f)
    }

    constructor(horizontal: Float = 0.0f, vertical: Float = 0.0f) : this(horizontal, horizontal, vertical, vertical)
}
