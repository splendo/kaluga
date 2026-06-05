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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.datetime.timer.RecurringTimer
import com.splendo.kaluga.datetime.timer.Timer
import com.splendo.kaluga.datetime.timer.elapsed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModel : ViewModel() {

    private fun newTimer() = RecurringTimer(1.minutes, coroutineScope = viewModelScope)

    private val timer = MutableStateFlow(newTimer())

    val elapsed: StateFlow<Duration> = timer.flatMapLatest { it.elapsed() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Duration.ZERO)
    val timerState: StateFlow<Timer.State?> = timer.flatMapLatest { it.state }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _timeZone = MutableStateFlow(KalugaTimeZone.current())
    val timeZone: StateFlow<KalugaTimeZone> = _timeZone.asStateFlow()

    val availableTimeZones: List<KalugaTimeZone> = KalugaTimeZone.availableIdentifiers.mapNotNull(KalugaTimeZone::get)

    private val now = MutableStateFlow(DefaultKalugaDate.now())
    val formattedNow: StateFlow<String> = combine(_timeZone, now) { zone, date ->
        KalugaDateFormatter.dateTimeFormat(DateFormatStyle.Long, DateFormatStyle.Long, zone).format(date)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    init {
        viewModelScope.launch {
            while (true) {
                now.value = DefaultKalugaDate.now()
                delay(100)
            }
        }
    }

    /** Start/pause the running timer, or build a fresh one once it has finished. */
    fun toggleTimer() = viewModelScope.launch {
        when (timerState.value) {
            is Timer.State.NotRunning.Paused -> timer.value.start()
            is Timer.State.NotRunning.Finished -> timer.value = newTimer()
            is Timer.State.Running -> timer.value.pause()
            null -> Unit
        }
    }

    fun selectTimeZone(timeZone: KalugaTimeZone) {
        _timeZone.value = timeZone
    }
}
