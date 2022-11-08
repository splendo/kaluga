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
import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.ButtonStyle
import com.splendo.kaluga.resources.stylable.GradientStyle

object ButtonStyles {

    val textButton by lazy {
        ButtonStyle(TextStyles.redText)
    }
    val redButton by lazy {
        ButtonStyle(
            TextStyles.whiteText,
            backgroundColor = DefaultColors.red,
            pressedBackgroundColor = DefaultColors.maroon,
            disabledBackgroundColor = DefaultColors.lightGray
        )
    }
    val roundedButton by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(DefaultColors.white, DefaultColors.deepSkyBlue, BackgroundStyle.Shape.Rectangle(10.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT))),
            pressedStyle = ButtonStateStyle(DefaultColors.azure, DefaultColors.lightSkyBlue, BackgroundStyle.Shape.Rectangle(5.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT))),
            disabledStyle = ButtonStateStyle(DefaultColors.black, DefaultColors.lightGray, BackgroundStyle.Shape.Rectangle(10.0f))
        )
    }
    val ovalButton by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(DefaultColors.white, DefaultColors.deepSkyBlue, BackgroundStyle.Shape.Oval),
            pressedStyle = ButtonStateStyle(DefaultColors.azure, DefaultColors.lightSkyBlue, BackgroundStyle.Shape.Oval),
            disabledStyle = ButtonStateStyle(DefaultColors.black, DefaultColors.lightGray, BackgroundStyle.Shape.Oval)
        )
    }
    val redButtonWithStroke by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.red),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.maroon),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    BackgroundStyle.Shape.Rectangle()
                )
            )
        )
    }
    val roundedButtonWithStroke by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    BackgroundStyle.Shape.Rectangle(10.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT))
                )
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.lightSkyBlue),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.azure),
                    BackgroundStyle.Shape.Rectangle(5.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT, BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT))
                )
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    BackgroundStyle.Shape.Rectangle(10.0f)
                )
            )
        )
    }
    val ovalButtonWithStroke by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.deepSkyBlue),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    BackgroundStyle.Shape.Oval
                )
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.lightSkyBlue),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.azure),
                    BackgroundStyle.Shape.Oval
                )
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Solid(DefaultColors.lightGray),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    BackgroundStyle.Shape.Oval
                )
            )
        )
    }

    val linearGradientButton by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Linear(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), GradientStyle.Linear.Orientation.LEFT_RIGHT)),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Linear(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), GradientStyle.Linear.Orientation.LEFT_RIGHT)),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Linear(listOf(DefaultColors.lightGray, DefaultColors.gray), GradientStyle.Linear.Orientation.LEFT_RIGHT)),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    BackgroundStyle.Shape.Rectangle()
                )
            )
        )
    }

    val radialGradientButton by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Radial(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), 50.0f, GradientStyle.CenterPoint(0.3f, 0.3f))),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Radial(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), 25.0f, GradientStyle.CenterPoint(0.6f, 0.6f))),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Radial(listOf(DefaultColors.lightGray, DefaultColors.gray), 50.0f)),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    BackgroundStyle.Shape.Rectangle()
                )
            )
        )
    }

    val angularGradientButton by lazy {
        ButtonStyle(
            defaultFont,
            12.0f,
            defaultStyle = ButtonStateStyle(
                DefaultColors.white,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Angular(listOf(DefaultColors.deepSkyBlue, DefaultColors.lightSkyBlue), GradientStyle.CenterPoint(0.3f, 0.3f))),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.white),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            pressedStyle = ButtonStateStyle(
                DefaultColors.azure,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Angular(listOf(DefaultColors.midnightBlue, DefaultColors.deepSkyBlue), GradientStyle.CenterPoint(0.6f, 0.6f))),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.mistyRose),
                    BackgroundStyle.Shape.Rectangle()
                )
            ),
            disabledStyle = ButtonStateStyle(
                DefaultColors.black,
                BackgroundStyle(
                    BackgroundStyle.FillStyle.Gradient(GradientStyle.Angular(listOf(DefaultColors.lightGray, DefaultColors.gray))),
                    BackgroundStyle.StrokeStyle.Stroke(2.0f, DefaultColors.black),
                    BackgroundStyle.Shape.Rectangle()
                )
            )
        )
    }
}
