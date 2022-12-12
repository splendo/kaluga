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

package com.splendo.kaluga.resources

import kotlinx.cinterop.get
import platform.CoreGraphics.CGColorGetAlpha
import platform.CoreGraphics.CGColorGetComponents
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIColor

actual data class KalugaColor(val uiColor: UIColor)

actual val KalugaColor.red: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(0)?.toDouble() ?: 0.0
actual val KalugaColor.redInt: Int get() = (red * 255.0).toInt()
actual val KalugaColor.green: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(1)?.toDouble() ?: 0.0
actual val KalugaColor.blueInt: Int get() = (blue * 255.0).toInt()
actual val KalugaColor.blue: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(2)?.toDouble() ?: 0.0
actual val KalugaColor.greenInt: Int get() = (green * 255.0).toInt()
actual val KalugaColor.alpha: Double get() = CGColorGetAlpha(uiColor.CGColor).toDouble()
actual val KalugaColor.alphaInt: Int get() = (alpha * 255.0).toInt()

actual fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double): KalugaColor = KalugaColor(UIColor.colorWithRed(red as CGFloat, green as CGFloat, blue as CGFloat, alpha as CGFloat))
actual fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int): KalugaColor {
    return colorFrom(redInt.toDouble() / 255.0, greenInt.toDouble() / 255.0, blueInt.toDouble() / 255.0, alphaInt.toDouble() / 255.0)
}
