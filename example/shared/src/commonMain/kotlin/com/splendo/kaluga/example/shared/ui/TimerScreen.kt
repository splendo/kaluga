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

package com.splendo.kaluga.example.shared.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.TimeZoneNameStyle
import com.splendo.kaluga.datetime.timer.RecurringTimer
import com.splendo.kaluga.datetime.timer.Timer
import com.splendo.kaluga.datetime.timer.elapsed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun TimerScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var timer by remember { mutableStateOf(RecurringTimer(1.minutes, coroutineScope = scope)) }
    var timeZone by remember { mutableStateOf(KalugaTimeZone.current()) }
    var pickerVisible by remember { mutableStateOf(false) }

    val elapsedSeconds by remember(timer) { timer.elapsed() }
        .collectAsState(initial = Duration.ZERO)
    val timerState by remember(timer) { timer.state }
        .collectAsState(initial = null)

    var now by remember { mutableStateOf(DefaultKalugaDate.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = DefaultKalugaDate.now()
            delay(100)
        }
    }
    val formattedNow = remember(timeZone, now) {
        KalugaDateFormatter
            .dateTimeFormat(DateFormatStyle.Long, DateFormatStyle.Long, timeZone)
            .format(now)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "${elapsedSeconds.inWholeSeconds} s",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                when (timerState) {
                    is Timer.State.NotRunning.Paused -> scope.launch { timer.start() }
                    is Timer.State.NotRunning.Finished ->
                        timer = RecurringTimer(1.minutes, coroutineScope = scope)
                    is Timer.State.Running -> scope.launch { timer.pause() }
                    null -> Unit
                }
            },
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
    }

    if (pickerVisible) {
        TimeZonePickerDialog(
            onPick = { timeZone = it; pickerVisible = false },
            onDismiss = { pickerVisible = false },
        )
    }
}

@Composable
private fun TimeZonePickerDialog(onPick: (KalugaTimeZone) -> Unit, onDismiss: () -> Unit) {
    val zones = remember {
        KalugaTimeZone.availableIdentifiers.mapNotNull(KalugaTimeZone::get)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time Zone") },
        text = {
            LazyColumn {
                items(zones, key = { it.identifier }) { tz ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPick(tz) },
                    ) { Text(tz.displayName(TimeZoneNameStyle.Long)) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
