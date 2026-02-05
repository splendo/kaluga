/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

/**
 *  Plays a sound from [MediaSource]
 *  Meant for short low latency audio
 */
interface SoundPlayer : AutoCloseable {
    /** Plays the sound */
    fun play()
}

/**
 * A default implementation of [SoundPlayer]
 * @param source the [MediaSource.Local] on which the media is found
*/
expect class DefaultSoundPlayer(source: MediaSource.Local) : SoundPlayer {
    /** Closes the sound player */
    override fun close()

    /** Plays the sound */
    override fun play()
}

/**
 * A [SoundPlayer] specific error
 * @param message s descriptive error message
 */
sealed class MediaSoundError(message: String) : Error(message) {
    data object UnexpectedMediaSourceShouldBeLocal : MediaSoundError("Unexpected media source type. Should be Local.")
    class CannotAccessMediaFile(detailedDescription: String? = null) : MediaSoundError("Cannot access media file. $detailedDescription")
    class CannotStartAudioEngine(detailedDescription: String? = null) : MediaSoundError("Cannot start audio engine. $detailedDescription")
    class CannotSetAudioSessionConfiguration(detailedDescription: String? = null) : MediaSoundError("Failed to set the audio session configuration. $detailedDescription")
}
