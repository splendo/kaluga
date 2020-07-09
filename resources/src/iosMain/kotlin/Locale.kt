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

package com.splendo.kaluga.resources

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual typealias Locale = NSLocale

actual fun createLocale(language: String): Locale = NSLocale(language)
actual fun createLocale(language: String, country: String): Locale = NSLocale("{${language}_$country}")
actual fun createLocale(language: String, country: String, variant: String): Locale = NSLocale("{${language}_$country.$variant}")

actual val defaultLocale: Locale get() = NSLocale.currentLocale
