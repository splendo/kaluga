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

package com.splendo.kaluga.links.manager

import com.splendo.kaluga.links.Links
import kotlinx.serialization.Serializable
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
data class Person(
    val name: String,
    val surname: String,
    val spokenLanguages: List<Languages> = emptyList()
)

enum class Languages {
    ITALIAN,
    FRENCH,
    ENGLISH,
    DUTCH,
    RUSSIAN
}

class CommonLinksManagerTest {

    private val linksManager = MockLinksManagerBuilder().create {
        linksStateChangeResult = it
        isOnLinksStateChangeCalled = true
    }

    private var isOnLinksStateChangeCalled = false
    private var linksStateChangeResult: Links? = null

    @AfterTest
    fun tearDown() {
        resetMocks()
    }

    @Test
    fun testHandleIncomingLinkSucceed() {
        val query = "name=Corrado&surname=Quattrocchi&spokenLanguageSize=3&spokenLanguages=ITALIAN&spokenLanguages=ENGLISH&spokenLanguages=DUTCH"
        val expectedResult = Person(
            "Corrado",
            "Quattrocchi",
            listOf(Languages.ITALIAN, Languages.ENGLISH, Languages.DUTCH)
        )
        linksManager.handleIncomingLink(query, Person.serializer())

        assertTrue { isOnLinksStateChangeCalled }
        assertEquals(expectedResult, (linksStateChangeResult as Links.Incoming.Result<Person>).data)
    }

    @Test
    fun testHandleIncomingLinkFailed() {
        // assertInitialState(this)

        val query = ""
        val expectedResult = "Query was empty"

        linksManager.handleIncomingLink(query, Person.serializer())

        assertTrue { isOnLinksStateChangeCalled }
        assertEquals(expectedResult, (linksStateChangeResult as Links.Failure).message)
    }

    @Test
    fun testHandleOutgoingLinkSucceed() {
        // assertInitialState(this)

        val url = "valid"

        linksManager.handleOutgoingLink(url)

        assertTrue { isOnLinksStateChangeCalled }
        assertEquals(url, (linksStateChangeResult as Links.Outgoing.Link).url)
    }

    @Test
    fun testHandleOutgoingLinkFailed() {
        // assertInitialState(this)

        val url = "not valid"
        val expectedResult = "URL is invalid"

        linksManager.handleOutgoingLink(url)

        assertTrue { isOnLinksStateChangeCalled }
        assertEquals(expectedResult, (linksStateChangeResult as Links.Failure).message)
    }

    private fun resetMocks() {
        linksStateChangeResult = null
        isOnLinksStateChangeCalled = false
    }
}
