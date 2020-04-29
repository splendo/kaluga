package com.splendo.kaluga.datetime

import kotlin.native.concurrent.ThreadLocal

interface DateTimeUtility {
    fun configure(transformUtility: DateTimeTransformUtility, formatterFactory: DateTimeFormatterFactory)
    var transform: DateTimeTransformUtility
    var formatterFactory: DateTimeFormatterFactory
}

@ThreadLocal
object StandardDateTimeUtility : DateTimeUtility {
    override fun configure(transformUtility: DateTimeTransformUtility, formatterFactory: DateTimeFormatterFactory) {
        this.transform = transformUtility
        this.formatterFactory = formatterFactory
    }

    override lateinit var transform: DateTimeTransformUtility
    override lateinit var formatterFactory: DateTimeFormatterFactory
}

interface DateTimeTransformUtility {
    // Components
    fun dateTimeFromLocalComponents(components: DateTimeComponents): DateTime
    fun componentsFromLocalDateTime(dateTime: DateTime): DateTimeComponents
    fun dateTimeFromGMTComponents(components: DateTimeComponents): DateTime
    fun componentsFromGMTDateTime(dateTime: DateTime): DateTimeComponents
}

interface DateTimeFormatterFactory {
    fun getFormatter(format: String, defaultTimeZoneOffset: Int = defaultTimeZoneOffset()): DateTimeFormatter
    fun defaultTimeZoneOffset(): Int
}

fun getUtility(): DateTimeUtility =
    StandardDateTimeUtility
