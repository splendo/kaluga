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
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultLinksManagerTest {

    private val linksManagerBuilder = MockLinksManagerBuilder()
    private val linksManager = linksManagerBuilder.create()

    @Test
    fun test_handle_incoming_link_succeed() {
        val result = linksManager.handleIncomingLink(
            DataTypesValues.url,
            DataTypesValues.serializer()
        )

        assertEquals(DataTypesValues.expectedValidValues, result)
    }

    @Test
    fun test_handle_incoming_link_url_is_empty() {
        val result = linksManager.handleIncomingLink(
            DataTypesValues.urlHost,
            DataTypesValues.serializer()
        )

        assertEquals(null, result)
    }

    @Test
    fun test_handle_incoming_link_is_valid() {
        val result = linksManager.validateLink(DataTypesValues.url)

        assertEquals(DataTypesValues.url, result)
    }

    @Test
    fun test_handle_incoming_link_is_invalid() {
        val result = linksManager.validateLink("")

        assertEquals(null, result)
    }
}
