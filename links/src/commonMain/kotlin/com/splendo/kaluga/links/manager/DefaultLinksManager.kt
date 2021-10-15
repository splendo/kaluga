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

import kotlinx.serialization.KSerializer

class DefaultLinksManager(
    private val linksHandler: LinksHandler,
    private val defaultParametersDecoder: ParametersDecoder
) : LinksManager {

    override fun <T> handleIncomingLink(url: String, serializer: KSerializer<T>): T? {
        val list = linksHandler.extractQueryAsList(url)
        if (list.isEmpty()) {
            return null
        }

        return defaultParametersDecoder.decodeFromList(list, serializer)
    }

    override fun validateLink(url: String): String? {
        return if (linksHandler.isValid(url)) {
            url
        } else {
            null
        }
    }
}

class LinksManagerBuilder(
    private val platformLinksHandler: PlatformLinksHandler,
    private val parametersDecoder: ParametersDecoder
) : LinksManager.Builder {
    override fun create(): LinksManager = DefaultLinksManager(platformLinksHandler, parametersDecoder)
}

expect class PlatformLinksHandler constructor() : LinksHandler
