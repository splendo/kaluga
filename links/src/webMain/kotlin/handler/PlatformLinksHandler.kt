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

package com.splendo.kaluga.links.handler

/**
 * Implementation of [LinksHandler] for the JS family, backed by the WHATWG
 * [`URL`](https://developer.mozilla.org/en-US/docs/Web/API/URL) parser.
 */
actual class PlatformLinksHandler actual constructor() : LinksHandler {

    actual override fun isValid(url: String): Boolean = isHttpUrl(url)

    actual override fun extractQueryAsMap(url: String): Map<String, List<String>> {
        val query = urlQuery(url).removePrefix("?")
        if (query.isEmpty()) return emptyMap()
        val result = linkedMapOf<String, MutableList<String>>()
        for (pair in query.split("&")) {
            if (pair.isEmpty()) continue
            val separator = pair.indexOf('=')
            val key = decodeQueryComponent(if (separator < 0) pair else pair.substring(0, separator))
            val value = if (separator < 0) "" else decodeQueryComponent(pair.substring(separator + 1))
            result.getOrPut(key) { mutableListOf() }.add(value)
        }
        return result
    }
}

// Valid only for the http(s) schemes, matching the other platforms (which reject e.g. "httpss://" or schemeless urls).
private fun isHttpUrl(url: String): Boolean =
    js("(function () { try { var u = new URL(url); return u.protocol === 'http:' || u.protocol === 'https:'; } catch (e) { return false; } })()")

// The url's search string (leading "?" included), or "" when the url is invalid or has no query.
private fun urlQuery(url: String): String = js("(function () { try { return new URL(url).search; } catch (e) { return ''; } })()")

private fun decodeQueryComponent(value: String): String = js("(function () { try { return decodeURIComponent(value); } catch (e) { return value; } })()")
