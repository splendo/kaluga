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

package com.splendo.kaluga.links.repository

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.links.BaseLinksTest
import com.splendo.kaluga.links.Links
import com.splendo.kaluga.links.manager.Person
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class LinksStateTest : BaseLinksTest() {

    @Test
    fun testErrorTransaction() {
        val errorMessage = "Error Message"
        val expectedResult = Links.Failure(errorMessage)

        runBlocking {
            launch {
                linksRepo.linksEventFlow.collect {
                    assertEquals(expectedResult, it)
                    cancel()
                }
            }

            launch {
                linksRepo.onLinksChange(Links.Failure(errorMessage))
            }
        }
    }

    @Test
    fun testIncomingTransaction() {
        val person = Person.dummyPerson
        val expectedResult = Links.Incoming.Result(person)

        runBlocking {
            launch {
                linksRepo.linksEventFlow.collect {
                    assertEquals(expectedResult, it)
                    cancel()
                }
            }

            launch {
                linksRepo.onLinksChange(Links.Incoming.Result(person))
            }
        }
    }

    @Test
    fun testOutgoingTransaction() {
        val link = "https://kaluga-test-example.io/${Person.dummyQuery}"
        val expectedResult = Links.Outgoing.Link(link)

        runBlocking {
            launch {
                linksRepo.linksEventFlow.collect {
                    assertEquals(expectedResult, it)
                    cancel()
                }
            }

            launch {
                linksRepo.onLinksChange(Links.Outgoing.Link(link))
            }
        }
    }
}
