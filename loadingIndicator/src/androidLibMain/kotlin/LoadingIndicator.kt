package com.splendo.kaluga.loadingIndicator

import android.content.res.Resources.ID_NULL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

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

/** On Android `View` is represented as resource id */
actual typealias View = Int
/** On Android host `Controller` is FragmentActivity */
actual typealias Controller = FragmentActivity

class AndroidLoadingIndicator private constructor(viewResId: View) : LoadingIndicator {

    class Builder(private val viewResId: View) : LoadingIndicator.Builder {
        override fun create() = AndroidLoadingIndicator(viewResId)
    }

    class LoadingDialog : DialogFragment() {

        private val viewResId get() = arguments?.getInt(RESOURCE_ID_KEY) ?: ID_NULL

        companion object {

            private const val RESOURCE_ID_KEY = "resId"

            fun newInstance(viewResId: View) = LoadingDialog().apply {
                arguments = Bundle().apply { putInt(RESOURCE_ID_KEY, viewResId) }
                isCancelable = false
                retainInstance = true
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
            return inflater.inflate(viewResId, container)
        }
    }

    private val loadingDialog = LoadingDialog.newInstance(viewResId)

    override val isVisible get() = loadingDialog.isVisible

    override fun present(controller: Controller, animated: Boolean, completion: () -> Unit): LoadingIndicator = apply {
        loadingDialog.show(controller.supportFragmentManager, "LoadingIndicator")
        completion()
    }

    override fun dismiss(animated: Boolean, completion: () -> Unit) {
        loadingDialog.dismiss()
        completion()
    }
}
