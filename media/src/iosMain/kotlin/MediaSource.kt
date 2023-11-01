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

import platform.AVFoundation.AVAsset
import platform.AVFoundation.AVAssetReferenceRestrictions
import platform.AVFoundation.AVURLAssetAllowsCellularAccessKey
import platform.AVFoundation.AVURLAssetAllowsConstrainedNetworkAccessKey
import platform.AVFoundation.AVURLAssetAllowsExpensiveNetworkAccessKey
import platform.AVFoundation.AVURLAssetHTTPCookiesKey
import platform.AVFoundation.AVURLAssetHTTPUserAgentKey
import platform.AVFoundation.AVURLAssetPreferPreciseDurationAndTimingKey
import platform.AVFoundation.AVURLAssetPrimarySessionIdentifierKey
import platform.AVFoundation.AVURLAssetReferenceRestrictionsKey
import platform.AVFoundation.AVURLAssetURLRequestAttributionKey
import platform.Foundation.NSHTTPCookie
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequestAttribution
import platform.Foundation.NSUUID

/**
 * The source at which [PlayableMedia] can be found
 */
actual sealed class MediaSource {

    /**
     * A [MediaSource] that has an associated [AVAsset]
     * @property asset the [AVAsset] associated with the media source
     */
    data class Asset(val asset: AVAsset) : MediaSource()

    /**
     * A [MediaSource] that is located at a [NSURL]
     * @property url the [NSURL] at which the media is located
     */
    data class URL(val url: NSURL, val options: List<Options> = emptyList()) : MediaSource() {
        sealed class Options {
            abstract val entry: Pair<String, Any?>

            data class AllowsCellularAccess(val isAllowed: Boolean) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetAllowsCellularAccessKey to isAllowed
            }

            data class AllowsConstrainedNetworkAccess(val isAllowed: Boolean) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetAllowsConstrainedNetworkAccessKey to isAllowed
            }

            data class AllowsExpensiveNetworkAccess(val isAllowed: Boolean) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetAllowsExpensiveNetworkAccessKey to isAllowed
            }

            data class HTTPCookies(val cookies: List<NSHTTPCookie>) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetHTTPCookiesKey to cookies
            }

            data class UserAgent(val userAgent: String) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetHTTPUserAgentKey to userAgent
            }

            data class PreferPreciseDurationAndTiming(val isPreferred: Boolean) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetPreferPreciseDurationAndTimingKey to isPreferred
            }

            data class PrimarySessionIdentifier(val identifier: NSUUID) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetPrimarySessionIdentifierKey to identifier
            }

            data class ReferenceRestrictions(val restrictions: List<AVAssetReferenceRestrictions>) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetReferenceRestrictionsKey to NSNumber(
                    unsignedInteger = restrictions.fold(0UL) { acc, restriction -> acc or restriction },
                )
            }

            data class RequestAttribution(val attribution: NSURLRequestAttribution) : Options() {
                override val entry: Pair<String, Any?> = AVURLAssetURLRequestAttributionKey to attribution
            }
        }
    }
}

/**
 * Attempts to create a [MediaSource] from a url string
 * @param url the url String of the media source
 * @return the [MediaSource] associated with [url] or `null` if none could be created
 */
actual fun mediaSourceFromUrl(
    url: String,
): MediaSource? = NSURL.URLWithString(url)?.let { MediaSource.URL(it, options = listOf(MediaSource.URL.Options.PreferPreciseDurationAndTiming(true))) }
