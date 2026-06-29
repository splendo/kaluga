/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.links

import androidx.lifecycle.ViewModel
import com.splendo.kaluga.example.arch.PlatformActions
import com.splendo.kaluga.links.DefaultLinksManager
import com.splendo.kaluga.links.LinksManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val DEMO_URL = "https://kaluga-links.web.app"

class LinksViewModel(private val linksManager: LinksManager = DefaultLinksManager.Builder().create()) : ViewModel() {

    data class IncomingAlert(val title: String, val message: String)

    private val _alert = MutableStateFlow<IncomingAlert?>(null)
    val alert: StateFlow<IncomingAlert?> = _alert.asStateFlow()

    fun handleIncomingLink(url: String?) {
        val incoming = url ?: return
        val repository = linksManager.handleIncomingLink(incoming, Repository.serializer())
        _alert.value = if (repository != null) {
            IncomingAlert(title = "Alert", message = repository.toString())
        } else {
            IncomingAlert(title = "Error Alert", message = "Query is invalid or empty.")
        }
    }

    fun dismissAlert() {
        _alert.value = null
    }

    fun openDemoUrl() {
        linksManager.validateLink(DEMO_URL)?.let(PlatformActions::openUrl)
    }
}
