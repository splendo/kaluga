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

import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.TintedImage

/**
 * The Image to render for a [ButtonStateStyle.WithImage]
 */
sealed interface ButtonImage {

    /**
     * An invisible [ButtonImage]
     */
    data object Hidden : ButtonImage

    /**
     * A [ButtonImage] that renders a [KalugaImage]
     * @property image the [KalugaImage] to render
     */
    data class Image(val image: KalugaImage) : ButtonImage

    /**
     * A [ButtonImage] that renders a [TintedImage]
     * @property image the [TintedImage] to render
     */
    data class Tinted(val image: TintedImage) : ButtonImage
}

/**
 * The size to use for a [ButtonImage]
 */
sealed interface ImageSize {

    /**
     * An [ImageSize] that uses the size of the image without resizing
     */
    data object Intrinsic : ImageSize

    /**
     * An [ImageSize] that resizes a [ButtonImage] to a given [width] and [height]
     * @property width the width to resize the image to
     * @property height the height to resize the image to
     */
    data class Sized(val width: Float, val height: Float) : ImageSize
}

/**
 * The placement of a [ButtonImage] within a [com.splendo.kaluga.resources.view.KalugaButton] for a [KalugaButtonStyle.WithImageAndText]
 */
enum class ImageGravity {

    /**
     * Places the image to the start of the button, respecting layout direction
     */
    START,

    /**
     * Places the image to the end of the button, respecting layout direction
     */
    END,

    /**
     * Places the image to the left of the button, regardless of layout direction
     */
    LEFT,

    /**
     * Places the image to the right of the button, regardless of layout direction
     */
    RIGHT,

    /**
     * Places the image to the top of the button
     */
    TOP,

    /**
     * Places the image to the bottom of the button
     */
    BOTTOM,
}
