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

import com.splendo.kaluga.base.text.format

actual class StringLoader(
    private val transformer: (String) -> String?,
    private val formatter: (String, Int) -> String?
) {
    actual constructor() : this({ it }, { format, value -> format.format(value) })
    actual fun loadString(identifier: String, defaultValue: String): String = transformer(identifier) ?: defaultValue
    actual fun loadQuantityString(
        identifier: String,
        quantity: Int,
        defaultValue: String
    ): String = formatter(identifier, quantity) ?: defaultValue
}

actual class ColorLoader(private val transformer: (String) -> Color?) {
    actual constructor() : this({ null })
    actual fun loadColor(identifier: String, defaultValue: Color?): Color? = transformer(identifier) ?: defaultValue
}

actual class ImageLoader(private val transformer: (String) -> Image?) {
    actual constructor() : this({ null })
    actual fun loadImage(identifier: String, defaultValue: Image?): Image? = transformer(identifier) ?: defaultValue
}

actual class FontLoader(private val transformer: suspend (String) -> Font?) {
    actual constructor() : this({ null })
    actual suspend fun loadFont(identifier: String, defaultValue: Font?): Font? = transformer(identifier) ?: defaultValue
}
