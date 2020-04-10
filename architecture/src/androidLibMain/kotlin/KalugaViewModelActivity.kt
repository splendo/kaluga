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

package com.splendo.kaluga.architecture

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel

abstract class KalugaViewModelActivity<VM : BaseViewModel> : AppCompatActivity() {

    abstract val viewModel: VM

    lateinit var viewModelWrapper: KalugaViewModelWrapper<VM>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewModelWrapper = KalugaViewModelWrapper(viewModel)
    }

    override fun onResume() {
        super.onResume()

        viewModelWrapper.onResume(this, supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()

        viewModelWrapper.onPause()
    }

}
