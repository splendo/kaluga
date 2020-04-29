package com.splendo.kaluga.datetime

import com.splendo.kaluga.formatted.Formatted
import com.splendo.kaluga.formatted.Formatter
import com.splendo.kaluga.formatted.Modifier

/*
    This class should be used by VM to provide date to the UI level
    It contains DateTime model and information how it was formatted and rounded.
    If we need to create DateTime object with the same formatting and rounding we can use method new of this class
*/

data class FormattedDateTime(override val value: DateTime?,
                             override val formatter: Formatter<DateTime>,
                             override val modifier: Modifier<DateTime>? = null
): Formatted<DateTime, FormattedDateTime> {
    override fun spawn(
        value: DateTime?,
        formatter: Formatter<DateTime>,
        modifier: Modifier<DateTime>?
    ): FormattedDateTime
        = FormattedDateTime(value, formatter, modifier)
}