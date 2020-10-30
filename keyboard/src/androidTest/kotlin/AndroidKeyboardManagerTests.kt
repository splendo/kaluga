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

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.keyboard.KeyboardManagerTests
import org.junit.Before
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.exceptions.base.MockitoException

class AndroidKeyboardManagerTests : KeyboardManagerTests() {

    companion object {
        private const val viewId = 1
    }

    private lateinit var mockActivity: Activity
    private lateinit var mockView: View
    private lateinit var mockWindowToken: IBinder
    private lateinit var mockInputMethodManager: InputMethodManager
    override lateinit var builder: KeyboardManager.Builder

    @Before
    fun before() {
        try {
            mockActivity = mock(Activity::class.java)
        } catch (mockException: MockitoException) {
            isUITest = true
            return
        }
        val mockLifecycleOwner = mock(LifecycleOwner::class.java)
        val mockFragmentManager = mock(FragmentManager::class.java)
        mockView = mock(View::class.java)
        mockInputMethodManager = mock(InputMethodManager::class.java)
        mockWindowToken = mock(IBinder::class.java)

        `when`(mockActivity.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(mockInputMethodManager)
        `when`(mockActivity.currentFocus).thenReturn(mockView)
        `when`(mockActivity.findViewById<View>(ArgumentMatchers.eq(viewId))).thenReturn(mockView)
        `when`(mockView.windowToken).thenReturn(mockWindowToken)
        `when`(mockInputMethodManager.isAcceptingText).thenReturn(true)

        builder = KeyboardManager.Builder()
        builder.subscribe(LifecycleSubscribable.LifecycleManager(mockActivity, mockLifecycleOwner, mockFragmentManager))
    }

    override fun verifyShow() {
        verify(mockView).requestFocus()
        verify(mockInputMethodManager).toggleSoftInput(eq(InputMethodManager.SHOW_FORCED), eq(InputMethodManager.HIDE_IMPLICIT_ONLY))
    }

    override fun verifyDismiss() {
        verify(mockInputMethodManager).hideSoftInputFromWindow(eq(mockWindowToken), eq(0))
    }

    override val view get() = viewId
}
