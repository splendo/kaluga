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

package com.splendo.kaluga.links

import com.splendo.kaluga.links.handler.LinksHandler
import com.splendo.kaluga.links.handler.PlatformLinksHandler
import com.splendo.kaluga.links.utils.decodeFromList
import kotlinx.serialization.KSerializer

/**
 * Manager for handling Links. Useful for handling deeplinks
 */
interface LinksManager {

    /**
     * Builder for creating a [LinksManager]
     */
    interface Builder {

        /**
         * Creates the [LinksManager]
         */
        fun create(): LinksManager
    }

    /**
     * Convert the query parameters of a url into [T] and return it.
     * When the given url is invalid, it returns null.
     *
     * @param T the type of data to parse the url into
     * @param url the whole url containing the query with values.
     * @param serializer serializer of type [T].
     * @return the [T] described by the [url] or `null` if the object was not described by the url
     **/
    fun <T> handleIncomingLink(url: String, serializer: KSerializer<T>): T?

    /**
     * Check a url and returns it if valid, otherwise it returns null.
     * @param url url to validate.
     * @return the [url] if valid, `null` otherwise
     **/
    fun validateLink(url: String): String?
}

/**
 * Default implementation of [LinksManager]
 * @param linksHandler the [LinksHandler] to handle processing the link
 */
class DefaultLinksManager(
    private val linksHandler: LinksHandler,
) : LinksManager {

    /**
     * Builder for creating a [DefaultLinksManager]
     * @param handler the [LinksHandler] to handle processing the link
     */
    class Builder(
        private val handler: LinksHandler,
    ) : LinksManager.Builder {

        constructor() : this(PlatformLinksHandler())

        override fun create(): LinksManager = DefaultLinksManager(handler)
    }

    override fun <T> handleIncomingLink(url: String, serializer: KSerializer<T>): T? {
        val list = linksHandler.extractQueryAsList(url)
        if (list.isEmpty()) {
            return null
        }

        return decodeFromList(list, serializer)
    }

    override fun validateLink(url: String): String? {
        return if (linksHandler.isValid(url)) {
            url
        } else {
            null
        }
    }
}
