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

package com.splendo.kaluga.links.repository

import com.splendo.kaluga.links.manager.MockLinksRepoBuilder
import com.splendo.kaluga.links.manager.Person
import com.splendo.kaluga.links.manager.TestConstants
import kotlin.test.Test
import kotlin.test.assertEquals

class LinksRepoTest {

    private val linksRepoBuilder = MockLinksRepoBuilder()
    private val linksRepo: LinksRepo = linksRepoBuilder.create()

    @Test
    fun testIncomingLinkErrorTransaction() {
        val result = linksRepo.handleIncomingLink("http://invalid.com", Person.serializer())
        assertEquals(null, result)
    }

    @Test
    fun testIncomingTransaction() {
        val result = linksRepo.handleIncomingLink(Person.dummyUrl, Person.serializer())
        assertEquals(Person.dummyPerson, result)
    }

    @Test
    fun testOutgoingTransaction() {
        TestConstants.VALID_URLS.forEach {
            val result = linksRepo.validateLink(it)
            assertEquals(it, result)
        }
    }

    @Test
    fun testOutgoingTransactionError() {
        TestConstants.INVALID_URLS.forEach {
            val result = linksRepo.validateLink(it)
            assertEquals(null, result)
        }
    }
}
