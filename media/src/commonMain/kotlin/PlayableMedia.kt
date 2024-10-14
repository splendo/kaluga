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

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

/**
 * A media that can be played by a [MediaPlayer]
 * @property source the [MediaSource] on which the media is found
 * @property duration the [Duration] of the media
 * @property currentPlayTime gets the [Duration] of playtime at the time this property is requested
 * @property resolution a [Flow] of the [Resolution] of the media. Note that if no [MediaSurface] has been bound to the media, this will be [Resolution.ZERO]
 * @property tracks a list of [TrackInfo] of the media
 */
interface PlayableMedia {
    val source: MediaSource
    val duration: Duration
    val currentPlayTime: Duration
    val resolution: Flow<Resolution>
    val tracks: List<TrackInfo>
}

/**
 * Default implementation of [PlayableMedia]
 */
expect class DefaultPlayableMedia : PlayableMedia {
    override val source: MediaSource
    override val duration: Duration
    override val currentPlayTime: Duration
    override val resolution: Flow<Resolution>
    override val tracks: List<TrackInfo>
}

/**
 * If `true` this [PlayableMedia] has a video component
 */
val PlayableMedia.isVideo: Boolean get() = tracks.any { it.type == TrackInfo.Type.VIDEO }

/**
 * Info of the track of a [PlayableMedia]
 * @property id identifier of the track
 * @property type the [TrackInfo.Type] of the track
 * @property language the language code of the track
 */
data class TrackInfo(val id: Int, val type: Type, val language: String) {
    /**
     * The type of the track
     */
    enum class Type {
        AUDIO,
        METADATA,
        SUBTITLE,
        TIMED_TEXT,
        VIDEO,
        UNKNOWN,
    }
}

/**
 * The screen resolution of a video
 * @property width the width in pixels of the video
 * @property height the height in pixels of the video
 */
data class Resolution(val width: Int, val height: Int) {
    companion object {

        /**
         * A [Resolution] of 0 by 0 pixels
         */
        val ZERO = Resolution(0, 0)
    }

    /**
     * The aspect ratio
     */
    val aspectRatio = "$width:$height"
}
