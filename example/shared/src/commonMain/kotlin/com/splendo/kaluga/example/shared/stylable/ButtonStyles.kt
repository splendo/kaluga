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

package com.splendo.kaluga.example.shared.stylable

import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.asImage
import com.splendo.kaluga.resources.defaultBoldFont
import com.splendo.kaluga.resources.defaultFont
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle
import com.splendo.kaluga.resources.stylable.GradientStyle
import com.splendo.kaluga.resources.stylable.ImageGravity
import com.splendo.kaluga.resources.stylable.ImageSize
import com.splendo.kaluga.resources.stylable.KalugaTextAlignment
import com.splendo.kaluga.resources.stylable.KalugaTextStyle

object ButtonStyles {

    val default by lazy {
        KalugaButtonStyle.textOnly {
            setTextStyle(TextStyles.defaultTitle)
            setBackground(
                defaultColor = DefaultColors.lightGray,
                pressedColor = DefaultColors.gray,
                disabledColor = DefaultColors.dimGray,
                shape = KalugaBackgroundStyle.Shape.Rectangle(4.0f),
            )
        }
    }

    val textButton by lazy {
        KalugaButtonStyle.textOnly {
            setTextStyle(TextStyles.redText)
            setBackground(DefaultColors.clear, shape = KalugaBackgroundStyle.Shape.Rectangle())
        }
    }

    val redButton by lazy {
        KalugaButtonStyle.textOnly {
            setTextStyle(TextStyles.whiteText)
            setBackground(
                defaultColor = DefaultColors.red,
                pressedColor = DefaultColors.maroon,
                disabledColor = DefaultColors.lightGray,
                shape = KalugaBackgroundStyle.Shape.Rectangle(),
            )
        }
    }
    val roundedButton by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                setBackgroundStyle(
                    DefaultColors.deepSkyBlue,
                    KalugaBackgroundStyle.Shape.Rectangle(
                        10.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT),
                    ),
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                setBackgroundStyle(
                    DefaultColors.lightSkyBlue,
                    KalugaBackgroundStyle.Shape.Rectangle(
                        5.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT),
                    ),
                )
            }
            disabledStyle {
                textColor = DefaultColors.black
                setBackgroundStyle(DefaultColors.lightGray, KalugaBackgroundStyle.Shape.Rectangle(10.0f))
            }
        }
    }
    val ovalButton by lazy {
        KalugaButtonStyle.textOnly {
            setTextStyle(KalugaTextStyle(defaultFont, DefaultColors.white, 12.0f), DefaultColors.azure, DefaultColors.black)
            setBackground(
                defaultColor = DefaultColors.deepSkyBlue,
                pressedColor = DefaultColors.lightSkyBlue,
                disabledColor = DefaultColors.lightGray,
                shape = KalugaBackgroundStyle.Shape.Oval,
            )
        }
    }
    val redButtonWithStroke by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle = ButtonStateStyle.textOnly {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.red),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            pressedStyle = ButtonStateStyle.textOnly {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.maroon),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            disabledStyle = ButtonStateStyle.textOnly {
                textColor = DefaultColors.black
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
        }
    }
    val roundedButtonWithStroke by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(
                        10.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT),
                    ),
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.azure),
                    KalugaBackgroundStyle.Shape.Rectangle(
                        5.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT),
                    ),
                )
            }
            disabledStyle {
                textColor = DefaultColors.black
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(10.0f),
                )
            }
        }
    }
    val ovalButtonWithStroke by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Oval,
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.azure),
                    KalugaBackgroundStyle.Shape.Oval,
                )
            }
            disabledStyle {
                textColor = DefaultColors.black
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Oval,
                )
            }
        }
    }

    val linearGradientButton by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Linear(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), GradientStyle.Linear.Orientation.LEFT_RIGHT),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Linear(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), GradientStyle.Linear.Orientation.LEFT_RIGHT),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            disabledStyle {
                textColor = DefaultColors.black
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Linear(listOf(DefaultColors.lightGray, DefaultColors.gray), GradientStyle.Linear.Orientation.LEFT_RIGHT),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
        }
    }

    val radialGradientButton by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Radial(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), 50.0f, GradientStyle.CenterPoint(0.3f, 0.3f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Radial(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), 25.0f, GradientStyle.CenterPoint(0.6f, 0.6f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            disabledStyle {
                textColor = DefaultColors.black
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(GradientStyle.Radial(listOf(DefaultColors.lightGray, DefaultColors.gray), 50.0f)),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
        }
    }

    val angularGradientButton by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Angular(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), GradientStyle.CenterPoint(0.3f, 0.3f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Angular(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), GradientStyle.CenterPoint(0.6f, 0.6f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
            disabledStyle {
                textColor = DefaultColors.black
                backgroundStyle = KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(GradientStyle.Angular(listOf(DefaultColors.lightGray, DefaultColors.gray))),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                )
            }
        }
    }

    private fun createTextButtonWithImageStyle(gravity: ImageGravity) = KalugaButtonStyle.withImageAndText {
        imageSize = ImageSize.Sized(24.0f, 24.0f)
        spacing = 8.0f
        imageGravity = gravity
        setTextStyle(KalugaTextStyle(defaultBoldFont, DefaultColors.white, 12.0f, KalugaTextAlignment.LEFT), DefaultColors.azure, DefaultColors.black)
        setImage("star".asImage()!!, DefaultColors.gold, DefaultColors.goldenrod, DefaultColors.white)
        setBackground(DefaultColors.lightGray, DefaultColors.gray, DefaultColors.dimGray)
    }

    val textButtonWithImageLeft by lazy {
        createTextButtonWithImageStyle(ImageGravity.LEFT)
    }

    val textButtonWithImageStart by lazy {
        createTextButtonWithImageStyle(ImageGravity.START)
    }

    val textButtonWithImageRight by lazy {
        createTextButtonWithImageStyle(ImageGravity.RIGHT)
    }

    val textButtonWithImageEnd by lazy {
        createTextButtonWithImageStyle(ImageGravity.END)
    }

    val textButtonWithImageTop by lazy {
        createTextButtonWithImageStyle(ImageGravity.TOP)
    }

    val textButtonWithImageBottom by lazy {
        createTextButtonWithImageStyle(ImageGravity.BOTTOM)
    }

    val textButtonWithImageIntrinsicSize by lazy {
        KalugaButtonStyle.withImageAndText {
            imageSize = ImageSize.Intrinsic
            spacing = 8.0f
            imageGravity = ImageGravity.START
            setTextStyle(KalugaTextStyle(defaultBoldFont, DefaultColors.white, 12.0f, KalugaTextAlignment.LEFT), DefaultColors.azure, DefaultColors.black)
            setImage("star".asImage()!!, DefaultColors.gold, DefaultColors.goldenrod, DefaultColors.white)
            setBackground(DefaultColors.lightGray, DefaultColors.gray, DefaultColors.dimGray)
        }
    }

    val textButtonWithStateImage by lazy {
        KalugaButtonStyle.withImageAndText {
            font = defaultBoldFont
            textSize = 12.0f
            textAlignment = KalugaTextAlignment.LEFT
            imageSize = ImageSize.Sized(24.0f, 24.0f)
            spacing = 8.0f
            imageGravity = ImageGravity.LEFT
            defaultStyle {
                textColor = DefaultColors.white
                setBackgroundStyle(DefaultColors.lightGray)
                setImage("star".asImage()!!, tint = DefaultColors.gold)
            }
            pressedStyle {
                textColor = DefaultColors.azure
                setBackgroundStyle(DefaultColors.gray)
                setImage("check".asImage()!!, tint = DefaultColors.goldenrod)
            }
            disabledStyle {
                textColor = DefaultColors.black
                setBackgroundStyle(DefaultColors.dimGray)
                setImage("cancel".asImage()!!, tint = DefaultColors.white)
            }
        }
    }

    val imageOnly by lazy {
        KalugaButtonStyle.imageOnly {
            imageSize = ImageSize.Sized(24.0f, 24.0f)
            defaultStyle {
                setBackgroundStyle(DefaultColors.lightGray)
                setImage("star".asImage()!!, tint = DefaultColors.gold)
            }
            pressedStyle {
                setBackgroundStyle(DefaultColors.gray)
                setImage("check".asImage()!!, tint = DefaultColors.goldenrod)
            }
            disabledStyle {
                setBackgroundStyle(DefaultColors.dimGray)
                setImage("cancel".asImage()!!, tint = DefaultColors.white)
            }
        }
    }

    val imageOnlyIntrinsic by lazy {
        KalugaButtonStyle.imageOnly {
            imageSize = ImageSize.Intrinsic
            defaultStyle {
                setBackgroundStyle(DefaultColors.lightGray)
                setImage("star".asImage()!!, tint = DefaultColors.gold)
            }
            pressedStyle {
                setBackgroundStyle(DefaultColors.gray)
                setImage("check".asImage()!!, tint = DefaultColors.goldenrod)
            }
            disabledStyle {
                setBackgroundStyle(DefaultColors.dimGray)
                setImage("cancel".asImage()!!, tint = DefaultColors.white)
            }
        }
    }

    val noContent by lazy {
        KalugaButtonStyle.withoutContent {
            setBackground(DefaultColors.lightGray, DefaultColors.gray, DefaultColors.dimGray)
        }
    }

    fun mediaButton(image: KalugaImage) = KalugaButtonStyle.imageOnly {
        setImage(image, DefaultColors.black)
        setBackground(DefaultColors.lightGray, DefaultColors.dimGray, DefaultColors.gray, KalugaBackgroundStyle.Shape.Oval)
    }

    val mediaButtonText by lazy {
        KalugaButtonStyle.textOnly {
            setTextStyle(KalugaTextStyle(defaultBoldFont, DefaultColors.black, 12.0f, KalugaTextAlignment.CENTER))
            setBackground(DefaultColors.lightGray, DefaultColors.dimGray, DefaultColors.gray, KalugaBackgroundStyle.Shape.Oval)
        }
    }

    fun mediaButtonWithImageAndText(image: KalugaImage) = KalugaButtonStyle.withImageAndText {
        setImage(image, DefaultColors.black)
        setTextStyle(KalugaTextStyle(defaultBoldFont, DefaultColors.black, 12.0f))
        imageGravity = ImageGravity.START
        spacing = 4.0f
        setBackground(DefaultColors.lightGray, DefaultColors.dimGray, DefaultColors.gray, KalugaBackgroundStyle.Shape.Oval)
    }

    fun mediaButtonFocus(image: KalugaImage) = KalugaButtonStyle.imageOnly {
        setImage(image, DefaultColors.azure, disabledTint = DefaultColors.dimGray)
        setBackground(DefaultColors.lightGray, DefaultColors.dimGray, DefaultColors.gray, KalugaBackgroundStyle.Shape.Oval)
    }

    fun mediaButtonFocusWithImageAndText(image: KalugaImage) = KalugaButtonStyle.withImageAndText {
        setImage(image, DefaultColors.azure, disabledTint = DefaultColors.dimGray)
        setTextStyle(KalugaTextStyle(defaultBoldFont, DefaultColors.azure, 12.0f), disabledColor = DefaultColors.dimGray)
        imageGravity = ImageGravity.START
        spacing = 4.0f
        setBackground(DefaultColors.lightGray, DefaultColors.dimGray, DefaultColors.gray, KalugaBackgroundStyle.Shape.Oval)
    }
}
