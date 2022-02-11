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

package com.splendo.kaluga.resources

/**
 * Class describing a color
 */
expect class KalugaColor

@Deprecated("Due to name clashes with platform classes and API changes this class has been renamed and changed to an interface. It will be removed in a future release.", ReplaceWith("KalugaState"))
typealias Color = KalugaColor

/**
 * Gets the red value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.red: Double
/**
 * Gets the red value of the color in a range between `0` and `255`
 */
expect val KalugaColor.redInt: Int
/**
 * Gets the green value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.green: Double
/**
 * Gets the green value of the color in a range between `0` and `255`
 */
expect val KalugaColor.greenInt: Int
/**
 * Gets the blue value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.blue: Double
/**
 * Gets the blue value of the color in a range between `0` and `255`
 */
expect val KalugaColor.blueInt: Int
/**
 * Gets the alpha value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.alpha: Double
/**
 * Gets the alpha value of the color in a range between `0` and `255`
 */
expect val KalugaColor.alphaInt: Int

/**
 * Creates a [KalugaColor] using red, green, blue, and (optional) alpha, all ranging between `0.0` and `1.0`.
 * @param red The red color value ranging between `0.0` and `1.0`.
 * @param green The green color value ranging between `0.0` and `1.0`.
 * @param blue The blue color value ranging between `0.0` and `1.0`.
 * @param alpha The alpha color value ranging between `0.0` and `1.0`. Defaults to `1.0`
 * @return The [KalugaColor] with the corresponding red, green, blue, and alpha values
 */
expect fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double = 1.0): KalugaColor

/**
 * Creates a [KalugaColor] using red, green, blue, and (optional) alpha, all ranging between `0` and `255`.
 * @param red The red color value ranging between `0` and `255`.
 * @param green The green color value ranging between `0` and `255`.
 * @param blue The blue color value ranging between `0` and `255`.
 * @param alpha The alpha color value ranging between `0` and `255`. Defaults to `255`
 * @return The [KalugaColor] with the corresponding red, green, blue, and alpha values
 */
expect fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int = 255): KalugaColor
