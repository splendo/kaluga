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

import com.splendo.kaluga.base.text.format

/**
 * Default implementation of a [StringLoader].
 * @param transformer method for getting a String from a String identifier
 * @param formatter method for formatting a String identifier given a quantity
 */
actual class DefaultStringLoader(
    private val transformer: (String) -> String?,
    private val formatter: (String, Int) -> String?,
) : StringLoader {
    actual constructor() : this({ it }, { format, value -> format.format(value) })

    override fun loadString(identifier: String, defaultValue: String): String = transformer(identifier) ?: defaultValue

    override fun loadQuantityString(identifier: String, quantity: Int, defaultValue: String): String = formatter(identifier, quantity) ?: defaultValue
}

/**
 * Default implementation of a [KalugaColorLoader].
 * @param transformer method for getting a [KalugaColor] from a String identifier
 */
actual class DefaultColorLoader(private val transformer: (String) -> KalugaColor?) : KalugaColorLoader {
    actual constructor() : this({ null })

    override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? = transformer(identifier) ?: defaultValue
}

/**
 * Default implementation of a [ImageLoader].
 * @param transformer method for getting a [KalugaImage] from a String identifier
 */
actual class DefaultImageLoader(private val transformer: (String) -> KalugaImage?) : ImageLoader {
    actual constructor() : this({ null })

    override fun loadImage(identifier: String, defaultValue: KalugaImage?): KalugaImage? = transformer(identifier) ?: defaultValue
}

/**
 * Default implementation of a [FontLoader].
 * @param transformer method for getting a [KalugaFont] from a String identifier
 */
actual class DefaultFontLoader(private val transformer: suspend (String) -> KalugaFont?) : FontLoader {
    actual constructor() : this({ null })

    override suspend fun loadFont(identifier: String, defaultValue: KalugaFont?): KalugaFont? = transformer(identifier) ?: defaultValue
}
