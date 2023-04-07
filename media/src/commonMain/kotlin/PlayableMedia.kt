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

expect class PlayableMedia {
    val source: MediaSource
    val duration: Duration
    val currentPlayTime: Duration
    val resolution: Flow<Resolution>
    val tracks: List<TrackInfo>
}

val PlayableMedia.isVideo: Boolean get() = tracks.any { it.type == TrackInfo.Type.VIDEO }

data class TrackInfo(
    val id: Int,
    val type: Type,
    val language: String
) {
    enum class Type {
        AUDIO,
        METADATA,
        SUBTITLE,
        TIMED_TEXT,
        VIDEO,
        UNKNOWN
    }
}

data class Resolution(val width: Int, val height: Int) {
    companion object {
        val ZERO = Resolution(0, 0)
    }

    val aspectRatio = "$width:$height"
}