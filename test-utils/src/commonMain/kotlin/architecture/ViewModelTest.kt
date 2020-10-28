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
import com.splendo.kaluga.test.BaseTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class BaseViewModelTest<VM : BaseViewModel> internal constructor() : BaseTest() {

    protected var viewModel: VM? = null

    @BeforeTest
    open fun setUp() {
        super.beforeTest()
        viewModel = createViewModel()
    }

    @AfterTest
    open fun tearDown() {
        super.afterTest()
        viewModel = null
    }

    protected abstract fun createViewModel(): VM
}

expect abstract class ViewModelTest<VM : BaseViewModel> constructor() : BaseViewModelTest<VM>
