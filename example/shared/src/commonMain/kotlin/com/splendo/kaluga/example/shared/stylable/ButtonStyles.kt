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
import com.splendo.kaluga.resources.defaultFont
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle
import com.splendo.kaluga.resources.stylable.GradientStyle

object ButtonStyles {

    val default by lazy {
        KalugaButtonStyle(
            TextStyles.defaultTitle,
            backgroundColor = DefaultColors.lightGray,
            pressedBackgroundColor = DefaultColors.gray,
            disabledBackgroundColor = DefaultColors.dimGray,
            shape = KalugaBackgroundStyle.Shape.Rectangle(4.0f),
        )
    }

    val textButton by lazy {
        KalugaButtonStyle(TextStyles.redText)
    }
    val redButton by lazy {
        KalugaButtonStyle(
            TextStyles.whiteText,
            backgroundColor = DefaultColors.red,
            pressedBackgroundColor = DefaultColors.maroon,
            disabledBackgroundColor = DefaultColors.lightGray,
        )
    }
    val roundedButton by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                DefaultColors.deepSkyBlue,
                KalugaBackgroundStyle.Shape.Rectangle(
                    10.0f,
                    setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT),
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                DefaultColors.lightSkyBlue,
                KalugaBackgroundStyle.Shape.Rectangle(
                    5.0f,
                    setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT),
                ),
            ),
            disabledStyle = ButtonStateStyle(DefaultColors.black, DefaultColors.lightGray, KalugaBackgroundStyle.Shape.Rectangle(10.0f)),
        )
    }
    val ovalButton by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(DefaultColors.white, DefaultColors.deepSkyBlue, KalugaBackgroundStyle.Shape.Oval),
            pressedStyle = ButtonStateStyle(DefaultColors.azure, DefaultColors.lightSkyBlue, KalugaBackgroundStyle.Shape.Oval),
            disabledStyle = ButtonStateStyle(DefaultColors.black, DefaultColors.lightGray, KalugaBackgroundStyle.Shape.Oval),
        )
    }
    val redButtonWithStroke by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.red),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.maroon),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
        )
    }
    val roundedButtonWithStroke by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(
                        10.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT),
                    ),
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.azure),
                    KalugaBackgroundStyle.Shape.Rectangle(
                        5.0f,
                        setOf(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT),
                    ),
                ),
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(10.0f),
                ),
            ),
        )
    }
    val ovalButtonWithStroke by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Oval,
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightSkyBlue),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.azure),
                    KalugaBackgroundStyle.Shape.Oval,
                ),
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Oval,
                ),
            ),
        )
    }

    val linearGradientButton by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Linear(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), GradientStyle.Linear.Orientation.LEFT_RIGHT),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Linear(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), GradientStyle.Linear.Orientation.LEFT_RIGHT),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Linear(listOf(DefaultColors.lightGray, DefaultColors.gray), GradientStyle.Linear.Orientation.LEFT_RIGHT),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
        )
    }

    val radialGradientButton by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Radial(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), 50.0f, GradientStyle.CenterPoint(0.3f, 0.3f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Radial(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), 25.0f, GradientStyle.CenterPoint(0.6f, 0.6f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(GradientStyle.Radial(listOf(DefaultColors.lightGray, DefaultColors.gray), 50.0f)),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
        )
    }

    val angularGradientButton by lazy {
        KalugaButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Angular(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), GradientStyle.CenterPoint(0.3f, 0.3f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(
                        GradientStyle.Angular(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), GradientStyle.CenterPoint(0.6f, 0.6f)),
                    ),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                KalugaBackgroundStyle(
                    KalugaBackgroundStyle.FillStyle.Gradient(GradientStyle.Angular(listOf(DefaultColors.lightGray, DefaultColors.gray))),
                    KalugaBackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    KalugaBackgroundStyle.Shape.Rectangle(),
                ),
            ),
        )
    }
}
