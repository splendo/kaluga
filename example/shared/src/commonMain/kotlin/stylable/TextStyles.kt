package com.splendo.kaluga.example.shared.stylable

import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.defaultBoldFont
import com.splendo.kaluga.resources.defaultFont
import com.splendo.kaluga.resources.defaultItalicFont
import com.splendo.kaluga.resources.defaultMonospaceFont
import com.splendo.kaluga.resources.stylable.TextAlignment
import com.splendo.kaluga.resources.stylable.TextStyle

object TextStyles {

    val defaultText by lazy {
        TextStyle(defaultFont, DefaultColors.dimGray, 12.0f)
    }
    val defaultTitle by lazy {
        TextStyle(defaultFont, DefaultColors.dimGray, 16.0f)
    }
    val defaultBoldText by lazy {
        TextStyle(defaultBoldFont, DefaultColors.dimGray, 12.0f)
    }
    val defaultItalicText by lazy {
        TextStyle(defaultItalicFont, DefaultColors.dimGray, 12.0f)
    }
    val defaultMonospaceText by lazy {
        TextStyle(defaultMonospaceFont, DefaultColors.dimGray, 12.0f)
    }
    val whiteText by lazy {
        TextStyle(defaultFont, DefaultColors.white, 12.0f)
    }
    val redText by lazy {
        TextStyle(defaultFont, DefaultColors.red, 12.0f)
    }
    val oppositeText by lazy {
        TextStyle(defaultFont, DefaultColors.dimGray, 12.0f, TextAlignment.END)
    }
}
