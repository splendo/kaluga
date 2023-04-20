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
import com.splendo.kaluga.test.base.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verification.VerificationRule
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.test.media.MockBaseMediaManager
import com.splendo.kaluga.test.media.MockMediaSurfaceController
import com.splendo.kaluga.test.media.MockMediaSurfaceProvider
import com.splendo.kaluga.test.media.MockPlayableMedia
import com.splendo.kaluga.test.media.createMockMediaSurface
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class BaseMediaManagerTest {

    @Test
    fun testEvents() = runBlocking {
        val mediaManager = MockBaseMediaManager(null, coroutineContext = coroutineContext)
        val mockSource = mediaSourceFromUrl("https://example.com")!!
        val mockPlayableMedia = MockPlayableMedia(mockSource)
        mediaManager.handlePrepared(mockPlayableMedia)
        assertEquals(MediaManager.Event.DidPrepare(mockPlayableMedia), mediaManager.events.first())
        mediaManager.handleCompleted()
        assertEquals(MediaManager.Event.DidComplete, mediaManager.events.first())
        mediaManager.handleError(PlaybackError.Unknown)
        assertEquals(MediaManager.Event.DidFailWithError(PlaybackError.Unknown), mediaManager.events.first())
        mediaManager.close()
        assertEquals(MediaManager.Event.DidEnd, mediaManager.events.first())
        coroutineContext.cancelChildren()
    }

    @Test
    fun testCreatePlayableMedia() = runBlocking {
        val mediaSurfaceProvider = MockMediaSurfaceProvider(MutableSharedFlow(1))
        val mediaSurfaceController = MockMediaSurfaceController()
        val surface = createMockMediaSurface()
        val mediaManager = MockBaseMediaManager(mediaSurfaceProvider, mediaSurfaceController = mediaSurfaceController, coroutineContext = coroutineContext)
        mediaSurfaceProvider.surface.tryEmit(surface)
        yieldMultiple(4)
        mediaSurfaceController.renderVideoOnSurfaceMock.verify(rule = VerificationRule.never())
        val mockSource = mediaSourceFromUrl("https://example.com")!!
        val mockPlayableMedia = MockPlayableMedia(mockSource)
        mediaManager.handleCreatePlayableMediaMock.on(eq(mockSource)).doReturn(mockPlayableMedia)
        assertEquals(mockPlayableMedia, mediaManager.createPlayableMedia(mockSource))
        yieldMultiple(4)
        mediaSurfaceController.renderVideoOnSurfaceMock.verify(eq(surface))
        mediaSurfaceController.renderVideoOnSurfaceMock.resetCalls()

        val otherMockSource = mediaSourceFromUrl("https://example.com")!!
        val otherMockPlayableMedia = MockPlayableMedia(otherMockSource)
        mediaManager.handleCreatePlayableMediaMock.on(eq(otherMockSource)).doReturn(otherMockPlayableMedia)
        mediaManager.createPlayableMedia(otherMockSource)
        yieldMultiple(4)
        val captor = AnyOrNullCaptor<MediaSurface>()
        mediaSurfaceController.renderVideoOnSurfaceMock.verify(captor, times = 2)
        assertNull(captor.captured[0])
        assertEquals(surface, captor.captured[1])
        mediaSurfaceController.renderVideoOnSurfaceMock.resetCalls()
        coroutineContext.cancelChildren()
        yieldMultiple(4)
        mediaSurfaceController.renderVideoOnSurfaceMock.verify(eq(null))
    }

    @Test
    fun testSeekTo() = runBlocking {
        val mediaManager = MockBaseMediaManager(null, coroutineContext = coroutineContext)
        mediaManager.startSeekMock.on().doReturn(Unit)
        val seek1 = async { mediaManager.seekTo(100.seconds) }
        val seek2 = async { mediaManager.seekTo(10.seconds) }
        val seek3 = async { mediaManager.seekTo(50.seconds) }
        val seek4 = async { mediaManager.seekTo(50.seconds) }

        assertFalse(seek1.isCompleted)
        assertFalse(seek2.await())
        assertFalse(seek3.isCompleted)
        assertFalse(seek4.isCompleted)

        mediaManager.handleSeekCompleted(true)
        assertTrue(seek1.await())
        assertFalse(seek3.isCompleted)
        assertFalse(seek4.isCompleted)

        mediaManager.handleSeekCompleted(true)
        assertTrue(seek3.await())
        assertTrue(seek4.await())

        coroutineContext.cancelChildren()
    }
}
