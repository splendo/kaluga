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

import kotlinx.browser.window

private fun encodeURIComponent(value: String): String = js("encodeURIComponent(value)")

actual object PlatformActions {
    actual fun openUrl(url: String) {
        window.open(url, "_blank")
    }

    actual fun openMail(recipients: List<String>, subject: String) {
        window.open("mailto:${recipients.joinToString(",")}?subject=${encodeURIComponent(subject)}", "_self")
    }
}
