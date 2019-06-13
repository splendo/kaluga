package com.splendo.components.location

actual data class Configuration (
    val platform: String
) {
    actual companion object {
        actual val default = Configuration(platform = "android")
    }
}