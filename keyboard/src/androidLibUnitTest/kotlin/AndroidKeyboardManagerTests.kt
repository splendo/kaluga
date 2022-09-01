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
package com.splendo.kaluga.keyboard

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.keyboard.AndroidKeyboardManagerTests.AndroidKeyboardTestContext
import kotlinx.coroutines.CoroutineScope
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class AndroidKeyboardManagerTests : KeyboardManagerTests<AndroidKeyboardTestContext>() {

    companion object {
        private const val viewId = 1
    }

    inner class AndroidKeyboardTestContext(coroutineScope: CoroutineScope) : KeyboardTestContext(), CoroutineScope by coroutineScope {
        override val focusHandler get() = ViewFocusHandler(viewId)
        override lateinit var builder: KeyboardManager.Builder

        val mockActivity: Activity = mock(Activity::class.java)
        var mockView: View = mock(View::class.java)
        var mockWindowToken: IBinder = mock(IBinder::class.java)
        var mockInputMethodManager: InputMethodManager = mock(InputMethodManager::class.java)

        override fun verifyShow() {
            verify(mockView).requestFocus()
            verify(mockInputMethodManager).toggleSoftInput(eq(InputMethodManager.SHOW_FORCED), eq(InputMethodManager.HIDE_IMPLICIT_ONLY))
        }

        override fun verifyDismiss() {
            verify(mockInputMethodManager).hideSoftInputFromWindow(eq(mockWindowToken), eq(0))
        }

        init {
            val mockLifecycleOwner = mock(LifecycleOwner::class.java)
            val mockFragmentManager = mock(FragmentManager::class.java)

            `when`(mockActivity.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(
                mockInputMethodManager
            )
            `when`(mockActivity.currentFocus).thenReturn(mockView)
            `when`(mockActivity.findViewById<View>(ArgumentMatchers.eq(viewId))).thenReturn(mockView)
            `when`(mockView.windowToken).thenReturn(mockWindowToken)
            `when`(mockInputMethodManager.isAcceptingText).thenReturn(true)

            builder = KeyboardManager.Builder()

            builder.subscribe(
                LifecycleSubscribable.LifecycleManager(
                    mockActivity,
                    mockLifecycleOwner,
                    mockFragmentManager
                )
            )
        }
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> AndroidKeyboardTestContext =
        { AndroidKeyboardTestContext(it) }
}
