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

import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * A surface on which the video component of a [PlayableMedia] can be rendered.
 * Exposes a [SurfaceHolder] to bind to
 * @property holder the [SurfaceHolder] to bind to
 */
actual data class MediaSurface(val holder: SurfaceHolder)

/**
 * Binds this [SurfaceView] to [binder], so a [MediaPlayer] using that binder renders its video here.
 * The view's [MediaSurface] is bound while its surface exists and unbound when it is destroyed (and
 * immediately if the surface is already available). Removing the returned [SurfaceHolder.Callback] from
 * the holder (and calling [MediaSurfaceBinder.unbind]) stops the binding.
 * @param binder the [MediaSurfaceBinder] to bind to, typically obtained from [MediaPlayer.surfaceBinder].
 * @return the [SurfaceHolder.Callback] added to this view's holder.
 */
fun SurfaceView.bind(binder: MediaSurfaceBinder): SurfaceHolder.Callback = holder.bind(binder)

/**
 * Binds this [SurfaceHolder] to [binder]. See [SurfaceView.bind].
 * @param binder the [MediaSurfaceBinder] to bind to, typically obtained from [MediaPlayer.surfaceBinder].
 * @return the [SurfaceHolder.Callback] added to this holder.
 */
fun SurfaceHolder.bind(binder: MediaSurfaceBinder): SurfaceHolder.Callback {
    val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            binder.bind(MediaSurface(holder))
        }
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = Unit
        override fun surfaceDestroyed(holder: SurfaceHolder) {
            binder.unbind()
        }
    }
    addCallback(callback)
    if (surface?.isValid == true) {
        binder.bind(MediaSurface(this))
    }
    return callback
}
