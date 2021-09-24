package com.splendo.kaluga.resources.view

import android.content.res.ColorStateList
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.splendo.kaluga.resources.stylable.TextStyle

fun TextView.bindLabel(label: KalugaLabel<*>) {
    text = when (label) {
        is KalugaLabel.Plain -> label.text
        is KalugaLabel.Styled -> {
            movementMethod = LinkMovementMethod.getInstance()
            label.text.spannable
        }
    }
    applyTextStyle(label.style)
}

fun TextView.applyTextStyle(textStyle: TextStyle) {
    typeface = textStyle.font
    textSize = textStyle.size
    setTextColor(ColorStateList(arrayOf(intArrayOf()), intArrayOf(textStyle.color)))
    isAllCaps = false
    gravity = textStyle.alignment.alignment(context).gravity
}