package com.splendo.mpp.log

enum class LogLevel(val logLevel: ru.pocketbyte.hydra.log.LogLevel) {

    /**
     * Debug Log Level. Should be shown only for debugging.
     */
    DEBUG(ru.pocketbyte.hydra.log.LogLevel.DEBUG),

    /**
     * Information Log Level.
     */
    INFO(ru.pocketbyte.hydra.log.LogLevel.INFO),

    /**
     * Warning Log Level. Should be used to log some warnings.
     */
    WARNING(ru.pocketbyte.hydra.log.LogLevel.WARNING),

    /**
     * Error Log Level. Should be used to log some errors.
     */
    ERROR(ru.pocketbyte.hydra.log.LogLevel.ERROR)
}