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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.resources.asColor
import com.splendo.kaluga.resources.asFont
import com.splendo.kaluga.resources.asImage
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.quantity
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MockResourcesTest {

    private companion object {
        const val ID = "id"
        const val ANOTHER_ID = "another-id"
    }

    @Test
    fun testMockStringLoaderGeneric() {
        val loader = MockStringLoader()
        assertEquals(ID, ID.localized(stringLoader = loader))
        assertEquals(ID, ID.quantity(1, stringLoader = loader))

        assertEquals(ANOTHER_ID, ANOTHER_ID.localized(stringLoader = loader))
        assertEquals(ANOTHER_ID, ANOTHER_ID.quantity(1, stringLoader = loader))
    }

    @Test
    fun testMockStringLoaderSpecific() {
        val loader = MockStringLoader(true)
        loader.loadStringMock.on(eq(ID)).doReturn("abc")
        loader.loadQuantityStringMock.on(eq(ID), eq(1)).doReturn("abc 1")
        assertEquals("abc", ID.localized(stringLoader = loader))
        assertEquals("abc 1", ID.quantity(1, stringLoader = loader))

        assertEquals(ANOTHER_ID, ANOTHER_ID.localized(stringLoader = loader))
        assertEquals("${ANOTHER_ID}_1", ANOTHER_ID.quantity(1, stringLoader = loader))
    }

    @Test
    fun testMockColorLoaderGeneric() {
        val loader = MockColorLoader()
        assertNull(ID.asColor(colorLoader = loader))
        assertNull(ANOTHER_ID.asColor(colorLoader = loader))
    }

    @Test
    fun testMockColorLoaderSpecific() {
        val mock = mockColor()
        val loader = MockColorLoader(true)
        loader.loadColorMock.on(eq(ID)).doReturn(mock)
        assertEquals(mock, ID.asColor(colorLoader = loader))
        assertNotNull(ANOTHER_ID.asColor(colorLoader = loader))
    }

    @Test
    fun testMockImageLoaderGeneric() {
        val loader = MockImageLoader()
        assertNull(ID.asImage(imageLoader = loader))
        assertNull(ANOTHER_ID.asImage(imageLoader = loader))
    }

    @Test
    fun testMockImageLoaderSpecific() {
        val mock = mockImage()
        val loader = MockImageLoader(true)
        loader.loadImageMock.on(eq(ID)).doReturn(mock)
        assertEquals(mock, ID.asImage(imageLoader = loader))
        assertNotNull(ANOTHER_ID.asImage(imageLoader = loader))
    }

    @Test
    fun testMockFontLoaderGeneric() = runBlocking {
        val loader = MockFontLoader()
        assertNull(ID.asFont(fontLoader = loader))
        assertNull(ANOTHER_ID.asFont(fontLoader = loader))
    }

    @Test
    fun testMockFontLoaderSpecific() = runBlocking {
        val mock = mockFont()
        val loader = MockFontLoader(true)
        loader.loadFontMock.on(eq(ID)).doReturn(mock)
        assertEquals(mock, ID.asFont(fontLoader = loader))
        assertNotNull(ANOTHER_ID.asFont(fontLoader = loader))
        Unit
    }
}
