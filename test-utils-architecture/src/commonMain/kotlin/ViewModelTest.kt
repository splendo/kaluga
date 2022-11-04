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

import com.splendo.kaluga.architecture.viewmodel.LifecycleViewModel
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.BaseUIThreadTest
import com.splendo.kaluga.test.base.UIThreadTest
import kotlinx.coroutines.CoroutineScope
import kotlin.test.BeforeTest

abstract class ViewModelTest<VM : LifecycleViewModel> : BaseTest() {

    lateinit var viewModel: VM

    protected abstract fun createViewModel(): VM

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        viewModel = createViewModel()
    }
}

abstract class SimpleUIThreadViewModelTest<VM : LifecycleViewModel> :
    UIThreadViewModelTest<UIThreadViewModelTest.ViewModelTestContext<VM>, VM>() {

    override val createTestContext: suspend (CoroutineScope) -> ViewModelTestContext<VM> =
        { LazyViewModelTestContext(it, ::createViewModel) }

    abstract fun createViewModel(): VM
}

/**
 * A [UIThreadTest] that takes a [ViewModelTestContext]
 */
abstract class UIThreadViewModelTest<VMC : UIThreadViewModelTest.ViewModelTestContext<VM>, VM : LifecycleViewModel>() :
    UIThreadTest<VMC>() {

    /**
     * [ViewModelTestContext] that lazily creates the view model
     * @param coroutineScope The [CoroutineScope] of the [LazyViewModelTestContext]
     * @param createViewModel Creator for the [LifecycleViewModel]
     */
    open class LazyViewModelTestContext<VM : LifecycleViewModel>(coroutineScope: CoroutineScope, private val createViewModel: () -> VM) : BaseUIThreadViewModelTest.LazyViewModelTestContext<VM>(coroutineScope, createViewModel), ViewModelTestContext<VM>

    /**
     * A [UIThreadTest.TestContext] that provides a [LifecycleViewModel]
     */
    interface ViewModelTestContext<VM : LifecycleViewModel> : BaseUIThreadViewModelTest.ViewModelTestContext<VM>, TestContext
}

/**
 * A [BaseUIThreadTest] that takes a [ViewModelTestContext]
 */
abstract class BaseUIThreadViewModelTest<C, VMC : BaseUIThreadViewModelTest.ViewModelTestContext<VM>, VM : LifecycleViewModel>() :
    BaseUIThreadTest<C, VMC>() {

    /**
     * [ViewModelTestContext] that lazily creates the view model
     * @param coroutineScope The [CoroutineScope] of the [LazyViewModelTestContext]
     * @param createViewModel Creator for the [LifecycleViewModel]
     */
    open class LazyViewModelTestContext<VM : LifecycleViewModel>(coroutineScope: CoroutineScope, private val createViewModel: () -> VM) :
        ViewModelTestContext<VM>, CoroutineScope by coroutineScope {
        override val viewModel: VM by lazy { createViewModel() }
    }

    /**
     * A [BaseUIThreadTest.TestContext] that provides a [LifecycleViewModel]
     */
    interface ViewModelTestContext<VM : LifecycleViewModel> : TestContext {
        val viewModel: VM
    }
}
