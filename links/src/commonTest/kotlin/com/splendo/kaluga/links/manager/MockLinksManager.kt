/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import kotlinx.serialization.KSerializer

class MockLinksManager : LinksManager {
    override fun <T> handleIncomingLink(url: String, serializer: KSerializer<T>): T? {
        return when (url) {
            Person.dummyUrl -> Person.dummyPerson as T
            else -> null
        }
    }

    override fun validateLink(url: String): String? {
        return if (TestConstants.VALID_URLS.contains(url)) {
            url
        } else {
            null
        }
    }
}

class MockLinksManagerBuilder : LinksManager.Builder {
    override fun create(): LinksManager {
        return MockLinksManager()
    }
}

class MockLinksBuilder : Links.Builder {
    override fun create(): Links {
        return Links(MockLinksManagerBuilder())
    }
}

object TestConstants {

    val VALID_URLS = listOf(
        "https://test.io",
        "http://test.io",
        "https://test.io?isValid=true",
        "https://test.io?list_1=first&list_2=second&list_3=third"
    )

    val INVALID_URLS = listOf(
        "notvalid.com",
        "not valid",
        "httpss://notvalid.com?/?isValid=false"
    )
}
