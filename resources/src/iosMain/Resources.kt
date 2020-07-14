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

import platform.Foundation.NSBundle
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIImage
import platform.UIKit.UITraitCollection
import platform.UIKit.colorNamed
import platform.UIKit.labelFontSize

actual class StringLoader(private val bundle: NSBundle, private val table: String?) {
    actual constructor() : this(NSBundle.mainBundle, null)
    actual fun loadString(identifier: String): String = bundle.localizedStringForKey(identifier, null, table)
}

actual class ColorLoader(private val bundle: NSBundle, private val traitCollection: UITraitCollection?) {
    actual constructor() : this(NSBundle.mainBundle, null)
    actual fun loadColor(identifier: String): Color? = UIColor.colorNamed(identifier, bundle, traitCollection)?.let { Color(it) }
}

actual class ImageLoader(private val bundle: NSBundle, private val traitCollection: UITraitCollection?) {
    actual constructor() : this(NSBundle.mainBundle, null)
    actual fun loadImage(identifier: String): Image? = UIImage.imageNamed(identifier, bundle, traitCollection)
}

actual class FontLoader actual constructor() {
    actual suspend fun loadFont(identifier: String): Font? = UIFont.fontWithName(identifier, UIFont.labelFontSize)
}
