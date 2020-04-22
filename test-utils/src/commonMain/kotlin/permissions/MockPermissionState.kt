// package com.splendo.kaluga.test.permissions
//
// import com.splendo.kaluga.base.runBlocking
// import com.splendo.kaluga.permissions.Permission
// import com.splendo.kaluga.permissions.PermissionManager
// import com.splendo.kaluga.permissions.PermissionState
// import com.splendo.kaluga.permissions.PermissionStateRepo
// import com.splendo.kaluga.utils.EmptyCompletableDeferred
// import com.splendo.kaluga.utils.complete
// import kotlinx.coroutines.CompletableDeferred
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.flow.collect
// import kotlinx.coroutines.flow.first
// import kotlinx.coroutines.launch
//
// class MockPermissionStateRepo<P:Permission>(coroutineScope: CoroutineScope) : PermissionStateRepo<P>(coroutineScope = coroutineScope) {
//
//     override val permissionManager = MockPermissionManager(this, coroutineScope)
//
// }
//
// class MockPermissionManager<P:Permission>(val permissionRepo: PermissionStateRepo<P>, coroutineScope: CoroutineScope) : PermissionManager<P>(permissionRepo, coroutineScope) {
//
//     var currentState: PermissionState<P> = PermissionState.Denied.Requestable(this)
//
//     fun setPermissionAllowed() {
//         changePermission { grantPermission() }
//     }
//
//     fun setPermissionDenied() {
//         changePermission { revokePermission(false) }
//     }
//
//     fun setPermissionLocked() {
//         changePermission { revokePermission(true) }
//     }
//
//     private fun changePermission(action: suspend () -> Unit) = runBlocking {
//         val completed = EmptyCompletableDeferred()
//         val job = launch {
//             permissionRepo.flow().collect {
//                 action()
//                 completed.complete()
//             }
//         }
//         completed.await()
//         job.cancel()
//         currentState = permissionRepo.flow().first()
//     }
//
//     var hasRequestedPermission = EmptyCompletableDeferred()
//     var hasStartedMonitoring = CompletableDeferred<Long>()
//     var hasStoppedMonitoring = EmptyCompletableDeferred()
//
//     fun reset() {
//         hasRequestedPermission = EmptyCompletableDeferred()
//         hasStartedMonitoring = CompletableDeferred()
//         hasStoppedMonitoring = EmptyCompletableDeferred()
//     }
//
//     override suspend fun requestPermission() {
//         hasRequestedPermission.complete()
//     }
//
//     override suspend fun initializeState(): PermissionState<P> {
//         return currentState
//     }
//
//     override suspend fun startMonitoring(interval: Long) {
//         hasStartedMonitoring.complete(interval)
//     }
//
//     override suspend fun stopMonitoring() {
//         hasStoppedMonitoring.complete()
//     }
//
//
//
// }