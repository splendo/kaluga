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
import org.junit.Test
import kotlin.test.assertEquals

class LinksManagerTestAndroid {

    private val linksManager = LinksManagerBuilder(PlatformLinksHandler()).create()

    @Test
    fun testHandleIncomingLinkSucceed() {
        val result = linksManager.handleIncomingLink(DataTypesValues.url, DataTypesValues.serializer())

        assertEquals(DataTypesValues.expectedValidValues, result)
    }

    @Test
    fun testHandleIncomingLinkFailed() {
        val query = ""

        val result = linksManager.handleIncomingLink(query, DataTypesValues.serializer())
        assertEquals(null, result)
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
