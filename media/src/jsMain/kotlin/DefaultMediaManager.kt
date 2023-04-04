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

import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

actual class PlayableMedia(actual val url: String) {
    actual val duration: Duration get() = Duration.ZERO
    actual val currentPlayTime: Duration get() = Duration.ZERO
}

actual class DefaultMediaManager(coroutineContext: CoroutineContext) : BaseMediaManager(coroutineContext) {

    class Builder : BaseMediaManager.Builder {
        override fun create(coroutineContext: CoroutineContext): BaseMediaManager = DefaultMediaManager(coroutineContext)
    }

    override var volume: Float = 0.0f

    override fun createPlayableMedia(url: String): PlayableMedia? = PlayableMedia(url)

    override fun initialize(playableMedia: PlayableMedia) {
        handlePrepared(playableMedia)
    }

    override fun play(rate: Float) {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun startSeek(duration: Duration) {
        TODO("Not yet implemented")
    }

    override fun cleanUp() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}
