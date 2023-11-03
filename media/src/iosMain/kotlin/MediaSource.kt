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

import com.splendo.kaluga.media.MediaSource.URL.Option
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
     * @property options a list of [Option] used to customize the initialization of the asset
     */
    data class URL(val url: NSURL, val options: List<Option> = emptyList()) : MediaSource() {

        /**
         * An option used to customize the initialization of a [URL] asset
         */
        sealed class Option {
            internal abstract val entry: Pair<String, Any?>

            /**
             * An [Option] value that indicates whether the system can make network requests on behalf of the asset when connected to a cellular network.
             * @property isAllowed if `true` the system can make network requests on behalf of the asset when connected to a cellular network
             */
            data class AllowsCellularAccess(val isAllowed: Boolean) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetAllowsCellularAccessKey to isAllowed
            }

            /**
             * An [Option] value that indicates whether the system allows network requests on behalf of this asset to use the constrained interface.
             * @property isAllowed if `true` the system allows network requests on behalf of this asset to use the constrained interface.
             */
            data class AllowsConstrainedNetworkAccess(val isAllowed: Boolean) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetAllowsConstrainedNetworkAccessKey to isAllowed
            }

            /**
             * An [Option] value that indicates whether the system allows network requests on behalf of this asset to use the expensive interface.
             * @property isAllowed if `true` the system allows network requests on behalf of this asset to use the expensive interface.
             */
            data class AllowsExpensiveNetworkAccess(val isAllowed: Boolean) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetAllowsExpensiveNetworkAccessKey to isAllowed
            }

            /**
             * An [Option] that provides the HTTP cookies that a [URL] asset may send with HTTP requests.
             * @property cookies a list of [NSHTTPCookie] that a [URL] asset may send with HTTP requests.
             */
            data class HTTPCookies(val cookies: List<NSHTTPCookie>) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetHTTPCookiesKey to cookies
            }

            /**
             * An [Option] that specifies the user agent of requests that an asset makes.
             * @property userAgent the user agent of requests that an asset makes.
             */
            data class UserAgent(val userAgent: String) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetHTTPUserAgentKey to userAgent
            }

            /**
             * An [Option] value that indicates whether the asset should provide accurate duration and precise random access by time.
             * @property isPreferred if `true` the asset should provide accurate duration and precise random access by time.
             */
            data class PreferPreciseDurationAndTiming(val isPreferred: Boolean) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetPreferPreciseDurationAndTimingKey to isPreferred
            }

            /**
             * An [Option] that specifies a UUID to set as the session identifier for HTTP requests that the asset makes.
             * @property identifier the [NSUUID] to set as the session identifier for HTTP requests that the asset makes.
             */
            data class PrimarySessionIdentifier(val identifier: NSUUID) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetPrimarySessionIdentifierKey to identifier
            }

            /**
             * An [Option] that represents the restrictions used by the asset when resolving references to external media data.
             * @property restrictions the list of [AVAssetReferenceRestrictions] used by the asset when resolving references to external media data.
             */
            data class ReferenceRestrictions(val restrictions: List<AVAssetReferenceRestrictions>) : Option() {
                override val entry: Pair<String, Any?> = AVURLAssetReferenceRestrictionsKey to NSNumber(
                    unsignedInteger = restrictions.fold(0UL) { acc, restriction -> acc or restriction },
                )
            }

            /**
             * A [Option] that specifies the attribution of the URLs that this asset requests.
             * @property attribution the [NSURLRequestAttribution] of the URLs that this asset requests.
             */
            data class RequestAttribution(val attribution: NSURLRequestAttribution) : Option() {
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
): MediaSource? = NSURL.URLWithString(url)?.let { MediaSource.URL(it, options = listOf(MediaSource.URL.Option.PreferPreciseDurationAndTiming(true))) }
