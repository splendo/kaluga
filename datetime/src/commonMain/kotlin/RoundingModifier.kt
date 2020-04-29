package com.splendo.kaluga.datetime

import com.splendo.kaluga.formatted.Modifier

class RoundingModifier(val minutesStep: Int) : Modifier<DateTime> {
    override fun apply(value: DateTime): DateTime = if (minutesStep in 2..30) {
        val millisInMinute = 60 * 1000
        val millisStep = minutesStep * millisInMinute
        val remainder = (value.timestamp % millisStep)
        val additive = if (remainder < millisStep / 2) 0 else millisStep
        val base = value.timestamp - remainder
        val roundedTimestamp = base + additive
        DateTime(roundedTimestamp)
    } else value
}
