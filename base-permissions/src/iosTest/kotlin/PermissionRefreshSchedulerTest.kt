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


//FIXME: It uses storage but test is in base
/*
class PermissionRefreshSchedulerTest : BaseTest() {

    private lateinit var permissionsManager: MockStoragePermissionManager

    @BeforeTest
    fun setUp() {
        super.beforeTest()

        val repo = MockStoragePermissionStateRepo()
        permissionsManager = repo.permissionManager
    }

    @Test
    fun testStartMonitoring() = runBlocking {
        val authorization: AtomicReference<IOSPermissionsHelper.AuthorizationStatus> = AtomicReference(IOSPermissionsHelper.AuthorizationStatus.NotDetermined)
        val timerHelper = PermissionRefreshScheduler(permissionsManager, { authorization.value }, this)

        timerHelper.startMonitoring(50)
        delay(50)
        assertFalse(permissionsManager.didGrantPermission.value.isCompleted)
        assertFalse(permissionsManager.didRevokePermission.value.isCompleted)

        authorization.set(IOSPermissionsHelper.AuthorizationStatus.Authorized)
        permissionsManager.reset()
        delay(60)
        assertTrue(permissionsManager.didGrantPermission.value.isCompleted)

        timerHelper.isWaiting.value = true
        authorization.set(IOSPermissionsHelper.AuthorizationStatus.Denied)
        permissionsManager.reset()
        delay(60)
        assertFalse(permissionsManager.didGrantPermission.value.isCompleted)
        assertFalse(permissionsManager.didRevokePermission.value.isCompleted)

        timerHelper.isWaiting.value = false
        delay(50)
        assertTrue(permissionsManager.didRevokePermission.value.await())

        authorization.set(IOSPermissionsHelper.AuthorizationStatus.Authorized)
        permissionsManager.reset()
        timerHelper.stopMonitoring()
        delay(50)
        assertFalse(permissionsManager.didGrantPermission.value.isCompleted)
        assertFalse(permissionsManager.didRevokePermission.value.isCompleted)
        Unit
    }
}

private class MockStoragePermissionStateRepo : PermissionStateRepo<StoragePermission>() {

    override val permissionManager = MockStoragePermissionManager(this)
}

private class MockStoragePermissionManager(mockPermissionRepo: MockStoragePermissionStateRepo) : PermissionManager<StoragePermission>(mockPermissionRepo) {

    var didGrantPermission: AtomicReference<EmptyCompletableDeferred> = AtomicReference(EmptyCompletableDeferred())
    var didRevokePermission: AtomicReference<CompletableDeferred<Boolean>> = AtomicReference(CompletableDeferred())

    var initialState: PermissionState<StoragePermission> = PermissionState.Denied.Requestable()

    val hasRequestedPermission = EmptyCompletableDeferred()
    val hasStartedMonitoring = CompletableDeferred<Long>()
    val hasStoppedMonitoring = EmptyCompletableDeferred()

    override suspend fun requestPermission() {
        hasRequestedPermission.complete()
    }

    override suspend fun initializeState(): PermissionState<StoragePermission> {
        return initialState
    }

    override suspend fun startMonitoring(interval: Long) {
        hasStartedMonitoring.complete(interval)
    }

    override suspend fun stopMonitoring() {
        hasStoppedMonitoring.complete()
    }

    override fun grantPermission() {
        didGrantPermission.value.complete()
    }

    override fun revokePermission(locked: Boolean) {
        didRevokePermission.value.complete(locked)
    }

    fun reset() {
        didGrantPermission.set(EmptyCompletableDeferred())
        didRevokePermission.set(CompletableDeferred())
    }
}
*/