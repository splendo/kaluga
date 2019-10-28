package com.splendo.kaluga.loadingIndicator

import platform.UIKit.UIViewController
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

class IosLoadingIndicatorTests {

    @Test
    fun builderMissingViewException() {

        assertFailsWith<IllegalArgumentException> {
            IOSLoadingIndicator
                .Builder()
                .create()
        }
    }

    @Test
    fun builderInitializer() {
        val view = UIViewController()
        val indicator = IOSLoadingIndicator
            .Builder()
            .setView(view)
            .create()
        assertNotNull(indicator)
    }
}

