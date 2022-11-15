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

package com.splendo.kaluga.links.manager

import com.splendo.kaluga.links.DataTypesValues
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LinksManagerTestIOS {

    private val linksManager = LinksManagerBuilder(PlatformLinksHandler()).create()

    @Test
    fun testHandleIncomingLinkSucceed() {
        val result = linksManager.handleIncomingLink(
            DataTypesValues.url,
            DataTypesValues.serializer()
        )

        assertEquals(DataTypesValues.expectedValidValues, result)
    }

    @Test
    fun testHandleIncomingLinkFailed() {
        val query = ""

        val result = linksManager.handleIncomingLink(query, DataTypesValues.serializer())
        assertEquals(null, result)
    }

    @Test
    fun test_throw_exception_when_field_is_missing() {
        val url = DataTypesValues.url
            .split("&")
            .filterNot { it.contains("enumValue") }
            .joinToString("&")

        assertFailsWith<SerializationException> {
            // MissingFieldException is internal in kotlinx.serialization
            linksManager.handleIncomingLink(url, DataTypesValues.serializer())
        }
    }

    @Test
    fun test_throw_exception_when_field_is_unknown() {
        val url = DataTypesValues.url
            .split("&").joinToString("&") {
                if (it.contains("enumValue")) {
                    "unknownField=idk"
                } else {
                    it
                }
            }

        assertFailsWith<SerializationException> {
            // UnknownFieldException is internal in kotlinx.serialization
            linksManager.handleIncomingLink(url, DataTypesValues.serializer())
        }
    }

    @Test
    fun testHandleOutgoingLinkSucceed() {
        val url = "https://valid-link?parameter=1"

        val result = linksManager.validateLink(url)
        assertEquals(url, result)
    }

    @Test
    fun testHandleOutgoingLinkFailed() {
        val url = "not valid"

        val result = linksManager.validateLink(url)
        assertEquals(null, result)
    }
}
