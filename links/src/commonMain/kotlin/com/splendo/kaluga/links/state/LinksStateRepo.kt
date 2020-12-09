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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.links.Links
import com.splendo.kaluga.links.manager.BaseLinksManager
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.ColdStateRepo
import kotlinx.serialization.KSerializer

class LinksStateRepo(
    private val linksManagerBuilder: BaseLinksManager.Builder
) : ColdStateRepo<LinksState>() {

    interface Builder {
        fun create(): LinksStateRepo
    }

    var linksManager: BaseLinksManager? = null

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
                                { state.ready(link) }
                            }
                            is Links.Failure -> {
                                { state.error(link) }
                            }
                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                        }
                    }
                    is LinksState.Ready<*> -> {
                        when (link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link) }
                            }
                            is Links.Failure -> {
                                { state.error(link) }
                            }
                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                        }
                    }
                    is LinksState.Pending -> {
                        when (link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link) }
                            }
                            is Links.Failure -> {
                                {state.error(link)}
                            }
                            is Links.Outgoing.Link -> {
                                { state.open(link.url) }
                            }
                        }
                    }
                    is LinksState.Open -> {
                        when(link) {
                            is Links.Incoming.Result<*> -> {
                                { state.ready(link) }
                            }

                            is Links.Failure -> {
                                { state.error(link) }
                            }

                            is Links.Outgoing.Link -> {
                                state.remain()
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
     * @param data Uri or NSURL
     * @param serializer The needed object's serializer
     * */
    fun <T> handleIncomingLink(data: Any, serializer: KSerializer<T>) {
        println("linksManager is null? ${linksManager == null}")
        linksManager?.handleIncomingLink(data, serializer)
    }

    /**
     * Check if the url is valid and returns [Links.Outgoing.Link] with the given url. State returns [Links.Failure]
     * when the url is invalid.
     * @param url Page to be visited.
     * */
    fun handleOutgoingLink(url: String) {
        debug { "kaluga: handleOutgoingLink" }
        println("linksManager is null? ${linksManager == null}")
        linksManager?.handleOutgoingLink(url)
    }
}
