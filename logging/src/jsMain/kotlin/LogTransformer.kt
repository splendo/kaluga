package com.splendo.mpp.log

actual fun transformTag(tag: String?): String? {
    return tag
}

actual fun transformMessage(message: String): String {
    return message
}