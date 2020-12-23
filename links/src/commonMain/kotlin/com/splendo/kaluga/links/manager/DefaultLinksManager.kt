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

typealias LinksStateChange = (Links) -> Unit

class DefaultLinksManager(
    private val onLinksStateChange: LinksStateChange,
    private val validator: LinksValidator
    ) : LinksManager {

    override fun <T> handleIncomingLink(query: String, serializer: KSerializer<T>) {
        val list = query.extractValuesAsList()
        if (list.isEmpty()) {
            onLinksStateChange(Links.Failure("Query was empty"))
            return
        }
        val deserializedObject = decodeFromList(list, serializer)

        onLinksStateChange(Links.Incoming.Result(deserializedObject))
    }

    override fun handleOutgoingLink(url: String) {
        if (validator.isValid(url)) {
            onLinksStateChange(Links.Outgoing.Link(url))
        } else {
            onLinksStateChange(Links.Failure("URL is invalid"))
        }
    }
}

fun String.extractValuesAsList(): List<Any> {
    if (isBlank()) {
        return emptyList()
    }
    return split("&")
        .filter { it.isNotEmpty() }
        .mapNotNull { it.split("=").lastOrNull() }
}

expect class LinksManagerBuilder
expect class PlatformLinksValidator : LinksValidator
