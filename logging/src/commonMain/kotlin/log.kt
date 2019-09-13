package com.splendo.mpp.log

import ru.pocketbyte.hydra.log.HydraLog

/**
 * This class hide HydraLog dependencies.
 */
private class InternalLogger(val logger: Logger) : ru.pocketbyte.hydra.log.Logger {
    override fun log(level: ru.pocketbyte.hydra.log.LogLevel, tag: String?, message: String) {
        logger.log(Logger.getLoggingComponentLogLevel(level), tag, message)
    }

    override fun log(level: ru.pocketbyte.hydra.log.LogLevel, tag: String?, exception: Throwable) {
        logger.log(Logger.getLoggingComponentLogLevel(level), tag, exception)
    }
}

/**
 * Initializes HydraLog with given logger.
 *
 * Be aware that once initialized HydraLog cannot be initialised with new loggers. You will get first logger you have used for initialisation.
 *
 * @param logger - logged to be used for log output
 * @return first logger used for initialisation.
 */
fun initLogger(logger: Logger): Logger? {
    try {
        HydraLog.init(InternalLogger(logger))
    } catch (e: IllegalStateException) {
        println("LOG: HydraLog was already initialised. {${e.message}}")
    }

    return logger()
}

/**
 * @return first logger you have used for initialisation.
 */
fun logger(): Logger? {
    return (HydraLog.logger as InternalLogger).logger
}

/**
 * Writes log with provided level and empty tag.
 * @param level Log level
 * @param message Message to be written into log
 */
fun log(level: LogLevel, message: String) {
    HydraLog.log(level.logLevel, null, message)
}

/**
 * Writes exception log with provided level and empty tag.
 * @param level Log level
 * @param exception Exception to be written into log
 */
fun log(level: LogLevel, exception: Throwable) {
    HydraLog.log(level.logLevel, null, exception)
}

/**
 * Writes log with provided level and empty tag.
 * @param level Log level
 * @param function Function that returns message to be written into log
 */
fun log(level: LogLevel, function: () -> String) {
    HydraLog.log(level.logLevel, null, function)
}

//================================================================
//== LogLevel.INFO ===============================================
/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun info(tag: String?, message: String) {
    HydraLog.log(LogLevel.INFO.logLevel, tag, message)
}

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun info(message: String) {
    HydraLog.log(LogLevel.INFO.logLevel, null, message)
}

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun info(tag: String?, exception: Throwable) {
    HydraLog.log(LogLevel.INFO.logLevel, tag, exception)
}

/**
 * Log exception with INFO log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun info(exception: Throwable) {
    HydraLog.log(LogLevel.INFO.logLevel, null, exception)
}

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun info(tag: String?, function: () -> String) {
    HydraLog.log(LogLevel.INFO.logLevel, tag, function)
}

/**
 * Writes log with INFO log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun info(function: () -> String) {
    HydraLog.log(LogLevel.INFO.logLevel, null, function)
}

//================================================================
//== LogLevel.DEBUG ==============================================
/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun debug(tag: String?, message: String) {
    HydraLog.log(LogLevel.DEBUG.logLevel, tag, message)
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun debug(message: String) {
    HydraLog.log(LogLevel.DEBUG.logLevel, null, message)
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun debug(tag: String?, exception: Throwable) {
    HydraLog.log(LogLevel.DEBUG.logLevel, tag, exception)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun debug(exception: Throwable) {
    HydraLog.log(LogLevel.DEBUG.logLevel, null, exception)
}

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun debug(tag: String?, function: () -> String) {
    HydraLog.log(LogLevel.DEBUG.logLevel, tag, function)
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun debug(function: () -> String) {
    HydraLog.log(LogLevel.DEBUG.logLevel, null, function)
}

//================================================================
//== LogLevel.WARNING ============================================
/**
 * Writes log with WARNING log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun warn(tag: String?, message: String) {
    HydraLog.log(LogLevel.WARNING.logLevel, tag, message)
}

/**
 * Writes log with WARNING log level and empty tag.
 * @param message Message to be written into log.
 */
fun warn(message: String) {
    HydraLog.log(LogLevel.WARNING.logLevel, null, message)
}

/**
 * Log exception with WARNING log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun warn(tag: String?, exception: Throwable) {
    HydraLog.log(LogLevel.WARNING.logLevel, tag, exception)
}

/**
 * Log exception with WARNING log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun warn(exception: Throwable) {
    HydraLog.log(LogLevel.WARNING.logLevel, null, exception)
}

/**
 * Writes log with WARNING log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun warn(tag: String?, function: () -> String) {
    HydraLog.log(LogLevel.WARNING.logLevel, tag, function)
}

/**
 * Writes log with WARNING log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun warn(function: () -> String) {
    HydraLog.log(LogLevel.WARNING.logLevel, null, function)
}

//================================================================
//== LogLevel.ERROR ==============================================
/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun error(tag: String?, message: String) {
    HydraLog.log(LogLevel.ERROR.logLevel, tag, message)
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun error(message: String) {
    HydraLog.log(LogLevel.ERROR.logLevel, null, message)
}

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun error(tag: String?, exception: Throwable) {
    HydraLog.log(LogLevel.ERROR.logLevel, tag, exception)
}

/**
 * Log exception with ERROR log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun error(exception: Throwable) {
    HydraLog.log(LogLevel.ERROR.logLevel, null, exception)
}

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun error(tag: String?, function: () -> String) {
    HydraLog.log(LogLevel.ERROR.logLevel, tag, function)
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun error(function: () -> String) {
    HydraLog.log(LogLevel.ERROR.logLevel, null, function)
}