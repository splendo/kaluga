package com.splendo.mpp.permissions

abstract class PermissionManager {
    abstract suspend fun checkSupport(): Support
    abstract suspend fun checkPermit(): Permit
    abstract suspend fun openSettings()
}

enum class Support {
    POWER_ON,
    POWER_OFF,
    NOT_SUPPORTED,
    RESETTING,
    UNAUTHORIZED
}

enum class Permit {
    ALLOWED,
    RESTRICTED,
    DENIED,
    UNDEFINED
}