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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.media.MockBaseMediaManager
import com.splendo.kaluga.test.media.MockMediaSurfaceController
import com.splendo.kaluga.test.media.MockMediaSurfaceProvider
import com.splendo.kaluga.test.media.MockPlayableMedia
import com.splendo.kaluga.test.media.MockPlaybackState
import com.splendo.kaluga.test.media.MockPlaybackStateRepo
import com.splendo.kaluga.test.media.MockVolumeController
import com.splendo.kaluga.test.media.createMockMediaSurface
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

class MediaPlayerTest {

    @Test
    fun testPlay() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val hasStartedPlaying = CompletableDeferred<Float>()
        mediaManager.playMock.on().doExecute { (rate) -> hasStartedPlaying.complete(rate) }
        val didPlay = async {
            mediaPlayer.play(PlaybackState.PlaybackParameters(1.0f, PlaybackState.LoopMode.NotLooping))
        }
        mediaManager.playMock.verify(rule = never())
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaPlayer.initializeFor(source)
        assertEquals(1.0f, hasStartedPlaying.await())
        mediaManager.handleCompleted()
        didPlay.await()

        val didCleanUp = EmptyCompletableDeferred()
        mediaManager.cleanUpMock.on().doExecute { didCleanUp.complete() }
        mediaPlayer.end()
        didCleanUp.await()
        coroutineContext.cancelChildren()
    }

    @Test
    fun testForcePlay() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val hasStartedInitialPlaying = CompletableDeferred<Float>()
        mediaManager.playMock.on().doExecute { (rate) -> hasStartedInitialPlaying.complete(rate) }
        val didPlay = async {
            mediaPlayer.play(PlaybackState.PlaybackParameters(1.0f, PlaybackState.LoopMode.NotLooping))
        }
        mediaManager.playMock.verify(rule = never())
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaPlayer.initializeFor(source)
        assertEquals(1.0f, hasStartedInitialPlaying.await())
        val secondPlay = CompletableDeferred<Float>()
        mediaManager.playMock.on().doExecute { (rate) -> secondPlay.complete(rate) }
        mediaPlayer.forceStart(PlaybackState.PlaybackParameters(2.0f, PlaybackState.LoopMode.LoopingForFixedNumber(1U)), true)
        assertEquals(2.0f, secondPlay.await())
        mediaManager.startSeekMock.verify(eq(ZERO))
        val finalPlay = CompletableDeferred<Float>()
        mediaManager.playMock.on().doExecute { (rate) -> finalPlay.complete(rate) }
        mediaManager.handleCompleted()
        assertEquals(2.0f, finalPlay.await())
        mediaManager.startSeekMock.verify(eq(ZERO), times = 2)
        assertFalse(didPlay.isCompleted)
        mediaManager.handleCompleted()

        didPlay.await()
        coroutineContext.cancelChildren()
    }

    @Test
    fun testPlayWithException() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val hasStartedPlaying = CompletableDeferred<Float>()
        mediaManager.playMock.on().doExecute { (rate) -> hasStartedPlaying.complete(rate) }
        assertFailsWith<PlaybackError.Unknown> {
            coroutineScope {
                val didPlay = async {
                    mediaPlayer.play(
                        PlaybackState.PlaybackParameters(
                            1.0f,
                            PlaybackState.LoopMode.NotLooping
                        )
                    )
                }
                mediaManager.playMock.verify(rule = never())
                val source = mediaSourceFromUrl("https://example.com")!!
                mediaPlayer.initializeFor(source)
                assertEquals(1.0f, hasStartedPlaying.await())
                mediaManager.handleError(PlaybackError.Unknown)
                didPlay.await()
            }
        }
        coroutineContext.cancelChildren()
    }

    @Test
    fun testInitializeNewSourceWhenInitialized() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val source = mediaSourceFromUrl("https://example.com")!!
        val firstInitialize = CompletableDeferred<PlayableMedia>()
        mediaManager.initializeMock.on().doExecute { (playableMedia) -> firstInitialize.complete(playableMedia) }
        mediaPlayer.initializeFor(source)

        assertEquals(source, firstInitialize.await().source)

        val secondInitialize = CompletableDeferred<PlayableMedia>()
        mediaManager.initializeMock.on().doExecute { (playableMedia) -> secondInitialize.complete(playableMedia) }
        val secondSource = mediaSourceFromUrl("https://example2.com")!!
        mediaPlayer.initializeFor(secondSource)
        assertEquals(secondSource, secondInitialize.await().source)
        mediaManager.handleResetMock.verify()

        coroutineContext.cancelChildren()
    }

    @Test
    fun testInitializeNewSourceWhenIdle() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaPlayer.initializeFor(source)
        mediaPlayer.controls.mapNotNull { it.getControlType<MediaPlayer.Controls.Play>() }.first()

        val secondInitialize = CompletableDeferred<PlayableMedia>()
        mediaManager.initializeMock.on().doExecute { (playableMedia) -> secondInitialize.complete(playableMedia) }
        val secondSource = mediaSourceFromUrl("https://example2.com")!!
        mediaPlayer.initializeFor(secondSource)
        assertEquals(secondSource, secondInitialize.await().source)
        mediaManager.handleResetMock.verify()

        coroutineContext.cancelChildren()
    }

    @Test
    fun testInitializeNewSourceWhenError() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaManager.initializeMock.on().doExecute { mediaManager.handleError(PlaybackError.Unknown) }
        mediaPlayer.initializeFor(source)
        assertEquals(PlaybackError.Unknown, mediaPlayer.controls.mapNotNull { it.getControlType<MediaPlayer.Controls.DisplayError>() }.first().error)

        val secondInitialize = CompletableDeferred<PlayableMedia>()
        mediaManager.initializeMock.on().doExecute { (playableMedia) -> secondInitialize.complete(playableMedia) }
        val secondSource = mediaSourceFromUrl("https://example2.com")!!
        mediaPlayer.initializeFor(secondSource)
        assertEquals(secondSource, secondInitialize.await().source)
        mediaManager.handleResetMock.verify()

        coroutineContext.cancelChildren()
    }

    @Test
    fun testFailToInitializeNewSourceAfterError() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaManager.initializeMock.on().doExecute { mediaManager.handleError(PlaybackError.Unknown) }
        mediaPlayer.initializeFor(source)
        assertEquals(PlaybackError.Unknown, mediaPlayer.controls.mapNotNull { it.getControlType<MediaPlayer.Controls.DisplayError>() }.first().error)

        val secondSource = mediaSourceFromUrl("https://example2.com")!!
        mediaManager.handleCreatePlayableMediaMock.on().doReturn(null)
        assertFailsWith<PlaybackError.MalformedMediaSource> {
            mediaPlayer.initializeFor(secondSource)
        }
        mediaManager.handleResetMock.verify()

        coroutineContext.cancelChildren()
    }

    @Test
    fun testFailToInitializeNewSourceAfterEnded() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()

        val didCleanUp = EmptyCompletableDeferred()
        mediaManager.cleanUpMock.on().doExecute { didCleanUp.complete() }
        mediaPlayer.end()
        didCleanUp.await()

        val source = mediaSourceFromUrl("https://example.com")!!
        assertFailsWith<PlaybackError.PlaybackHasEnded> {
            mediaPlayer.initializeFor(source)
        }

        coroutineContext.cancelChildren()
    }

    @Test
    fun testReinitializeSourceWhenActive() = runBlocking {
        val mediaManagerBuilder = MockBaseMediaManager.Builder()
        val surfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaPlayer = DefaultMediaPlayer(surfaceProvider, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaPlayer.initializeFor(source)
        val isPlaying = CompletableDeferred<Float>()
        mediaManager.playMock.on().doExecute { (rate) -> isPlaying.complete(rate) }
        mediaPlayer.forceStart(PlaybackState.PlaybackParameters())
        assertEquals(1.0f, isPlaying.await())

        val secondInitialize = CompletableDeferred<PlayableMedia>()
        mediaManager.initializeMock.on().doExecute { (playableMedia) -> secondInitialize.complete(playableMedia) }
        mediaPlayer.initializeFor(source)
        assertEquals(source, secondInitialize.await().source)
        mediaManager.handleResetMock.verify()

        coroutineContext.cancelChildren()
    }

    @Test
    fun testVolumeController() = runBlocking {
        val volumeController = MockVolumeController()
        val mediaManagerBuilder = MockBaseMediaManager.Builder(volumeController)
        val mediaPlayer = DefaultMediaPlayer(null, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()

        volumeController.currentVolume.value = 0.5f
        assertEquals(0.5f, mediaPlayer.currentVolume.first())
        mediaPlayer.updateVolume(0.4f)
        assertEquals(0.4f, mediaPlayer.currentVolume.first())

        val didCleanUp = EmptyCompletableDeferred()
        mediaManager.cleanUpMock.on().doExecute { didCleanUp.complete() }
        mediaPlayer.end()
        didCleanUp.await()

        assertFailsWith<PlaybackError.PlaybackHasEnded> { mediaPlayer.updateVolume(1.0f) }
        coroutineContext.cancelChildren()
    }

    @Test
    fun testSurfaceController() = runBlocking {
        val mediaSurfaceController = MockMediaSurfaceController()
        val mediaManagerBuilder = MockBaseMediaManager.Builder(mediaSurfaceController = mediaSurfaceController)
        val mediaPlayer = DefaultMediaPlayer(null, mediaManagerBuilder, coroutineContext)
        val mediaManager = mediaManagerBuilder.builtMediaManagers.first()

        val surface = createMockMediaSurface()
        mediaPlayer.renderVideoOnSurface(surface)
        mediaSurfaceController.renderVideoOnSurfaceMock.verify(eq(surface))

        val didCleanUp = EmptyCompletableDeferred()
        mediaManager.cleanUpMock.on().doExecute { didCleanUp.complete() }
        mediaPlayer.end()
        didCleanUp.await()

        mediaSurfaceController.renderVideoOnSurfaceMock.resetCalls()
        assertFailsWith<PlaybackError.PlaybackHasEnded> {
            mediaPlayer.renderVideoOnSurface(surface)
        }
        mediaSurfaceController.renderVideoOnSurfaceMock.verify(eq(surface), never())
        coroutineContext.cancelChildren()
    }

    @Test
    fun testControlsPlay() = runBlocking {
        val configuration = MockPlaybackState.Configuration()
        val repo = MockPlaybackStateRepo({ MockPlaybackState.Uninitialized(configuration) }, coroutineContext)
        val mediaPlayer = DefaultMediaPlayer({ repo }, coroutineContext)

        assertEquals(emptySet(), mediaPlayer.controls.first().controlTypes)
        val source = mediaSourceFromUrl("https://example.com")!!
        mediaPlayer.initializeFor(source)
        assertEquals(setOf(MediaPlayer.Controls.AwaitPreparation), mediaPlayer.controls.first().controlTypes)
        repo.takeAndChangeState(PlaybackState.Initialized::class) { it.prepared(MockPlayableMedia(source)) }

        val idleControls = mediaPlayer.controls.first()
        assertEquals(2, idleControls.controlTypes.size)
        idleControls.getControlType<MediaPlayer.Controls.Seek>()!!.perform(10.seconds)
        configuration.seekToMock.verify(eq(10.seconds))

        val parameters = PlaybackState.PlaybackParameters(2.0f, PlaybackState.LoopMode.LoopingForever)
        idleControls.getControlType<MediaPlayer.Controls.Play>()!!.perform(parameters)

        val playControls = mediaPlayer.controls.first()
        assertEquals(5, playControls.controlTypes.size)
        assertNotNull(playControls.getControlType<MediaPlayer.Controls.Pause>())
        assertNotNull(playControls.getControlType<MediaPlayer.Controls.Stop>())
        assertEquals(parameters.rate, playControls.getControlType<MediaPlayer.Controls.SetRate>()!!.currentRate)
        assertEquals(parameters.loopMode, playControls.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.currentLoopMode)
        configuration.seekToMock.resetCalls()
        playControls.getControlType<MediaPlayer.Controls.Seek>()!!.perform(20.seconds)
        configuration.seekToMock.verify(eq(20.seconds))

        playControls.getControlType<MediaPlayer.Controls.SetRate>()!!.perform(1.0f)
        val updatedPlayControls = mediaPlayer.controls.first()
        assertEquals(5, updatedPlayControls.controlTypes.size)
        assertEquals(1.0f, updatedPlayControls.getControlType<MediaPlayer.Controls.SetRate>()!!.currentRate)

        updatedPlayControls.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.perform(PlaybackState.LoopMode.NotLooping)
        val updatedPlayControls2 = mediaPlayer.controls.first()
        assertEquals(5, updatedPlayControls2.controlTypes.size)
        assertEquals(PlaybackState.LoopMode.NotLooping, updatedPlayControls2.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.currentLoopMode)

        repo.takeAndChangeState(PlaybackState.Playing::class) { it.completedLoop }

        val completedControls = mediaPlayer.controls.first()
        assertEquals(3, completedControls.controlTypes.size)
        assertNotNull(completedControls.getControlType<MediaPlayer.Controls.Play>())
        configuration.seekToMock.resetCalls()
        completedControls.getControlType<MediaPlayer.Controls.Seek>()!!.perform(30.seconds)
        configuration.seekToMock.verify(eq(30.seconds))
        completedControls.getControlType<MediaPlayer.Controls.Stop>()!!.perform()

        val stopControls = mediaPlayer.controls.first()
        assertEquals(1, stopControls.controlTypes.size)
        val didPlay = async { stopControls.getControlType<MediaPlayer.Controls.Play>()!!.perform(parameters) }

        repo.first { it is PlaybackState.Initialized }
        repo.takeAndChangeState(PlaybackState.Initialized::class) { it.prepared(MockPlayableMedia(source)) }

        didPlay.await()

        val playAfterStopControls = mediaPlayer.controls.first()
        assertEquals(5, playAfterStopControls.controlTypes.size)

        repo.takeAndChangeState(PlaybackState.Active::class) { it.failWithError(PlaybackError.Unknown) }
        val errorControls = mediaPlayer.controls.first()
        assertEquals(1, errorControls.controlTypes.size)
        assertEquals(PlaybackError.Unknown, errorControls.getControlType<MediaPlayer.Controls.DisplayError>()!!.error)

        repo.takeAndChangeState(PlaybackState.Active::class) { it.end }
        val endControls = mediaPlayer.controls.first()
        assertEquals(1, endControls.controlTypes.size)
        assertEquals(PlaybackError.PlaybackHasEnded, endControls.getControlType<MediaPlayer.Controls.DisplayError>()!!.error)

        coroutineContext.cancelChildren()
    }

    @Test
    fun testPauseControls() = runBlocking {
        val parameters = PlaybackState.PlaybackParameters(2.0f, PlaybackState.LoopMode.LoopingForever)
        val source = mediaSourceFromUrl("https://example.com")!!
        val configuration = MockPlaybackState.Configuration()
        val repo = MockPlaybackStateRepo({ MockPlaybackState.Playing(parameters, MockPlayableMedia(source), configuration) }, coroutineContext)
        val mediaPlayer = DefaultMediaPlayer({ repo }, coroutineContext)
        val playControls = mediaPlayer.controls.first()
        assertEquals(5, playControls.controlTypes.size)
        playControls.getControlType<MediaPlayer.Controls.Pause>()!!.perform()

        val pauseControls = mediaPlayer.controls.first()
        assertEquals(6, pauseControls.controlTypes.size)
        assertNotNull(pauseControls.getControlType<MediaPlayer.Controls.Stop>())
        assertEquals(parameters.rate, pauseControls.getControlType<MediaPlayer.Controls.SetRate>()!!.currentRate)
        assertEquals(parameters.loopMode, pauseControls.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.currentLoopMode)
        pauseControls.getControlType<MediaPlayer.Controls.Seek>()!!.perform(10.seconds)
        configuration.seekToMock.verify(eq(10.seconds))
        pauseControls.getControlType<MediaPlayer.Controls.SetRate>()!!.perform(1.0f)

        val updatedPauseControls = mediaPlayer.controls.first()
        assertEquals(6, updatedPauseControls.controlTypes.size)
        assertEquals(1.0f, updatedPauseControls.getControlType<MediaPlayer.Controls.SetRate>()!!.currentRate)

        updatedPauseControls.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.perform(PlaybackState.LoopMode.NotLooping)
        val updatedPauseControls2 = mediaPlayer.controls.first()
        assertEquals(6, updatedPauseControls2.controlTypes.size)
        assertEquals(PlaybackState.LoopMode.NotLooping, updatedPauseControls2.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.currentLoopMode)

        configuration.seekToMock.resetCalls()
        updatedPauseControls2.getControlType<MediaPlayer.Controls.Unpause>()!!.perform()

        val unpausedPlayControls = mediaPlayer.controls.first()
        assertEquals(5, unpausedPlayControls.controlTypes.size)
        assertEquals(1.0f, unpausedPlayControls.getControlType<MediaPlayer.Controls.SetRate>()!!.currentRate)
        assertEquals(PlaybackState.LoopMode.NotLooping, unpausedPlayControls.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.currentLoopMode)
        configuration.seekToMock.verify(rule = never())
        unpausedPlayControls.getControlType<MediaPlayer.Controls.Pause>()!!.perform()

        val pausedAgainControls = mediaPlayer.controls.first()
        assertEquals(6, pausedAgainControls.controlTypes.size)
        pausedAgainControls.getControlType<MediaPlayer.Controls.Play>()!!.perform(parameters)
        configuration.seekToMock.verify(eq(ZERO), times = 2)

        val resetPlayControls = mediaPlayer.controls.first()
        assertEquals(5, resetPlayControls.controlTypes.size)
        assertEquals(parameters.rate, resetPlayControls.getControlType<MediaPlayer.Controls.SetRate>()!!.currentRate)
        assertEquals(parameters.loopMode, resetPlayControls.getControlType<MediaPlayer.Controls.SetLoopMode>()!!.currentLoopMode)

        coroutineContext.cancelChildren()
    }
}
