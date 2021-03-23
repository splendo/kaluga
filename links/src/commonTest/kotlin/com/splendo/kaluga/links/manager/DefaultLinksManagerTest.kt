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
) {
    companion object {
        val dummyUrl = "http://url.com?name=Corrado&surname=Quattrocchi&spokenLanguageSize=3&spokenLanguages=ITALIAN&spokenLanguages=ENGLISH&spokenLanguages=DUTCH"
        val dummyPerson = Person(
            "Corrado",
            "Quattrocchi",
            listOf(Languages.ITALIAN, Languages.ENGLISH, Languages.DUTCH)
        )
    }
}

enum class Languages {
    ITALIAN,
    ENGLISH,
    DUTCH,
}

class CommonLinksManagerTest {

    private val linksManager = MockLinksManagerBuilder().create(
        {
            incomingLinksChangeResult = it
            isOnLinksChangeCalled = true
        },
        {
            outgoingLinksResult = it
            isOnLinkValidatedCalled = true
        }
    )

    private var isOnLinksChangeCalled = false
    private var incomingLinksChangeResult: Links? = null

    private var isOnLinkValidatedCalled = false
    private var outgoingLinksResult: Links? = null

    @AfterTest
    fun tearDown() {
        resetMocks()
    }

    @Test
    fun testHandleIncomingLinkSucceed() {
        linksManager.handleIncomingLink(Person.dummyUrl, Person.serializer())

        assertTrue { isOnLinksChangeCalled }
        assertEquals(Person.dummyPerson, (incomingLinksChangeResult as Links.Incoming.Result<Person>).data)
    }

    @Test
    fun testHandleIncomingLinkFailed() {
        val query = ""
        val expectedResult = "Query was empty"

        linksManager.handleIncomingLink(query, Person.serializer())

        assertTrue { isOnLinksChangeCalled }
        assertEquals(expectedResult, (incomingLinksChangeResult as Links.Failure).message)
    }

    @Test
    fun testHandleOutgoingLinkSucceed() {
        val url = "http://valid-link?parameter=1"

        linksManager.validateLink(url)

        assertTrue { isOnLinkValidatedCalled }
        assertEquals(url, (outgoingLinksResult as Links.Outgoing.Link).url)
    }

    @Test
    fun testHandleOutgoingLinkFailed() {
        val url = "not valid"
        val expectedResult = "URL is invalid"

        linksManager.validateLink(url)

        assertTrue { isOnLinkValidatedCalled }
        assertEquals(expectedResult, (outgoingLinksResult as Links.Failure).message)
    }

    private fun resetMocks() {
        incomingLinksChangeResult = null
        isOnLinksChangeCalled = false

        outgoingLinksResult = null
        isOnLinkValidatedCalled = false
    }
}
