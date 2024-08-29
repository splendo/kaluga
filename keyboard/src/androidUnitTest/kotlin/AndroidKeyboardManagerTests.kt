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
package com.splendo.kaluga.keyboard

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethod.SHOW_EXPLICIT
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.keyboard.AndroidKeyboardManagerTests.AndroidKeyboardTestContext
import kotlinx.coroutines.CoroutineScope
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class AndroidKeyboardManagerTests : KeyboardManagerTests<ViewFocusHandler, AndroidKeyboardTestContext>() {

    companion object {
        private const val VIEW_ID = 1
    }

    inner class AndroidKeyboardTestContext(coroutineScope: CoroutineScope) :
        KeyboardTestContext<ViewFocusHandler>(),
        CoroutineScope by coroutineScope {

        val handler = mock(Handler::class.java)

        override val focusHandler get() = ViewFocusHandler(VIEW_ID, handler)
        override var builder: ViewKeyboardManager.Builder

        val mockActivity: Activity = mock(Activity::class.java)
        var mockView: View = mock(View::class.java)
        var mockDecorView: View = mock(View::class.java)
        var mockWindow: Window = mock(Window::class.java)
        var mockWindowToken: IBinder = mock(IBinder::class.java)
        var mockInputMethodManager: InputMethodManager = mock(InputMethodManager::class.java)

        init {
            `when`(handler.post(any())).thenAnswer { invocation ->
                invocation.getArgument(0, Runnable::class.java).run()
                true
            }
            `when`(mockActivity.isDestroyed).thenReturn(false)
            `when`(mockActivity.isFinishing).thenReturn(false)
            `when`(mockActivity.hasWindowFocus()).thenReturn(true)
        }

        override fun verifyShow() {
            verify(mockView).requestFocus()
            verify(mockInputMethodManager).showSoftInput(eq(mockView), eq(SHOW_EXPLICIT))
            Unit
        }

        override fun verifyDismiss() {
            verify(mockInputMethodManager).hideSoftInputFromWindow(eq(mockWindowToken), eq(0))
        }

        init {
            val mockLifecycleOwner = mock(LifecycleOwner::class.java)
            val mockFragmentManager = mock(FragmentManager::class.java)

            `when`(mockActivity.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(
                mockInputMethodManager,
            )
            `when`(mockActivity.currentFocus).thenReturn(mockView)
            `when`(mockActivity.findViewById<View>(ArgumentMatchers.eq(VIEW_ID))).thenReturn(mockView)
            `when`(mockActivity.window).thenReturn(mockWindow)
            `when`(mockWindow.decorView).thenReturn(mockDecorView)
            `when`(mockDecorView.windowToken).thenReturn(mockWindowToken)
            `when`(mockInputMethodManager.isAcceptingText).thenReturn(true)

            builder = ViewKeyboardManager.Builder()

            builder.subscribe(
                ActivityLifecycleSubscribable.LifecycleManager(
                    mockActivity,
                    mockLifecycleOwner,
                    mockFragmentManager,
                ),
            )
        }
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> AndroidKeyboardTestContext =
        { AndroidKeyboardTestContext(it) }
}
