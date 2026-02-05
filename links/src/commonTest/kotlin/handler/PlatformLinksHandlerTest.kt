/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.links.handler

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class PlatformLinksHandlerTest(private val linksValidator: PlatformLinksHandler) {

    companion object {

        val VALID_URLS = listOf(
            "https://test.io",
            "http://test.io",
            "https://test.io?isValid=true",
            "https://test.io?list_1=first&list_2=second&list_3=third",
        )

        val INVALID_URLS = listOf(
            "notvalid.com",
            "not valid",
            "httpss://notvalid.com?/?isValid=false",
        )
    }

    @Test
    fun testLinksValidatorSucceed() {
        VALID_URLS.forEach {
            assertTrue("Cannot handle $it") { linksValidator.isValid(it) }
        }
    }

    @Test
    fun testLinksValidatorFailed() {
        INVALID_URLS.forEach {
            assertFalse("Can handle $it") { linksValidator.isValid(it) }
        }
    }

    @Test
    fun testQueryExtractorSucceed() {
        val url = "https://test.io?list=first&list=second&list=third"

        val map = linksValidator.extractQueryAsMap(url)

        assertEquals(mapOf("list" to listOf("first", "second", "third")), map)
    }

    @Test
    fun testQueryExtractorEmptyQuery() {
        val url = "https://test.io"

        assertEquals(emptyMap(), linksValidator.extractQueryAsMap(url))
    }

    @Test
    fun testNestedLinksUndamaged() {
        val url = "https://redirect.com?link=https%3A%2F%2Fexample.com%3Fparam%3Dvalue"
        assertEquals(mapOf("link" to listOf("https://example.com?param=value")), linksValidator.extractQueryAsMap(url))
    }
}
