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
 * The source at which [PlayableMedia] can be found
 */
expect sealed class MediaSource {
    /**
     * A [MediaSource] that is located on the device
     */
    sealed class Local : MediaSource
}

/**
 * Attempts to create a [MediaSource] from a url string
 * @param url the url String of the media source
 * @return the [MediaSource] associated with [url] or `null` if none could be created
 */
expect fun mediaSourceFromUrl(url: String): MediaSource?

/**
 * Attempts to create a [MediaSource] from a file name
 * @param fileName the name of the media source file
 * @param fileType the type of the media source file
 * @return the [MediaSource.Local] associated with the file or `null` if none could be created
 */
expect fun mediaSourceFromLocalFile(fileName: String, fileType: String): MediaSource.Local
