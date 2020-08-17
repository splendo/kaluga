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

package com.splendo.kaluga.hud

import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

class IOSHUDTests : HUDTests() {

    class HUDViewController : UIViewController(null, null) {

        var mockPresentingHUD: MockPresentingHUD? = null

        override fun presentedViewController(): UIViewController? {
            return mockPresentingHUD
        }

        override fun presentViewController(viewControllerToPresent: UIViewController, animated: Boolean, completion: (() -> Unit)?) {
            (viewControllerToPresent as? MockPresentingHUD)?.let {
                mockPresentingHUD = it
                it.parent = this
                completion?.invoke()
            }
        }

        override fun dismissViewControllerAnimated(flag: Boolean, completion: (() -> Unit)?) {
            mockPresentingHUD?.parent = null
            mockPresentingHUD = null
            completion?.invoke()
        }
    }

    class MockPresentingHUD(val hudViewController: UIViewController) : UIViewController(null, null) {

        var parent: UIViewController? = null

        override fun presentingViewController(): UIViewController? {
            return parent
        }
    }

    private lateinit var window: UIWindow
    override val builder get() = createBuilder(HUDViewController())
    private fun createBuilder(hostView: UIViewController): HUD.Builder = HUD.Builder(hostView) { MockPresentingHUD(it) }

    @Test
    fun builderInitializer() {
        assertNotNull(
            builder.build()
        )
    }

    @Test
    fun builderSetStyleAndTitle() {
        assertNotNull(
            builder.build {
                setStyle(HUDStyle.CUSTOM)
                setTitle("Foo")
            }
        )
    }

    @Test
    fun presentIndicator() = runBlocking {
        val hostView = HUDViewController()
        val indicator = createBuilder(hostView).build()
        assertNull(hostView.presentedViewController)
        assertFalse(indicator.isVisible)
        val didPresent = EmptyCompletableDeferred()
        launch {
            indicator.present(false)
            assertTrue(indicator.isVisible)
            didPresent.complete()
        }
        didPresent.await()
    }

    @Test
    fun dismissIndicator() = runBlocking {
        val hostView = HUDViewController()
        val indicator = createBuilder(hostView).build()
        assertNull(hostView.presentedViewController)
        assertFalse(indicator.isVisible)
        val didFinishPresenting = EmptyCompletableDeferred()
        launch {
            indicator.present(false)
            assertTrue(indicator.isVisible)
            indicator.dismiss(false)
            assertFalse(indicator.isVisible)
            didFinishPresenting.complete()
        }
        didFinishPresenting.await()
    }
}
