package com.splendo.mpp.log

import android.annotation.SuppressLint
import android.os.Build

actual class LogTransformer {

    @SuppressLint("ObsoleteSdkInt")
    actual fun transformTag(tag: String?): String? {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return tag?.substring(0, 23)
        } else {
            return tag
        }
    }

    actual fun transformMessage(message: String): String {
        return message
    }

    companion object {
        const val TAG_LENGTH_LIMIT = 23
    }
}