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
import com.splendo.kaluga.resources.KalugaColor
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

object ButtonStyles {

    val default by lazy {
        val shape = KalugaBackgroundStyle.Shape.Rectangle(4.0f)
        KalugaButtonStyle.textOnly {
            setTextStyle(TextStyles.defaultTitle)
            defaultStyle {
                setBackgroundStyle(
                    backgroundColor = DefaultColors.lightGray,
                    shape = shape
                )
            }
            pressedStyle {
                setBackgroundStyle(
                    backgroundColor = DefaultColors.gray,
                    shape = shape
                )
            }
            disabledStyle {
                setBackgroundStyle(
                    backgroundColor = DefaultColors.dimGray,
                    shape = shape
                )
            }
        }
    }

    val textButton by lazy {
        val shape = KalugaBackgroundStyle.Shape.Rectangle()
        KalugaButtonStyle.textOnly {
            setTextStyle(TextStyles.redText)
            defaultStyle {
                setBackgroundStyle(DefaultColors.clear, shape)
            }
            pressedStyle {
                setBackgroundStyle(DefaultColors.clear, shape)
            }
            disabledStyle {
                setBackgroundStyle(DefaultColors.clear, shape)
            }
        }
    }
    val redButton by lazy {
        val shape = KalugaBackgroundStyle.Shape.Rectangle()
        KalugaButtonStyle.textOnly {
            setTextStyle(TextStyles.whiteText)
            defaultStyle {
                setBackgroundStyle(
                    backgroundColor = DefaultColors.red,
                    shape = shape
                )
            }
            pressedStyle {
                setBackgroundStyle(
                    backgroundColor = DefaultColors.maroon,
                    shape = shape
                )
            }
            disabledStyle {
                setBackgroundStyle(
                    backgroundColor = DefaultColors.lightGray,
                    shape = shape
                )
            }
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
                    )
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                setBackgroundStyle(
                    DefaultColors.lightSkyBlue,
                    KalugaBackgroundStyle.Shape.Rectangle(
                        5.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT),
                    )
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
            font = defaultFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.white
                setBackgroundStyle(DefaultColors.deepSkyBlue, KalugaBackgroundStyle.Shape.Oval)
            }
            pressedStyle {
                textColor = DefaultColors.azure
                setBackgroundStyle(DefaultColors.lightSkyBlue, KalugaBackgroundStyle.Shape.Oval)
            }
            disabledStyle {
                textColor = DefaultColors.black
                setBackgroundStyle(DefaultColors.lightGray, KalugaBackgroundStyle.Shape.Oval)
            }
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

    val textButtonWithImageLeft by lazy {
        KalugaButtonStyle.withImageAndText {
            font = defaultBoldFont
            textSize = 12.0f
            textAlignment = KalugaTextAlignment.LEFT
            imageSize = ImageSize.Sized(24.0f, 24.0f)
            spacing = 8.0f
            imageGravity = ImageGravity.LEFT
            defaultStyle {
                setBackgroundStyle(DefaultColors.lightGray)
                setImage("star".asImage()!!, tint = DefaultColors.gold)
            }
            pressedStyle {
                setBackgroundStyle(DefaultColors.gray)
                setImage("star".asImage()!!, tint = DefaultColors.goldenrod)
            }
            disabledStyle {
                setBackgroundStyle(DefaultColors.dimGray)
                setImage("star".asImage()!!, tint = DefaultColors.white)
            }
        }
    }

    val mediaButton by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultBoldFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.black
                setBackgroundStyle(
                    DefaultColors.lightGray,
                    KalugaBackgroundStyle.Shape.Oval
                )
            }
            pressedStyle {
                textColor = DefaultColors.black
                setBackgroundStyle(
                    DefaultColors.dimGray,
                    KalugaBackgroundStyle.Shape.Oval
                )
            }
            disabledStyle {
                textColor = DefaultColors.dimGray
                setBackgroundStyle(
                    DefaultColors.gray,
                    KalugaBackgroundStyle.Shape.Oval
                )
            }
        }
    }

    val mediaButtonFocus by lazy {
        KalugaButtonStyle.textOnly {
            font = defaultBoldFont
            textSize = 12.0f
            defaultStyle {
                textColor = DefaultColors.azure
                setBackgroundStyle(
                    DefaultColors.lightGray,
                    KalugaBackgroundStyle.Shape.Oval
                )
            }
            pressedStyle {
                textColor = DefaultColors.azure
                setBackgroundStyle(
                    DefaultColors.dimGray,
                    KalugaBackgroundStyle.Shape.Oval
                )
            }
            disabledStyle {
                textColor = DefaultColors.dimGray
                setBackgroundStyle(
                    DefaultColors.gray,
                    KalugaBackgroundStyle.Shape.Oval
                )
            }
        }
    }
}
