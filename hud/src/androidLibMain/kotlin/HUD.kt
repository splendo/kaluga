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

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources.ID_NULL
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.UIContextObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

actual class HUD private constructor(@LayoutRes viewResId: Int, actual val hudConfig: HudConfig, uiContextObserver: UIContextObserver) : CoroutineScope by MainScope() {

    actual class Builder(private val uiContextObserver: UIContextObserver = UIContextObserver()) : BaseHUDBuilder(), LifecycleSubscribable by uiContextObserver {

        actual fun create(hudConfig: HudConfig) = HUD(
            R.layout.loading_indicator_view,
            hudConfig,
            uiContextObserver
        )
    }

    internal class LoadingDialog : DialogFragment() {

        var presentCompletionBlock: () -> Unit = {}
        var dismissCompletionBlock: () -> Unit = {}

        private val viewResId get() = arguments?.getInt(RESOURCE_ID_KEY) ?: ID_NULL
        private val style get() = HUDStyle.valueOf(arguments?.getInt(STYLE_KEY) ?: HUDStyle.SYSTEM.value)
        private val title get() = arguments?.getString(TITLE_KEY)

        @ColorInt
        private fun backgroundColor(context: Context): Int = when (style) {
            HUDStyle.SYSTEM -> resolveAttrColor(context, android.R.attr.windowBackground)
            HUDStyle.CUSTOM -> ContextCompat.getColor(context, R.color.li_colorBackground)
        }

        @ColorInt
        private fun foregroundColor(context: Context): Int = when (style) {
            HUDStyle.SYSTEM -> resolveAttrColor(context, android.R.attr.colorAccent)
            HUDStyle.CUSTOM -> ContextCompat.getColor(context, R.color.li_colorAccent)
        }

        @ColorInt
        private fun resolveAttrColor(context: Context, @AttrRes colorAttrRes: Int): Int {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(colorAttrRes, typedValue, true)
            return typedValue.data
        }

        companion object {

            private const val RESOURCE_ID_KEY = "resId"
            private const val STYLE_KEY = "style"
            private const val TITLE_KEY = "title"

            fun newInstance(@LayoutRes viewResId: Int, hudConfig: HudConfig) = LoadingDialog().apply {
                arguments = Bundle().apply {
                    putInt(RESOURCE_ID_KEY, viewResId)
                    putInt(STYLE_KEY, hudConfig.style.ordinal)
                    putString(TITLE_KEY, hudConfig.title)
                }
                isCancelable = false
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // Dismiss existing dialog on rotation
            // We will handle this state via dialogState and show it again if needed
            if (savedInstanceState != null) {
                dismiss()
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(viewResId, container).apply {
                findViewById<LinearLayout>(R.id.content_view).setBackgroundColor(backgroundColor(inflater.context))
                findViewById<ProgressBar>(R.id.progress_bar).indeterminateTintList = ColorStateList.valueOf(foregroundColor(inflater.context))
                findViewById<TextView>(R.id.text_view).text = title
                findViewById<TextView>(R.id.text_view).visibility = if (title == null) View.GONE else View.VISIBLE
            }
        }

        override fun onStart() {
            super.onStart()
            presentCompletionBlock()
        }

        override fun onStop() {
            super.onStop()
            dismissCompletionBlock()
        }
    }

    private sealed class DialogState {
        object Gone : DialogState()
        object Visible : DialogState()
    }

    private val loadingDialog = LoadingDialog.newInstance(viewResId, hudConfig)
    private var dialogState = MutableLiveData<DialogState>()
    private var previousUIContextData: UIContextObserver.UIContextData? = null

    init {
        launch {
            uiContextObserver.uiContextData.collect { uiContextData ->
                unsubscribeIfNeeded(previousUIContextData)
                subscribeIfNeeded(uiContextData)
                previousUIContextData = uiContextData
            }
        }
    }

    private fun unsubscribeIfNeeded(uiContextData: UIContextObserver.UIContextData?) {
        if (uiContextData != null) {
            runBlocking(Dispatchers.Main.immediate) {
                dialogState.removeObservers(uiContextData.lifecycleOwner)
            }
        }
    }

    private fun subscribeIfNeeded(uiContextData: UIContextObserver.UIContextData?) {
        if (uiContextData != null) {
            runBlocking(Dispatchers.Main.immediate) {
                dialogState.observe(uiContextData.lifecycleOwner, Observer {
                    when (it) {
                        is DialogState.Visible -> loadingDialog.show(uiContextData.fragmentManager, "Kaluga.HUD")
                        is DialogState.Gone -> loadingDialog.dismiss()
                    }
                })
            }
        }
    }

    actual val isVisible get() = loadingDialog.isVisible

    actual fun present(animated: Boolean, completion: () -> Unit): HUD = apply {
        loadingDialog.presentCompletionBlock = completion
        dialogState.postValue(DialogState.Visible)
    }

    actual fun dismiss(animated: Boolean, completion: () -> Unit) {
        loadingDialog.dismissCompletionBlock = completion
        dialogState.postValue(DialogState.Gone)
    }
}
