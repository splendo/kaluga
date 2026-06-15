/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.arch

import java.awt.Desktop
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

actual object PlatformActions {
    actual fun openUrl(url: String) {
        browseIfSupported(URI(url))
    }

    actual fun openMail(recipients: List<String>, subject: String) {
        val encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8.name())
        val mailto = "mailto:${recipients.joinToString(",")}?subject=$encodedSubject"
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            Desktop.getDesktop().mail(URI(mailto))
        } else {
            browseIfSupported(URI(mailto))
        }
    }

    private fun browseIfSupported(uri: URI) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(uri)
        }
    }
}
