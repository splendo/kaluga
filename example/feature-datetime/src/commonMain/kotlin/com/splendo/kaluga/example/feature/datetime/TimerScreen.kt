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

package com.splendo.kaluga.example.feature.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.datetime.KalugaTimeZone
import com.splendo.kaluga.datetime.TimeZoneNameStyle
import com.splendo.kaluga.datetime.timer.Timer
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration

@Composable
fun TimerScreen(modifier: Modifier = Modifier, viewModel: TimerViewModel = koinViewModel()) {
    val elapsedSeconds by viewModel.elapsed.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val timeZone by viewModel.timeZone.collectAsState()
    val formattedNow by viewModel.formattedNow.collectAsState()
    var pickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "${elapsedSeconds.inWholeSeconds} s",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = viewModel::toggleTimer,
        ) {
            Text(
                when (timerState) {
                    is Timer.State.NotRunning.Paused -> "Start"
                    is Timer.State.NotRunning.Finished -> "Reset"
                    is Timer.State.Running -> "Pause"
                    null -> ""
                },
            )
        }
        OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { pickerVisible = true }) {
            Text(timeZone.displayName(TimeZoneNameStyle.Long))
        }
        Text(formattedNow, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        TimeZoneDetails(timeZone)
    }

    if (pickerVisible) {
        TimeZonePickerDialog(
            zones = viewModel.availableTimeZones,
            onPick = {
                viewModel.selectTimeZone(it)
                pickerVisible = false
            },
            onDismiss = { pickerVisible = false },
        )
    }
}

@Composable
private fun TimeZoneDetails(timeZone: KalugaTimeZone) {
    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    Text("Time Zone", style = MaterialTheme.typography.titleMedium)
    Field("Identifier", timeZone.identifier)
    Field("Offset from GMT", formatOffset(timeZone.offsetFromGMT))
    Field("Uses DST (now)", if (timeZone.usesDaylightSavingsTime()) "Yes" else "No")
    Field("DST offset", formatOffset(timeZone.daylightSavingsOffset))
    Field("Name (long)", timeZone.displayName(TimeZoneNameStyle.Long, withDaylightSavings = false))
    Field("Name (short)", timeZone.displayName(TimeZoneNameStyle.Short, withDaylightSavings = false))
    Field("Name (long, DST)", timeZone.displayName(TimeZoneNameStyle.Long, withDaylightSavings = true))
    Field("Name (short, DST)", timeZone.displayName(TimeZoneNameStyle.Short, withDaylightSavings = true))
}

@Composable
private fun Field(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun formatOffset(duration: Duration): String = duration.toComponents { hours, minutes, _, _ ->
    val sign = if (hours < 0 || minutes < 0) "-" else "+"
    val h = if (hours < 0) -hours else hours
    val m = if (minutes < 0) -minutes else minutes
    "${sign}${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}"
}

@Composable
private fun TimeZonePickerDialog(zones: List<KalugaTimeZone>, onPick: (KalugaTimeZone) -> Unit, onDismiss: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, zones) {
        if (query.isBlank()) {
            zones
        } else {
            val q = query.trim().lowercase()
            zones.filter { tz ->
                tz.identifier.lowercase().contains(q) ||
                    tz.displayName(TimeZoneNameStyle.Long).lowercase().contains(q) ||
                    tz.displayName(TimeZoneNameStyle.Short).lowercase().contains(q)
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time Zone") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Search identifier or name") },
                )
                LazyColumn {
                    items(filtered, key = { it.identifier }) { tz ->
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onPick(tz) },
                        ) { Text("${tz.identifier} — ${tz.displayName(TimeZoneNameStyle.Long)}") }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
