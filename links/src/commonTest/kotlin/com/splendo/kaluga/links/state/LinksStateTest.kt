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

package com.splendo.kaluga.links.state

import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.links.BaseLinksTest
import com.splendo.kaluga.links.Links
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowTestBlock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinksStateTest : BaseLinksTest() {

    override fun flowable(): Flowable<LinksState> =
        linksStateRepo.flowable

    @Test
    fun testInitialValue() = testLinksState {
        assertInitialState(this)
    }

    @Test
    fun testPendingStateTransaction() = testLinksState {
        assertInitialState(this)
        val errorMessage = "Error Message"

        action {
             linksStateRepo.onLinksStateChange(Links.Failure(errorMessage))
        }

        test {
            assertTrue { it is LinksState.Error }
            assertEquals(errorMessage, (it as LinksState.Error).message)
        }
        resetStateTo<LinksState.Pending>(Links.Pending, this)

        val resultMessage = "result"
        action {
            linksStateRepo.onLinksStateChange(Links.Incoming.Result(resultMessage))
        }

        test {
            assertTrue { it is LinksState.Ready<*> }
            assertEquals(resultMessage, (it as LinksState.Ready<*>).data)
        }
        resetStateTo<LinksState.Pending>(Links.Pending, this)

        val url = "http://links.test"
        action {
            linksStateRepo.onLinksStateChange(Links.Outgoing.Link(url))
        }

        test {
            assertTrue { it is LinksState.Open }
            assertEquals(url, (it as LinksState.Open).url)
        }
    }

    @Test
    fun testErrorStateTransaction() = testLinksState {
        assertInitialState(this)
        val defaultError = "Error Message"
        val resultMessage = "result"
        action {
            linksStateRepo.onLinksStateChange(Links.Incoming.Result(resultMessage))
        }

        test {
            assertTrue { it is LinksState.Ready<*> }
            assertEquals(resultMessage, (it as LinksState.Ready<*>).data)
        }
        resetStateTo<LinksState.Error>(Links.Failure(defaultError), this)

        val url = "http://links.test"
        action {
            linksStateRepo.onLinksStateChange(Links.Outgoing.Link(url))
        }

        test {
            assertTrue { it is LinksState.Open }
            assertEquals(url, (it as LinksState.Open).url)
        }
        resetStateTo<LinksState.Error>(Links.Failure(defaultError), this)

        action {
            linksStateRepo.onLinksStateChange(Links.Pending)
        }

        test {
            assertTrue { it is LinksState.Pending }
        }
        resetStateTo<LinksState.Error>(Links.Failure(defaultError), this)

        // test if the error message change in case a Error state is incoming and
        // the current state is Error
        val errorMessage = "new error"
        action {
            linksStateRepo.onLinksStateChange(Links.Failure(errorMessage))
        }

        test {
            assertTrue { it is LinksState.Error }
            assertEquals(errorMessage, (it as LinksState.Error).message)
        }
    }

    @Test
    fun testOpenStateTransaction() = testLinksState {
        assertInitialState(this)
        val defaultLink = "http://links.test"

        val resultMessage = "result"
        action {
            linksStateRepo.onLinksStateChange(Links.Incoming.Result(resultMessage))
        }

        test {
            assertTrue { it is LinksState.Ready<*> }
            assertEquals(resultMessage, (it as LinksState.Ready<*>).data)
        }
        resetStateTo<LinksState.Open>(Links.Outgoing.Link(defaultLink), this)

        action {
            linksStateRepo.onLinksStateChange(Links.Pending)
        }

        test {
            assertTrue { it is LinksState.Pending }
        }
        resetStateTo<LinksState.Open>(Links.Outgoing.Link(defaultLink), this)

        val errorMessage = "Error Message"
        action {
            linksStateRepo.onLinksStateChange(Links.Failure(errorMessage))
        }

        test {
            assertTrue { it is LinksState.Error }
            assertEquals(errorMessage, (it as LinksState.Error).message)
        }
        resetStateTo<LinksState.Open>(Links.Outgoing.Link(defaultLink), this)

        val newUrl = "http://links.test?isTest=true"
        action {
            linksStateRepo.onLinksStateChange(Links.Outgoing.Link(newUrl))
        }

        test {
            assertTrue { it is LinksState.Open }
            assertEquals(newUrl, (it as LinksState.Open).url)
        }
    }

    @Test
    fun testReadyStateTransaction() = testLinksState {
        assertInitialState(this)
        val defaultData = "Default Data"

        val errorMessage = "Error Message"
        action {
            linksStateRepo.onLinksStateChange(Links.Failure(errorMessage))
        }

        test {
            assertTrue { it is LinksState.Error }
            assertEquals(errorMessage, (it as LinksState.Error).message)
        }
        resetStateTo<LinksState.Ready<*>>(Links.Incoming.Result(defaultData), this)

        val url = "http://links.test?isTest=true"
        action {
            linksStateRepo.onLinksStateChange(Links.Outgoing.Link(url))
        }

        test {
            assertTrue { it is LinksState.Open }
            assertEquals(url, (it as LinksState.Open).url)
        }
        resetStateTo<LinksState.Ready<*>>(Links.Incoming.Result(defaultData), this)

        val newData = "new string value"
        action {
            linksStateRepo.onLinksStateChange(Links.Incoming.Result(newData))
        }

        test {
            assertTrue { it is LinksState.Ready<*> }
            assertEquals(newData, (it as LinksState.Ready<String>).data)
        }
    }

    private fun testLinksState(test: FlowTestBlock<LinksState>) {
        linksStateRepo = linksStateRepoBuilder.create()

        testWithFlow(test)
    }

    private suspend inline fun <reified T> resetStateTo(link: Links, testBlock: FlowTest<LinksState>) {
        testBlock.action {
            linksStateRepo.onLinksStateChange(link)
        }
        testBlock.test {
            assertTrue { it is T }
        }
    }
}