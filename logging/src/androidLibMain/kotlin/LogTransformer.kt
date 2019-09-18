package com.splendo.mpp.log

import android.annotation.SuppressLint
import android.os.Build

const val TAG_LENGTH_LIMIT = 23

@SuppressLint("ObsoleteSdkInt")
internal actual fun transformTag(tag: String?): String? {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && tag != null && tag.length > TAG_LENGTH_LIMIT) {
        tag.substring(0, 23)
    } else {
        tag
    }
}

internal actual fun transformMessage(message: String): String {
    return message
}
