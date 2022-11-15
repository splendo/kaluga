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

package com.splendo.kaluga.links

import com.splendo.kaluga.links.manager.PlatformLinksHandler
import com.splendo.kaluga.links.manager.TestConstants.INVALID_URLS
import com.splendo.kaluga.links.manager.TestConstants.VALID_URLS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IosLinksHandlerTest {

    private val linksValidator = PlatformLinksHandler()

    @Test
    fun testLinksValidatorSucceed() {
        VALID_URLS.forEach {
            assertTrue { linksValidator.isValid(it) }
        }
    }

    @Test
    fun testLinksValidatorFailed() {
        INVALID_URLS.forEach {
            assertFalse { linksValidator.isValid(it) }
        }
    }

    @Test
    fun testQueryExtractorSucceed() {
        val url = "https://test.io?list_1=first&list_2=second&list_3=third"

        assertEquals(
            mapOf(
                "list_1" to "first",
                "list_2" to "second",
                "list_3" to "third"
            ),
            linksValidator.extractQuery(url)
        )
    }

    @Test
    fun testQueryExtractorEmptyQuery() {
        val url = "https://test.io"

        assertEquals(emptyMap<String, Any>(), linksValidator.extractQuery(url))
    }
}
