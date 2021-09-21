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

import com.splendo.kaluga.resources.Color
import com.splendo.kaluga.resources.Font
import com.splendo.kaluga.resources.clearColor

data class ButtonStyle(
    val font: Font,
    val textSize: Float,
    val defaultStyle: ButtonStateStyle,
    val pressedStyle: ButtonStateStyle = defaultStyle,
    val disabledStyle: ButtonStateStyle = defaultStyle
)

data class ButtonStateStyle(
    val textColor: Color,
    val backgroundStyle: BackgroundStyle,
) {
    constructor(textColor: Color) : this(
        textColor,
        BackgroundStyle(
            BackgroundStyle.FillStyle.Solid(clearColor)
        )
    )

    constructor(textColor: Color, backgroundColor: Color) : this(
        textColor,
        BackgroundStyle(
            BackgroundStyle.FillStyle.Solid(backgroundColor)
        )
    )
}
