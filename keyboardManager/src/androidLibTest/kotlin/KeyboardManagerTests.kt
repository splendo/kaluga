package com.splendo.kaluga.test

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.splendo.kaluga.keyboardmanager.KeyboardManagerBuilder
import com.splendo.kaluga.keyboardmanager.KeyboardManagerTests
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify

class AndroidKeyboardManagerTests: KeyboardManagerTests() {

    private lateinit var mockActivity: Activity
    private lateinit var mockView: View
    private lateinit var mockWindowToken: IBinder
    private lateinit var mockInputMethodManager: InputMethodManager

    @Before
    fun before() {
        mockActivity = mock(Activity::class.java)
        mockView = mock(View::class.java)
        mockInputMethodManager = mock(InputMethodManager::class.java)
        mockWindowToken = mock(IBinder::class.java)

        `when`(mockActivity.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(mockInputMethodManager)
        `when`(mockActivity.currentFocus).thenReturn(mockView)
        `when`(mockView.windowToken).thenReturn(mockWindowToken)
        `when`(mockInputMethodManager.isAcceptingText).thenReturn(true)
    }

    override fun verifyShow() {
        verify(mockView).requestFocus()
        verify(mockInputMethodManager).toggleSoftInput(eq(InputMethodManager.SHOW_FORCED), eq(InputMethodManager.HIDE_IMPLICIT_ONLY))
    }

    override fun verifyDismiss() {
        verify(mockInputMethodManager).hideSoftInputFromWindow(eq(mockWindowToken), eq(0))
    }

    override val builder get() = KeyboardManagerBuilder(mockActivity)
    override val view get() = mockView
}
