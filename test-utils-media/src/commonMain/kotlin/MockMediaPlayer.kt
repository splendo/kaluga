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

import com.splendo.kaluga.media.MediaPlayer
import com.splendo.kaluga.media.MediaPlayer.Controls
import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.media.MediaSurfaceController
import com.splendo.kaluga.media.PlayableMedia
import com.splendo.kaluga.media.PlaybackError
import com.splendo.kaluga.media.PlaybackState
import com.splendo.kaluga.media.VolumeController
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Mock implementation of [MediaPlayer]
 * @property playableMedia A [MutableStateFlow] of the [PlayableMedia] for which the player is controlling playback
 * @property controls A [MutableStateFlow] of the [Controls] available for playback
 * @param volumeController a [MockVolumeController] to act as the [VolumeController]
 * @param mediaSurfaceController a [MockMediaSurfaceController] to act as the [MediaSurfaceController]

 */
class MockMediaPlayer(
    override val playableMedia: MutableStateFlow<PlayableMedia?>,
    override val controls: MutableStateFlow<MediaPlayer.Controls>,
    val volumeController: MockVolumeController,
    val mediaSurfaceController: MockMediaSurfaceController,
) : MediaPlayer, VolumeController by volumeController, MediaSurfaceController by mediaSurfaceController {

    /**
     * A [com.splendo.kaluga.test.base.mock.SuspendMethodMock] for [initializeFor]
     */
    val initializeForMock = this::initializeFor.mock()

    /**
     * A [com.splendo.kaluga.test.base.mock.SuspendMethodMock] for [forceStart]
     */
    val forceStartMock = this::forceStart.mock()

    /**
     * A [com.splendo.kaluga.test.base.mock.SuspendMethodMock] for [awaitCompletion]
     */
    val awaitCompletionMock = this::awaitCompletion.mock()

    /**
     * A [com.splendo.kaluga.test.base.mock.SuspendMethodMock] for [reset]
     */
    val resetMock = this::reset.mock()

    /**
     * A [com.splendo.kaluga.test.base.mock.MethodMock] for [close]
     */
    val endMock = this::close.mock()

    init {
        forceStartMock.on().doThrow(PlaybackError.Unknown)
        awaitCompletionMock.on().doThrow(PlaybackError.Unknown)
        resetMock.on().doThrow(PlaybackError.PlaybackHasEnded)
    }

    override suspend fun initializeFor(source: MediaSource): Unit = initializeForMock.call(source)

    override suspend fun forceStart(playbackParameters: PlaybackState.PlaybackParameters, restartIfStarted: Boolean): Unit =
        forceStartMock.call(playbackParameters, restartIfStarted)

    override suspend fun awaitCompletion(): Unit = awaitCompletionMock.call()
    override suspend fun reset(): Unit = resetMock.call()
    override fun close(): Unit = endMock.call()
}
