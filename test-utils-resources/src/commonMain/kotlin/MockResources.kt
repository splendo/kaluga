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

package com.splendo.kaluga.test.resources

import com.splendo.kaluga.resources.Font
import com.splendo.kaluga.resources.FontLoader
import com.splendo.kaluga.resources.Image
import com.splendo.kaluga.resources.ImageLoader
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.KalugaColorLoader
import com.splendo.kaluga.resources.StringLoader
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock

class MockStringLoader(private val returnMock: Boolean = false) : StringLoader {

    val loadStringMock = ::loadString.mock()
    val loadQuantityStringMock = ::loadQuantityString.mock()

    init {
        loadStringMock.on().doExecute { (identifier, defaultValue) ->
            if (returnMock) identifier else defaultValue
        }
        loadQuantityStringMock.on().doExecute { (identifier, quantity, defaultValue) ->
            if (returnMock) "${identifier}_$quantity" else defaultValue
        }
    }

    override fun loadString(identifier: String, defaultValue: String): String = loadStringMock.call(identifier, defaultValue)

    override fun loadQuantityString(
        identifier: String,
        quantity: Int,
        defaultValue: String
    ): String = loadQuantityStringMock.call(identifier, quantity, defaultValue)
}

class MockColorLoader(private val returnMock: Boolean = false) : KalugaColorLoader {

    val loadColorMock = ::loadColor.mock()

    init {
        loadColorMock.on().doExecute { (_, defaultValue) ->
            if (returnMock) mockColor() else defaultValue
        }
    }

    override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? = loadColorMock.call(identifier, defaultValue)
}

class MockImageLoader(private val returnMock: Boolean = false) : ImageLoader {

    val loadImageMock = ::loadImage.mock()

    init {
        loadImageMock.on().doExecute { (_, defaultValue) ->
            if (returnMock) mockImage() else defaultValue
        }
    }

    override fun loadImage(identifier: String, defaultValue: Image?): Image? = loadImageMock.call(identifier, defaultValue)
}

class MockFontLoader(private val returnMock: Boolean = false) : FontLoader {

    val loadFontMock = ::loadFont.mock()

    init {
        loadFontMock.on().doExecute { (_, defaultValue) ->
            if (returnMock) mockFont() else defaultValue
        }
    }

    override suspend fun loadFont(identifier: String, defaultValue: Font?): Font? =
        loadFontMock.call(identifier, defaultValue)
}

expect fun mockColor(): KalugaColor
expect fun mockImage(): Image
expect fun mockFont(): Font
