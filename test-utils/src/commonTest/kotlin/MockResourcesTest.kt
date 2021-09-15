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

package com.splendo.kaluga.test

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.resources.asColor
import com.splendo.kaluga.resources.asFont
import com.splendo.kaluga.resources.asImage
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.quantity
import com.splendo.kaluga.test.mock.resources.MockColorLoader
import com.splendo.kaluga.test.mock.resources.MockFontLoader
import com.splendo.kaluga.test.mock.resources.MockImageLoader
import com.splendo.kaluga.test.mock.resources.MockStringLoader
import com.splendo.kaluga.test.mock.resources.mockColor
import com.splendo.kaluga.test.mock.resources.mockFont
import com.splendo.kaluga.test.mock.resources.mockImage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MockResourcesTest {

    private companion object {
        const val ID = "id"
        const val ANOTHER_ID = "another-id"
    }

    @Test
    fun testMockStringLoaderGeneric() {
        val loader = MockStringLoader("abc", "abc %s")
        assertEquals("abc", ID.localized(stringLoader = loader))
        assertEquals("abc 1", ID.quantity( 1, stringLoader = loader))

        assertEquals("abc", ANOTHER_ID.localized(stringLoader = loader))
        assertEquals("abc 1", ANOTHER_ID.quantity( 1, stringLoader = loader))
    }

    @Test
    fun testMockStringLoaderSpecific() {
        val loader = MockStringLoader(
            mapOf(ID to "abc"),
            mapOf(ID to "abc %s")
        )
        assertEquals("abc", ID.localized(stringLoader = loader))
        assertEquals("abc 1", ID.quantity( 1, stringLoader = loader))

        assertEquals(ANOTHER_ID, ANOTHER_ID.localized(stringLoader = loader))
        assertEquals(ANOTHER_ID, ANOTHER_ID.quantity( 1, stringLoader = loader))
    }

    @Test
    fun testMockColorLoaderGeneric() {
        val mock = mockColor()
        val loader = MockColorLoader(mock)
        assertEquals(mock, ID.asColor(colorLoader = loader))
        assertEquals(mock, ANOTHER_ID.asColor(colorLoader = loader))
    }

    @Test
    fun testMockColorLoaderSpecific() {
        val mock = mockColor()
        val loader = MockColorLoader(mapOf(ID to mock))
        assertEquals(mock, ID.asColor(colorLoader = loader))
        assertNull(ANOTHER_ID.asColor(colorLoader = loader))
    }

    @Test
    fun testMockImageLoaderGeneric() {
        val mock = mockImage()
        val loader = MockImageLoader(mock)
        assertEquals(mock, ID.asImage(imageLoader = loader))
        assertEquals(mock, ANOTHER_ID.asImage(imageLoader = loader))
    }

    @Test
    fun testMockImageLoaderSpecific() {
        val mock = mockImage()
        val loader = MockImageLoader(mapOf(ID to mock))
        assertEquals(mock, ID.asImage(imageLoader = loader))
        assertNull(ANOTHER_ID.asImage(imageLoader = loader))
    }

    @Test
    fun testMockFontLoaderGeneric() = runBlocking {
        val mock = mockFont()
        val loader = MockFontLoader(mock)
        assertEquals(mock, ID.asFont(fontLoader = loader))
        assertEquals(mock, ANOTHER_ID.asFont(fontLoader = loader))
    }

    @Test
    fun testMockFontLoaderSpecific() = runBlocking {
        val mock = mockFont()
        val loader = MockFontLoader(mapOf(ID to mock))
        assertEquals(mock, ID.asFont(fontLoader = loader))
        assertNull(ANOTHER_ID.asFont(fontLoader = loader))
    }
}
