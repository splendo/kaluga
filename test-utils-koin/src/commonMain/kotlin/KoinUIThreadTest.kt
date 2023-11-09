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

import com.splendo.kaluga.test.base.BaseUIThreadTest
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

abstract class KoinUIThreadTest<TC : KoinUIThreadTest.KoinTestContext> : BaseKoinUIThreadTest<Unit, TC>() {

    open class KoinTestContext(
        appDeclaration: KoinAppDeclaration? = null,
        koinModules: List<Module>,
    ) : BaseKoinUIThreadTest.KoinTestContext(appDeclaration, koinModules) {

        constructor(vararg koinModules: Module) : this(null, koinModules.toList())
        constructor(appDeclaration: KoinAppDeclaration, vararg koinModules: Module) : this(
            appDeclaration,
            koinModules.toList(),
        )
    }

    abstract val createTestContext: suspend (scope: CoroutineScope) -> TC
    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> TC get() {
        val createTestContext = this.createTestContext
        return { _, scope ->
            createTestContext(scope)
        }
    }

    fun testOnUIThread(cancelScopeAfterTest: Boolean = false, block: suspend TC.() -> Unit) = testOnUIThread(Unit, cancelScopeAfterTest, block)
}

abstract class BaseKoinUIThreadTest<C, TC : BaseKoinUIThreadTest.KoinTestContext> : BaseUIThreadTest<C, TC>() {

    open class KoinTestContext(
        appDeclaration: KoinAppDeclaration? = null,
        koinModules: List<Module>,
    ) :
        TestContext, KoinComponent {

        constructor(vararg koinModules: Module) : this(null, koinModules.toList())
        constructor(appDeclaration: KoinAppDeclaration, vararg koinModules: Module) : this(
            appDeclaration,
            koinModules.toList(),
        )

        init {
            startKoin {
                appDeclaration?.invoke(this)
                modules(koinModules)
            }
            loadKoinModules(koinModules.toList())
        }

        override fun dispose() {
            stopKoin()
        }
    }
}
