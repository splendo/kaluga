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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.links.Links
import com.splendo.kaluga.links.manager.LinksManager
import com.splendo.kaluga.state.ColdStateRepo
import kotlinx.serialization.KSerializer

class LinksStateRepo(
    private val linksManagerBuilder: LinksManager.Builder
) : ColdStateRepo<LinksState>() {

    interface Builder {
        fun create(): LinksStateRepo
    }

    private val _linksManager: AtomicReference<LinksManager?> = AtomicReference(null)
    internal var linksManager: LinksManager?
        get() = _linksManager.get()
        set(value) = _linksManager.set(value)

    override suspend fun initialValue(): LinksState {
        linksManager = linksManagerBuilder.create(::onLinksStateChange)

        return LinksState.Pending
    }

    override suspend fun deinitialize(state: LinksState) {
        linksManager = null
    }

    internal fun onLinksStateChange(link: Links) {
        runBlocking {
            takeAndChangeState { state ->
                when (state) {
                    is LinksState.Error -> {
                        when (link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link.data) }
                            }
                            is Links.Failure -> {
                                { state.error(link.message) }
                            }
                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                            Links.Pending -> {
                                { state.pending() }
                            }
                        }
                    }
                    is LinksState.Ready<*> -> {
                        when (link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link.data) }
                            }
                            is Links.Failure -> {
                                { state.error(link.message) }
                            }
                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                            Links.Pending -> {
                                { state.pending() }
                            }
                        }
                    }
                    is LinksState.Pending -> {
                        when (link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link.data) }
                            }
                            is Links.Failure -> {
                                { state.error(link.message) }
                            }
                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                            Links.Pending -> {
                                state.remain()
                            }
                        }
                    }
                    is LinksState.Open -> {
                        when (link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link.data) }
                            }

                            is Links.Failure -> {
                                { state.error(link.message) }
                            }

                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                            Links.Pending -> {
                                { state.pending() }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Convert an incoming url into an object and return it wrapped in [Links.Incoming.Result].
     * When the given Uri/NSURL is invalid, the state returns [Links.Failure].
     * @param query query containing the values from the link.
     * @param serializer The needed object's serializer.
     * */
    fun <T> handleIncomingLink(query: String, serializer: KSerializer<T>) {
        linksManager?.handleIncomingLink(query, serializer)
    }

    /**
     * Check if the url is valid and returns [Links.Outgoing.Link] with the given url. State returns [Links.Failure]
     * when the url is invalid.
     * @param url Page to be visited.
     * */
    fun handleOutgoingLink(url: String) {
        linksManager?.handleOutgoingLink(url)
    }
}
