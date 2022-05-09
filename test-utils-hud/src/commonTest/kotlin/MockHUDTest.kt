import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.hud.BaseHUD
import com.splendo.kaluga.hud.HUDStyle
import com.splendo.kaluga.hud.HudConfig
import com.splendo.kaluga.hud.presentDuring
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import com.splendo.kaluga.test.hud.MockHUD
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

class MockHUDTest : UIThreadViewModelTest<MockHUDTest.CustomViewModelTestContext, MockHUDTest.ViewModel>() {

    class ViewModel(val hudBuilder: BaseHUD.Builder) : BaseViewModel() {
        fun buildAndShowHud(config: HudConfig, isFinished: Deferred<Unit>): Pair<Deferred<BaseHUD>, Deferred<Unit>> {
            val isShowing = CompletableDeferred<BaseHUD>()
            val hasFinishedShowing = CompletableDeferred<Unit>()
            coroutineScope.launch {
                val hud = hudBuilder.create(config, this)
                hud.presentDuring {
                    isShowing.complete(this)
                    isFinished.await()
                }
                hasFinishedShowing.complete()
            }
            return isShowing to hasFinishedShowing
        }
    }

    class CustomViewModelTestContext : ViewModelTestContext<ViewModel> {
        val mockHudBuilder = MockHUD.Builder()
        override val viewModel: ViewModel = ViewModel(mockHudBuilder)
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> CustomViewModelTestContext = { CustomViewModelTestContext() }

    @Test
    fun testCustomUIThreadViewModelTest() = testOnUIThread {
        val config = HudConfig(HUDStyle.CUSTOM, "title")
        val isFinished = EmptyCompletableDeferred()
        val (isShowing, hasFinishedShowing) = viewModel.buildAndShowHud(config, isFinished)
        val displayedHUD = isShowing.await()
        assertEquals(config, displayedHUD.hudConfig)
        mockHudBuilder.createMock.verify(eq(config))
        assertEquals(displayedHUD, mockHudBuilder.builtHUDs.first())
        assertTrue(displayedHUD.isVisible)
        mockHudBuilder.builtHUDs.first().presentMock.verify()
        isFinished.complete()
        hasFinishedShowing.await()
        assertFalse(displayedHUD.isVisible)
        mockHudBuilder.builtHUDs.first().dismissMock.verify()
    }
}
