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

package com.splendo.kaluga.test.koin

import com.splendo.kaluga.architecture.viewmodel.LifecycleViewModel
import com.splendo.kaluga.test.architecture.BaseUIThreadViewModelTest
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

abstract class KoinUIThreadViewModelTest<Context : KoinUIThreadViewModelTest.KoinViewModelTestContext<ViewModel>, ViewModel : LifecycleViewModel> :
    KoinUIThreadTest<Context>() {

    abstract class KoinViewModelTestContext<ViewModel : LifecycleViewModel>(
        appDeclaration: KoinAppDeclaration? = null,
        koinModules: List<Module>,
    ) : KoinTestContext(appDeclaration, koinModules),
        UIThreadViewModelTest.ViewModelTestContext<ViewModel> {
        constructor(vararg koinModules: Module) : this(null, koinModules.toList())
        constructor(appDeclaration: KoinAppDeclaration, vararg koinModules: Module) : this(
            appDeclaration,
            koinModules.toList(),
        )

        override fun dispose() {
            super<UIThreadViewModelTest.ViewModelTestContext>.dispose()
            super<KoinTestContext>.dispose()
        }
    }
}

abstract class BaseKoinUIThreadViewModelTest<Configuration, Context : BaseKoinUIThreadViewModelTest.KoinViewModelTestContext<ViewModel>, ViewModel : LifecycleViewModel> :
    BaseKoinUIThreadTest<Configuration, Context>() {

    abstract class KoinViewModelTestContext<ViewModel : LifecycleViewModel>(
        appDeclaration: KoinAppDeclaration? = null,
        koinModules: List<Module>,
    ) : KoinTestContext(appDeclaration, koinModules),
        BaseUIThreadViewModelTest.ViewModelTestContext<ViewModel> {
        constructor(vararg koinModules: Module) : this(null, koinModules.toList())
        constructor(appDeclaration: KoinAppDeclaration, vararg koinModules: Module) : this(
            appDeclaration,
            koinModules.toList(),
        )
        override fun dispose() {
            super<BaseUIThreadViewModelTest.ViewModelTestContext>.dispose()
            super<KoinTestContext>.dispose()
        }
    }
}
