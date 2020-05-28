/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

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
