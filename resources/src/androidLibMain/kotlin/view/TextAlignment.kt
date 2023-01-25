package com.splendo.kaluga.resources.view

import android.content.Context
import android.os.Build
import android.text.Layout
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.splendo.kaluga.resources.stylable.KalugaTextAlignment

fun KalugaTextAlignment.alignment(context: Context): Layout.Alignment {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0)
    } else {
        context.resources.configuration.locale
    }
    return when (this) {
        KalugaTextAlignment.LEFT -> if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL) {
            Layout.Alignment.ALIGN_OPPOSITE
        } else {
            Layout.Alignment.ALIGN_NORMAL
        }
        KalugaTextAlignment.RIGHT -> if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_LTR) {
            Layout.Alignment.ALIGN_OPPOSITE
        } else {
            Layout.Alignment.ALIGN_NORMAL
        }
        KalugaTextAlignment.CENTER -> Layout.Alignment.ALIGN_CENTER
        KalugaTextAlignment.END -> Layout.Alignment.ALIGN_OPPOSITE
        KalugaTextAlignment.START -> Layout.Alignment.ALIGN_NORMAL
    }
}

val Layout.Alignment.viewAlignment: Int get() = when (this) {
    Layout.Alignment.ALIGN_NORMAL -> View.TEXT_ALIGNMENT_TEXT_START
    Layout.Alignment.ALIGN_OPPOSITE -> View.TEXT_ALIGNMENT_TEXT_END
    Layout.Alignment.ALIGN_CENTER -> View.TEXT_ALIGNMENT_CENTER
}

val Layout.Alignment.gravity: Int get() = when (this) {
    Layout.Alignment.ALIGN_NORMAL -> Gravity.START
    Layout.Alignment.ALIGN_OPPOSITE -> Gravity.END
    Layout.Alignment.ALIGN_CENTER -> Gravity.CENTER_HORIZONTAL
}
