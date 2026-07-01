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
import platform.UIKit.UITraitCollection.Companion.traitCollectionWithUserInterfaceStyle
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.colorWithDynamicProvider
import platform.UIKit.resolvedColorWithTraitCollection

/**
 * Class describing a color
 * @property uiColor the [UIColor] describing the color
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual sealed class KalugaColor {
    abstract val uiColor: UIColor

    /**
     * A [KalugaColor] that is represented by a single rgb-value
     */
    actual data class RGBColor(override val uiColor: UIColor) : KalugaColor()

    /**
     * A [KalugaColor] that has a different [RGBColor] for [isInDarkMode] being `true` or `false`.
     * @property defaultColor the [RGBColor] to use when [isInDarkMode] is `false`.
     * @property darkColor the [RGBColor] to use when [isInDarkMode] is `true`.
     */
    actual data class DarkLightColor(override val uiColor: UIColor) : KalugaColor() {
        actual val defaultColor = RGBColor(
            uiColor.resolvedColorWithTraitCollection(traitCollectionWithUserInterfaceStyle(UIUserInterfaceStyle.UIUserInterfaceStyleLight)),
        )
        actual val darkColor = RGBColor(
            uiColor.resolvedColorWithTraitCollection(traitCollectionWithUserInterfaceStyle(UIUserInterfaceStyle.UIUserInterfaceStyleDark)),
        )
    }
}

/**
 * Gets the red value of the color in a range between `0.0` and `1.0`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.red: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(0) ?: 0.0

/**
 * Gets the red value of the color in a range between `0` and `255`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.redInt: Int get() = (red * 255.0).toInt()

/**
 * Gets the green value of the color in a range between `0.0` and `1.0`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.green: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(1) ?: 0.0

/**
 * Gets the green value of the color in a range between `0` and `255`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.blueInt: Int get() = (blue * 255.0).toInt()

/**
 * Gets the blue value of the color in a range between `0.0` and `1.0`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.blue: Double get() = CGColorGetComponents(uiColor.CGColor)?.get(2) ?: 0.0

/**
 * Gets the blue value of the color in a range between `0` and `255`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.greenInt: Int get() = (green * 255.0).toInt()

/**
 * Gets the alpha value of the color in a range between `0.0` and `1.0`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.alpha: Double get() = CGColorGetAlpha(uiColor.CGColor)

/**
 * Gets the alpha value of the color in a range between `0` and `255`.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual val KalugaColor.RGBColor.alphaInt: Int get() = (alpha * 255.0).toInt()

/**
 * Creates a [KalugaColor.RGBColor] using red, green, blue, and (optional) alpha, all ranging between `0.0` and `1.0`.
 * @param red The red color value ranging between `0.0` and `1.0`.
 * @param green The green color value ranging between `0.0` and `1.0`.
 * @param blue The blue color value ranging between `0.0` and `1.0`.
 * @param alpha The alpha color value ranging between `0.0` and `1.0`. Defaults to `1.0`
 * @return The [KalugaColor.RGBColor] with the corresponding red, green, blue, and alpha values
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double): KalugaColor.RGBColor = KalugaColor.RGBColor(UIColor.colorWithRed(red, green, blue, alpha))

/**
 * Creates a [KalugaColor.RGBColor] using red, green, blue, and (optional) alpha, all ranging between `0` and `255`.
 * @param redInt The red color value ranging between `0` and `255`.
 * @param greenInt The green color value ranging between `0` and `255`.
 * @param blueInt The blue color value ranging between `0` and `255`.
 * @param alphaInt The alpha color value ranging between `0` and `255`. Defaults to `255`
 * @return The [KalugaColor.RGBColor] with the corresponding red, green, blue, and alpha values
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int): KalugaColor.RGBColor =
    colorFrom(redInt.toDouble() / 255.0, greenInt.toDouble() / 255.0, blueInt.toDouble() / 255.0, alphaInt.toDouble() / 255.0)

/**
 * Creates a [KalugaColor.DarkLightColor] that uses [darkModeColor] when [isInDarkMode] and this color otherwise.
 * If this color has a dark mode already it will be overwritten.
 * If [darkModeColor] has a dark mode, it will be used.
 * @param darkModeColor the [KalugaColor.RGBColor] to use when [isInDarkMode]
 * @return a [KalugaColor.DarkLightColor] that supports a custom color in dark mode.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
actual infix fun KalugaColor.RGBColor.withDarkMode(darkModeColor: KalugaColor.RGBColor): KalugaColor.DarkLightColor = KalugaColor.DarkLightColor(
    UIColor.colorWithDynamicProvider { traits ->
        if (traits?.userInterfaceStyle() == UIUserInterfaceStyle.UIUserInterfaceStyleDark) {
            darkModeColor.uiColor
        } else {
            uiColor
        }
    },
)
