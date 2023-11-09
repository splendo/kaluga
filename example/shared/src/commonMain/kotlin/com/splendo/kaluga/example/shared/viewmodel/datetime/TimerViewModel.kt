/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.datetime

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.TimeZoneNameStyle
import com.splendo.kaluga.datetime.timer.RecurringTimer
import com.splendo.kaluga.datetime.timer.Timer
import com.splendo.kaluga.datetime.timer.elapsed
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class TimerViewModel(private val alertPresenterBuilder: AlertPresenter.Builder) : BaseLifecycleViewModel(alertPresenterBuilder) {

    private val timer = MutableStateFlow(RecurringTimer(1.minutes, coroutineScope = coroutineScope))
    private val timeZoneActions = KalugaTimeZone.availableIdentifiers.mapNotNull { identifier ->
        KalugaTimeZone.get(identifier)?.let { timeZone ->
            Alert.Action(timeZone.displayName(TimeZoneNameStyle.Long)) {
                currentTimeZone.value = timeZone
            }
        }
    }

    val elapsed = timer.flatMapLatest { timer -> timer.elapsed().map { "${it.inWholeSeconds} s" } }.toInitializedObservable("0 s", coroutineScope)
    val button = timer.flatMapLatest { timer ->
        timer.state.map { state ->
            when (state) {
                is Timer.State.NotRunning.Paused -> KalugaButton.Plain("Start", ButtonStyles.default) { coroutineScope.launch { timer.start() } }
                is Timer.State.NotRunning.Finished -> KalugaButton.Plain("Reset", ButtonStyles.default) {
                    this.timer.value = RecurringTimer(1.minutes, coroutineScope = coroutineScope)
                }
                is Timer.State.Running -> KalugaButton.Plain("Pause", ButtonStyles.default) { coroutineScope.launch { timer.pause() } }
            }
        }
    }.toInitializedObservable<KalugaButton>(KalugaButton.Plain("", ButtonStyles.default) {}, coroutineScope)

    private val currentTimeZone = MutableStateFlow(KalugaTimeZone.current())
    val timeZonePickerButton = currentTimeZone.map { it.timeZonePickerButton }
        .toInitializedObservable(currentTimeZone.value.timeZonePickerButton, coroutineScope)

    private val format = currentTimeZone.map { timeZone ->
        KalugaDateFormatter.dateTimeFormat(DateFormatStyle.Long, DateFormatStyle.Long, timeZone)
    }

    val currentTime = combine(
        format,
        flow {
            while (true) {
                emit(Unit)
                delay(100)
            }
        },
    ) { format, _ ->
        format.format(DefaultKalugaDate.now())
    }.toInitializedObservable("", coroutineScope)

    private fun showTimeZonePicker() {
        coroutineScope.launch {
            alertPresenterBuilder.buildActionSheet(this) {
                setTitle("Select Time Zone")
                addActions(timeZoneActions)
            }.show()
        }
    }

    private val KalugaTimeZone.timeZonePickerButton: KalugaButton get() = KalugaButton.Plain(displayName(TimeZoneNameStyle.Long), ButtonStyles.default) {
        showTimeZonePicker()
    }
}
