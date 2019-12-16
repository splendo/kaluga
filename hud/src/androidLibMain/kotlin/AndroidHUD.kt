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
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
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

class AndroidHUD private constructor(viewResId: Int, hudConfig: HudConfig, uiContextTrackingBuilder: UiContextTrackingBuilder) : HUD {

    class Builder(private val uiContextTrackingBuilder: UiContextTrackingBuilder) : HUD.Builder() {
        override fun create(hudConfig: HudConfig) = AndroidHUD(
            R.layout.loading_indicator_view,
            hudConfig,
            uiContextTrackingBuilder
        )
    }

    internal class LoadingDialog : DialogFragment() {

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

            fun newInstance(@IdRes viewResId: Int, hudConfig: HudConfig) = LoadingDialog().apply {
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
    }

    private sealed class DialogState {
        object Gone: DialogState()
        object Visible: DialogState()
    }

    private val loadingDialog = LoadingDialog.newInstance(viewResId, hudConfig)
    private var dialogState = MutableLiveData<DialogState>()

    init {
        subscribeIfNeeded(uiContextTrackingBuilder.uiContextData)
        uiContextTrackingBuilder.onUiContextDataWillChange = { newValue, oldValue ->
            unsubscribeIfNeeded(oldValue)
            subscribeIfNeeded(newValue)
        }
    }

    private fun unsubscribeIfNeeded(uiContextData: UiContextTrackingBuilder.UiContextData?) {
        if (uiContextData != null) {
            MainScope().launch {
                dialogState.removeObservers(uiContextData.lifecycleOwner)
            }
        }
    }

    private fun subscribeIfNeeded(uiContextData: UiContextTrackingBuilder.UiContextData?) {
        if (uiContextData != null) {
            MainScope().launch {
                dialogState.observe(uiContextData.lifecycleOwner, Observer {
                    when (it) {
                        is DialogState.Visible -> {
                            loadingDialog.show(uiContextData.fragmentManager, "Kaluga.HUD")
                        }
                        is DialogState.Gone -> {
                            loadingDialog.dismiss()
                        }
                    }
                })
            }
        }
    }

    override val isVisible get() = loadingDialog.isVisible

    override fun present(animated: Boolean, completion: () -> Unit): HUD = apply {
        dialogState.postValue(DialogState.Visible)
        completion()
    }

    override fun dismiss(animated: Boolean, completion: () -> Unit) {
        dialogState.postValue(DialogState.Gone)
        completion()
    }

    override fun dismissAfter(timeMillis: Long, animated: Boolean) = apply {
        MainScope().launch {
            delay(timeMillis)
            dismiss(animated)
        }
    }

    override fun setTitle(title: String?) {
        loadingDialog.view?.findViewById<TextView>(R.id.text_view)?.apply {
            text = title
            visibility = if (title == null) View.GONE else View.VISIBLE
        }
    }
}
