package com.splendo.kaluga.resources.view

import android.os.Build
import android.text.Layout
import android.text.TextUtils
import android.view.View
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.resources.stylable.TextAlignment

val TextAlignment.alignment: Layout.Alignment get() {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        ApplicationHolder.applicationContext.resources.configuration.locales.get(0)
    } else {
        ApplicationHolder.applicationContext.resources.configuration.locale
    }
    return when (this) {
        TextAlignment.LEFT -> if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL) {
            Layout.Alignment.ALIGN_OPPOSITE
        } else {
            Layout.Alignment.ALIGN_NORMAL
        }
        TextAlignment.RIGHT -> if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_LTR) {
            Layout.Alignment.ALIGN_OPPOSITE
        } else {
            Layout.Alignment.ALIGN_NORMAL
        }
        TextAlignment.CENTER -> Layout.Alignment.ALIGN_CENTER
        TextAlignment.OPPOSITE -> Layout.Alignment.ALIGN_OPPOSITE
        TextAlignment.NORMAL -> Layout.Alignment.ALIGN_NORMAL
    }
}