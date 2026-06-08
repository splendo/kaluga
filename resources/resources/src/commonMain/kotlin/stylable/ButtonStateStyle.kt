/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.TintedImage
import com.splendo.kaluga.resources.tinted

/**
 * The State of a [KalugaButtonStyle]
 * This manages the looks of a button depending on its state.
 * @see [KalugaButtonStyle.defaultStyle]
 * @see [KalugaButtonStyle.pressedStyle]
 * @see [KalugaButtonStyle.disabledStyle]
 */
sealed interface ButtonStateStyle {

    companion object {

        /**
         * Creates a [ButtonStateStyle.TextOnly] for a given [textColor] and [backgroundStyle]
         * @param textColor the [KalugaColor] to set as [ButtonStateStyle.TextOnly.textColor]
         * @param backgroundStyle the [KalugaBackgroundStyle] to set as [ButtonStateStyle.TextOnly.backgroundStyle]
         * @return a [ButtonStateStyle.TextOnly]
         */
        @Deprecated(
            "Use ButtonStateStyle.textOnly",
            replaceWith = ReplaceWith(
                "ButtonStateStyle.textOnly {\n" +
                    "            this.textColor = textColor\n" +
                    "            this.backgroundStyle = backgroundStyle\n" +
                    "        }",
            ),
        )
        operator fun invoke(textColor: KalugaColor, backgroundStyle: KalugaBackgroundStyle) = textOnly {
            this.textColor = textColor
            this.backgroundStyle = backgroundStyle
        }

        /**
         * Creates a [ButtonStateStyle.TextOnly] for a given [textColor] and [backgroundColor] and [shape]
         * @param textColor the [KalugaColor] to set as [ButtonStateStyle.TextOnly.textColor]
         * @param backgroundColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle]
         * @param shape the [KalugaBackgroundStyle.Shape] of the [KalugaBackgroundStyle]
         * @return a [ButtonStateStyle.TextOnly]
         */
        @Deprecated(
            "Use ButtonStateStyle.textOnly",
            replaceWith = ReplaceWith(
                "ButtonStateStyle.textOnly{\n" +
                    "            this.textColor = textColor\n" +
                    "            setBackgroundStyle(backgroundColor, shape)\n" +
                    "        }",
            ),
        )
        operator fun invoke(
            textColor: KalugaColor,
            backgroundColor: KalugaColor = DefaultColors.clear,
            shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle(),
        ) = textOnly {
            this.textColor = textColor
            setBackgroundStyle(backgroundColor, shape)
        }

        /**
         * Creates a [ButtonStateStyle.WithoutContent]
         * @param dsl configures the [ButtonStateStyle.WithoutContent] using a [ButtonStateStyleDSL.WithoutContent]
         * @return a [ButtonStateStyle.WithoutContent]
         */
        fun withoutContent(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit): WithoutContent {
            val builder = ButtonStateStyleDSL.WithoutContent().apply(dsl)
            return WithoutContent(builder.backgroundStyle)
        }

        /**
         * Creates a [ButtonStateStyle.TextOnly]
         * @param dsl configures the [ButtonStateStyle.TextOnly] using a [ButtonStateStyleDSL.TextOnly]
         * @return a [ButtonStateStyle.TextOnly]
         */
        fun textOnly(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit): TextOnly {
            val builder = ButtonStateStyleDSL.TextOnly().apply(dsl)
            return TextOnly(builder.textColor, builder.backgroundStyle)
        }

        /**
         * Creates a [ButtonStateStyle.ImageOnly]
         * @param dsl configures the [ButtonStateStyle.ImageOnly] using a [ButtonStateStyleDSL.ImageOnly]
         * @return a [ButtonStateStyle.ImageOnly]
         */
        fun imageOnly(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit): ImageOnly {
            val builder = ButtonStateStyleDSL.ImageOnly().apply(dsl)
            return ImageOnly(builder.image, builder.backgroundStyle)
        }

        /**
         * Creates a [ButtonStateStyle.WithImageAndText]
         * @param dsl configures the [ButtonStateStyle.WithImageAndText] using a [ButtonStateStyleDSL.WithImageAndText]
         * @return a [ButtonStateStyle.WithImageAndText]
         */
        fun withImageAndText(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit): WithImageAndText {
            val builder = ButtonStateStyleDSL.WithImageAndText().apply(dsl)
            return WithImageAndText(builder.textColor, builder.image, builder.backgroundStyle)
        }
    }

    /**
     * The [KalugaBackgroundStyle] of the button in the state
     */
    val backgroundStyle: KalugaBackgroundStyle

    /**
     * A [ButtonStateStyle] for a [KalugaButtonStyle.WithoutText]
     */
    sealed interface WithoutText : ButtonStateStyle

    /**
     * A [ButtonStateStyle.WithoutText] for a [KalugaButtonStyle.WithoutContent]
     */
    data class WithoutContent internal constructor(override val backgroundStyle: KalugaBackgroundStyle) : WithoutText

    /**
     * A [ButtonStateStyle] for a [KalugaButtonStyle.WithText]
     * @property textColor the [KalugaColor] to render the text in for the given state
     */
    sealed interface WithText : ButtonStateStyle {
        val textColor: KalugaColor
    }

    /**
     * A [ButtonStateStyle.WithText] for a [KalugaButtonStyle.TextOnly]
     */
    data class TextOnly internal constructor(override val textColor: KalugaColor, override val backgroundStyle: KalugaBackgroundStyle) : WithText

    /**
     * A [ButtonStateStyle] for a [KalugaButtonStyle.WithImage]
     * @property image the [ButtonImage] to add for the given state
     */
    sealed interface WithImage : ButtonStateStyle {
        val image: ButtonImage
    }

    /**
     * A [ButtonStateStyle.WithoutText] and [KalugaButtonStyle.WithImage] for a [KalugaButtonStyle.ImageOnly]
     */
    data class ImageOnly internal constructor(override val image: ButtonImage, override val backgroundStyle: KalugaBackgroundStyle) :
        WithoutText,
        WithImage

    /**
     * A [ButtonStateStyle.WithText] and [KalugaButtonStyle.WithImage] for a [KalugaButtonStyle.WithImageAndText]
     */
    data class WithImageAndText internal constructor(override val textColor: KalugaColor, override val image: ButtonImage, override val backgroundStyle: KalugaBackgroundStyle) :
        WithText,
        WithImage
}

/**
 * DSL for creating a [ButtonStateStyle]
 */
sealed interface ButtonStateStyleDSL {

    private companion object {
        fun createBackgroundStyle(backgroundColor: KalugaColor = DefaultColors.clear, shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle()) =
            KalugaBackgroundStyle(
                KalugaBackgroundStyle.FillStyle.Solid(backgroundColor),
                shape = shape,
            )
    }

    /**
     * The [KalugaBackgroundStyle] of the button in the state
     */
    var backgroundStyle: KalugaBackgroundStyle

    /**
     * Sets [backgroundStyle] to a [KalugaBackgroundStyle] with [KalugaBackgroundStyle.FillStyle.Solid] based on a [backgroundColor] and [shape]
     * @param backgroundColor the [KalugaColor] to set as the [KalugaBackgroundStyle.FillStyle.Solid.color] of the [KalugaBackgroundStyle]
     * @param shape the [KalugaBackgroundStyle.Shape] of the [KalugaBackgroundStyle]
     */
    fun setBackgroundStyle(backgroundColor: KalugaColor = DefaultColors.clear, shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle()) {
        backgroundStyle = createBackgroundStyle(backgroundColor, shape)
    }

    /**
     * A [ButtonStateStyleDSL] for creating a [ButtonStateStyle.WithoutContent]
     */
    class WithoutContent internal constructor() : ButtonStateStyleDSL {
        override var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()
    }

    /**
     * A [ButtonStateStyleDSL] for creating a [ButtonStateStyle.WithText]
     * @property textColor the [KalugaColor] to render the text in for the given state
     */
    sealed interface WithText : ButtonStateStyleDSL {
        var textColor: KalugaColor
    }

    /**
     * A [ButtonStateStyleDSL.WithText] for creating a [ButtonStateStyle.TextOnly]
     */
    class TextOnly internal constructor() : WithText {
        override var textColor: KalugaColor = DefaultColors.black
        override var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()
    }

    /**
     * A [ButtonStateStyleDSL] for creating a [ButtonStateStyle.WithImage]
     * @property image the [ButtonImage] to add for the given state
     */
    sealed class WithImage : ButtonStateStyleDSL {
        internal var image: ButtonImage = ButtonImage.Hidden
        override var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()

        /**
         * Sets [ButtonStateStyle.WithImage.image] to [ButtonImage.Hidden]
         */
        fun hide() {
            image = ButtonImage.Hidden
        }

        /**
         * Sets [ButtonStateStyle.WithImage.image] to [ButtonImage.Image] for the given [image]
         * @param image the [KalugaImage] to set as the image of the state
         */
        fun setImage(image: KalugaImage) {
            this.image = ButtonImage.Image(image)
        }

        /**
         * Sets [ButtonStateStyle.WithImage.image] to [ButtonImage.Tinted] for the given [image]
         * @param image the [TintedImage] to set as the image of the state
         */
        fun setImage(image: TintedImage) {
            this.image = ButtonImage.Tinted(image)
        }

        /**
         * Sets [ButtonStateStyle.WithImage.image] to [ButtonImage.Tinted] for the given [image] and [tint]
         * @param image the [KalugaImage] to tint as the image of the state
         * @param tint the [KalugaColor] to tint [image] as
         */
        fun setImage(image: KalugaImage, tint: KalugaColor) {
            this.image = ButtonImage.Tinted(image.tinted(tint))
        }
    }

    /**
     * A [ButtonStateStyleDSL.WithImage] for creating a [ButtonStateStyle.ImageOnly]
     */
    class ImageOnly internal constructor() : WithImage()

    /**
     * A [ButtonStateStyleDSL.WithImage] and [ButtonStateStyleDSL.WithText] for creating a [ButtonStateStyle.WithImageAndText]
     */
    class WithImageAndText internal constructor() :
        WithImage(),
        WithText {
        override var textColor: KalugaColor = DefaultColors.black
    }
}
