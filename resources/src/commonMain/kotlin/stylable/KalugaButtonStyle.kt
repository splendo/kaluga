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

/**
 * The style to apply to a [com.splendo.kaluga.resources.view.KalugaButton]
 * @property StateStyle the [ButtonStateStyle] that [defaultStyle], [pressedStyle] and [disabledStyle] are in
 * @property padding the [Padding] of the content of the button
 * @property defaultStyle the [StateStyle] that the button is in by default
 * @property pressedStyle the [StateStyle] that the button is in when pressed
 * @property disabledStyle the [StateStyle] that the button is in when disabled
 */
sealed interface KalugaButtonStyle<StateStyle : ButtonStateStyle> {
    val padding: Padding
    val defaultStyle: StateStyle
    val pressedStyle: StateStyle
    val disabledStyle: StateStyle

    companion object {

        /**
         * Creates a [KalugaButtonStyle.TextOnly]
         * @param font the [KalugaFont] that the text is in
         * @param textSize the size of the text
         * @param textAlignment the [KalugaTextAlignment] of the text
         * @param defaultStyle the [ButtonStateStyle.TextOnly]  that the button is in by default
         * @param pressedStyle the [ButtonStateStyle.TextOnly]  that the button is in when pressed
         * @param disabledStyle the [ButtonStateStyle.TextOnly]  that the button is in when disabled
         */
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
                    "        }",
            ),
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

        /**
         * Creates a [KalugaButtonStyle.TextOnly]
         * @param textStyle the [KalugaTextStyle] that the text is in
         * @param textAlignment the [KalugaTextAlignment] of the text
         * @param backgroundColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle] by default
         * @param pressedBackgroundColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle] when pressed
         * @param disabledBackgroundColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle] when disabled
         * @param shape the [KalugaBackgroundStyle.Shape] of the [KalugaBackgroundStyle]
         * @return a [ButtonStateStyle.TextOnly]
         */
        @Deprecated(
            "Use KalugaButtonStyle.TextOnly",
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
                    "        }",
            ),
        )
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

        /**
         * Creates a [KalugaButtonStyle.WithoutContent]
         * @param dsl configures the [KalugaButtonStyle.WithoutContent] using a [KalugaButtonStyleDSL.WithoutContent]
         * @return a [KalugaButtonStyle.WithoutContent]
         */
        fun withoutContent(dsl: KalugaButtonStyleDSL.WithoutContent.() -> Unit): WithoutContent {
            val builder = KalugaButtonStyleDSL.WithoutContent().apply(dsl)
            return WithoutContent(
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle,
            )
        }

        /**
         * Creates a [KalugaButtonStyle.TextOnly]
         * @param dsl configures the [KalugaButtonStyle.TextOnly] using a [KalugaButtonStyleDSL.TextOnly]
         * @return a [KalugaButtonStyle.TextOnly]
         */
        fun textOnly(dsl: KalugaButtonStyleDSL.TextOnly.() -> Unit): TextOnly {
            val builder = KalugaButtonStyleDSL.TextOnly().apply(dsl)
            return TextOnly(
                builder.font,
                builder.textSize,
                builder.textAlignment,
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle,
            )
        }

        /**
         * Creates a [KalugaButtonStyle.ImageOnly]
         * @param dsl configures the [KalugaButtonStyle.ImageOnly] using a [KalugaButtonStyleDSL.ImageOnly]
         * @return a [KalugaButtonStyle.ImageOnly]
         */
        fun imageOnly(dsl: KalugaButtonStyleDSL.ImageOnly.() -> Unit): ImageOnly {
            val builder = KalugaButtonStyleDSL.ImageOnly().apply(dsl)
            return ImageOnly(
                builder.imageSize,
                builder.padding,
                builder.defaultStyle,
                builder.pressedStyle ?: builder.defaultStyle,
                builder.disabledStyle ?: builder.defaultStyle,
            )
        }

        /**
         * Creates a [KalugaButtonStyle.WithImageAndText]
         * @param dsl configures the [KalugaButtonStyle.WithImageAndText] using a [KalugaButtonStyleDSL.WithImageAndText]
         * @return a [KalugaButtonStyle.WithImageAndText]
         */
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
                builder.disabledStyle ?: builder.defaultStyle,
            )
        }

        internal val hiddenTextStyle = textOnly {
            textSize = 0.0f
            defaultStyle {
                textColor = DefaultColors.clear
            }
        }
    }

    /**
     * A [KalugaButtonStyle] for a [com.splendo.kaluga.resources.view.KalugaButton.WithoutText]
     * @property StateStyle the [ButtonStateStyle.WithoutText] that [defaultStyle], [pressedStyle] and [disabledStyle] are in
     */
    sealed interface WithoutText<StateStyle : ButtonStateStyle.WithoutText> : KalugaButtonStyle<StateStyle>

    /**
     * A [KalugaButtonStyle.WithoutText] that does not display any content
     * @property defaultStyle the [ButtonStateStyle.WithoutContent] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.WithoutContent] that the button is in when pressed
     * @property disabledStyle the [ButtonStateStyle.WithoutContent] that the button is in when disabled
     */
    data class WithoutContent internal constructor(
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.WithoutContent,
        override val pressedStyle: ButtonStateStyle.WithoutContent,
        override val disabledStyle: ButtonStateStyle.WithoutContent,
    ) : WithoutText<ButtonStateStyle.WithoutContent>

    /**
     * A [KalugaButtonStyle] for a [com.splendo.kaluga.resources.view.KalugaButton.WithText]
     * @property font the [KalugaFont] that the text is in
     * @property textSize the size of the text
     * @property textAlignment the [KalugaTextAlignment] of the text
     */
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

    /**
     * A [KalugaButtonStyle.WithText] that only displays text
     * @property defaultStyle the [ButtonStateStyle.TextOnly] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.TextOnly] that the button is in when pressed
     * @property disabledStyle the [ButtonStateStyle.TextOnly] that the button is in when disabled
     */
    data class TextOnly internal constructor(
        override val font: KalugaFont,
        override val textSize: Float,
        override val textAlignment: KalugaTextAlignment,
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.TextOnly,
        override val pressedStyle: ButtonStateStyle.TextOnly,
        override val disabledStyle: ButtonStateStyle.TextOnly,
    ) : WithText<ButtonStateStyle.TextOnly>

    /**
     * A [KalugaButtonStyle] that adds an image to the [com.splendo.kaluga.resources.view.KalugaButton]
     * @property StateStyle the [ButtonStateStyle.WithImage] that [defaultStyle], [pressedStyle] and [disabledStyle] are in
     * @property imageSize the [ImageSize] of the [ButtonImage]
     */
    sealed interface WithImage<StateStyle : ButtonStateStyle.WithImage> : KalugaButtonStyle<StateStyle> {
        val imageSize: ImageSize

        /**
         * Gets the [ButtonImage] of the button depending on the state
         * @param isEnabled if `true` the button is enabled
         * @param isPressed if `true` the button is pressed
         * @return the [ButtonImage] to set as the image of the button in the current state
         */
        fun getStateImage(isEnabled: Boolean, isPressed: Boolean) = getStateStyle(isEnabled, isPressed).image
    }

    /**
     * A [KalugaButtonStyle.WithImage] that only displays a [ButtonImage]
     * @property defaultStyle the [ButtonStateStyle.ImageOnly] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.ImageOnly] that the button is in when pressed
     * @property disabledStyle the [ButtonStateStyle.ImageOnly] that the button is in when disabled
     */
    data class ImageOnly internal constructor(
        override val imageSize: ImageSize,
        override val padding: Padding,
        override val defaultStyle: ButtonStateStyle.ImageOnly,
        override val pressedStyle: ButtonStateStyle.ImageOnly,
        override val disabledStyle: ButtonStateStyle.ImageOnly,
    ) : WithoutText<ButtonStateStyle.ImageOnly>,
        WithImage<ButtonStateStyle.ImageOnly>

    /**
     * A [KalugaButtonStyle.WithText] and [KalugaButtonStyle.WithImage] that displays both text and a [ButtonImage]
     * @property imageGravity the [ImageGravity] to determine placement of the [ButtonImage] relative to the text
     * @property spacing the spacing between the [ButtonImage] and text
     * @property defaultStyle the [ButtonStateStyle.WithImageAndText] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.WithImageAndText] that the button is in when pressed
     * @property disabledStyle the [ButtonStateStyle.WithImageAndText] that the button is in when disabled
     */
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
    ) : WithText<ButtonStateStyle.WithImageAndText>,
        WithImage<ButtonStateStyle.WithImageAndText>

    /**
     * Gets the [StateStyle] of the button depending on the state
     * @param isEnabled if `true` the button is enabled
     * @param isPressed if `true` the button is pressed
     * @return the [StateStyle] to apply to the text of the button in the current state
     */
    fun getStateStyle(isEnabled: Boolean, isPressed: Boolean): StateStyle = if (!isEnabled) {
        disabledStyle
    } else if (isPressed) {
        pressedStyle
    } else {
        defaultStyle
    }
}

/**
 * DSL for creating a [KalugaButtonStyle]
 * @property StateStyle the [ButtonStateStyle] associated with the [KalugaButtonStyle] to create
 * @property StateStyleDSL the [ButtonStateStyleDSL] for creating the [ButtonStateStyle]
 */
sealed interface KalugaButtonStyleDSL<StateStyle : ButtonStateStyle, StateStyleDSL : ButtonStateStyleDSL> {

    /**
     * Configured the [Padding] of the content of the button
     */
    var padding: Padding

    /**
     * the [StateStyle] that the button is in by default
     */
    var defaultStyle: StateStyle

    /**
     * the [StateStyle] that the button is in when pressed.
     * If `null` [defaultStyle] will be used
     */
    var pressedStyle: StateStyle?

    /**
     * the [StateStyle] that the button is in when disabled.
     * If `null` [defaultStyle] will be used
     */
    var disabledStyle: StateStyle?

    /**
     * Sets [defaultStyle] using the creation using a [StateStyleDSL]
     * The [StateStyleDSL] will be preconfigured with the layout of the previous [defaultStyle]
     * @param dsl configures the [StateStyle] for [defaultStyle]. Will be preconfigured with the last known [defaultStyle]
     */
    fun defaultStyle(dsl: StateStyleDSL.() -> Unit)

    /**
     * Sets [pressedStyle] using the creation using a [StateStyleDSL]
     * The [StateStyleDSL] will be preconfigured with the layout of the previous [pressedStyle] or [defaultStyle] if `null`
     * @param dsl configures the [StateStyle] for [pressedStyle]. Will be preconfigured with the last known [pressedStyle] of [defaultStyle] if `null`
     */
    fun pressedStyle(dsl: StateStyleDSL.() -> Unit)

    /**
     * Sets [disabledStyle] using the creation using a [StateStyleDSL]
     * The [StateStyleDSL] will be preconfigured with the layout of the previous [disabledStyle] or [defaultStyle] if `null`
     * @param dsl configures the [StateStyle] for [disabledStyle]. Will be preconfigured with the last known [disabledStyle] of [defaultStyle] if `null`
     */
    fun disabledStyle(dsl: StateStyleDSL.() -> Unit)

    /**
     * Sets [backgroundStyle] to a [KalugaBackgroundStyle] with [KalugaBackgroundStyle.FillStyle.Solid] based on a [KalugaColor] and [shape]
     * @param defaultColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle] by default
     * @param pressedColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle] when pressed
     * @param disabledColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle] when disabled
     * @param shape the [KalugaBackgroundStyle.Shape] of the [KalugaBackgroundStyle]
     */
    fun setBackground(
        defaultColor: KalugaColor,
        pressedColor: KalugaColor = defaultColor,
        disabledColor: KalugaColor = defaultColor,
        shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle(),
    ) {
        defaultStyle {
            setBackgroundStyle(defaultColor, shape)
        }
        pressedStyle {
            setBackgroundStyle(pressedColor, shape)
        }
        disabledStyle {
            setBackgroundStyle(disabledColor, shape)
        }
    }

    /**
     * A [KalugaButtonStyleDSL] for creating a [KalugaButtonStyle.WithoutContent]
     * @property defaultStyle the [ButtonStateStyle.WithoutContent] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.WithoutContent] that the button is in when pressed. If `null` [defaultStyle] will be used.
     * @property disabledStyle the [ButtonStateStyle.WithoutContent] that the button is in when disabled. If `null` [defaultStyle] will be used.
     */
    class WithoutContent internal constructor() : KalugaButtonStyleDSL<ButtonStateStyle.WithoutContent, ButtonStateStyleDSL.WithoutContent> {
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.WithoutContent = ButtonStateStyle.withoutContent { }
        override var pressedStyle: ButtonStateStyle.WithoutContent? = null
        override var disabledStyle: ButtonStateStyle.WithoutContent? = null

        /**
         * Sets [defaultStyle] using the creation using a [ButtonStateStyleDSL.WithoutContent]
         * The [ButtonStateStyleDSL.WithoutContent] will be preconfigured with the layout of the previous [defaultStyle]
         * @param dsl configures the [ButtonStateStyleDSL.WithoutContent] for [defaultStyle]. Will be preconfigured with the last known [defaultStyle]
         */
        override fun defaultStyle(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        /**
         * Sets [pressedStyle] using the creation using a [ButtonStateStyleDSL.WithoutContent]
         * The [ButtonStateStyleDSL.WithoutContent] will be preconfigured with the layout of the previous [pressedStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.WithoutContent] for [pressedStyle]. Will be preconfigured with the last known [pressedStyle] of [defaultStyle] if `null`
         */
        override fun pressedStyle(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        /**
         * Sets [disabledStyle] using the creation using a [ButtonStateStyleDSL.WithoutContent]
         * The [ButtonStateStyleDSL.WithoutContent] will be preconfigured with the layout of the previous [disabledStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.WithoutContent] for [disabledStyle]. Will be preconfigured with the last known [disabledStyle] of [defaultStyle] if `null`
         */
        override fun disabledStyle(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }

        private fun copyAndUpdateStateStyle(stateStyle: ButtonStateStyle.WithoutContent, dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit) = ButtonStateStyle.withoutContent {
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
        }
    }

    /**
     * A [KalugaButtonStyleDSL] for creating a [KalugaButtonStyle.WithText]
     * @property StateStyle the [ButtonStateStyle.WithText] associated with the [KalugaButtonStyle.WithText] to create
     * @property StateStyleDSL the [ButtonStateStyleDSL.WithText] for creating the [ButtonStateStyle.WithText]
     * @property font the [KalugaFont] that the text is in
     * @property textSize the size of the text
     * @property textAlignment the [KalugaTextAlignment] of the text
     */
    sealed interface WithText<StateStyle : ButtonStateStyle.WithText, StateStyleDSL : ButtonStateStyleDSL.WithText> : KalugaButtonStyleDSL<StateStyle, StateStyleDSL> {

        var font: KalugaFont
        var textSize: Float
        var textAlignment: KalugaTextAlignment

        /**
         * Sets [font], [textSize], and [textAlignment] according to a [KalugaTextStyle] and [ButtonStateStyle.WithText.textColor] for all states
         * @param textStyle the [KalugaTextStyle] to configure the button with
         * @param pressedColor the [KalugaColor] to apply to [pressedStyle]
         * @param disabledColor the [KalugaColor] to apply to [disabledColor]
         */
        fun setTextStyle(textStyle: KalugaTextStyle, pressedColor: KalugaColor = textStyle.color, disabledColor: KalugaColor = textStyle.color) {
            font = textStyle.font
            textSize = textStyle.size
            textAlignment = textStyle.alignment
            defaultStyle { textColor = textStyle.color }
            pressedStyle { textColor = pressedColor }
            disabledStyle { textColor = disabledColor }
        }
    }

    /**
     * A [KalugaButtonStyleDSL.WithText] for creating a [KalugaButtonStyle.TextOnly]
     * @property defaultStyle the [ButtonStateStyle.TextOnly] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.TextOnly] that the button is in when pressed. If `null` [defaultStyle] will be used.
     * @property disabledStyle the [ButtonStateStyle.TextOnly] that the button is in when disabled. If `null` [defaultStyle] will be used.
     */
    class TextOnly internal constructor() : WithText<ButtonStateStyle.TextOnly, ButtonStateStyleDSL.TextOnly> {
        private var _font: KalugaFont? = null
        override var font: KalugaFont get() = _font ?: defaultBoldFont
            set(value) {
                _font = value
            }
        override var textSize: Float = 12.0f
        override var textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.TextOnly = ButtonStateStyle.textOnly { }
        override var pressedStyle: ButtonStateStyle.TextOnly? = null
        override var disabledStyle: ButtonStateStyle.TextOnly? = null

        /**
         * Sets [defaultStyle] using the creation using a [ButtonStateStyleDSL.TextOnly]
         * The [ButtonStateStyleDSL.TextOnly] will be preconfigured with the layout of the previous [defaultStyle]
         * @param dsl configures the [ButtonStateStyleDSL.TextOnly] for [defaultStyle]. Will be preconfigured with the last known [defaultStyle]
         */
        override fun defaultStyle(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        /**
         * Sets [pressedStyle] using the creation using a [ButtonStateStyleDSL.TextOnly]
         * The [ButtonStateStyleDSL.TextOnly] will be preconfigured with the layout of the previous [pressedStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.TextOnly] for [pressedStyle]. Will be preconfigured with the last known [pressedStyle] of [defaultStyle] if `null`
         */
        override fun pressedStyle(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        /**
         * Sets [disabledStyle] using the creation using a [ButtonStateStyleDSL.TextOnly]
         * The [ButtonStateStyleDSL.TextOnly] will be preconfigured with the layout of the previous [disabledStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.TextOnly] for [disabledStyle]. Will be preconfigured with the last known [disabledStyle] of [defaultStyle] if `null`
         */
        override fun disabledStyle(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }

        private fun copyAndUpdateStateStyle(stateStyle: ButtonStateStyle.TextOnly, dsl: ButtonStateStyleDSL.TextOnly.() -> Unit) = ButtonStateStyle.textOnly {
            textColor = stateStyle.textColor
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
        }
    }

    /**
     * A [KalugaButtonStyleDSL] for creating a [KalugaButtonStyle.WithImage]
     * @property StateStyle the [ButtonStateStyle.WithImage] associated with the [KalugaButtonStyle.WithImage] to create
     * @property StateStyleDSL the [ButtonStateStyleDSL.WithImage] for creating the [ButtonStateStyle.WithImage]
     * @property imageSize the [ImageSize] of the [ButtonImage]
     */
    sealed interface WithImage<StateStyle : ButtonStateStyle.WithImage, StateStyleDSL : ButtonStateStyleDSL.WithImage> : KalugaButtonStyleDSL<StateStyle, StateStyleDSL> {
        var imageSize: ImageSize

        /**
         * Sets the [ButtonStateStyle.WithImage.image] to a [ButtonImage.Image] for all states
         * @param image the [KalugaImage] to set as the image
         */
        fun setImage(image: KalugaImage) {
            defaultStyle { setImage(image) }
            pressedStyle { setImage(image) }
            disabledStyle { setImage(image) }
        }

        /**
         * Sets the [ButtonStateStyle.WithImage.image] to a [ButtonImage.Tinted] for all states
         * @param image the [TintedImage] to set as the image
         * @param pressedTint the [KalugaColor] to set the image as when pressed
         * @param disabledTint the [KalugaColor] to set the image as when disabled
         */
        fun setImage(image: TintedImage, pressedTint: KalugaColor = image.tint, disabledTint: KalugaColor = image.tint) {
            defaultStyle { setImage(image) }
            pressedStyle { setImage(image.image, pressedTint) }
            disabledStyle { setImage(image.image, disabledTint) }
        }

        /**
         * Sets the [ButtonStateStyle.WithImage.image] to a [ButtonImage.Tinted] for all states
         * @param image the [KalugaImage] to tint
         * @param defaultTint the [KalugaColor] to set the image as by default
         * @param pressedTint the [KalugaColor] to set the image as when pressed
         * @param disabledTint the [KalugaColor] to set the image as when disabled
         */
        fun setImage(image: KalugaImage, defaultTint: KalugaColor, pressedTint: KalugaColor = defaultTint, disabledTint: KalugaColor = defaultTint) {
            defaultStyle { setImage(image, defaultTint) }
            pressedStyle { setImage(image, pressedTint) }
            disabledStyle { setImage(image, disabledTint) }
        }
    }

    /**
     * A [KalugaButtonStyleDSL.WithImage] for creating a [KalugaButtonStyle.ImageOnly]
     * @property defaultStyle the [ButtonStateStyle.ImageOnly] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.ImageOnly] that the button is in when pressed. If `null` [defaultStyle] will be used.
     * @property disabledStyle the [ButtonStateStyle.ImageOnly] that the button is in when disabled. If `null` [defaultStyle] will be used.
     */
    class ImageOnly internal constructor() : WithImage<ButtonStateStyle.ImageOnly, ButtonStateStyleDSL.ImageOnly> {
        override var imageSize: ImageSize = ImageSize.Intrinsic
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.ImageOnly = ButtonStateStyle.imageOnly { }
        override var pressedStyle: ButtonStateStyle.ImageOnly? = null
        override var disabledStyle: ButtonStateStyle.ImageOnly? = null

        /**
         * Sets [defaultStyle] using the creation using a [ButtonStateStyleDSL.ImageOnly]
         * The [ButtonStateStyleDSL.ImageOnly] will be preconfigured with the layout of the previous [defaultStyle]
         * @param dsl configures the [ButtonStateStyleDSL.ImageOnly] for [defaultStyle]. Will be preconfigured with the last known [defaultStyle]
         */
        override fun defaultStyle(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        /**
         * Sets [pressedStyle] using the creation using a [ButtonStateStyleDSL.ImageOnly]
         * The [ButtonStateStyleDSL.ImageOnly] will be preconfigured with the layout of the previous [pressedStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.ImageOnly] for [pressedStyle]. Will be preconfigured with the last known [pressedStyle] of [defaultStyle] if `null`
         */
        override fun pressedStyle(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        /**
         * Sets [disabledStyle] using the creation using a [ButtonStateStyleDSL.ImageOnly]
         * The [ButtonStateStyleDSL.ImageOnly] will be preconfigured with the layout of the previous [disabledStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.ImageOnly] for [disabledStyle]. Will be preconfigured with the last known [disabledStyle] of [defaultStyle] if `null`
         */
        override fun disabledStyle(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }
        private fun copyAndUpdateStateStyle(stateStyle: ButtonStateStyle.ImageOnly, dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit) = ButtonStateStyle.imageOnly {
            image = stateStyle.image
            backgroundStyle = stateStyle.backgroundStyle
            apply(dsl)
        }
    }

    /**
     * A [KalugaButtonStyleDSL.WithText] and [KalugaButtonStyleDSL.WithImage] for creating a [KalugaButtonStyle.WithImageAndText]
     * @property imageGravity the [ImageGravity] to determine placement of the [ButtonImage] relative to the text
     * @property spacing the spacing between the [ButtonImage] and text
     * @property defaultStyle the [ButtonStateStyle.WithImageAndText] that the button is in by default
     * @property pressedStyle the [ButtonStateStyle.WithImageAndText] that the button is in when pressed. If `null` [defaultStyle] will be used.
     * @property disabledStyle the [ButtonStateStyle.WithImageAndText] that the button is in when disabled. If `null` [defaultStyle] will be used.
     */
    class WithImageAndText internal constructor() :
        WithText<ButtonStateStyle.WithImageAndText, ButtonStateStyleDSL.WithImageAndText>,
        WithImage<ButtonStateStyle.WithImageAndText, ButtonStateStyleDSL.WithImageAndText> {
        private var _font: KalugaFont? = null
        override var font: KalugaFont get() = _font ?: defaultBoldFont
            set(value) {
                _font = value
            }
        override var textSize: Float = 12.0f
        override var textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER
        override var imageSize: ImageSize = ImageSize.Intrinsic
        var imageGravity: ImageGravity = ImageGravity.START
        var spacing: Float = 0.0f
        override var padding: Padding = Padding.defaultButtonPadding
        override var defaultStyle: ButtonStateStyle.WithImageAndText = ButtonStateStyle.withImageAndText { }
        override var pressedStyle: ButtonStateStyle.WithImageAndText? = null
        override var disabledStyle: ButtonStateStyle.WithImageAndText? = null

        /**
         * Sets [defaultStyle] using the creation using a [ButtonStateStyleDSL.WithImageAndText]
         * The [ButtonStateStyleDSL.WithImageAndText] will be preconfigured with the layout of the previous [defaultStyle]
         * @param dsl configures the [ButtonStateStyleDSL.WithImageAndText] for [defaultStyle]. Will be preconfigured with the last known [defaultStyle]
         */
        override fun defaultStyle(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) {
            defaultStyle = copyAndUpdateStateStyle(defaultStyle, dsl)
        }

        /**
         * Sets [pressedStyle] using the creation using a [ButtonStateStyleDSL.WithImageAndText]
         * The [ButtonStateStyleDSL.WithImageAndText] will be preconfigured with the layout of the previous [pressedStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.WithImageAndText] for [pressedStyle]. Will be preconfigured with the last known [pressedStyle] of [defaultStyle] if `null`
         */
        override fun pressedStyle(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) {
            pressedStyle = copyAndUpdateStateStyle(pressedStyle ?: defaultStyle, dsl)
        }

        /**
         * Sets [disabledStyle] using the creation using a [ButtonStateStyleDSL.WithImageAndText]
         * The [ButtonStateStyleDSL.WithImageAndText] will be preconfigured with the layout of the previous [disabledStyle] or [defaultStyle] if `null`
         * @param dsl configures the [ButtonStateStyleDSL.WithImageAndText] for [disabledStyle]. Will be preconfigured with the last known [disabledStyle] of [defaultStyle] if `null`
         */
        override fun disabledStyle(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) {
            disabledStyle = copyAndUpdateStateStyle(disabledStyle ?: defaultStyle, dsl)
        }

        private fun copyAndUpdateStateStyle(stateStyle: ButtonStateStyle.WithImageAndText, dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit) =
            ButtonStateStyle.withImageAndText {
                textColor = stateStyle.textColor
                image = stateStyle.image
                backgroundStyle = stateStyle.backgroundStyle
                apply(dsl)
            }
    }
}
