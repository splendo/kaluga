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

import com.splendo.kaluga.links.BaseLinksTest
import com.splendo.kaluga.links.state.LinksState
import com.splendo.kaluga.test.FlowTestBlock
import kotlinx.serialization.Serializable
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

class CommonLinksManagerTest : BaseLinksTest() {

    private lateinit var linksManager: BaseLinksManager

    @Test
    fun testHandleIncomingLinkSucceed() = testWithLinksState {
        assertInitialState(this)

        val query = "name=Corrado&surname=Quattrocchi&spokenLanguageSize=3&spokenLanguages=ITALIAN&spokenLanguages=ENGLISH&spokenLanguages=DUTCH"
        val expectedResult = Person(
            "Corrado",
            "Quattrocchi",
            listOf(Languages.ITALIAN, Languages.ENGLISH, Languages.DUTCH)
        )

        action {
            linksStateRepo.handleIncomingLink(query, Person.serializer())
        }

        test {
            assertTrue { it is LinksState.Ready<*> }
            assertEquals(expectedResult, (it as LinksState.Ready<Person>).data)
        }

    }

    @Test
    fun testHandleIncomingLinkFailed() = testWithLinksState {
        assertInitialState(this)

        val query = ""
        val expectedResult = "Query was empty"

        action {
            linksStateRepo.handleIncomingLink(query, Person.serializer())
        }

        test {
            assertTrue { it is LinksState.Error }
            assertEquals(expectedResult, (it as LinksState.Error).message)
        }
    }

    private fun testWithLinksState(block: FlowTestBlock<LinksState>) {
        linksStateRepo = linksStateRepoBuilder.create()

        testWithFlow(block)
    }
}