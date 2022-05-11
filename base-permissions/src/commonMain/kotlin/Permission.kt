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

package com.splendo.kaluga.permissions

import co.touchlab.stately.collections.IsoMutableMap
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClassifier

/**
 * Permissions that can be requested by Kaluga
 * Each permission component should declare subclass of [Permission]
 */
abstract class Permission

/**
 *  Base type for all permissions builders. Each permission component implements a concrete permissions builder
 *  and declares helper methods for registering this builder in the [PermissionsBuilder].
 */
interface BasePermissionsBuilder

/**
 * Closure that takes permission and coroutine contexts and creates [PermissionStateRepo].
 * Each platform registers [PermissionStateRepoBuilder] in the register permission helper method.
 */
typealias PermissionStateRepoBuilder = (permission: Permission, coroutineContext: CoroutineContext) -> PermissionStateRepo<*>

expect class PermissionContext
expect val defaultPermissionContext: PermissionContext

/**
 * Builder for providing the proper [PermissionManager] for each [Permission]
 * @param context [PermissionContext] an additional parameter platform can pass to the [PermissionsBuilder]. [NSBundle] on iOS and [Contect] on Andoid.
 */
open class PermissionsBuilder(val context: PermissionContext = defaultPermissionContext) {

    private val builders = IsoMutableMap<KClassifier, BasePermissionsBuilder>()
    private val repoBuilders = IsoMutableMap<KClassifier, PermissionStateRepoBuilder>()

    fun <T : BasePermissionsBuilder> register(builder: T, permission: KClassifier): T {
        builders[permission] = builder
        return builder
    }

    operator fun get(permission: Permission): BasePermissionsBuilder =
        builders[permission::class] ?: throw Error("The Builder for $permission was not registered")

    fun registerPermissionStateRepoBuilder(permission: KClassifier, permissionStateRepoBuilder: PermissionStateRepoBuilder) {
        repoBuilders[permission] = permissionStateRepoBuilder
    }

    fun createPermissionStateRepo(permission: Permission, coroutineContext: CoroutineContext): PermissionStateRepo<*> =
        repoBuilders[permission::class]?.let { it(permission, coroutineContext) } ?: throw Error("Permission state repo factory was not registered for $permission")
}

/**
 * Manager to request the [PermissionStateRepo] of a given [Permission]
 * @param builder The [PermissionsBuilder] to build the [PermissionManager] associated with each [Permission]
 * @param coroutineContext The [CoroutineContext] to run permission checks from
 */
class Permissions(private val builder: PermissionsBuilder, private val coroutineContext: CoroutineContext = Dispatchers.Main) {

    private val permissionStateRepos: IsoMutableMap<Permission, PermissionStateRepo<*>> = IsoMutableMap()

    private fun <P : Permission> permissionStateRepo(permission: P) =
        (permissionStateRepos[permission] ?: builder.createPermissionStateRepo(permission, coroutineContext).also { permissionStateRepos[permission] = it }) as PermissionStateRepo<P>

    /**
     * Gets a [Flow] of [PermissionState] for a given [Permission]
     * @param permission The [Permission] for which the [PermissionState] flow should be provided
     * @return A [Flow] of [PermissionState] for the given [Permission]
     */
    operator fun <P : Permission> get(permission: P): Flow<PermissionState<P>> {
        return permissionStateRepo(permission)
    }

    /**
     * Gets a the of [PermissionManager] for a given [Permission]
     * @param permission The [Permission] for which the [PermissionManager] should be returned
     * @return The [PermissionManager] for the given [Permission]
     */
    fun <P : Permission> getManager(permission: P): PermissionManager<P> {
        return permissionStateRepo(permission).permissionManager
    }

    /**
     * Requests a [Permission]
     * @return `true` if the permission was granted, `false` otherwise.
     */
    suspend fun <P : Permission> request(p: P): Boolean {
        return get(p).request(getManager(p))
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
suspend fun <P : Permission> Flow<PermissionState<out P>>.request(permissionManager: PermissionManager<out P>): Boolean {
    debug("Request", "Request")
    return this.transformLatest { state ->
        debug("Request", "Latest $state")
        when (state) {
            is PermissionState.Allowed -> emit(true)
            is PermissionState.Denied.Requestable -> state.request(permissionManager)
            is PermissionState.Denied.Locked -> emit(false)
            is PermissionState.Unknown -> {}
        }
    }.first()
}
