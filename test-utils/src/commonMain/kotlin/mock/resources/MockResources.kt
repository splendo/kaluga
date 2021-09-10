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
import com.splendo.kaluga.resources.Color
import com.splendo.kaluga.resources.ColorLoader
import com.splendo.kaluga.resources.Font
import com.splendo.kaluga.resources.FontLoader
import com.splendo.kaluga.resources.Image
import com.splendo.kaluga.resources.ImageLoader
import com.splendo.kaluga.resources.StringLoader

class MockStringLoader (
    private val stringMap: Map<String, String?>,
    private val quantityStringMap: Map<String, String?>
): StringLoader {
    constructor(string: String? = null, quantityString: String? = null):
        this(
            emptyMap<String, String?>().withDefault { string },
            emptyMap<String, String?>().withDefault { quantityString }
        )

    override fun loadString(identifier: String, defaultValue: String): String =
        stringMap[identifier] ?: defaultValue

    override fun loadQuantityString(
        identifier: String,
        quantity: Int,
        defaultValue: String
    ): String =
        quantityStringMap[identifier]?.format(quantity) ?: defaultValue
}

class MockColorLoader(private val colorMap: Map<String, Color?>): ColorLoader {
    constructor(color: Color? = null): this(emptyMap<String, Color?>().withDefault { color })

    override fun loadColor(identifier: String, defaultValue: Color?): Color? =
        colorMap[identifier] ?: defaultValue
}

class MockImageLoader(private val imageMap: Map<String, Image?>): ImageLoader {
    constructor(image: Image? = null): this(emptyMap<String, Image?>().withDefault { image })

    override fun loadImage(identifier: String, defaultValue: Image?): Image? =
        imageMap[identifier] ?: defaultValue
}

class MockFontLoader(private val fontMap: Map<String, Font?>): FontLoader {
    constructor(font: Font? = null): this(emptyMap<String, Font?>().withDefault { font })

    override suspend fun loadFont(identifier: String, defaultValue: Font?): Font? =
        fontMap[identifier] ?: defaultValue
}

expect fun mockColor(): Color
expect fun mockImage(): Image
// NOTE: returns null in Android unit tests
expect fun mockFont(): Font?
