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

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import com.splendo.kaluga.base.ApplicationHolder
import java.io.FileDescriptor
import java.net.HttpCookie
import java.net.MalformedURLException
import java.net.URL

/**
 * The source at which [PlayableMedia] can be found
 */
actual sealed class MediaSource {

    /**
     * A [MediaSource] that has an associated [AssetFileDescriptor]
     * @property descriptor the [AssetFileDescriptor] associated with the media source
     */
    data class Asset(val descriptor: AssetFileDescriptor) : MediaSource()

    /**
     * A [MediaSource] that has an associated [FileDescriptor]
     * @property descriptor the [FileDescriptor] associated with the media source
     */
    data class File(val descriptor: FileDescriptor) : MediaSource()

    /**
     * A [MediaSource] that is located at a [URL]
     * @property url the [URL] at which the media is located
     */
    data class Url(val url: URL) : MediaSource()

    /**
     * A [MediaSource] that is located at a content [Uri]
     *
     * **Note** that the cross domain redirection is allowed by default, but that can be changed with key/value pairs through the [headers] parameter with "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value to disallow or allow cross domain redirection.
     * @property context the [Context] to use when resolving the [Uri]
     * @property uri the Content [Uri] of the media
     * @property headers the headers to be sent together with the request for the data. The headers must not include cookies
     * @property cookies the list of [HttpCookie] to be sent with the request
     */
    data class Content(
        val context: Context = ApplicationHolder.applicationContext,
        val uri: Uri,
        val headers: Map<String, String>? = null,
        val cookies: List<HttpCookie>? = null,
    ) : MediaSource()
}

/**
 * Attempts to create a [MediaSource] from a url string
 * @param url the url String of the media source
 * @return the [MediaSource] associated with [url] or `null` if none could be created
 */
actual fun mediaSourceFromUrl(url: String): MediaSource? = try {
    MediaSource.Url(URL(url))
} catch (e: MalformedURLException) {
    null
}
