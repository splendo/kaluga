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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.example.arch.PlatformActions
import com.splendo.kaluga.links.DefaultLinksManager
import com.splendo.kaluga.links.LinksManager

private const val DEMO_URL = "https://kaluga-links.web.app"

private data class IncomingAlert(val title: String, val message: String)

@Composable
fun LinksScreen(incomingUrl: String? = null, modifier: Modifier = Modifier) {
    val linksManager: LinksManager = remember { DefaultLinksManager.Builder().create() }
    var alert by remember { mutableStateOf<IncomingAlert?>(null) }

    LaunchedEffect(incomingUrl) {
        val url = incomingUrl ?: return@LaunchedEffect
        val repository = linksManager.handleIncomingLink(url, Repository.serializer())
        alert = if (repository != null) {
            IncomingAlert(title = "Alert", message = repository.toString())
        } else {
            IncomingAlert(title = "Error Alert", message = "Query is invalid or empty.")
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Open a Kaluga demo URL in the default browser.")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val validated = linksManager.validateLink(DEMO_URL)
                if (validated != null) {
                    PlatformActions.openUrl(validated)
                }
            },
        ) {
            Text("Open Browser")
        }
    }

    alert?.let { current ->
        AlertDialog(
            onDismissRequest = { alert = null },
            title = { Text(current.title) },
            text = { Text(current.message) },
            confirmButton = {
                TextButton(onClick = { alert = null }) { Text("OK") }
            },
        )
    }
}
