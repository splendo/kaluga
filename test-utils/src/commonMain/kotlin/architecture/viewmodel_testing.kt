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

package com.splendo.kaluga.test.architecture

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlin.test.BeforeTest

abstract class ViewModelTest<VM : BaseViewModel> : BaseTest() {
    lateinit var viewModel: VM

    protected abstract fun createViewModel(): VM

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        viewModel = createViewModel()
    }
}

abstract class SimpleUIThreadViewModelTest<VM : BaseViewModel> : UIThreadViewModelTest<UIThreadViewModelTest.ViewModelTestContext<VM>, VM>() {

    class SimpleViewModelTestContext<VM>(private val createViewModel: () -> VM) : ViewModelTestContext<VM>() {
        override fun createViewModel(): VM = createViewModel.invoke()
    }

    override fun createViewModelContext(): ViewModelTestContext<VM> = SimpleViewModelTestContext(::createViewModel)

    abstract fun createViewModel(): VM
}

abstract class UIThreadViewModelTest<VMC : UIThreadViewModelTest.ViewModelTestContext<VM>, VM : BaseViewModel> : BaseTest() {

    abstract class ViewModelTestContext<VM> {

        val viewModel by lazy { createViewModel() }

        abstract fun createViewModel(): VM

        open fun dispose() {}
    }

    abstract fun createViewModelContext(): VMC

    fun testWithViewModel(block: suspend VMC.() -> Unit): Unit = runBlocking(Dispatchers.Main) {
        val viewModelContext = createViewModelContext()
        try {
            block(viewModelContext)
        } finally {
            viewModelContext.dispose()
        }
    }
}
