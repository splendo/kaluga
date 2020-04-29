package com.splendo.kaluga.datetimewithklock

import com.splendo.kaluga.datetime.getUtility

fun useKlockForDateTimeUtility() {
    getUtility().configure(
            transformUtility = KlockDateTimeTransformUtility,
            formatterFactory = KlockDateTimeFormatterFactory
        )
    }
