package com.splendo.kaluga.base.utils

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.Foundation.NSMakeRange
import platform.Foundation.NSRange

val IntRange.nsRange: CValue<NSRange> get() = NSMakeRange(start.toULong(), (endInclusive + 1 - start).toULong())
val CValue<NSRange>.range: IntRange get() = useContents { IntRange(location.toInt(), (location + length).toInt() - 1) }
