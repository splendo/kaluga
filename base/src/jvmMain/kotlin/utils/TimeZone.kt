/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.utils

actual class TimeZone internal constructor(internal val timeZone: java.util.TimeZone) : BaseTimeZone() {

    actual companion object {
        actual fun get(identifier: String): TimeZone? {
            return java.util.TimeZone.getTimeZone(identifier)?.let {
                TimeZone(it)
            }
        }
        actual fun current(): TimeZone {
            return TimeZone(java.util.TimeZone.getDefault())
        }
        actual val availableIdentifiers get() = java.util.TimeZone.getAvailableIDs().asList()
    }

    override val identifier: String = timeZone.id
    override fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: Locale): String {
        val styleJava = when (style) {
            TimeZoneNameStyle.Short -> java.util.TimeZone.SHORT
            TimeZoneNameStyle.Long -> java.util.TimeZone.LONG
        }
        return timeZone.getDisplayName(withDaylightSavings, styleJava, locale.locale)
    }
    override val offsetFromGMTInMilliseconds = timeZone.rawOffset.toLong()
    override val daylightSavingsOffsetInMilliseconds: Long = timeZone.dstSavings.toLong()
    override fun offsetFromGMTAtDateInMilliseconds(date: KalugaDate): Long = timeZone.getOffset(date.millisecondSinceEpoch).toLong()
    override fun usesDaylightSavingsTime(date: KalugaDate): Boolean = timeZone.inDaylightTime(date.date)
    override fun copy(): TimeZone = TimeZone(timeZone.clone() as java.util.TimeZone)
    override fun equals(other: Any?): Boolean {
        return (other as? TimeZone)?.let { timeZone == other.timeZone } ?: false
    }
}
