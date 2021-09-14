package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.resources.stylable.TextStyle
import platform.UIKit.UILabel
import platform.UIKit.UITextField

fun UILabel.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
}

fun UITextField.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
}
