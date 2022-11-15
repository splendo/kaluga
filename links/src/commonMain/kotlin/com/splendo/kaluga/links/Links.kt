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

package com.splendo.kaluga.links

import com.splendo.kaluga.links.manager.PlatformLinksHandler
import com.splendo.kaluga.links.models.LinksManager
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

class Links(
    linksManagerBuilder: LinksManager.Builder
) {

    interface Builder {
        fun create(): Links
    }

    internal val linksManager: LinksManager = linksManagerBuilder.create()

    inline fun <reified T> handleIncomingLink(url: String): T? =
        handleIncomingLink(url, serializer<T>())

    /**
     * Convert an incoming url's query into [T] and return it.
     * When the given url is invalid, it returns null.
     *
     * @param url the whole url containing the query with values.
     * @param serializer serializer of type [T].
     **/
    fun <T> handleIncomingLink(url: String, serializer: KSerializer<T>): T? {
        return linksManager.handleIncomingLink(url, serializer)
    }

    /**
     * Check the url and returns it if valid, otherwise it returns null.
     * @param url url to validate.
     **/
    fun validateLink(url: String): String? {
        return linksManager.validateLink(url)
    }
}

expect class LinksBuilder(platformLinksHandler: PlatformLinksHandler) : Links.Builder
