package com.splendo.kaluga.resources.view

import android.content.res.ColorStateList
import android.widget.TextView
import com.splendo.kaluga.resources.stylable.TextStyle

fun TextView.applyTextStyle(textStyle: TextStyle) {
    typeface = textStyle.font
    textSize = textStyle.size
    setTextColor(ColorStateList(arrayOf(intArrayOf()), intArrayOf(textStyle.color)))
    isAllCaps = false
}
