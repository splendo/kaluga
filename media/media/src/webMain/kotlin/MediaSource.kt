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

/**
 * The source at which [PlayableMedia] can be found. On the web a media source is always a URL loaded by
 * an `HTMLMediaElement`.
 */
actual sealed class MediaSource {

    /**
     * The URL the media element should load.
     */
    abstract val url: String

    /**
     * A [MediaSource] that is located on the device
     */
    actual sealed class Local : MediaSource()
}

/**
 * A [MediaSource] referenced by an (absolute or relative) URL.
 * @property url the URL of the media
 */
data class URLMediaSource(override val url: String) : MediaSource()

/**
 * A [MediaSource.Local] referencing a bundled/relative file by its URL.
 * @property url the relative URL of the file
 */
data class LocalFileMediaSource(override val url: String) : MediaSource.Local()

actual fun mediaSourceFromUrl(url: String): MediaSource? = URLMediaSource(url)

actual fun mediaSourceFromLocalFile(fileName: String, fileType: String): MediaSource.Local = LocalFileMediaSource("$fileName.$fileType")
