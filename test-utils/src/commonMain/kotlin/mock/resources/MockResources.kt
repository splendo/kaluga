/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.resources

import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.KalugaColorLoader
import com.splendo.kaluga.resources.Font
import com.splendo.kaluga.resources.FontLoader
import com.splendo.kaluga.resources.Image
import com.splendo.kaluga.resources.ImageLoader
import com.splendo.kaluga.resources.StringLoader

class MockStringLoader private constructor (
    private val string: String?,
    private val quantityString: String?,
    private val stringMap: Map<String, String?>,
    private val quantityStringMap: Map<String, String?>
) : StringLoader {
    constructor(string: String? = null, quantityString: String? = null) :
        this(string, quantityString, emptyMap(), emptyMap())
    constructor(stringMap: Map<String, String?>, quantityStringMap: Map<String, String?>) :
        this(null, null, stringMap, quantityStringMap)

    override fun loadString(identifier: String, defaultValue: String): String =
        stringMap[identifier] ?: string ?: defaultValue

    override fun loadQuantityString(
        identifier: String,
        quantity: Int,
        defaultValue: String
    ): String =
        (quantityStringMap[identifier] ?: quantityString)?.format(quantity) ?: defaultValue
}

class MockColorLoader private constructor (
    private val color: KalugaColor?,
    private val colorMap: Map<String, KalugaColor?>
) : KalugaColorLoader {
    constructor(color: KalugaColor? = null) : this(color, emptyMap())
    constructor(colorMap: Map<String, KalugaColor?>) : this(null, colorMap)

    override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? =
        colorMap[identifier] ?: color ?: defaultValue
}

class MockImageLoader private constructor (
    private val image: Image?,
    private val imageMap: Map<String, Image?>
) : ImageLoader {
    constructor(image: Image? = null) : this(image, emptyMap())
    constructor(imageMap: Map<String, Image?>) : this(null, imageMap)

    override fun loadImage(identifier: String, defaultValue: Image?): Image? =
        imageMap[identifier] ?: image ?: defaultValue
}

class MockFontLoader private constructor (
    private val font: Font?,
    private val fontMap: Map<String, Font?>
) : FontLoader {
    constructor(font: Font? = null) : this(font, emptyMap())
    constructor(fontMap: Map<String, Font?>) : this(null, fontMap)

    override suspend fun loadFont(identifier: String, defaultValue: Font?): Font? =
        fontMap[identifier] ?: font ?: defaultValue
}

expect fun mockColor(): KalugaColor
expect fun mockImage(): Image
expect fun mockFont(): Font
