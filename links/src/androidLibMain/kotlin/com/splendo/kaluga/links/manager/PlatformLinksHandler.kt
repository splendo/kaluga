/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.links.manager

import android.net.UrlQuerySanitizer
import android.webkit.URLUtil
import com.splendo.kaluga.links.models.LinksHandler
import com.splendo.kaluga.links.models.ParametersNameValue

actual class PlatformLinksHandler : LinksHandler {
    override fun isValid(url: String): Boolean = URLUtil.isValidUrl(url)

    override fun extractQuery(url: String): ParametersNameValue {
        val params = UrlQuerySanitizer(url)

        if (params.parameterList == null) {
            return emptyMap()
        }
        return params.parameterList.associate { it.mParameter to it.mValue }
    }
}
