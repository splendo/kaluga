/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.resources.Font
import com.splendo.kaluga.resources.KalugaColor

data class ButtonStyle(
    val font: Font,
    val textSize: Float,
    val textAlignment: TextAlignment = TextAlignment.CENTER,
    val defaultStyle: ButtonStateStyle,
    val pressedStyle: ButtonStateStyle = defaultStyle,
    val disabledStyle: ButtonStateStyle = defaultStyle
) {
    constructor(
        textStyle: TextStyle,
        textAlignment: TextAlignment = TextAlignment.CENTER,
        backgroundColor: KalugaColor = DefaultColors.clear,
        pressedBackgroundColor: KalugaColor = backgroundColor,
        disabledBackgroundColor: KalugaColor = backgroundColor,
        shape: BackgroundStyle.Shape = BackgroundStyle.Shape.Rectangle()
    ) : this(
        textStyle.font,
        textStyle.size,
        textAlignment,
        ButtonStateStyle(
            textStyle.color,
            backgroundColor,
            shape
        ),
        ButtonStateStyle(
            textStyle.color,
            pressedBackgroundColor,
            shape
        ),
        ButtonStateStyle(
            textStyle.color,
            disabledBackgroundColor,
            shape
        )
    )

    fun getStateTextStyle(isEnabled: Boolean, isPressed: Boolean) =
        TextStyle(font, getStateStyle(isEnabled, isPressed).textColor, textSize, textAlignment)

    fun getStateStyle(isEnabled: Boolean, isPressed: Boolean): ButtonStateStyle {
        return if (!isEnabled) {
            disabledStyle
        } else if (isPressed) {
            pressedStyle
        } else {
            defaultStyle
        }
    }
}

data class ButtonStateStyle(
    val textColor: KalugaColor,
    val backgroundStyle: BackgroundStyle,
) {

    constructor(textColor: KalugaColor, backgroundColor: KalugaColor = DefaultColors.clear, shape: BackgroundStyle.Shape = BackgroundStyle.Shape.Rectangle()) : this(
        textColor,
        BackgroundStyle(
            BackgroundStyle.FillStyle.Solid(backgroundColor),
            shape = shape
        )
    )
}
