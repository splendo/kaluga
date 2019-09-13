package com.splendo.mpp.log

/**
 * This class hide HydraLog dependencies.
 */
internal class InternalLogger(val logger: Logger) : ru.pocketbyte.hydra.log.Logger {

    override fun log(level: ru.pocketbyte.hydra.log.LogLevel, tag: String?, message: String) {
        logger.log(Logger.getLoggingComponentLogLevel(level), tag, message)
    }

    override fun log(level: ru.pocketbyte.hydra.log.LogLevel, tag: String?, exception: Throwable) {
        logger.log(Logger.getLoggingComponentLogLevel(level), tag, exception)
    }
}

expect class LogTransformer {
    fun transformTag(tag: String?): String?
    fun transformMessage(message: String): String
}