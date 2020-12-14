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

import com.splendo.kaluga.state.State

sealed class LinksState : State() {

    fun <T> ready(data: T) =
        Ready(data)

    val error: suspend (message: String) -> Error = { message ->
        Error(message)
    }

    val open: suspend (url: String) -> Open = { url ->
        Open(url)
    }

    val pending: suspend () -> Pending = {
        Pending
    }

    data class Error(
        val message: String
    ) : LinksState()

    data class Ready<T>(
        val data: T
    ) : LinksState()

    data class Open(
        val url: String
    ) : LinksState()

    object Pending : LinksState()
}
