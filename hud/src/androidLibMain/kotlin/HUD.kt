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

@file:JvmName("AndroidHUD")

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
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.base.mainHandler
import com.splendo.kaluga.base.utils.byOrdinalOrDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Default [BaseHUD] implementation.
 */
actual class HUD private constructor(
    @LayoutRes viewResId: Int,
    actual override val hudConfig: HudConfig,
    lifecycleManagerObserver: LifecycleManagerObserver,
    coroutineScope: CoroutineScope,
) : BaseHUD(coroutineScope) {

    /**
     * Builder class for creating a [HUD]
     * @param lifecycleManagerObserver the [LifecycleManagerObserver] to observe lifecycle changes.
     */
    actual class Builder(
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    ) : BaseHUD.Builder(), ActivityLifecycleSubscribable by lifecycleManagerObserver {

        /**
         * Creates a [HUD] based on [hudConfig].
         * @param hudConfig The [HudConfig] to apply to the [HUD].
         * @param coroutineScope The [CoroutineScope] managing the lifecycle of the HUD.
         * @return the created [HUD]
         */
        actual override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope) = HUD(
            R.layout.loading_indicator_view,
            hudConfig,
            lifecycleManagerObserver,
            coroutineScope,
        )
    }

    internal class LoadingDialog : DialogFragment() {

        var presentCompletionBlock: () -> Unit = {}
        var dismissCompletionBlock: () -> Unit = {}

        private val viewResId get() = arguments?.getInt(RESOURCE_ID_KEY) ?: ID_NULL
        private val style get() = Enum.byOrdinalOrDefault(arguments?.getInt(STYLE_KEY) ?: -1, HUDStyle.SYSTEM)
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
                findViewById<TextView>(R.id.text_view).apply {
                    text = title
                    visibility = if (title == null) View.GONE else View.VISIBLE
                }
            }
        }

        override fun onResume() {
            super.onResume()
            mainHandler.post {
                presentCompletionBlock()
                presentCompletionBlock = {}
            }
        }

        override fun onPause() {
            super.onPause()
            mainHandler.post {
                dismissCompletionBlock()
                dismissCompletionBlock = {}
            }
        }
    }

    private sealed class DialogState {
        data object Gone : DialogState()
        data object Visible : DialogState()
    }

    private val loadingDialog = LoadingDialog.newInstance(viewResId, hudConfig)
    private var dialogState = MutableStateFlow<DialogState>(DialogState.Gone)

    init {
        launch {
            combine(lifecycleManagerObserver.managerState, dialogState) { uiContextData, dialogPresentation ->
                Pair(uiContextData, dialogPresentation)
            }.collect { contextPresentation ->
                when (contextPresentation.second) {
                    is DialogState.Visible -> contextPresentation.first?.fragmentManager?.let { loadingDialog.show(it, "Kaluga.HUD") }
                    is DialogState.Gone -> if (loadingDialog.isAdded) loadingDialog.dismiss()
                }
            }
        }
    }

    actual override val isVisible get() = loadingDialog.isVisible

    actual override suspend fun present(animated: Boolean): HUD {
        return suspendCoroutine { continuation ->
            loadingDialog.presentCompletionBlock = {
                continuation.resume(this)
            }
            dialogState.value = DialogState.Visible
        }
    }

    actual override suspend fun dismiss(animated: Boolean) {
        suspendCoroutine<Unit> { continuation ->
            loadingDialog.dismissCompletionBlock = {
                continuation.resume(Unit)
            }

            dialogState.value = DialogState.Gone
        }
    }
}
