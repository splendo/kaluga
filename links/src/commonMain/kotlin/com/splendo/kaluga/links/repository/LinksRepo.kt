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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.links.Links
import com.splendo.kaluga.links.manager.LinksManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.KSerializer

class LinksRepo(
    linksManagerBuilder: LinksManager.Builder
) {

    interface Builder {
        fun create(): LinksRepo
    }

    private val _linksEventFlow = MutableSharedFlow<Links>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val linksEventFlow = _linksEventFlow.asSharedFlow()

    internal var linksManager: LinksManager? = null

    init {
        linksManager = linksManagerBuilder.create(::onLinksChange)
    }

    internal fun onLinksChange(link: Links) {
        runBlocking {
            post(link)
        }
    }

    /**
     * Convert an incoming url into an object and emit it as [Links.Incoming.Result].
     * When the given Uri/NSURL is invalid, it emits [Links.Failure].
     * @param query query containing the values from the link.
     * @param serializer The needed object's serializer.
     * */
    fun <T> handleIncomingLink(query: String, serializer: KSerializer<T>) {
        linksManager?.handleIncomingLink(query, serializer)
    }

    /**
     * Check if the url is valid and emit a [Links.Outgoing.Link] in [linksEventFlow]. It emits [Links.Failure]
     * when the url is invalid.
     * @param url Page to be visited.
     * */
    fun validateLink(url: String) {
        linksManager?.validateLink(url)
    }

    private suspend fun post(link: Links) {
        _linksEventFlow.emit(link)
    }
}
