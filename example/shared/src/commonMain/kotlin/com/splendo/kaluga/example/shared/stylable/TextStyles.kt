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
import com.splendo.kaluga.resources.FontWeight
import com.splendo.kaluga.resources.Style
import com.splendo.kaluga.resources.Traits
import com.splendo.kaluga.resources.createDefaultFont
import com.splendo.kaluga.resources.defaultBoldFont
import com.splendo.kaluga.resources.defaultFont
import com.splendo.kaluga.resources.defaultItalicFont
import com.splendo.kaluga.resources.defaultMonospaceFont
import com.splendo.kaluga.resources.stylable.KalugaTextAlignment
import com.splendo.kaluga.resources.stylable.KalugaTextStyle

object TextStyles {

    val defaultText by lazy {
        KalugaTextStyle(defaultFont, DefaultColors.dimGray, 12.0f)
    }
    val defaultTitle by lazy {
        KalugaTextStyle(defaultFont, DefaultColors.dimGray, 16.0f)
    }
    val defaultBoldText by lazy {
        KalugaTextStyle(defaultBoldFont, DefaultColors.dimGray, 12.0f)
    }
    val defaultItalicText by lazy {
        KalugaTextStyle(defaultItalicFont, DefaultColors.dimGray, 12.0f)
    }
    val defaultMonospaceText by lazy {
        KalugaTextStyle(defaultMonospaceFont, DefaultColors.dimGray, 12.0f)
    }
    val semiBoldText by lazy {
        KalugaTextStyle(createDefaultFont(FontWeight.SEMI_BOLD), DefaultColors.dimGray, 12.0f)
    }
    val serifText by lazy {
        KalugaTextStyle(createDefaultFont(FontWeight.NORMAL, Style.SERIF), DefaultColors.dimGray, 12.0f)
    }
    val italicBoldText by lazy {
        KalugaTextStyle(createDefaultFont(FontWeight.BOLD, traits = setOf(Traits.ITALIC)), DefaultColors.dimGray, 12.0f)
    }
    val lightItalicMonospaceText by lazy {
        KalugaTextStyle(createDefaultFont(FontWeight.LIGHT, Style.MONOSPACE, traits = setOf(Traits.ITALIC)), DefaultColors.dimGray, 12.0f)
    }
    val whiteText by lazy {
        KalugaTextStyle(defaultFont, DefaultColors.white, 12.0f)
    }
    val redText by lazy {
        KalugaTextStyle(defaultFont, DefaultColors.red, 12.0f)
    }
    val oppositeText by lazy {
        KalugaTextStyle(defaultFont, DefaultColors.dimGray, 12.0f, KalugaTextAlignment.END)
    }
}
