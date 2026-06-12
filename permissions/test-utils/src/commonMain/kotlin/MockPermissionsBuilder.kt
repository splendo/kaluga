/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.test

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.pairParametersMock
import com.splendo.kaluga.permissions.base.BasePermissionStateRepo
import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class MockBasePermissionsBuilder<P : Permission> : BasePermissionsBuilder<P>
class MockPermissionsBuilder(
    @PublishedApi
    internal val initialActiveState: MockPermissionState.ActiveState = MockPermissionState.ActiveState.ALLOWED,
    @PublishedApi
    internal val setupMocks: Boolean = true,
) : PermissionsBuilder(mockPermissionContext) {

    class RegistryMock<P : Permission>(
        @PublishedApi
        internal val permissionClass: KClass<P>,
    ) {
        val mock = pairParametersMock<P, CoroutineContext, BasePermissionStateRepo<P>>()
        val buildRepos = concurrentMutableListOf<MockBasePermissionStateRepo<P>>()

        inline fun <reified O : Permission> castOrNull(): RegistryMock<O>? = if (O::class == permissionClass) {
            @Suppress("UNCHECKED_CAST")
            this as RegistryMock<O>
        } else {
            null
        }
    }

    @PublishedApi
    internal val registries = concurrentMutableListOf<RegistryMock<*>>()

    inline fun <reified P : Permission> registry(): RegistryMock<P> = registries.firstNotNullOfOrNull { registered ->
        registered.castOrNull<P>()
    } ?: RegistryMock(P::class)

    suspend inline fun <reified P : Permission> registerPermissionBuilder() {
        val registry = RegistryMock(P::class)
        registries.add(registry)
        if (setupMocks) {
            registry.mock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    registry.buildRepos.add(it)
                }
            }
        }
        register(MockBasePermissionsBuilder<P>())
        registerPermissionStateRepoBuilder(registry.mock::call)
    }
}
