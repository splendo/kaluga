/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.media.compose

import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.splendo.kaluga.media.MediaSurfaceBinder
import com.splendo.kaluga.media.bind

@Composable
actual fun MediaSurfaceContainer(binder: MediaSurfaceBinder, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> SurfaceView(context).apply { bind(binder) } },
    )
    DisposableEffect(binder) {
        onDispose { binder.unbind() }
    }
}
