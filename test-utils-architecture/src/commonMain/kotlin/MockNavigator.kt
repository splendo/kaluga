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

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationException
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock

class MockNavigationException(action: NavigationAction<*>) : NavigationException("Mock exception for $action")

/**
 * Mock implementation of [Navigator]
 */
open class MockNavigator<A : NavigationAction<*>>(handleAction: (A) -> Unit = { }) : Navigator<A> {

    companion object {
        /**
         * Creates a [MockNavigator] that automatically throws a [MockNavigationException] when [navigate] is called.
         */
        fun <A : NavigationAction<*>> navigatorThatThrows() = MockNavigator<A> { throw MockNavigationException(it) }
    }

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] of [navigate]
     */
    val navigateMock = this::navigate.mock()
    override fun navigate(action: A): Unit = navigateMock.call(action)

    init {
        navigateMock.on().doExecute { (action) -> handleAction(action) }
    }
}
