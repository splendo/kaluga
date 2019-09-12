package com.splendo.mpp.util

class LoggerMock : Logger {

    val exceptionsList: MutableList<Throwable?> = mutableListOf()
    val messageList: MutableList<String?> = mutableListOf()
    val tagList: MutableList<String?> = mutableListOf()
    val levelList: MutableList<LogLevel?> = mutableListOf()

    override fun log(level: LogLevel, tag: String?, message: String) {
        this.levelList.add(level)
        this.tagList.add(tag)
        this.messageList.add(message)
        this.exceptionsList.add(null)
    }

    override fun log(level: LogLevel, tag: String?, exception: Throwable) {
        this.levelList.add(level)
        this.tagList.add(tag)
        this.messageList.add(null)
        this.exceptionsList.add(exception)
    }

    fun clear() {
        levelList.clear()
        messageList.clear()
        tagList.clear()
        exceptionsList.clear()
    }

}