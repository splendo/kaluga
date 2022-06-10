/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.base

import co.touchlab.stately.collections.IsoMutableMap
import com.splendo.kaluga.base.singleThreadDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

/**
 * Permissions that can be requested by Kaluga
 * Each permission component should declare subclass of [Permission]
 */
abstract class Permission {
    abstract val name: String
}

/**
 *  Base type for all permissions builders. Each permission component implements a concrete permissions builder
 *  and declares helper methods for registering this builder in the [PermissionsBuilder].
 */
interface BasePermissionsBuilder<P : Permission>

/**
 * Closure that takes permission and coroutine contexts and creates [PermissionStateRepo].
 * Each platform registers [PermissionStateRepoBuilder] in the register permission helper method.
 */
private interface PermissionStateRepoBuilder<P : Permission> {
    fun create(permission: P, coroutineContext: CoroutineContext): BasePermissionStateRepo<P>
}

expect class PermissionContext
expect val defaultPermissionContext: PermissionContext

/**
 * Builder for providing the proper [PermissionManager] for each [Permission]
 * @param context [PermissionContext] an additional parameter platform can pass to the [PermissionsBuilder]. [NSBundle] on iOS and [Contect] on Andoid.
 */
open class PermissionsBuilder(val context: PermissionContext = defaultPermissionContext) {

    private val builders = IsoMutableMap<KClassifier, BasePermissionsBuilder<*>>()
    private val repoBuilders = IsoMutableMap<KClassifier, PermissionStateRepoBuilder<*>>()

    inline fun <reified P : Permission, B : BasePermissionsBuilder<P>> register(builder: B): B = register(P::class, builder)
    fun <P : Permission, B : BasePermissionsBuilder<P>> register(permission: KClass<P>, builder: B): B {
        builders[permission] = builder
        return builder
    }

    operator fun <P : Permission> get(permission: P): BasePermissionsBuilder<P> =
        builders[permission::class] as? BasePermissionsBuilder<P> ?: throw Error("The Builder for $permission was not registered")

    inline fun <reified P : Permission> registerPermissionStateRepoBuilder(noinline permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>) = registerPermissionStateRepoBuilder(P::class, permissionStateRepoBuilder)
    fun <P : Permission> registerPermissionStateRepoBuilder(permission: KClass<P>, permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>) {
        repoBuilders[permission] = object : PermissionStateRepoBuilder<P> {
            override fun create(
                permission: P,
                coroutineContext: CoroutineContext
            ): BasePermissionStateRepo<P> {
                return permissionStateRepoBuilder(permission, coroutineContext)
            }
        }
    }

    fun <P : Permission> createPermissionStateRepo(permission: P, coroutineContext: CoroutineContext): BasePermissionStateRepo<*> =
        (repoBuilders[permission::class] as? PermissionStateRepoBuilder<P>)?.let {
            it.create(permission, coroutineContext)
        } ?: throw Error("Permission state repo factory was not registered for $permission")
}

/**
 * Manager to request the [PermissionStateRepo] of a given [Permission]
 * @param builder The [PermissionsBuilder] to build the [PermissionManager] associated with each [Permission]
 * @param coroutineContext The [CoroutineContext] to run permission checks from
 */
class Permissions(
    private val builder: PermissionsBuilder,
    private val coroutineContext: CoroutineContext = singleThreadDispatcher("Permissions"),
    private val contextCreator: CoroutineContext.(String) -> CoroutineContext = { this + singleThreadDispatcher(it) }
) {

    private val permissionStateRepos: IsoMutableMap<Permission, BasePermissionStateRepo<*>> = IsoMutableMap()

    private fun <P : Permission> permissionStateRepo(permission: P) =
        (permissionStateRepos[permission] ?: builder.createPermissionStateRepo(permission, coroutineContext.contextCreator(permission.name)).also { permissionStateRepos[permission] = it }) as BasePermissionStateRepo<P>

    /**
     * Gets a [Flow] of [PermissionState] for a given [Permission]
     * @param permission The [Permission] for which the [PermissionState] flow should be provided
     * @return A [Flow] of [PermissionState] for the given [Permission]
     */
    operator fun <P : Permission> get(permission: P): PermissionStateFlowRepo<P> {
        return permissionStateRepo(permission)
    }

    /**
     * Requests a [Permission]
     * @return `true` if the permission was granted, `false` otherwise.
     */
    suspend fun <P : Permission> request(p: P): Boolean {
        return get(p).run {
            withContext(coroutineContext) {
                request()
            }
        }
    }

    fun clean() {
        permissionStateRepos.values.forEach { it.cancel() }
        permissionStateRepos.clear()
    }
}

/**
 * Requests a [Permission] on a [Flow] of [PermissionState]
 * @return `true` if the permission was granted, `false` otherwise.
 */
suspend fun <P : Permission> Flow<PermissionState<out P>>.request(): Boolean {
    return this.transformLatest { state ->
        when (state) {
            is PermissionState.Allowed -> emit(true)
            is PermissionState.Denied.Requestable -> state.request()
            is PermissionState.Denied.Locked -> emit(false)
            is PermissionState.Inactive -> {}
        }
    }.first()
}
