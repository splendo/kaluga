package com.splendo.mpp.log

/**
 * This class is used to hide HydraLog dependency.
 */
inline class InternalLogger(val logger: Logger) : ru.pocketbyte.hydra.log.Logger {

    override fun log(level: ru.pocketbyte.hydra.log.LogLevel, tag: String?, message: String) {
        logger.log(
            level.getLogLevel(),
            transformTag(tag),
            transformMessage(message)
        )
    }

    override fun log(level: ru.pocketbyte.hydra.log.LogLevel, tag: String?, exception: Throwable) {
        logger.log(
            level.getLogLevel(),
            transformTag(tag),
            exception
        )
    }
}

internal expect fun transformTag(tag: String?): String?
internal expect fun transformMessage(message: String): String