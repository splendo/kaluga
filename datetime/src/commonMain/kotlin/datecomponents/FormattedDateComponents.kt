package com.splendo.kaluga.datetime.datecomponents

import com.splendo.kaluga.datetime.DateTime
import com.splendo.kaluga.formatted.Formatted
import com.splendo.kaluga.formatted.Formatter
import com.splendo.kaluga.formatted.Modifier

data class FormattedDateComponents(
    override val value: DateComponents,
    override val formatter: Formatter<DateComponents> = DateComponentsFormatter.formatterForComponent(value),
    override val modifier: Modifier<DateComponents>? = null
) : Formatted<DateComponents, FormattedDateComponents> {
    override fun spawn(
        value: DateComponents?,
        formatter: Formatter<DateComponents>,
        modifier: Modifier<DateComponents>?
    ): FormattedDateComponents =
        FormattedDateComponents(value ?: DateComponents.undefinedDay(), formatter, modifier)

    fun new(dateTime: DateTime): FormattedDateComponents? =
            new(value = value?.new(dateTime))
}
