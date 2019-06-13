package com.splendo.components.location

enum class AccessOptions {
    WHEN_IN_USE, ALWAYS
}

actual data class Configuration (
    var accessOptions: AccessOptions
) {
    actual companion object {
        actual val default = Configuration(
            accessOptions = AccessOptions.WHEN_IN_USE
        )
    }
}
