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

package com.splendo.kaluga.media

import com.splendo.kaluga.base.state.ColdStateFlowRepo
import com.splendo.kaluga.base.state.HotStateFlowRepo
import com.splendo.kaluga.base.state.StateRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

/**
 * A [StateRepo]/[MutableStateFlow] of [PlaybackState]
 */
typealias PlaybackStateFlowRepo = StateRepo<PlaybackState, MutableStateFlow<PlaybackState>>

/**
 * An abstract [ColdStateFlowRepo] for managing [PlaybackState]
 * @param createInitialState method for creating the initial [PlaybackState.Uninitialized] given an implementation of this [HotStateFlowRepo]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
abstract class BasePlaybackStateRepo(
    createInitialState: HotStateFlowRepo<PlaybackState>.() -> PlaybackState.Uninitialized,
    coroutineContext: CoroutineContext
) : HotStateFlowRepo<PlaybackState>(
    coroutineContext = coroutineContext,
    initialState = createInitialState,
)

/**
 * A [BasePlaybackStateRepo] managed using a [MediaManager]
 * @param mediaManager the [MediaManager] to manage the [PlaybackState]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
open class PlaybackStateRepo(
    mediaManager: MediaManager,
    coroutineContext: CoroutineContext
) : BasePlaybackStateRepo(
    createInitialState = {
        PlaybackStateImpl.Uninitialized(mediaManager = mediaManager)
    },
    coroutineContext = coroutineContext
)
