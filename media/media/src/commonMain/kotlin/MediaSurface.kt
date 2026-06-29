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

import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.defaultLogger
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A surface on which the video component of a [PlayableMedia] can be rendered
 */
expect class MediaSurface

/**
 * A bindable handle for a [MediaSurface]. A view binds the surface it hosts via [bind] (and detaches it
 * via [unbind]); the [MediaManager] — obtained from a [MediaPlayer.surfaceBinder] — observes the bound
 * surface and renders video onto it. The view drives binding directly, so no host lifecycle awareness is
 * involved.
 * @param logger the [Logger] used to warn when [bind] replaces an already-bound surface. Defaults to [defaultLogger].
 */
class MediaSurfaceBinder(private val logger: Logger = defaultLogger) {

    private val mutableSurface = MutableStateFlow<MediaSurface?>(null)

    /**
     * A [StateFlow] of the currently bound [MediaSurface], observed by the [MediaManager].
     */
    internal val surface: StateFlow<MediaSurface?> = mutableSurface.asStateFlow()

    /**
     * Binds [surface] so the video component renders onto it. Only one surface can be bound at a time;
     * binding while another surface is still bound replaces it and logs a warning, as it usually means a
     * second view bound to the same binder without the first one unbinding.
     */
    fun bind(surface: MediaSurface) {
        if (mutableSurface.value != null) {
            logger.warn(TAG) { "A MediaSurface is already bound; replacing it. Unbind the previous surface before binding a new one." }
        }
        mutableSurface.value = surface
    }

    /**
     * Detaches the currently bound [MediaSurface], if any.
     */
    fun unbind() {
        mutableSurface.value = null
    }

    private companion object {
        const val TAG = "MediaSurfaceBinder"
    }
}
