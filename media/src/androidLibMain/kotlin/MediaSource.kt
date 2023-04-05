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

actual sealed class MediaSource {
    data class Asset(val descriptor: AssetFileDescriptor) : MediaSource()
    data class File(val descriptor: FileDescriptor) : MediaSource()
    data class Url(val url: URL) : MediaSource()
    data class Content(val context: Context = ApplicationHolder.applicationContext, val uri: Uri, val headers: Map<String, String>? = null, val cookies: List<HttpCookie>? = null) : MediaSource()
}

actual fun mediaSourceFromUrl(url: String): MediaSource? = try {
    MediaSource.Url(URL(url))
} catch (e: MalformedURLException) {
    null
}
