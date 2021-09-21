package com.splendo.kaluga.resources.view

import android.content.res.ColorStateList
import android.text.Layout
import android.view.View
import android.widget.TextView
import com.splendo.kaluga.resources.stylable.TextStyle

fun TextView.bindLabel(label: Label<*>) {
    text = when (label) {
        is Label.Plain -> label.text
        is Label.Styled -> label.text.spannable
    }
    applyTextStyle(label.style)
    textAlignment = when (label.alignment.alignment) {
        Layout.Alignment.ALIGN_NORMAL -> View.TEXT_ALIGNMENT_TEXT_START
        Layout.Alignment.ALIGN_OPPOSITE -> View.TEXT_ALIGNMENT_TEXT_END
        Layout.Alignment.ALIGN_CENTER -> View.TEXT_ALIGNMENT_CENTER
    }
}

fun TextView.applyTextStyle(textStyle: TextStyle) {
    typeface = textStyle.font
    textSize = textStyle.size
    setTextColor(ColorStateList(arrayOf(intArrayOf()), intArrayOf(textStyle.color)))
    isAllCaps = false
}