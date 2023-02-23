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

package com.splendo.kaluga.links

import kotlinx.serialization.KSerializer
import platform.Foundation.NSURL

/**
 * Convert an incoming [NSURL] query into [T] and return it.
 * When the given url is invalid, it returns null.
 *
 * @param T the type of data to parse the url into
 * @param url the [NSURL] containing the query with values.
 * @param serializer serializer of type [T].
 * @return the [T] described by the [url] or `null` if the object was not described by the [NSURL]
 **/
fun <T> LinksManager.handleIncomingLink(url: NSURL, serializer: KSerializer<T>): T? = handleIncomingLink(url.absoluteString.orEmpty(), serializer)

/**
 * Check a [NSURL] and returns it if valid, otherwise it returns null.
 * @param url [NSURL] to validate.
 * @return the [url] if valid, `null` otherwise
 **/
fun LinksManager.validateLink(url: NSURL): NSURL? = validateLink(url.absoluteString.orEmpty())?.let { url }
