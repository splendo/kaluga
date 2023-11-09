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

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.localizedStringWithFormat
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIImage
import platform.UIKit.UITraitCollection
import platform.UIKit.colorNamed
import platform.UIKit.labelFontSize

/**
 * Default implementation of a [StringLoader].
 * @param bundle the [NSBundle] from which to load the string resource
 * @param table the string table to search in. If tableName is 'null` or is an empty string, the method attempts to use the table in `Localizable.strings`
 */
actual class DefaultStringLoader(private val bundle: NSBundle, private val table: String?) : StringLoader {
    actual constructor() : this(NSBundle.mainBundle, null)
    override fun loadString(identifier: String, defaultValue: String): String = bundle.localizedStringForKey(identifier, defaultValue, table)
    override fun loadQuantityString(identifier: String, quantity: Int, defaultValue: String): String {
        val format = bundle.localizedStringForKey(identifier, defaultValue, table)
        return NSString.localizedStringWithFormat(format, quantity)
    }
}

/**
 * Default implementation of a [KalugaColorLoader].
 * @param bundle the [NSBundle] from which to load the color resource
 * @param traitCollection The [UITraitCollection] that specifies the gamut to use when selecting the color.
 */
actual class DefaultColorLoader(private val bundle: NSBundle, private val traitCollection: UITraitCollection?) : KalugaColorLoader {
    actual constructor() : this(NSBundle.mainBundle, null)
    override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? = UIColor
        .colorNamed(identifier, bundle, traitCollection)
        ?.let { KalugaColor(it) } ?: defaultValue
}

/**
 * Default implementation of an [ImageLoader].
 * @param bundle the [NSBundle] from which to load the image resource
 * @param traitCollection The [UITraitCollection] associated with the intended environment for the image.
 * Use this parameter to ensure that the system loads the correct variant of the image.
 * If you specify `null`, this method uses the traits associated with the main screen.
 */
actual class DefaultImageLoader(private val bundle: NSBundle, private val traitCollection: UITraitCollection?) : ImageLoader {
    actual constructor() : this(NSBundle.mainBundle, null)
    override fun loadImage(identifier: String, defaultValue: KalugaImage?): KalugaImage? = UIImage.imageNamed(identifier, bundle, traitCollection) ?: defaultValue
}

/**
 * Default implementation of a [FontLoader].
 */
actual class DefaultFontLoader actual constructor() : FontLoader {
    override suspend fun loadFont(identifier: String, defaultValue: KalugaFont?): KalugaFont? = UIFont.fontWithName(identifier, UIFont.labelFontSize) ?: defaultValue
}
