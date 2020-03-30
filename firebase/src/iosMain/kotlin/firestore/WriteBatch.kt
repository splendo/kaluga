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

package com.splendo.kaluga.firebase.firestore

import Firebase.FirebaseFirestore.FIRWriteBatch

actual typealias WriteBatch = FIRWriteBatch

@Suppress("UNCHECKED_CAST")
actual fun WriteBatch.set(
    documentReference: DocumentReference,
    data: Map<String, Any?>
): WriteBatch = setData(data as Map<Any?, *>, documentReference)

@Suppress("UNCHECKED_CAST")
actual fun WriteBatch.update(
    documentReference: DocumentReference,
    data: Map<String, Any?>
): WriteBatch = updateData(data as Map<Any?, *>, documentReference)

actual fun WriteBatch.delete(documentReference: DocumentReference): WriteBatch = deleteDocument(documentReference)

actual fun WriteBatch.commit(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    commitWithCompletion { error ->
        if (error != null) {
            onFailure(Exception(error.localizedDescription))
        } else {
            onSuccess()
        }
    }
}
