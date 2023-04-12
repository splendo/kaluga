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

package com.splendo.kaluga.test.media

import com.splendo.kaluga.media.BasePlaybackStateRepo
import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.media.PlayableMedia
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.test.base.mock.SuspendMethodMock
import kotlin.coroutines.CoroutineContext

/**
 * Mock implementation of [BasePlaybackStateRepo]
 * This will only use [MockPlaybackState]
 * @param mediaProvider the [PlayableMediaProvider] to convert a [MediaSource] to a [PlayableMedia]
 * @param seekToMock a [SuspendMethodMock] to be called when [PlaybackState.Prepared.seekTo] is called
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
class MockPlaybackStateRepo(
    mediaProvider: PlayableMediaProvider,
    val seekToMock: SeekToMock = MockPlaybackState.createSeekToMock(),
    coroutineContext: CoroutineContext
) : BasePlaybackStateRepo(
    { MockPlaybackState.Uninitialized(mediaProvider, seekToMock) },
    coroutineContext
)
