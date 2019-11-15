package com.splendo.kaluga.loadingIndicator

import platform.UIKit.*
import kotlin.test.*

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
    fun builderInitializer() {
        assertNotNull(IOSLoadingIndicator
            .Builder(UIViewController())
            .create()
        )
    }

    @Test
    fun builderSetStyle() {
        assertNotNull(IOSLoadingIndicator
            .Builder(UIViewController()).build {
                setStyle(LoadingIndicator.Style.DARK)
            }
        )
    }

    private lateinit var window: UIWindow

    @BeforeTest
    fun setUp() {
        window = UIWindow(UIScreen.mainScreen.bounds)
        window.makeKeyAndVisible()
    }

    @Test
    fun presentIndicator() {
        val hostView = UIViewController()
        val indicator = IOSLoadingIndicator
            .Builder(hostView)
            .create()
        window.rootViewController = hostView
        assertNull(hostView.presentedViewController)
        assertFalse(indicator.isVisible)
        indicator.present(false)
        assertTrue(indicator.isVisible)
    }

    @Test
    fun dismissIndicator() {
        val hostView = UIViewController()
        val indicator = IOSLoadingIndicator
            .Builder(hostView)
            .create()
        window.rootViewController = hostView
        assertNull(hostView.presentedViewController)
        assertFalse(indicator.isVisible)
        indicator.present(false) {
            assertTrue(indicator.isVisible)
            indicator.dismiss(false) {
                assertNull(hostView.presentedViewController)
                assertFalse(indicator.isVisible)
            }
        }
    }
}
