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

package com.splendo.kaluga.example.feature.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.example.arch.PlatformActions

private enum class InfoEntry(val title: String) {
    About("About"),
    Website("Kaluga.io"),
    GitHub("GitHub"),
    Mail("Contact"),
}

@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    var aboutDialogVisible by remember { mutableStateOf(false) }
    val entries = remember { InfoEntry.entries }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(entries, key = { it.name }) { entry ->
            Button(
                onClick = {
                    when (entry) {
                        InfoEntry.About -> aboutDialogVisible = true

                        InfoEntry.Website -> PlatformActions.openUrl("https://kaluga.splendo.com")

                        InfoEntry.GitHub -> PlatformActions.openUrl("https://github.com/splendo/kaluga")

                        InfoEntry.Mail -> PlatformActions.openMail(
                            recipients = listOf("info@splendo.com"),
                            subject = "Question about Kaluga",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(entry.title)
            }
        }
    }

    if (aboutDialogVisible) {
        AlertDialog(
            onDismissRequest = { aboutDialogVisible = false },
            title = { Text("About Us") },
            text = { Text("Kaluga is developed by Splendo Consulting BV") },
            confirmButton = {
                TextButton(onClick = { aboutDialogVisible = false }) {
                    Text("OK")
                }
            },
        )
    }
}
