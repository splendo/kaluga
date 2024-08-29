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
import platform.UIKit.UIColor

/**
 * Class describing a color
 * @property uiColor the [UIColor] describing the color
 */
actual data class KalugaColor(val uiColor: UIColor)

/**
 * Gets the red value of the color in a range between `0.0` and `1.0`
 */
actual val KalugaColor.red: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(0)?.toDouble() ?: 0.0

/**
 * Gets the red value of the color in a range between `0` and `255`
 */
actual val KalugaColor.redInt: Int get() = (red * 255.0).toInt()

/**
 * Gets the green value of the color in a range between `0.0` and `1.0`
 */
actual val KalugaColor.green: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(1)?.toDouble() ?: 0.0

/**
 * Gets the green value of the color in a range between `0` and `255`
 */
actual val KalugaColor.blueInt: Int get() = (blue * 255.0).toInt()

/**
 * Gets the blue value of the color in a range between `0.0` and `1.0`
 */
actual val KalugaColor.blue: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(2)?.toDouble() ?: 0.0

/**
 * Gets the blue value of the color in a range between `0` and `255`
 */
actual val KalugaColor.greenInt: Int get() = (green * 255.0).toInt()

/**
 * Gets the alpha value of the color in a range between `0.0` and `1.0`
 */
actual val KalugaColor.alpha: Double get() = CGColorGetAlpha(uiColor.CGColor).toDouble()

/**
 * Gets the alpha value of the color in a range between `0` and `255`
 */
actual val KalugaColor.alphaInt: Int get() = (alpha * 255.0).toInt()

/**
 * Creates a [KalugaColor] using red, green, blue, and (optional) alpha, all ranging between `0.0` and `1.0`.
 * @param red The red color value ranging between `0.0` and `1.0`.
 * @param green The green color value ranging between `0.0` and `1.0`.
 * @param blue The blue color value ranging between `0.0` and `1.0`.
 * @param alpha The alpha color value ranging between `0.0` and `1.0`. Defaults to `1.0`
 * @return The [KalugaColor] with the corresponding red, green, blue, and alpha values
 */
actual fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double): KalugaColor = KalugaColor(UIColor.colorWithRed(red, green, blue, alpha))

/**
 * Creates a [KalugaColor] using red, green, blue, and (optional) alpha, all ranging between `0` and `255`.
 * @param redInt The red color value ranging between `0` and `255`.
 * @param greenInt The green color value ranging between `0` and `255`.
 * @param blueInt The blue color value ranging between `0` and `255`.
 * @param alphaInt The alpha color value ranging between `0` and `255`. Defaults to `255`
 * @return The [KalugaColor] with the corresponding red, green, blue, and alpha values
 */
actual fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int): KalugaColor =
    colorFrom(redInt.toDouble() / 255.0, greenInt.toDouble() / 255.0, blueInt.toDouble() / 255.0, alphaInt.toDouble() / 255.0)
