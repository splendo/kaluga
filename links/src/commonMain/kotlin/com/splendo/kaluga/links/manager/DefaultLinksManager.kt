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
import com.splendo.kaluga.links.utils.decodeFromList
import kotlinx.serialization.KSerializer

typealias OnLinksChange = (Links) -> Unit

class DefaultLinksManager(
    private val onLinksChange: OnLinksChange,
    private val onLinkValidated: OnLinksChange,
    private val linksHandler: LinksHandler
) : LinksManager {

    override fun <T> handleIncomingLink(url: String, serializer: KSerializer<T>) {
        val list = linksHandler.extractQueryAsList(url)
        if (list.isEmpty()) {
            onLinksChange(Links.Failure("Query was empty"))
            return
        }
        val deserializedObject = decodeFromList(list, serializer)
        onLinksChange(Links.Incoming.Result(deserializedObject))
    }

    override fun validateLink(url: String) {
        if (linksHandler.isValid(url)) {
            onLinkValidated(Links.Outgoing.Link(url))
        } else {
            onLinkValidated(Links.Failure("URL is invalid"))
        }
    }
}

expect class LinksManagerBuilder
expect class PlatformLinksHandler : LinksHandler
