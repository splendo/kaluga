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
import kotlin.time.Duration.Companion.seconds

/**
 * Default implementation of [PlayableMedia] for the JS family, reading live from the player's
 * `HTMLMediaElement` (held in the registry under [id]).
 */
actual class DefaultPlayableMedia internal constructor(actual override val source: MediaSource, private val id: String, actual override val resolution: Flow<Resolution>) :
    PlayableMedia {

    actual override val duration: Duration get() = mediaDuration(id).seconds
    actual override val currentPlayTime: Duration get() = mediaCurrentTime(id).seconds

    // The web doesn't expose a reliable track list cross-browser; a video track is reported when the media
    // has video dimensions, plus an audio track (most media carries audio) so `isVideo` resolves correctly.
    actual override val tracks: List<TrackInfo> get() = buildList {
        val hasVideo = mediaHeight(id) > 0
        if (hasVideo) add(TrackInfo(0, TrackInfo.Type.VIDEO, ""))
        add(TrackInfo(if (hasVideo) 1 else 0, TrackInfo.Type.AUDIO, ""))
    }
}
