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
 * Handler for processing a link
 */
interface LinksHandler {

    /**
     * Checks if a url string is valid
     * @param url the url string to validate
     * @return `true` if the url is valid, false otherwise
     */
    fun isValid(url: String): Boolean

    /**
     * Processes the query parameters of a url string into a list
     * @param url the url string to extract the query parameters from
     * @return a map containing the all query parameters of the [url]
     */
    fun extractQueryAsMap(url: String): Map<String, List<String>>
}
