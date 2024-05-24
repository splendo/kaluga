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

        @Deprecated("Use ButtonStateStyle.textOnly")
        operator fun invoke(
            textColor: KalugaColor,
            backgroundStyle: KalugaBackgroundStyle,
        ) = textOnly {
            this.textColor = textColor
            this.backgroundStyle = backgroundStyle
        }

        @Deprecated("Use ButtonStateStyle.textOnly")
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
            return ImageOnly(builder.image, builder.size, builder.backgroundStyle)
        }

        fun withImageAndText(dsl: ButtonStateStyleDSL.WithImageAndText.() -> Unit): WithImageAndText {
            val builder = ButtonStateStyleDSL.WithImageAndText().apply(dsl)
            return WithImageAndText(builder.textColor, builder.image, builder.size, builder.backgroundStyle)
        }
    }

    val backgroundStyle: KalugaBackgroundStyle

    sealed interface WithoutText : ButtonStateStyle
    data class WithoutContent(override val backgroundStyle: KalugaBackgroundStyle) : WithoutText
    sealed interface WithText : ButtonStateStyle {
        val textColor: KalugaColor
    }

    data class TextOnly(
        override val textColor: KalugaColor,
        override val backgroundStyle: KalugaBackgroundStyle,
    ) : WithText

    sealed interface WithImage {
        val image: ButtonImage
        val size: ImageSize
    }

    data class ImageOnly(
        override val image: ButtonImage,
        override val size: ImageSize,
        override val backgroundStyle: KalugaBackgroundStyle,
    ) : WithoutText, WithImage

    data class WithImageAndText(
        override val textColor: KalugaColor,
        override val image: ButtonImage,
        override val size: ImageSize,
        override val backgroundStyle: KalugaBackgroundStyle,
    ) : WithText, WithImage
}
sealed class ButtonStateStyleDSL {

    private companion object {
        fun createBackgroundStyle(
            backgroundColor: KalugaColor = DefaultColors.clear,
            shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle(),
        ) = KalugaBackgroundStyle(
            KalugaBackgroundStyle.FillStyle.Solid(backgroundColor),
            shape = shape,
        )
    }

    var backgroundStyle: KalugaBackgroundStyle = createBackgroundStyle()

    fun setBackgroundStyle(
        backgroundColor: KalugaColor = DefaultColors.clear,
        shape: KalugaBackgroundStyle.Shape,
    ) {
        backgroundStyle = createBackgroundStyle(backgroundColor, shape)
    }

    class WithoutContent : ButtonStateStyleDSL()

    class TextOnly : ButtonStateStyleDSL() {
        var textColor: KalugaColor = DefaultColors.black
    }

    sealed class WithImage : ButtonStateStyleDSL() {
        internal var image: ButtonImage = ButtonImage.Hidden
        var size: ImageSize = ImageSize.Intrinsic

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

    class ImageOnly : WithImage()

    class WithImageAndText : WithImage() {
        var textColor: KalugaColor = DefaultColors.black
    }
}
