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

import platform.UIKit.UIFont
import platform.UIKit.UIFontDescriptorSystemDesignDefault
import platform.UIKit.UIFontDescriptorSystemDesignMonospaced
import platform.UIKit.UIFontDescriptorSystemDesignSerif
import platform.UIKit.UIFontDescriptorTraitItalic
import platform.UIKit.UIFontWeightBlack
import platform.UIKit.UIFontWeightBold
import platform.UIKit.UIFontWeightHeavy
import platform.UIKit.UIFontWeightLight
import platform.UIKit.UIFontWeightMedium
import platform.UIKit.UIFontWeightRegular
import platform.UIKit.UIFontWeightSemibold
import platform.UIKit.UIFontWeightThin
import platform.UIKit.UIFontWeightUltraLight
import platform.UIKit.labelFontSize

/**
 * Class describing a font
 */
actual typealias KalugaFont = UIFont

/**
 * The default system [KalugaFont]
 */
actual val defaultFont: KalugaFont get() = UIFont.systemFontOfSize(UIFont.labelFontSize)

/**
 * The default bold system [KalugaFont]
 */
actual val defaultBoldFont: KalugaFont get() = UIFont.boldSystemFontOfSize(UIFont.labelFontSize)

/**
 * The default italic system [KalugaFont]
 */
actual val defaultItalicFont: KalugaFont get() = UIFont.italicSystemFontOfSize(UIFont.labelFontSize)

/**
 * The default monospace system [KalugaFont]
 */
actual val defaultMonospaceFont: KalugaFont get() = UIFont.monospacedSystemFontOfSize(UIFont.labelFontSize, UIFontWeightRegular)

/**
 * Creates a system font with a given weight, [FontStyle] and [FontTrait]
 * @param weight the weight to apply. Must be in range [1, 100]
 * @param style the [FontStyle] to apply
 * @param traits the set of [FontTrait] to apply
 * @return a [KalugaFont] representing the system font with the given specifications
 */
actual fun createDefaultFont(weight: Int, style: FontStyle, traits: Set<FontTrait>): KalugaFont {
    val calculateWeight: (Double, Double, FontWeight) -> Double = { uiFontWeight, factor, fontWeight ->
        val difference = factor * ((fontWeight.value - weight).toDouble() / 100.0)
        uiFontWeight - difference
    }
    // UIFontWeight is rather weird in that it is not evenly distributed.
    // For instance, UIFontWeightRegular is 0.0, UIFontWeightMedium is 0.23000000417232513, and UIFontWeightLight is -0.4000000059604645
    // To account for these inconsistencies, we interpolate between the steps
    val actualWeight = when {
        weight <= FontWeight.THIN.value -> {
            // Not a bug, iOS flips these names
            calculateWeight(UIFontWeightUltraLight, UIFontWeightThin - UIFontWeightUltraLight, FontWeight.THIN)
        }
        weight <= FontWeight.EXTRA_LIGHT.value -> {
            calculateWeight(UIFontWeightThin, UIFontWeightThin - UIFontWeightUltraLight, FontWeight.EXTRA_LIGHT)
        }
        weight <= FontWeight.LIGHT.value -> {
            calculateWeight(UIFontWeightLight, UIFontWeightLight - UIFontWeightThin, FontWeight.LIGHT)
        }
        weight <= FontWeight.NORMAL.value -> {
            calculateWeight(UIFontWeightRegular, UIFontWeightRegular - UIFontWeightLight, FontWeight.NORMAL)
        }
        weight <= FontWeight.MEDIUM.value -> {
            calculateWeight(UIFontWeightMedium, UIFontWeightMedium - UIFontWeightRegular, FontWeight.MEDIUM)
        }
        weight <= FontWeight.SEMI_BOLD.value -> {
            calculateWeight(UIFontWeightSemibold, UIFontWeightSemibold - UIFontWeightMedium, FontWeight.SEMI_BOLD)
        }
        weight <= FontWeight.BOLD.value -> {
            calculateWeight(UIFontWeightBold, UIFontWeightBold - UIFontWeightSemibold, FontWeight.BOLD)
        }
        weight <= FontWeight.EXTRA_BOLD.value -> {
            calculateWeight(UIFontWeightHeavy, UIFontWeightHeavy - UIFontWeightBold, FontWeight.EXTRA_BOLD)
        }
        weight <= FontWeight.EXTRA_LIGHT.value -> {
            calculateWeight(UIFontWeightBlack, UIFontWeightBlack - UIFontWeightHeavy, FontWeight.BLACK)
        }
        else -> {
            val difference = ((weight - FontWeight.BLACK.value).toDouble() / 100.0) * UIFontWeightBlack - UIFontWeightHeavy
            UIFontWeightBlack + difference
        }
    }
    val font = UIFont.systemFontOfSize(UIFont.labelFontSize, actualWeight)
    val design = when (style) {
        FontStyle.DEFAULT -> UIFontDescriptorSystemDesignDefault
        FontStyle.SERIF -> UIFontDescriptorSystemDesignSerif
        FontStyle.MONOSPACE -> UIFontDescriptorSystemDesignMonospaced
    }
    val symbolicTraits = traits.fold(0U) { acc, trait ->
        when (trait) {
            FontTrait.ITALIC -> acc or UIFontDescriptorTraitItalic
        }
    }
    return UIFont.fontWithDescriptor(
        font.fontDescriptor.fontDescriptorWithDesign(design)!!
            .fontDescriptorWithSymbolicTraits(symbolicTraits)!!,
        UIFont.labelFontSize,
    )
}
