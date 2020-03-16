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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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

class AndroidHUD private constructor(@LayoutRes viewResId: Int, hudConfig: HudConfig, uiContextObserver: UiContextObserver) : HUD {

    class Builder : HUD.Builder() {

        private val uiContextObserver = UiContextObserver()

        fun subscribe(lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager) {
            uiContextObserver.uiContextData = UiContextObserver.UiContextData(
                lifecycleOwner, fragmentManager
            )
        }

        fun unsubscribe() {
            uiContextObserver.uiContextData = null
        }

        override fun create(hudConfig: HudConfig) = AndroidHUD(
            R.layout.loading_indicator_view,
            hudConfig,
            uiContextObserver
        )
    }

    internal class LoadingDialog : DialogFragment() {

        var presentCompletionBlock: () -> Unit = {}
        var dismissCompletionBlock: () -> Unit = {}

        private val viewResId get() = arguments?.getInt(RESOURCE_ID_KEY) ?: ID_NULL
        private val style get() = HUD.Style.valueOf(arguments?.getInt(STYLE_KEY) ?: HUD.Style.SYSTEM.value)
        private val title get() = arguments?.getString(TITLE_KEY)

        @ColorInt
        private fun backgroundColor(context: Context): Int = when (style) {
            HUD.Style.SYSTEM -> resolveAttrColor(context, android.R.attr.windowBackground)
            HUD.Style.CUSTOM -> ContextCompat.getColor(context, R.color.li_colorBackground)
        }

        @ColorInt
        private fun foregroundColor(context: Context): Int = when (style) {
            HUD.Style.SYSTEM -> resolveAttrColor(context, android.R.attr.colorAccent)
            HUD.Style.CUSTOM -> ContextCompat.getColor(context, R.color.li_colorAccent)
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

    init {
        checkNotNull(uiContextObserver.uiContextData) { "Please subscribe builder to existing UI context before building HUD" }
        subscribeIfNeeded(uiContextObserver.uiContextData)
        uiContextObserver.onUiContextDataWillChange = { newValue, oldValue ->
            unsubscribeIfNeeded(oldValue)
            subscribeIfNeeded(newValue)
        }
    }

    private fun unsubscribeIfNeeded(uiContextData: UiContextObserver.UiContextData?) {
        if (uiContextData != null) {
            MainScope().launch {
                dialogState.removeObservers(uiContextData.lifecycleOwner)
            }
        }
    }

    private fun subscribeIfNeeded(uiContextData: UiContextObserver.UiContextData?) {
        if (uiContextData != null) {
            MainScope().launch {
                dialogState.observe(uiContextData.lifecycleOwner, Observer {
                    when (it) {
                        is DialogState.Visible -> loadingDialog.show(uiContextData.fragmentManager, "Kaluga.HUD")
                        is DialogState.Gone -> loadingDialog.dismiss()
                    }
                })
            }
        }
    }

    override val isVisible get() = loadingDialog.isVisible

    override fun present(animated: Boolean, completion: () -> Unit): HUD = apply {
        loadingDialog.presentCompletionBlock = completion
        dialogState.postValue(DialogState.Visible)
    }

    override fun dismiss(animated: Boolean, completion: () -> Unit) {
        loadingDialog.dismissCompletionBlock = completion
        dialogState.postValue(DialogState.Gone)
    }
}
