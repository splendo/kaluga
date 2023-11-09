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

import com.splendo.kaluga.resources.FontLoader
import com.splendo.kaluga.resources.ImageLoader
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.KalugaColorLoader
import com.splendo.kaluga.resources.KalugaFont
import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.StringLoader
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock

/**
 * Mock implementation of [StringLoader]
 * @param returnMock If `true` returns the identifier rather than the default value
 */
class MockStringLoader(private val returnMock: Boolean = false) : StringLoader {

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [loadString]
     */
    val loadStringMock = ::loadString.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [loadQuantityString]
     */
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

    override fun loadQuantityString(identifier: String, quantity: Int, defaultValue: String): String = loadQuantityStringMock.call(identifier, quantity, defaultValue)
}

/**
 * Mock implementation of [StringLoader]
 * @param returnMock If `true` returns [mockColor] rather than the default value
 */
class MockColorLoader(private val returnMock: Boolean = false) : KalugaColorLoader {

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [loadColor]
     */
    val loadColorMock = ::loadColor.mock()

    init {
        loadColorMock.on().doExecute { (_, defaultValue) ->
            if (returnMock) mockColor() else defaultValue
        }
    }

    override fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor? = loadColorMock.call(identifier, defaultValue)
}

/**
 * Mock implementation of [StringLoader]
 * @param returnMock If `true` returns [mockImage] rather than the default value
 */
class MockImageLoader(private val returnMock: Boolean = false) : ImageLoader {

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [loadImage]
     */
    val loadImageMock = ::loadImage.mock()

    init {
        loadImageMock.on().doExecute { (_, defaultValue) ->
            if (returnMock) mockImage() else defaultValue
        }
    }

    override fun loadImage(identifier: String, defaultValue: KalugaImage?): KalugaImage? = loadImageMock.call(identifier, defaultValue)
}

/**
 * Mock implementation of [StringLoader]
 * @param returnMock If `true` returns [mockFont] rather than the default value
 */
class MockFontLoader(private val returnMock: Boolean = false) : FontLoader {

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [loadFont]
     */
    val loadFontMock = ::loadFont.mock()

    init {
        loadFontMock.on().doExecute { (_, defaultValue) ->
            if (returnMock) mockFont() else defaultValue
        }
    }

    override suspend fun loadFont(identifier: String, defaultValue: KalugaFont?): KalugaFont? = loadFontMock.call(identifier, defaultValue)
}

/**
 * Creates a mocked [KalugaColor]
 */
expect fun mockColor(): KalugaColor

/**
 * Creates a mocked [KalugaImage]
 */
expect fun mockImage(): KalugaImage

/**
 * Creates a mocked [KalugaFont]
 */
expect fun mockFont(): KalugaFont
