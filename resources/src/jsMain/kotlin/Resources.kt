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

actual class DefaultStringLoader(
    private val transformer: (String) -> String?,
    private val formatter: (String, Int) -> String?
) : StringLoader {
    actual constructor() : this({ it }, { format, value -> format.format(value) })

    override fun loadString(identifier: String, defaultValue: String): String =
        transformer(identifier) ?: defaultValue

    override fun loadQuantityString(
        identifier: String,
        quantity: Int,
        defaultValue: String
    ): String = formatter(identifier, quantity) ?: defaultValue
}

actual class DefaultColorLoader(private val transformer: (String) -> KalugaColor?) : KalugaColorLoader {
    actual constructor() : this({ null })

    override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? =
        transformer(identifier) ?: defaultValue
}

actual class DefaultImageLoader(private val transformer: (String) -> Image?) : ImageLoader {
    actual constructor() : this({ null })

    override fun loadImage(identifier: String, defaultValue: Image?): Image? =
        transformer(identifier) ?: defaultValue
}

actual class DefaultFontLoader(private val transformer: suspend (String) -> Font?) : FontLoader {
    actual constructor() : this({ null })

    override suspend fun loadFont(identifier: String, defaultValue: Font?): Font? =
        transformer(identifier) ?: defaultValue
}
