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

package com.splendo.kaluga.links

import com.splendo.kaluga.links.manager.PlatformLinksValidator
import com.splendo.kaluga.links.manager.TestConstants.INVALID_URLS
import com.splendo.kaluga.links.manager.TestConstants.VALID_URLS
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IosLinksValidatorTest {

    private val linksValidator = PlatformLinksValidator()

    @Test
    fun testLinksValidatorSucceed() {
        VALID_URLS.forEach {
            assertTrue { linksValidator.isValid(it) }
        }
    }

    @Test
    fun testLinksValidatorFailed() {
        INVALID_URLS.forEach {
            println("Element is $it")
            assertFalse { linksValidator.isValid(it) }
        }
    }
}