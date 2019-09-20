package com.splendo.mpp.log

interface Logger {

    /**
     * Writes log with provided level and tag.
     * @param level Log level
     * @param tag Tag of the log record. Nullable
     * @param message Message to be written into log
     */
    fun log(level: LogLevel, tag: String?, message: String)

    /**
     * Writes exception log with provided level and tag.
     * @param level Log level
     * @param tag Tag of the log record. Nullable
     * @param exception Exception to be written into log
     */
    fun log(level: LogLevel, tag: String?, exception: Throwable)

}

internal inline fun ru.pocketbyte.hydra.log.LogLevel.getLogLevel(): LogLevel {
    return when (this) {
        ru.pocketbyte.hydra.log.LogLevel.DEBUG -> LogLevel.DEBUG
        ru.pocketbyte.hydra.log.LogLevel.INFO -> LogLevel.INFO
        ru.pocketbyte.hydra.log.LogLevel.WARNING -> LogLevel.WARNING
        ru.pocketbyte.hydra.log.LogLevel.ERROR -> LogLevel.ERROR
    }
}
