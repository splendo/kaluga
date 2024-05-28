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

sealed interface ButtonStateStyle {

    companion object {

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

        fun withoutContent(dsl: ButtonStateStyleDSL.WithoutContent.() -> Unit): WithoutContent {
            val builder = ButtonStateStyleDSL.WithoutContent().apply(dsl)
            return WithoutContent(builder.backgroundStyle)
        }

        fun textOnly(dsl: ButtonStateStyleDSL.TextOnly.() -> Unit): TextOnly {
            val builder = ButtonStateStyleDSL.TextOnly().apply(dsl)
            return TextOnly(builder.textColor, builder.backgroundStyle)
        }

        fun imageOnly(dsl: ButtonStateStyleDSL.ImageOnly.() -> Unit): ImageOnly {
            val builder = ButtonStateStyleDSL.ImageOnly().apply(dsl)
            return ImageOnly(builder.image, builder.backgroundStyle)
        }

        fun withImageAndText(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit): WithImageAndText {
            val builder = ButtonStateStyleDSL.WithImageAndText().apply(dsl)
            return WithImageAndText(builder.textColor, builder.image, builder.backgroundStyle)
        }
    }

    val backgroundStyle: KalugaBackgroundStyle

    sealed interface WithoutText : ButtonStateStyle
    data class WithoutContent internal constructor(override val backgroundStyle: KalugaBackgroundStyle) : WithoutText
    sealed interface WithText : ButtonStateStyle {
        val textColor: KalugaColor
    }

    data class TextOnly internal constructor(
        override val textColor: KalugaColor,
        override val backgroundStyle: KalugaBackgroundStyle,
    ) : WithText

    sealed interface WithImage : ButtonStateStyle {
        val image: ButtonImage
    }

    data class ImageOnly internal constructor(
        override val image: ButtonImage,
        override val backgroundStyle: KalugaBackgroundStyle,
    ) : WithoutText, WithImage

    data class WithImageAndText internal constructor(
        override val textColor: KalugaColor,
        override val image: ButtonImage,
        override val backgroundStyle: KalugaBackgroundStyle,
    ) : WithText, WithImage
}
sealed interface ButtonStateStyleDSL {

    private companion object {
        fun createBackgroundStyle(backgroundColor: KalugaColor = DefaultColors.clear, shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle()) =
            KalugaBackgroundStyle(
                KalugaBackgroundStyle.FillStyle.Solid(backgroundColor),
                shape = shape,
            )
    }

    var backgroundStyle: KalugaBackgroundStyle

    fun setBackgroundStyle(backgroundColor: KalugaColor = DefaultColors.clear, shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle()) {
        backgroundStyle = createBackgroundStyle(backgroundColor, shape)
    }

    class WithoutContent internal constructor() : ButtonStateStyleDSL {
        override var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()
    }

    sealed interface WithText : ButtonStateStyleDSL {
        var textColor: KalugaColor
    }

    class TextOnly internal constructor() : WithText {
        override var textColor: KalugaColor = DefaultColors.black
        override var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()
    }

    sealed class WithImage : ButtonStateStyleDSL {
        internal var image: ButtonImage = ButtonImage.Hidden
        override var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()

        fun hide() {
            image = ButtonImage.Hidden
        }

        fun setImage(image: KalugaImage) {
            this.image = ButtonImage.Image(image)
        }

        fun setImage(image: TintedImage) {
            this.image = ButtonImage.Tinted(image)
        }

        fun setImage(image: KalugaImage, tint: KalugaColor) {
            this.image = ButtonImage.Tinted(image.tinted(tint))
        }
    }

    class ImageOnly internal constructor() : WithImage()

    class WithImageAndText internal constructor() : WithImage(), WithText {
        override var textColor: KalugaColor = DefaultColors.black
    }
}
