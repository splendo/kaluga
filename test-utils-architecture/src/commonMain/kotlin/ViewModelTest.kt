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

package com.splendo.kaluga.test.architecture

import co.touchlab.stately.ensureNeverFrozen
import com.splendo.kaluga.architecture.viewmodel.ViewModel
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.BaseUIThreadTest
import com.splendo.kaluga.test.base.UIThreadTest
import kotlinx.coroutines.CoroutineScope
import kotlin.test.BeforeTest

abstract class ViewModelTest<VM : ViewModel>(allowFreezing: Boolean = false) : BaseTest() {

    init {
        if (!allowFreezing) ensureNeverFrozen()
    }

    lateinit var viewModel: VM

    protected abstract fun createViewModel(): VM

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        viewModel = createViewModel()
    }
}

abstract class SimpleUIThreadViewModelTest<VM : ViewModel> :
    UIThreadViewModelTest<UIThreadViewModelTest.ViewModelTestContext<VM>, VM>(allowFreezing = true) {

    override val createTestContext: suspend (CoroutineScope) -> ViewModelTestContext<VM> =
        { LazyViewModelTestContext(it, ::createViewModel) }

    abstract fun createViewModel(): VM
}

/**
 * A [UIThreadTest] that takes a [ViewModelTestContext]
 */
abstract class UIThreadViewModelTest<VMC : UIThreadViewModelTest.ViewModelTestContext<VM>, VM : ViewModel>(allowFreezing: Boolean = false) :
    UIThreadTest<VMC>(allowFreezing) {

    /**
     * [ViewModelTestContext] that lazily creates the view model
     * @param coroutineScope The [CoroutineScope] of the [LazyViewModelTestContext]
     * @param createViewModel Creator for the [ViewModel]
     */
    open class LazyViewModelTestContext<VM : ViewModel>(coroutineScope: CoroutineScope, private val createViewModel: () -> VM) : BaseUIThreadViewModelTest.LazyViewModelTestContext<VM>(coroutineScope, createViewModel), ViewModelTestContext<VM>

    /**
     * A [UIThreadTest.TestContext] that provides a [ViewModel]
     */
    interface ViewModelTestContext<VM : ViewModel> : BaseUIThreadViewModelTest.ViewModelTestContext<VM>, TestContext
}

/**
 * A [BaseUIThreadTest] that takes a [ViewModelTestContext]
 */
abstract class BaseUIThreadViewModelTest<CONF, VMC : BaseUIThreadViewModelTest.ViewModelTestContext<VM>, VM : ViewModel>(allowFreezing: Boolean = false) :
    BaseUIThreadTest<CONF, VMC>(allowFreezing) {

    /**
     * [ViewModelTestContext] that lazily creates the view model
     * @param coroutineScope The [CoroutineScope] of the [LazyViewModelTestContext]
     * @param createViewModel Creator for the [ViewModel]
     */
    open class LazyViewModelTestContext<VM : ViewModel>(coroutineScope: CoroutineScope, private val createViewModel: () -> VM) :
        ViewModelTestContext<VM>, CoroutineScope by coroutineScope {
        override val viewModel: VM by lazy { createViewModel() }
    }

    /**
     * A [BaseUIThreadTest.TestContext] that provides a [ViewModel]
     */
    interface ViewModelTestContext<VM : ViewModel> : TestContext {
        val viewModel: VM
    }
}
