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

import com.splendo.kaluga.test.base.BaseFlowTest
import com.splendo.kaluga.test.base.mock.matcher.AnyCaptor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.media.MockMediaManager
import com.splendo.kaluga.test.media.MockMediaSurfaceController
import com.splendo.kaluga.test.media.MockVolumeController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.time.Duration

class PlaybackStateTest : BaseFlowTest<Unit, PlaybackStateTest.Context, PlaybackState, PlaybackStateRepo>() {

    companion object {
        val source = mediaSourceFromUrl("https://www.example.com")!!
        val playbackParameters = PlaybackState.PlaybackParameters(1.0f, PlaybackState.LoopMode.NotLooping)
    }

    class Context(coroutineScope: CoroutineScope) : TestContext {

        val events = MutableSharedFlow<MediaManager.Event>(1)
        val volumeController = MockVolumeController()
        val mediaSurfaceController = MockMediaSurfaceController()

        val mediaManager = MockMediaManager(events, volumeController, mediaSurfaceController)
        val playbackStateRepo = PlaybackStateRepo(mediaManager, coroutineScope.coroutineContext)
    }

    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> Context = { _, scope ->
        Context(scope)
    }
    override val flowFromTestContext: suspend Context.() -> PlaybackStateRepo = { playbackStateRepo }

    @Test
    fun testPlayback() = testWithFlowAndTestContext(Unit) {
        startPlaying(playbackParameters)
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(source, it.playableMedia.source)
            assertEquals(playbackParameters, it.playbackParameters)
            mediaManager.playMock.verify(eq(playbackParameters.rate))
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidComplete)
        }
        test {
            assertIs<PlaybackState.Completed>(it)
            assertEquals(source, it.playableMedia.source)
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Completed::class) {
                it.stop
            }
        }
        test {
            assertIs<PlaybackState.Stopped>(it)
            mediaManager.stopMock.verify()
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Stopped::class) {
                it.reinitialize
            }
        }
        test {
            assertIs<PlaybackState.Initialized>(it)
            mediaManager.initializeMock.verify(times = 2)
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Active::class) {
                it.reset
            }
        }
        test {
            assertIs<PlaybackState.Uninitialized>(it)
            mediaManager.resetMock.verify()
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidEnd)
        }
        test {
            assertIs<PlaybackState.Ended>(it)
        }
    }

    @Test
    fun testPause() = testWithFlowAndTestContext(Unit) {
        startPlaying(playbackParameters)
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(source, it.playableMedia.source)
            assertEquals(playbackParameters, it.playbackParameters)
            mediaManager.playMock.verify(eq(playbackParameters.rate))
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Playing::class) {
                it.pause
            }
        }
        test {
            assertIs<PlaybackState.Paused>(it)
            assertEquals(source, it.playableMedia.source)
            mediaManager.pauseMock.verify()
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Paused::class) {
                it.play
            }
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(source, it.playableMedia.source)
            assertEquals(playbackParameters, it.playbackParameters)
            mediaManager.playMock.verify(eq(playbackParameters.rate), times = 2)
        }
    }

    @Test
    fun testUpdatePlaybackRate() = testWithFlowAndTestContext(Unit) {
        startPlaying(playbackParameters)
        test {
            assertIs<PlaybackState.Playing>(it)
            mediaManager.playMock.verify(eq(playbackParameters.rate))
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Playing::class) {
                it.updatePlaybackParameters(playbackParameters.copy(rate = 2.0f))
            }
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(playbackParameters.copy(rate = 2.0f), it.playbackParameters)
            mediaManager.playMock.verify(eq(2.0f))
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Playing::class) {
                it.pause
            }
        }
        test {
            assertIs<PlaybackState.Paused>(it)
            assertEquals(playbackParameters.copy(rate = 2.0f), it.playbackParameters)
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Paused::class) {
                it.updatePlaybackParameters(playbackParameters.copy(rate = 4.0f))
            }
        }
        test {
            assertIs<PlaybackState.Paused>(it)
            assertEquals(playbackParameters.copy(rate = 4.0f), it.playbackParameters)
            mediaManager.playMock.verify(eq(4.0f), rule = never())
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Paused::class) {
                it.play
            }
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(playbackParameters.copy(rate = 4.0f), it.playbackParameters)
            mediaManager.playMock.verify(eq(4.0f))
        }
    }

    @Test
    fun testLoopMode() = testWithFlowAndTestContext(Unit) {
        startPlaying(playbackParameters.copy(loopMode = PlaybackState.LoopMode.LoopingForever))
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(PlaybackState.LoopMode.LoopingForever, it.playbackParameters.loopMode)
            mediaManager.playMock.verify()
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidComplete)
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(PlaybackState.LoopMode.LoopingForever, it.playbackParameters.loopMode)
            mediaManager.playMock.verify(times = 2)
            mediaManager.seekToMock.verify(eq(Duration.ZERO))
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Playing::class) {
                it.updatePlaybackParameters(playbackParameters.copy(loopMode = PlaybackState.LoopMode.NotLooping))
            }
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(PlaybackState.LoopMode.NotLooping, it.playbackParameters.loopMode)
            mediaManager.playMock.verify(times = 3)
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidComplete)
        }
        test {
            assertIs<PlaybackState.Completed>(it)
            mediaManager.playMock.resetCalls()
            mediaManager.seekToMock.resetCalls()
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Completed::class) {
                it.start(playbackParameters.copy(loopMode = PlaybackState.LoopMode.LoopingForFixedNumber(1U)))
            }
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(PlaybackState.LoopMode.LoopingForFixedNumber(1U), it.playbackParameters.loopMode)
            mediaManager.playMock.verify()
            mediaManager.seekToMock.verify(eq(Duration.ZERO))
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidComplete)
        }
        test {
            assertIs<PlaybackState.Playing>(it)
            assertEquals(PlaybackState.LoopMode.LoopingForFixedNumber(0U), it.playbackParameters.loopMode)
            mediaManager.playMock.verify(times = 2)
            mediaManager.seekToMock.verify(eq(Duration.ZERO), times = 2)
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidComplete)
        }
        test {
            assertIs<PlaybackState.Completed>(it)
        }
    }

    @Test
    fun testInvalidSource() = testWithFlowAndTestContext(Unit) {
        test {
            assertIs<PlaybackState.Uninitialized>(it)
        }
        mainAction {
            mediaManager.createPlayableMediaMock.on().doReturn(null)
            playbackStateRepo.takeAndChangeState(PlaybackState.Uninitialized::class) {
                it.initialize(source)
            }
        }
        test {
            assertIs<PlaybackState.Error>(it)
            assertEquals(PlaybackError.MalformedMediaSource, it.error)
        }
    }

    @Test
    fun testError() = testWithFlowAndTestContext(Unit) {
        test {
            assertIs<PlaybackState.Uninitialized>(it)
        }
        mainAction {
            events.tryEmit(MediaManager.Event.DidFailWithError(PlaybackError.Unknown))
        }
        test {
            assertIs<PlaybackState.Error>(it)
            assertEquals(PlaybackError.Unknown, it.error)
        }
    }

    private suspend fun startPlaying(playbackParameters: PlaybackState.PlaybackParameters) {
        test {
            assertIs<PlaybackState.Uninitialized>(it)
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Uninitialized::class) {
                it.initialize(source)
            }
        }
        test {
            assertIs<PlaybackState.Initialized>(it)
        }
        mainAction {
            val captor = AnyCaptor<PlayableMedia>()
            mediaManager.initializeMock.verify(captor)
            val playableMedia = captor.lastCaptured
            assertNotNull(playableMedia)
            assertEquals(source, playableMedia.source)

            events.tryEmit(MediaManager.Event.DidPrepare(playableMedia))
        }
        test {
            assertIs<PlaybackState.Idle>(it)
            assertEquals(source, it.playableMedia.source)
        }
        mainAction {
            playbackStateRepo.takeAndChangeState(PlaybackState.Idle::class) {
                it.play(playbackParameters)
            }
        }
    }
}
