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
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest.ViewModelTestContext
import com.splendo.kaluga.test.architecture.viewmodel.cleanUp
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.BaseUIThreadTest
import com.splendo.kaluga.test.base.UIThreadTest
import kotlinx.coroutines.CoroutineScope
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class ViewModelTest<ViewModel : LifecycleViewModel> : BaseTest() {

    lateinit var viewModel: ViewModel

    protected abstract fun createViewModel(): ViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        viewModel = createViewModel()
    }

    @AfterTest
    override fun afterTest() {
        super.afterTest()
        viewModel.cleanUp()
    }
}

abstract class SimpleUIThreadViewModelTest<ViewModel : LifecycleViewModel> : UIThreadViewModelTest<ViewModelTestContext<ViewModel>, ViewModel>() {

    override val createTestContext: suspend (CoroutineScope) -> ViewModelTestContext<ViewModel> =
        { LazyViewModelTestContext(it, ::createViewModel) }

    abstract fun createViewModel(): ViewModel
}

/**
 * A [UIThreadTest] that takes a [UIThreadViewModelTest.ViewModelTestContext]
 */
abstract class UIThreadViewModelTest<Context : ViewModelTestContext<ViewModel>, ViewModel : LifecycleViewModel> : UIThreadTest<Context>() {

    /**
     * [BaseUIThreadViewModelTest.LazyViewModelTestContext] that lazily creates the view model
     * @param coroutineScope The [CoroutineScope] of the [LazyViewModelTestContext]
     * @param createViewModel Creator for the [LifecycleViewModel]
     */
    open class LazyViewModelTestContext<ViewModel : LifecycleViewModel>(coroutineScope: CoroutineScope, private val createViewModel: () -> ViewModel) :
        BaseUIThreadViewModelTest.LazyViewModelTestContext<ViewModel>(coroutineScope, createViewModel),
        ViewModelTestContext<ViewModel>

    /**
     * A [BaseUIThreadViewModelTest.ViewModelTestContext] that provides a [LifecycleViewModel]
     */
    interface ViewModelTestContext<ViewModel : LifecycleViewModel> :
        BaseUIThreadViewModelTest.ViewModelTestContext<ViewModel>,
        TestContext
}

/**
 * A [BaseUIThreadTest] that takes a [BaseUIThreadViewModelTest.ViewModelTestContext]
 */
abstract class BaseUIThreadViewModelTest<Configuration, Context : BaseUIThreadViewModelTest.ViewModelTestContext<ViewModel>, ViewModel : LifecycleViewModel> :
    BaseUIThreadTest<Configuration, Context>() {

    /**
     * [ViewModelTestContext] that lazily creates the view model
     * @param coroutineScope The [CoroutineScope] of the [LazyViewModelTestContext]
     * @param createViewModel Creator for the [LifecycleViewModel]
     */
    open class LazyViewModelTestContext<ViewModel : LifecycleViewModel>(coroutineScope: CoroutineScope, private val createViewModel: () -> ViewModel) :
        ViewModelTestContext<ViewModel>,
        CoroutineScope by coroutineScope {
        override val viewModel: ViewModel by lazy { createViewModel() }
    }

    /**
     * A [BaseUIThreadTest.TestContext] that provides a [LifecycleViewModel]
     */
    interface ViewModelTestContext<ViewModel : LifecycleViewModel> : TestContext {
        val viewModel: ViewModel

        override fun dispose() {
            viewModel.cleanUp()
        }
    }
}
