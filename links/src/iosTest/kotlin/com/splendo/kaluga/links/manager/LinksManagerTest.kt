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

import kotlin.test.Test
import kotlin.test.assertEquals

class LinksManagerTestIOS {

    private val linksManager = LinksManagerBuilder(
        PlatformLinksHandler(),
        DefaultParametersDecoder()
    ).create()

    @Test
    fun testHandleIncomingLinkSucceed() {
        val result = linksManager.handleIncomingLink(Person.dummyUrl, Person.serializer())

        assertEquals(Person.dummyPerson, result)
    }

    @Test
    fun testHandleIncomingLinkFailed() {
        val query = ""

        val result = linksManager.handleIncomingLink(query, Person.serializer())
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
