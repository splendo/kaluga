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

import com.splendo.kaluga.media.MediaSource
import com.splendo.kaluga.media.MediaSurface
import com.splendo.kaluga.media.PlayableMedia
import com.splendo.kaluga.media.Resolution
import com.splendo.kaluga.media.TrackInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration

/**
 * Mock implementation of [PlayableMedia]
 * @property source the [MediaSource] on which the media is found
 * @property duration the [Duration] of the media
 * @property currentPlayTime gets the [Duration] of playtime at the time this property is requested
 * @property resolution a [MutableStateFlow] of the [Resolution] of the media. Note that if no [MediaSurface] has been bound to the media, this will be [Resolution.ZERO]
 * @property tracks a list of [TrackInfo] of the media
 */
class MockPlayableMedia(
    override val source: MediaSource,
    override val duration: Duration,
    override var currentPlayTime: Duration,
    override val resolution: MutableStateFlow<Resolution> = MutableStateFlow(Resolution.ZERO),
    override val tracks: List<TrackInfo> = emptyList()
) : PlayableMedia
