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

    private lateinit var window: UIWindow

    @BeforeTest
    fun setUp() {
        window = UIWindow(UIScreen.mainScreen.bounds)
        window.makeKeyAndVisible()
    }

    @Test
    fun presentIndicator() {
        val indicatorView = UIViewController()
        indicatorView.view.backgroundColor = UIColor.blackColor
        val indicator = IOSLoadingIndicator
            .Builder(indicatorView)
            .create()
        val hostView = UIViewController()
        window.rootViewController = hostView
        assertNull(hostView.presentedViewController)
        assertFalse(indicator.isVisible)
        indicator.present(hostView, false)
        assertTrue(indicator.isVisible)
        assertEquals(indicatorView, hostView.presentedViewController)
    }

    @Test
    fun dismissIndicator() {
        val indicatorView = UIViewController()
        indicatorView.view.backgroundColor = UIColor.blackColor
        val indicator = IOSLoadingIndicator
            .Builder(indicatorView)
            .create()
        val hostView = UIViewController()
        window.rootViewController = hostView
        assertNull(hostView.presentedViewController)
        assertFalse(indicator.isVisible)
        indicator.present(hostView, false) {
            assertEquals(indicatorView, hostView.presentedViewController)
            assertTrue(indicator.isVisible)
            indicator.dismiss(false) {
                assertNull(hostView.presentedViewController)
                assertFalse(indicator.isVisible)
            }
        }
    }
}
