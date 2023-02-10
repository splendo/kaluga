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

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.base.singleThreadDispatcher
import kotlinx.coroutines.CoroutineName
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

    /**
     * The name of the permission
     */
    abstract val name: String
}

/**
 *  Base type for all permissions builders.
 *  Each permission component implements a concrete permissions builder and registers it in [PermissionsBuilder.register].
 *  @param P the type of [Permission] represented by this permission builder.
 */
interface BasePermissionsBuilder<P : Permission>

/**
 * Interface that takes permission and coroutine contexts and creates [BasePermissionStateRepo].
 * Each platform registers [PermissionStateRepoBuilder] in [PermissionsBuilder.registerPermissionStateRepoBuilder].
 *  @param P the type of [Permission] represented by this permission state repo builder.
 */
interface PermissionStateRepoBuilder<P : Permission> {

    /**
     * Creates a [BasePermissionStateRepo] associated with [permission]
     * @param permission the [P] the resulting [BasePermissionStateRepo] should manage.
     * @param coroutineContext the [CoroutineContext] to manage the [BasePermissionStateRepo]
     */
    fun create(permission: P, coroutineContext: CoroutineContext): BasePermissionStateRepo<P>
}

/**
 * Platform specific context for creating a [BasePermissionsBuilder]
 */
expect class PermissionContext

/**
 * Default [PermissionContext] of the system
 */
expect val defaultPermissionContext: PermissionContext

/**
 * Error when [PermissionsBuilder] tries to register or access invalid permissions.
 */
class PermissionsBuilderError(message: String) : Error(message)

/**
 * Builder for providing the proper [BasePermissionsBuilder] and [PermissionStateRepoBuilder] for each [Permission]
 * @param context the [PermissionContext] to be used
 */
open class PermissionsBuilder(val context: PermissionContext = defaultPermissionContext) {

    private val builders = concurrentMutableMapOf<KClassifier, BasePermissionsBuilder<*>>()
    private val repoBuilders = concurrentMutableMapOf<KClassifier, PermissionStateRepoBuilder<*>>()

    /**
     * Registers a [BasePermissionsBuilder] for a a given type of [Permission].
     * Only one builder can be registered per type of [Permission].
     * Make sure to call [unregister] before calling this if a permission has been registered before.
     * This method is thread-safe.
     * @param P the type of [Permission] for which to register the builder.
     * @param Builder the type of [BasePermissionsBuilder] to register for this permission
     * @param builder the [Builder] to register for the permission.
     * @throws [PermissionsBuilderError] if the permission was already registered.
     * @return the registered [Builder]
     */
    inline fun <reified P : Permission, Builder : BasePermissionsBuilder<P>> register(builder: Builder): Builder = register(P::class, builder)

    /**
     * Registers a [BasePermissionsBuilder] for a a given type of [Permission].
     * Only one builder can be registered per type of [Permission].
     * Make sure to call [unregister] before calling this if a permission has been registered before.
     * This method is thread-safe.
     * @param P the type of [Permission] for which to register the builder.
     * @param Builder the type of [BasePermissionsBuilder] to register for this permission
     * @param permission the [KClass] of the [P] to register
     * @param builder the [Builder] to register for the permission.
     * @throws [PermissionsBuilderError] if the permission was already registered.
     * @return the registered [Builder]
     */
    fun <P : Permission, Builder : BasePermissionsBuilder<P>> register(permission: KClass<P>, builder: Builder): Builder = builders.synchronized {
        if (this[permission] == null) {
            this[permission] = builder
            builder
        } else {
            throw PermissionsBuilderError("Builder for $permission was already registered")
        }
    }

    /**
     * Gets the registered [BasePermissionsBuilder] for a a given type of [Permission].
     * If no bulder has been registered yet
     * Make sure to call [unregister] before calling this if a permission has been registered before.
     * This method is thread-safe.
     * @param P the type of [Permission] for which to register the builder.
     * @param Builder the type of [BasePermissionsBuilder] to register for this permission
     * @param builder the [Builder] to register for the permission.
     * @throws [PermissionsBuilderError] if the permission was already registered.
     * @return the registered [Builder]
     */
    inline fun <reified P : Permission, B : BasePermissionsBuilder<P>> registerOrGet(builder: B): BasePermissionsBuilder<P> = registerOrGet(P::class, builder)
    @Suppress("UNCHECKED_CAST")
    fun <P : Permission, B : BasePermissionsBuilder<P>> registerOrGet(permission: KClass<P>, builder: B): BasePermissionsBuilder<P> = builders.getOrPut(permission::class) { builder } as BasePermissionsBuilder<P>

    fun <P : Permission> unregister(permission: P) {
        builders.remove(permission::class)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <P : Permission> get(permission: P): BasePermissionsBuilder<P> =
        builders[permission::class] as? BasePermissionsBuilder<P> ?: throw PermissionsBuilderError("The Builder for $permission was not registered")

    inline fun <reified P : Permission> registerPermissionStateRepoBuilder(noinline permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>) = registerPermissionStateRepoBuilder(P::class, permissionStateRepoBuilder)
    fun <P : Permission> registerPermissionStateRepoBuilder(permission: KClass<P>, permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>) {
        repoBuilders.synchronized {
            if (this[permission] == null) {
                createPermissionStateRepoBuilder(permissionStateRepoBuilder).also { this[permission] = it }
            } else {
                throw PermissionsBuilderError("Builder for $permission PermissionStateRepo was already registered")
            }
        }
    }

    inline fun <reified P : Permission> registerOrGetPermissionStateRepoBuilder(noinline permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>) = registerOrGetPermissionStateRepoBuilder(P::class, permissionStateRepoBuilder)
    @Suppress("UNCHECKED_CAST")
    fun <P : Permission> registerOrGetPermissionStateRepoBuilder(permission: KClass<P>, permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>) = repoBuilders.getOrPut(permission) { createPermissionStateRepoBuilder(permissionStateRepoBuilder) } as PermissionStateRepoBuilder<P>

    fun <P : Permission> unregisterPermissionStateRepoBuilder(permission: P) {
        repoBuilders.remove(permission::class)
    }

    @Suppress("UNCHECKED_CAST")
    fun <P : Permission> createPermissionStateRepo(permission: P, coroutineContext: CoroutineContext): BasePermissionStateRepo<*> =
        (repoBuilders[permission::class] as? PermissionStateRepoBuilder<P>)?.create(permission, coroutineContext)
            ?: throw PermissionsBuilderError("Permission state repo factory was not registered for $permission")

    private inline fun <P : Permission> createPermissionStateRepoBuilder(crossinline permissionStateRepoBuilder: (P, CoroutineContext) -> BasePermissionStateRepo<P>): PermissionStateRepoBuilder<P> = object : PermissionStateRepoBuilder<P> {
        override fun create(
            permission: P,
            coroutineContext: CoroutineContext
        ): BasePermissionStateRepo<P> {
            return permissionStateRepoBuilder(permission, coroutineContext)
        }
    }
}

private val defaultPermissionDispatcher by lazy {
    singleThreadDispatcher("Permissions")
}

/**
 * Manager to request the [PermissionStateRepo] of a given [Permission]
 * @param builder The [PermissionsBuilder] to build the [PermissionManager] associated with each [Permission]
 * @param coroutineContext The [CoroutineContext] to run permission checks from
 */
class Permissions(
    private val builder: PermissionsBuilder,
    private val coroutineContext: CoroutineContext = defaultPermissionDispatcher,
) {

    private val permissionStateRepos = concurrentMutableMapOf<Permission, BasePermissionStateRepo<*>>()

    @Suppress("UNCHECKED_CAST")
    private fun <P : Permission> permissionStateRepo(permission: P): BasePermissionStateRepo<P> = permissionStateRepos.getOrPut(permission) {
        builder.createPermissionStateRepo(permission, coroutineContext + CoroutineName(permission.name))
    } as BasePermissionStateRepo<P>

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
        permissionStateRepos.synchronized {
            values.forEach { it.cancel() }
            clear()
        }
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
            is PermissionState.Inactive, is PermissionState.Active -> {}
        }
    }.first()
}
