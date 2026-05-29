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

package com.splendo.kaluga.example.shared.ui

/**
 * Platform actions invoked from CMP screens for behaviour that has no portable Compose equivalent
 * (deep-linking to a browser or a mail client). The in-app review prompt was an iOS/Android-only
 * concept and is intentionally not part of this expect surface — host the review button in
 * mobileshared if you still need it.
 */
expect object PlatformActions {
    fun openUrl(url: String)
    fun openMail(recipients: List<String>, subject: String)
}
